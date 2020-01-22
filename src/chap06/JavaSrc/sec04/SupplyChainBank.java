package chap06.sec04;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple8;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import chap06.com.alc.SupplyChainContract;
import rx.Subscription;
import rx.functions.Action1;

public class SupplyChainBank {

	public static void main(String[] args) {
		new SupplyChainBank();
	}

	// 區塊鏈節點位址
	private static String blockchainNode = "http://127.0.0.1:8080/";

	// 智能合約位址
	private static String contractAddr = "0x069ce65305532f6e125366a9f98b90de511ff4e1";

	// 銀行金鑰檔
	private String bankKey = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";

	// 銀行EOA
	String bank = "0x4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";

	// 製造商金鑰檔
	private String factoryKey = "C:\\MyGeth\\node01\\keystore\\UTC--2018-11-25T03-41-09.743521200Z--576b11fb5d5c380fcf973b62c3ab59f19f9300fe";

	// 製造商EOA
	String factory = "0x576B11Fb5D5C380fCF973b62C3aB59f19f9300fE";

	//供應商EOA
	String supplier = "0xDa85610910365341D3372fa350F865Ce50224a91";
	
	public SupplyChainBank() {
		// step1. 銀行設定製造商
		initFactory(bankKey, "16888", factory);

		// step2. 存放餘額於智能合約
		transferETH(bank, contractAddr, bankKey, "16888", "200");

		// step3. 銀行進行事件傾聽
		// step4. 傾聽事件後，取得交易資訊
		// step5. 進行放款的動作
		startOracle(contractAddr);

		// step6. 供應商確認撥款
	}

	// 設定製造商
	private void initFactory(String keyFile, String myPWD, String factory) {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);

			// 取得合約包裹物件
			SupplyChainContract contract = SupplyChainContract.load(contractAddr, web3, credentials,
					SupplyChainContract.GAS_PRICE, SupplyChainContract.GAS_LIMIT);

			// 設定製造商位址
			contract.setFactory(factory).send();
			System.out.println("設定製造商,完成");

		} catch (Exception e) {
			System.out.println("設定製造商錯誤,錯誤:" + e);
		}
	}

	// 傳送ETH
	private void transferETH(String fromEOA, String toEOA, String keyFile, String pwd, String eth) {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 驗證加簽物件
			Credentials credentials = WalletUtils.loadCredentials(pwd, keyFile);

			// 設定ETH數量
			BigInteger ethValue = Convert.toWei(eth, Convert.Unit.ETHER).toBigInteger();

			// 設定nonce亂數
			EthGetTransactionCount ethGetTransactionCount = web3
					.ethGetTransactionCount(fromEOA, DefaultBlockParameterName.LATEST).sendAsync().get();
			BigInteger nonce = ethGetTransactionCount.getTransactionCount();

			// 設定Gas
			BigInteger gasPrice = new BigInteger("" + 1);
			BigInteger gasLimit = new BigInteger("" + 30000);

			// 建立RawTransaction物件
			RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, toEOA,
					ethValue);

			// 對交易進行加簽與加密
			byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
			String hexValue = Numeric.toHexString(signedMessage);

			// 提出交易
			EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();

			String txnHash = ethSendTransaction.getTransactionHash();
			System.out.println("傳送ETH交易序號:" + txnHash);

		} catch (Exception e) {
			System.out.println("transferETH,錯誤:" + e);
		}
	}

	// 啟動Oracle服務
	public void startOracle(String contractAddr) {
		try {
			// 連線區塊鏈節點

			Admin web3 = Admin.build(new HttpService(blockchainNode));

			// 設定過濾條件
			EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST,
					contractAddr);

			// 取得事件topic的hash code
			String eventTopicHash = Hash.sha3String("TransIns");

			// 交易事件的Log
			Function transLog = new Function("", Collections.<Type>emptyList(),
					Arrays.asList(new TypeReference<Uint>() {
					}));

			// 持續偵測事件
			Subscription subscription = web3.ethLogObservable(filter).subscribe(new Action1<Log>() {
				public void call(Log log) {
					List<String> list = log.getTopics();
					// 輪詢事件中的Topic
					for (String topic : list) {
						if (topic.equals(eventTopicHash)) {
							System.out.println("處理交易事件");
							handleTransEvent(log, transLog);
						}
					}
				}
			});
		} catch (Exception e) {
			System.out.println("Oracle偵測錯誤:" + e);
		}
	}

	// 處理交易事件
	private void handleTransEvent(Log log, Function function) {
		try {
			List<Type> nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), function.getOutputParameters());
			int inx = 0;
			long transKey = 0l; // 交易ID
			for (Type type : nonIndexedValues) {
				System.out.println("Type String:" + type.getTypeAsString());
				System.out.println("Type Value:" + type.getValue());
				if (inx == 0) {
					// 回傳的參數，乃是交易編號
					try {
						transKey = ((BigInteger) type.getValue()).longValue();
					} catch (Exception e) {
						System.out.println("convert error:" + e);
					}
				}
				inx++;
			}

			// 判斷供應鏈交易是否存在
			Long transValueObj = querySupplyChainTrans(bankKey, "16888", transKey);
			if (transValueObj != null && transValueObj.longValue() > 0) {
				// 執行放款
				System.out.println("準備進行放款");
				executeLoan(bankKey, "16888", transKey, transValueObj.longValue());
			} else {
				// 不執行放款
				System.out.println("交易不存在,不進行放款");
			}

		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}

	// 查詢供應鏈交易
	private Long querySupplyChainTrans(String keyFile, String myPWD, long transKey) {
		Long transValueObj = null;
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);

			// 取得合約包裹物件
			SupplyChainContract contract = SupplyChainContract.load(contractAddr, web3, credentials,
					SupplyChainContract.GAS_PRICE, SupplyChainContract.GAS_LIMIT);

			// 查詢交易是否存在
			if (contract.isTransExist(new BigInteger("" + transKey)).send()) {
				System.out.println("供應鏈交易存在");

				// 取得交易物件
				Tuple8 transData = contract.transData(new BigInteger("" + transKey)).send();

				// 交易憑單編號
				String transNo = (String) transData.getValue1();

				// 交易說明
				String transMemo = (String) transData.getValue2();

				// 供應商
				String supplier = (String) transData.getValue3();

				// 交易時間
				BigInteger transTime = (BigInteger) transData.getValue4();

				// 實體交易金額
				BigInteger transValue = (BigInteger) transData.getValue5();
				transValueObj = transValue.longValue();

				// 放款時間
				BigInteger loanTime = (BigInteger) transData.getValue6();

				// 放款金額
				BigInteger loanValue = (BigInteger) transData.getValue7();

				// 交易存在旗標
				Boolean exist = (Boolean) transData.getValue8();

				// 時間呈現格式
				SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Calendar bolckTimeCal = Calendar.getInstance();

				System.out.println("交易憑單編號:" + transNo);
				System.out.println("交易說明:" + transMemo);
				System.out.println("供應商:" + supplier);

				bolckTimeCal.setTimeInMillis(transTime.longValueExact() * 1000);
				System.out.println("交易時間:" + timeFormat.format(bolckTimeCal.getTime()));

				System.out.println("實體交易金額:" + transValue);

				bolckTimeCal.setTimeInMillis(loanTime.longValueExact() * 1000);
				System.out.println("放款時間:" + timeFormat.format(bolckTimeCal.getTime()));

				System.out.println("放款金額:" + loanValue.longValue());
				System.out.println("交易存在旗標:" + exist);
			} else {
				System.out.println("供應鏈交易不存在");
				transValueObj = null;
			}

		} catch (Exception e) {
			System.out.println("查詢供應鏈交易錯誤,錯誤:" + e);
		}
		return transValueObj;
	}

	// 執行放款
	private void executeLoan(String keyFile, String myPWD, long transKey, long transValue) {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);

			// 取得合約包裹物件
			SupplyChainContract contract = SupplyChainContract.load(contractAddr, web3, credentials,
					SupplyChainContract.GAS_PRICE, SupplyChainContract.GAS_LIMIT);

			// 查詢交易是否存在
			Boolean isExist = contract.isTransExist(new BigInteger("" + transKey)).send();
			if (isExist) {
				System.out.println("供應鏈交易存在，準備進行放款");

				// 計算放款額度
				long loanValue = transValue / 10;
				BigInteger weiValue = Convert.toWei("" + loanValue, Convert.Unit.ETHER).toBigInteger();
				
				// 執行放款
				contract.loanEth(new BigInteger("" + transKey), new BigInteger("" + weiValue)).send();
				System.out.println("完成放款");

			} else {
				System.out.println("供應鏈交易不存在");
			}

		} catch (Exception e) {
			System.out.println("執行放款錯誤,錯誤:" + e);
		}
	}
}
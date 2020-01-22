package chap06.sec05;

import java.math.BigInteger;
import java.util.Arrays;
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
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import chap06.com.alc.InsuranceContract;
import rx.Subscription;
import rx.functions.Action1;

public class InsuranceCorp {

	public static void main(String[] args) {
		new InsuranceCorp();
	}

	// 區塊鏈節點位址
	private static String blockchainNode = "http://127.0.0.1:8080/";

	// 智能合約位址
	private static String contractAddr = "0xc6a3fb214038e574fff84d358eb080d3200c5fe3";

	// 保險公司金鑰檔
	private String insuranceCorpKey = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";

	// 保險公司EOA
	String insuranceCorp = "0x4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";

	// 醫院金鑰檔
	private String hospitalKey = "C:\\MyGeth\\node01\\keystore\\UTC--2018-11-25T03-41-09.743521200Z--576b11fb5d5c380fcf973b62c3ab59f19f9300fe";

	// 醫院EOA
	String hospital = "0x576B11Fb5D5C380fCF973b62C3aB59f19f9300fE";

	// 保險客戶EOA
	String patient = "0xDa85610910365341D3372fa350F865Ce50224a91";

	public InsuranceCorp() {
		// step1. 保險公司設定醫院EOA
		initHospital(insuranceCorpKey, "16888", hospital);

		// step2. 傾聽離院申請事件
		startOracle(contractAddr);		
	}

	// 設定醫院
	private void initHospital(String keyFile, String myPWD, String hospital) {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);

			// 取得合約包裹物件
			InsuranceContract contract = InsuranceContract.load(contractAddr, web3, credentials,
					InsuranceContract.GAS_PRICE, InsuranceContract.GAS_LIMIT);

			// 設定醫院位址
			contract.setHospital(hospital).send();
			System.out.println("設定醫院,完成");

		} catch (Exception e) {
			System.out.println("設定醫院錯誤,錯誤:" + e);
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
			EthFilter filter = new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST,
					contractAddr);

			// 取得事件topic的hash code
			String eventTopicHash = Hash.sha3String("insRecord");

			// 交易事件的Log
			Function transLog = new Function("", Collections.<Type>emptyList(),
					Arrays.asList(new TypeReference<Uint>() {
					}, new TypeReference<Uint>() {
					}, new TypeReference<Uint>() {
					}, new TypeReference<Uint>() {
					}));

			System.out.println("Oracle service start...");
			
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

	// 處理事件內容
	private void handleTransEvent(Log log, Function function) {
		try {
			List<Type> nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), function.getOutputParameters());
			int inx = 0;
			String address = ""; // 保險客戶EOA
			BigInteger recordInx = BigInteger.ZERO; // 病歷序號
			BigInteger days = BigInteger.ZERO; // 住院天數
			BigInteger money = BigInteger.ZERO; // 住院金額

			for (Type type : nonIndexedValues) {
				// 第一個參數是address
				if (inx == 0) {
					try {
						// 將位址，轉換成16進制的字串
						address = Numeric.toHexStringWithPrefix((BigInteger) type.getValue());
					} catch (Exception e) {
					}
				}

				// 第二個參數是病歷序號
				if (inx == 1) {
					recordInx = (BigInteger) type.getValue();
				}

				// 第三個參數是住院天數
				if (inx == 2) {
					days = (BigInteger) type.getValue();
				}

				// 第四個參數是住院金額
				if (inx == 3) {
					money = (BigInteger) type.getValue();
				}
				inx++;
			}
			
			System.out.println("保險受益人:" + address);
			System.out.println("病歷序號:" + recordInx.longValueExact());
			System.out.println("住院天數:" + days.longValueExact());
			System.out.println("住院金額:" + money.longValueExact());
			
			// 撥款理賠金
			String payMoney = "" + (money.longValueExact()/1000);
			transferETH(insuranceCorp, address, insuranceCorpKey, "16888", payMoney);

		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}
}
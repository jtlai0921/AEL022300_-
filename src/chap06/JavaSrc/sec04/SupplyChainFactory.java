package chap06.sec04;

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
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ChainId;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.response.Callback;
import org.web3j.tx.response.QueuingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;

import chap06.com.alc.SupplyChainContract;

public class SupplyChainFactory {

	public static void main(String[] args) {
		new SupplyChainFactory();
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

	// 供應商EOA
	String supplier = "0xDa85610910365341D3372fa350F865Ce50224a91";

	public SupplyChainFactory() {
		// step1. 上傳一筆供應鏈交易資訊
		insSupplyTrans(factoryKey, "16888", "ABC888", "購買網路設備", supplier, 200);
	}

	// 新增供應鏈交易
	private void insSupplyTrans(String keyFile, String myPWD, String transNo, String transMemo, String supplier,
			long transValue) {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);
			System.out.println("身份驗證");

			int attemptsPerTxHash = 30;
			long frequency = 1000;

			// 建立交易處理器
			TransactionReceiptProcessor myProcessor = new QueuingTransactionReceiptProcessor(web3,
					new InsTransCallBack(), attemptsPerTxHash, frequency);

			// 建立交易管理器
			TransactionManager transactionManager = new RawTransactionManager(web3, credentials, ChainId.NONE,
					myProcessor);
			System.out.println("建立交易管理器");

			// 取得合約包裹物件
			SupplyChainContract contract = SupplyChainContract.load(contractAddr, web3, transactionManager,
					SupplyChainContract.GAS_PRICE, SupplyChainContract.GAS_LIMIT);
			System.out.println("取得合約");

			// 加入一筆供應鏈交易
			contract.insSupplyTrans(transNo, transMemo, supplier, new BigInteger("" + transValue)).sendAsync();			
			System.out.println("新增供應鏈交易");
		} catch (Exception e) {
			System.out.println("新增供應鏈交易錯誤,錯誤:" + e);
		}
	}	
}

// 處理函數具有整數回傳值
class InsTransCallBack implements Callback {
	// 交易被接受的回叫函數
	public void accept(TransactionReceipt recp) {

		// 定義函數回傳值
		Function function = new Function("", Collections.<Type>emptyList(), Arrays.asList(new TypeReference<Uint>() {
		}));

		// 取得回傳值
		List<Log> list = recp.getLogs();
		List<Type> nonIndexedValues = FunctionReturnDecoder.decode(list.get(0).getData(),
				function.getOutputParameters());

		// 第一個回傳值是uint
		BigInteger newsKey = (BigInteger) nonIndexedValues.get(0).getValue();
		System.out.println("供應鏈交易主鍵:" + newsKey.intValue());
	}

	public void exception(Exception exception) {
		System.out.println("交易失敗, err:" + exception);
	}
}
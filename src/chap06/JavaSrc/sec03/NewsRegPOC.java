package chap06.sec03;

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

import chap06.com.alc.RegisterContract;

public class NewsRegPOC {

	public static void main(String[] args) {
		new NewsRegPOC();
	}

	// 區塊鏈節點位址
	private static String blockchainNode = "http://127.0.0.1:8080/";

	// 智能合約位址
	private static String contractAddr = "0x7d9a6b46ea40683393ffa40327f5fbd5ceeaab3f";

	// 礦工帳號的金鑰檔
	private String keyFilbase = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";

	long startTime = 0;
	long endTime = 0;

	public NewsRegPOC() {
		//註冊一個新聞頻道
		//regNews(keyFilbase, "16888", "0x298a71b8d049ccf6ee8cb6c9d5c31136e47f9e96", "真相新聞網");
		
		// 查詢指定的新聞頻道
		queryContract(keyFilbase, "16888", "0x9a8512326b0c74ec0fd066e17eb34877d361f790"); 
	}

	// 註冊一則新聞
	private void regNews(String keyFile, String myPWD, String newsContractAddr, String newsContractName) {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);

			int attemptsPerTxHash = 30;
			long frequency = 1000;

			// 記錄交易開始時間
			startTime = System.currentTimeMillis();

			// 建立交易處理器
			TransactionReceiptProcessor myProcessor = new QueuingTransactionReceiptProcessor(web3,
					new NewsRegCallBack(), attemptsPerTxHash, frequency);

			// 建立交易管理器
			TransactionManager transactionManager = new RawTransactionManager(web3, credentials, ChainId.NONE,
					myProcessor);

			// 取得合約包裹物件
			RegisterContract contract = RegisterContract.load(contractAddr, web3, transactionManager,
					RegisterContract.GAS_PRICE, RegisterContract.GAS_LIMIT);

			// 加入一則新的新聞
			contract.regContract(newsContractAddr, newsContractName).sendAsync();
			
		} catch (Exception e) {
			System.out.println("建立新聞錯誤,錯誤:" + e);
		}
	}

	// 查詢新聞頻道
	private void queryContract(String keyFile, String myPWD, String newsContractAddr) {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);

			// 取得合約包裹物件
			RegisterContract contract = RegisterContract.load(contractAddr, web3, credentials,
					RegisterContract.GAS_PRICE, RegisterContract.GAS_LIMIT);

			// 查詢是否存在
			Boolean isExist = contract.isContractExist(newsContractAddr).send();
			System.out.println("新聞頻道是否存在:" + isExist.booleanValue());

			// 查詢新聞數量
			BigInteger newsCnt = contract.contractCnt().send();
			System.out.println("註冊新聞數量:" + newsCnt);

			// 查詢新聞頻道
			String newsName = contract.addrToNameMapping(newsContractAddr).send();
			System.out.println("新聞頻道名稱:" + newsName);

		} catch (Exception e) {
			System.out.println("查詢新聞頻道錯誤,錯誤:" + e);
		}
	}
}

// 處理函數具有整數回傳值
class NewsRegCallBack implements Callback {
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
		System.out.println("news reg ID:" + nonIndexedValues.get(0).getValue());
	}

	public void exception(Exception exception) {
		System.out.println("交易失敗, err:" + exception);
	}
}
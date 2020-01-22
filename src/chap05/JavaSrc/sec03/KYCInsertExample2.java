package chap05.sec03;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

import chap05.com.alc.KYC;

public class KYCInsertExample2 {

	static long startTime;

	public static void main(String[] args) {
		try {
			// 連接區塊鏈節點
			String blockchainNode = "http://127.0.0.1:8080/";
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			String coinBaseFile = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";
			String myPWD = "16888";
			Credentials credentials = WalletUtils.loadCredentials(myPWD, coinBaseFile);

			int attemptsPerTxHash = 20;
			long frequency = 1000;
			
			startTime = System.currentTimeMillis();
			
			String chainID = web3.netVersion().send().getNetVersion();
			System.out.println("chainID:" + chainID);
			
			TransactionReceiptProcessor myProcessor = new QueuingTransactionReceiptProcessor(web3,
					new MyCallback(), attemptsPerTxHash, frequency);
			
			TransactionManager transactionManager = new RawTransactionManager(
			        web3, credentials, ChainId.NONE, myProcessor);
			
			// 取得合約包裹物件
			String contractAddr = "0xeb1da6170755d8a60b045cde6181ecddc8dd81b0";
			KYC contract = KYC.load(contractAddr, web3, transactionManager, KYC.GAS_PRICE, KYC.GAS_LIMIT);
			
			// 合約函數之參數設定
			BigInteger id = new BigInteger("" + 16888);
			String name = "Allan";
			BigInteger age = new BigInteger("" + 27);

			// 使用合約函數
			// System.out.println("step 1");			
			contract.doInsert(id, name, age).sendAsync();
			long endTime = System.currentTimeMillis();
			System.out.println("合約執行時間:" + (endTime - startTime) + " ms");
			
		} catch (Exception e) {
			System.out.println("交易錯誤:" + e);
		}

		//System.exit(0);
	}
}

class MyCallback implements Callback {

	long startTime = 0;
	long endTime = 0;

	public MyCallback() {
		// 記錄開始時間
		startTime = System.currentTimeMillis();
	}

	//交易被接受的回叫函數
	public void accept(TransactionReceipt recp) {
		endTime = System.currentTimeMillis();
		System.out.println("交易確認時間:" + (endTime - startTime) + " ms");
		
		String txnHash = recp.getTransactionHash();
		System.out.println("txnHash:" + txnHash);
		System.out.println("blockNum:" + recp.getBlockNumber());
		List<Log> list = recp.getLogs();
		if (list != null && list.size() > 0) {
			for (Log log : list) {
				System.out.println("log data:" + log.getData());
			}
		}
	}

	public void exception(Exception exception) {
		System.out.println("交易失敗, err:" + exception);
	}
}
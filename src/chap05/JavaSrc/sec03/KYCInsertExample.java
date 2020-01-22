package chap05.sec03;

import java.math.BigInteger;
import java.util.List;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import chap05.com.alc.KYC;

public class KYCInsertExample {
	public static void main(String[] args) {
		try {
			// 連接區塊鏈節點
			String blockchainNode = "http://127.0.0.1:8080/";
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			String coinBaseFile = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";
			String myPWD = "16888";
			Credentials credentials = WalletUtils.loadCredentials(myPWD, coinBaseFile);

			// 取得合約包裹物件
			String contractAddr = "0xeb1da6170755d8a60b045cde6181ecddc8dd81b0";
			KYC contract = KYC.load(contractAddr, web3, credentials, KYC.GAS_PRICE, KYC.GAS_LIMIT);

			// 合約函數之參數設定
			BigInteger id = new BigInteger("" + 16888);
			String name = "Allan";
			BigInteger age = new BigInteger("" + 27);

			// 使用合約函數，並取回交易序號
			long startTime = System.currentTimeMillis();
			TransactionReceipt recp = contract.doInsert(id, name, age).send();
			
			long endTime = System.currentTimeMillis();
			System.out.println("執行時間:" + (endTime - startTime) + " ms");
			
			String txnHash = recp.getTransactionHash();
			
			System.out.println("txnHash:" + txnHash);
			System.out.println("blockNum:" + recp.getBlockNumber());
			List<Log> list = recp.getLogs();
			if (list != null && list.size() > 0) {
				for (Log log : list) {
					System.out.println("log data:" + log.getData());
				}
			}

			// 查詢資料
			String rtnName = contract.queryName(id).send();
			System.out.println("name:" + rtnName);

			BigInteger rtnAge = contract.queryAge(id).send();
			System.out.println("age:" + rtnAge);

		} catch (Exception e) {
			System.out.println("交易錯誤:" + e);
		}
	}
}

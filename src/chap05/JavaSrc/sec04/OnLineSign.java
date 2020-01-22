package chap05.sec04;

import java.math.BigInteger;

import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

public class OnLineSign {
	public static void main(String[] args) {
		try {

			// 連接區塊鏈節點
			String blockchainNode = "http://127.0.0.1:8080/";
			Admin web3 = Admin.build(new HttpService(blockchainNode));

			// 設定出金的EOA的位址與密碼
			String fromEoA = "0x4CD063815f7f7a26504AE42a3693B4BBDf0B9B1A";
			String eoaPwd = "16888";

			// 對帳號解鎖
			PersonalUnlockAccount personalUnlockAccount = web3.personalUnlockAccount(fromEoA, eoaPwd).sendAsync().get();
			if (personalUnlockAccount.accountUnlocked()) {
				// 設定入金帳號
				String toEOA = "0x2C95ad4f733f133897BF07B48edD60D08Be5Aa93";

				// 設定ETH數量
				BigInteger ethValue = Convert.toWei("100.0", Convert.Unit.ETHER).toBigInteger();

				// 設定nonce亂數
				EthGetTransactionCount ethGetTransactionCount = web3
						.ethGetTransactionCount(fromEoA, DefaultBlockParameterName.LATEST).sendAsync().get();
				BigInteger nonce = ethGetTransactionCount.getTransactionCount();

				// 設定Gas
				BigInteger gasPrice = new BigInteger("" + 1);
				BigInteger gasLimit = new BigInteger("" + 30000);
				Transaction transaction = Transaction.createEtherTransaction(fromEoA, nonce, gasPrice, gasLimit, toEOA,
						ethValue);

				// 發送交易
				org.web3j.protocol.core.methods.response.EthSendTransaction response = web3
						.ethSendTransaction(transaction).sendAsync().get();

				// 取得交易序號
				String transactionHash = response.getTransactionHash();
				System.out.println("交易序號:" + transactionHash);
			}

		} catch (Exception e) {
			System.out.println("交易錯誤:" + e);
		}
	}
}

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

			// �s���϶���`�I
			String blockchainNode = "http://127.0.0.1:8080/";
			Admin web3 = Admin.build(new HttpService(blockchainNode));

			// �]�w�X����EOA����}�P�K�X
			String fromEoA = "0x4CD063815f7f7a26504AE42a3693B4BBDf0B9B1A";
			String eoaPwd = "16888";

			// ��b������
			PersonalUnlockAccount personalUnlockAccount = web3.personalUnlockAccount(fromEoA, eoaPwd).sendAsync().get();
			if (personalUnlockAccount.accountUnlocked()) {
				// �]�w�J���b��
				String toEOA = "0x2C95ad4f733f133897BF07B48edD60D08Be5Aa93";

				// �]�wETH�ƶq
				BigInteger ethValue = Convert.toWei("100.0", Convert.Unit.ETHER).toBigInteger();

				// �]�wnonce�ü�
				EthGetTransactionCount ethGetTransactionCount = web3
						.ethGetTransactionCount(fromEoA, DefaultBlockParameterName.LATEST).sendAsync().get();
				BigInteger nonce = ethGetTransactionCount.getTransactionCount();

				// �]�wGas
				BigInteger gasPrice = new BigInteger("" + 1);
				BigInteger gasLimit = new BigInteger("" + 30000);
				Transaction transaction = Transaction.createEtherTransaction(fromEoA, nonce, gasPrice, gasLimit, toEOA,
						ethValue);

				// �o�e���
				org.web3j.protocol.core.methods.response.EthSendTransaction response = web3
						.ethSendTransaction(transaction).sendAsync().get();

				// ���o����Ǹ�
				String transactionHash = response.getTransactionHash();
				System.out.println("����Ǹ�:" + transactionHash);
			}

		} catch (Exception e) {
			System.out.println("������~:" + e);
		}
	}
}

package chap05.sec04;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

public class OffLineSign {
	public static void main(String[] args) {
		try {
			// �s���϶���`�I
			String blockchainNode = "http://127.0.0.1:8080/";
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// �]�w�X���b��
			String pwd = "16888";
			String keyFile = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";
			Credentials credentials = WalletUtils.loadCredentials(pwd, keyFile);

			// �]�w�J���b��
			String toEOA = "0xDC2801a98e4086b34E6BBaA6BC791D22DeA0e593";

			// �]�wETH�ƶq
			BigInteger ethValue = Convert.toWei("200.0", Convert.Unit.ETHER).toBigInteger();

			// �]�wnonce�ü�
			String fromEoA = "0x4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";
			EthGetTransactionCount ethGetTransactionCount = web3
					.ethGetTransactionCount(fromEoA, DefaultBlockParameterName.LATEST).sendAsync().get();
			BigInteger nonce = ethGetTransactionCount.getTransactionCount();

			// �]�wGas
			BigInteger gasPrice = new BigInteger("" + 1);
			BigInteger gasLimit = new BigInteger("" + 30000);
			
			//�إ�RawTransaction����
			RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, toEOA,
					ethValue);

			// �����i��[ñ�P�[�K
			byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
			String hexValue = Numeric.toHexString(signedMessage);

			// ���X���
			EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();
			String transactionHash = ethSendTransaction.getTransactionHash();
			System.out.println("����Ǹ�:" + transactionHash);

		} catch (Exception e) {
			System.out.println("������~:" + e);
		}
	}
}

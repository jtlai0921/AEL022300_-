package chap06.sec02;

import java.math.BigInteger;
import java.util.List;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import chap06.com.alc.OpenAuction;

public class AuctionDApp {

	public static void main(String[] args) {
		new AuctionDApp();
	}

	//�϶���`�I��}
	private static String blockchainNode = "http://127.0.0.1:8080/";

	// ����X����}
	private static String contractAddr = "0xa95eaac45799954c6c362d733c53b1440a035519";

	// ���_���x�s���|
	private String keyFile01 = "C:\\MyGeth\\node01\\keystore\\UTC--2018-11-25T03-41-09.743521200Z--576b11fb5d5c380fcf973b62c3ab59f19f9300fe";
	private String keyFile02 = "C:\\MyGeth\\node01\\keystore\\UTC--2018-11-14T13-37-02.324633700Z--da85610910365341d3372fa350f865ce50224a91";
	private String keyFile03 = "C:\\MyGeth\\node01\\keystore\\UTC--2018-11-14T13-38-25.785341700Z--acf34ee2ea0eeaca037b8fb9b64d5361f053da9a";

	// �q�u�b�������_��
	private String keyFilbase = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";

	// ���ե�EOA
	String user1 = "0x576B11Fb5D5C380fCF973b62C3aB59f19f9300fE";
	String user2 = "0xDa85610910365341D3372fa350F865Ce50224a91";
	String user3 = "0xacf34EE2EA0EeaCa037b8fB9B64D5361f053DA9a";

	long startTime = 0;
	long endTime = 0;

	public AuctionDApp() {
		// �Ұʩ��
		openAuction(120);

		long startTime2 = 0;
		long endTime2 = 0;

		startTime2 = System.currentTimeMillis();

		// ���ݨ�}�l���
		boolean isAuctionOpen = queryAuction();
		while (!isAuctionOpen) {
			isAuctionOpen = queryAuction();
		}

		// �Ĥ@���v�Ъ̶i��X��
		transferETH(user1, contractAddr, keyFile01, "16888", "10.0");

		endTime2 = System.currentTimeMillis();
		System.out.println("user 1 �X������:" + (endTime2 - startTime2) + " ms");

		try {
			Thread.sleep(1000 * 20 * 1);
		} catch (Exception e) {
		}

		startTime2 = System.currentTimeMillis();

		// �ĤG���v�Ъ̶i��X��
		transferETH(user2, contractAddr, keyFile02, "16888", "20.0");

		endTime2 = System.currentTimeMillis();
		System.out.println("user 2�X������:" + (endTime2 - startTime2) + " ms");

		try {
			Thread.sleep(1000 * 120 * 1);
		} catch (Exception e) {
		}

		// ������次��
		endAuction();
		
		//�d�ߩ�浲�G
		queryAuction();
	}

	// �Ұʩ�次��
	private void openAuction(int timeLimit) {
		try {
			// �s���϶���`�I
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���w���_�ɡA�αb�K����
			String coinBaseFile = keyFile03;
			String myPWD = "16888";
			Credentials credentials = WalletUtils.loadCredentials(myPWD, coinBaseFile);

			// ���o�X���]�q����
			OpenAuction contract = OpenAuction.load(contractAddr, web3, credentials, OpenAuction.GAS_PRICE,
					OpenAuction.GAS_LIMIT);

			// �]�w��������180��
			BigInteger _timeLimit = new BigInteger("" + timeLimit);
			contract.setAuctionStart(_timeLimit).send();
			System.out.println("�]�w���}�l");

		} catch (Exception e) {
			System.out.println("�]�w���}�l,���~:" + e);
		}
	}

	// �������
	private void endAuction() {
		try {
			// �s���϶���`�I
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���w���_�ɡA�αb�K����
			String coinBaseFile = keyFile03;
			String myPWD = "16888";
			Credentials credentials = WalletUtils.loadCredentials(myPWD, coinBaseFile);

			startTime = System.currentTimeMillis();

			// ���o�X���]�q����
			OpenAuction contract = OpenAuction.load(contractAddr, web3, credentials, OpenAuction.GAS_PRICE,
					OpenAuction.GAS_LIMIT);

			// �������
			contract.setAuctionEnd().sendAsync();
			System.out.println("�]�w��浲��");

		} catch (Exception e) {
			System.out.println("�]�w��浲��,���~:" + e);
		}
	}

	// �ǰeETH
	private void transferETH(String fromEOA, String toEOA, String keyFile, String pwd, String eth) {
		try {
			// �s���϶���`�I
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���ҥ[ñ����
			Credentials credentials = WalletUtils.loadCredentials(pwd, keyFile);

			// �]�wETH�ƶq
			BigInteger ethValue = Convert.toWei(eth, Convert.Unit.ETHER).toBigInteger();

			// �]�wnonce�ü�
			EthGetTransactionCount ethGetTransactionCount = web3
					.ethGetTransactionCount(fromEOA, DefaultBlockParameterName.LATEST).sendAsync().get();
			BigInteger nonce = ethGetTransactionCount.getTransactionCount();

			// �]�wGas
			BigInteger gasPrice = new BigInteger("" + 1);
			BigInteger gasLimit = new BigInteger("" + 80000);

			// �إ�RawTransaction����
			RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, toEOA,
					ethValue);

			// �����i��[ñ�P�[�K
			byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
			String hexValue = Numeric.toHexString(signedMessage);

			// ���X���
			EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();

			String txnHash = ethSendTransaction.getTransactionHash();
			System.out.println("�ǰeETH����Ǹ�:" + txnHash);

		} catch (Exception e) {
			System.out.println("transferETH,���~:" + e);
		}
	}

	// �d�ߩ�污�p
	public boolean queryAuction() {
		boolean isAuctionOpen = false;
		try {
			// �s���϶���`�I
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���w���_�ɡA�αb�K����
			String coinBaseFile = keyFilbase;
			String myPWD = "16888";
			Credentials credentials = WalletUtils.loadCredentials(myPWD, coinBaseFile);

			// ���o�X���]�q����
			OpenAuction contract = OpenAuction.load(contractAddr, web3, credentials, OpenAuction.GAS_PRICE,
					OpenAuction.GAS_LIMIT);

			System.out.println("���}�l�ɶ�:" + contract.auctionStart().send());
			isAuctionOpen = contract.startFlg().send();
			System.out.println("���}�l�X��:" + isAuctionOpen);

			System.out.println("��浲���ɶ�:" + contract.auctionLimit().send());
			System.out.println("��浲���X��:" + contract.endFlg().send());

			System.out.println("���q�H:" + contract.highestBidder().send());
			System.out.println("�̰��Ъ�:" + contract.highestBid().send());

		} catch (Exception e) {
			System.out.println("queryAuction ���~:" + e);
		}
		return isAuctionOpen;
	}

}

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
import org.web3j.utils.Convert;

import chap06.com.alc.NewsContract;

public class NewsPOC {

	public static void main(String[] args) {
		new NewsPOC();
	}

	// �϶���`�I��}
	private static String blockchainNode = "http://127.0.0.1:8080/";

	// ����X����}
	private static String contractAddr = "0x9a8512326b0c74ec0fd066e17eb34877d361f790";

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

	public NewsPOC() {
		// createNews(keyFilbase, "16888", "�o�������P�����T���ԡA�禬����I�h");

		// �h��\ť�̹�s�D�i����y
		// rewardNews(keyFile01, "16888", 5, 2);

		// �ĤG��\ť�̶i����y
		// rewardNews(keyFile02, "16888", 5, 10);

		// �d�߫��w���s�D
		queryNews(keyFilbase, "16888", 5);
	}

	// �إߤ@�h�s�D
	private void createNews(String keyFile, String myPWD, String newCtx) {
		try {
			// �s���϶���`�I
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���w���_�ɡA�αb�K����
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);
			System.out.println("��������");

			int attemptsPerTxHash = 30;
			long frequency = 1000;

			// �O������}�l�ɶ�
			startTime = System.currentTimeMillis();

			// �إߥ���B�z��
			TransactionReceiptProcessor myProcessor = new QueuingTransactionReceiptProcessor(web3,
					new NewsCreateCallBack(), attemptsPerTxHash, frequency);

			// �إߥ���޲z��
			TransactionManager transactionManager = new RawTransactionManager(web3, credentials, ChainId.NONE,
					myProcessor);
			System.out.println("�إߥ���޲z��");

			// ���o�X���]�q����
			NewsContract contract = NewsContract.load(contractAddr, web3, transactionManager, NewsContract.GAS_PRICE,
					NewsContract.GAS_LIMIT);
			System.out.println("���o�X��");

			// �[�J�@�h�s���s�D
			contract.addNews(newCtx).sendAsync();
			System.out.println("�s�W�s�D");
		} catch (Exception e) {
			System.out.println("�إ߷s�D���~,���~:" + e);
		}
	}

	// ���y�@�h�s�D
	private void rewardNews(String keyFile, String myPWD, int newsKey, int eth) {
		try {
			// �s���϶���`�I
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���w���_�ɡA�αb�K����
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);

			// ���o�X���]�q����
			NewsContract contract = NewsContract.load(contractAddr, web3, credentials, NewsContract.GAS_PRICE,
					NewsContract.GAS_LIMIT);

			// ���y�@�h�s�D
			BigInteger weiValue = Convert.toWei("" + eth, Convert.Unit.ETHER).toBigInteger();
			contract.rewardNews(new BigInteger("" + newsKey), weiValue).send();

		} catch (Exception e) {
			System.out.println("���y�s�D���~,���~:" + e);
		}
	}

	// �d�ߤ@�h�s�D
	private void queryNews(String keyFile, String myPWD, int newsKey) {
		try {
			// �s���϶���`�I
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���w���_�ɡA�αb�K����
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);
			System.out.println("��������");

			// �O������}�l�ɶ�
			startTime = System.currentTimeMillis();

			// ���o�X���]�q����
			NewsContract contract = NewsContract.load(contractAddr, web3, credentials, NewsContract.GAS_PRICE,
					NewsContract.GAS_LIMIT);
			System.out.println("���o�X��");

			// �d�߷s�D�O�_�s�b
			Boolean isExist = contract.isNewsExist(new BigInteger("" + newsKey)).send();
			System.out.println("�s�D�O�_�s�b:" + isExist.booleanValue());

			// �d�߷s�D���e
			String newsCxt = contract.queryCtx(new BigInteger("" + newsKey)).send();
			System.out.println("�s�D���e:" + newsCxt);

			// �d�߷s�D�ֿn���y
			BigInteger reward = contract.queryReward(new BigInteger("" + newsKey)).send();
			System.out.println("�s�D���ֿn���y:" + reward);

		} catch (Exception e) {
			System.out.println("�d�߷s�D���~,���~:" + e);
		}
	}
}

// �B�z��ƨ㦳��Ʀ^�ǭ�
class NewsCreateCallBack implements Callback {
	// ����Q�������^�s���
	public void accept(TransactionReceipt recp) {

		// �w�q��Ʀ^�ǭ�
		Function function = new Function("", Collections.<Type>emptyList(), Arrays.asList(new TypeReference<Uint>() {
		}));

		// ���o�^�ǭ�
		List<Log> list = recp.getLogs();
		List<Type> nonIndexedValues = FunctionReturnDecoder.decode(list.get(0).getData(),
				function.getOutputParameters());

		// �Ĥ@�Ӧ^�ǭȬOuint
		BigInteger newsKey = (BigInteger) nonIndexedValues.get(0).getValue();
		System.out.println("news ID:" + newsKey.intValue());
	}

	public void exception(Exception exception) {
		System.out.println("�������, err:" + exception);
	}
}
package chap06.sec06;

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

import chap06.com.alc.EthPump;

public class PumpOwner {

	public static void main(String[] args) {
		new PumpOwner();
	}

	// �϶���`�I��}
	private static String blockchainNode = "http://127.0.0.1:8080/";

	// ����X����}
	private static String contractAddr = "0xe42481327a9a4386eb7cbabf495794ca897fdedd";

	// �X���D���HEOA
	private String host = "0x4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";

	// �X���D���H���_��
	private String hostKey = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";

	// �X�ɤHEOA
	private String owner = "0x576B11Fb5D5C380fCF973b62C3aB59f19f9300fE";

	// �X�ɤH���_��
	private String ownerKey = "C:\\MyGeth\\node01\\keystore\\UTC--2018-11-25T03-41-09.743521200Z--576b11fb5d5c380fcf973b62c3ab59f19f9300fe";

	// �ɥΤHEOA
	String borrower = "0xDa85610910365341D3372fa350F865Ce50224a91";

	// �ɥΤH���_��
	private String borrowerKey = "C:\\MyGeth\\node01\\keystore\\UTC--2018-11-14T13-37-02.324633700Z--da85610910365341d3372fa350f865ce50224a91";

	public PumpOwner() {
		// �s�W���~
		//insertGoods(ownerKey, "16888", "bicycle", "10", true);

		// �]�w���~�k��
		doGoodsReturn(ownerKey, "16888", "bicycle", 1);
	}

	// �s�W���~��T
	private void insertGoods(String keyFile, String myPWD, String stickName, String eth, boolean available) {
		try {
			// �s���϶���`�I
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���w���_�ɡA�αb�K����
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);
			System.out.println("��������");

			int attemptsPerTxHash = 30;
			long frequency = 1000;

			// �إߥ���B�z��
			TransactionReceiptProcessor myProcessor = new QueuingTransactionReceiptProcessor(web3,
					new AddGoodsCallBack(), attemptsPerTxHash, frequency);

			// �إߥ���޲z��
			TransactionManager transactionManager = new RawTransactionManager(web3, credentials, ChainId.NONE,
					myProcessor);
			System.out.println("�إߥ���޲z��");

			// ���o�X���]�q����
			EthPump contract = EthPump.load(contractAddr, web3, transactionManager, EthPump.GAS_PRICE,
					EthPump.GAS_LIMIT);
			System.out.println("���o�X��");

			// �]�wETH�ƶq
			BigInteger weiValue = Convert.toWei(eth, Convert.Unit.ETHER).toBigInteger();

			// �[�J�@�����~
			contract.addGoods(stickName, weiValue, available).send();
			System.out.println("�s�W���~����");

		} catch (Exception e) {
			System.out.println("�s�W���~,���~:" + e);
		}
	}

	// �k�٪��~
	private void doGoodsReturn(String keyFile, String myPWD, String stickName, int inx) {
		try {
			// �s���϶���`�I
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���w���_�ɡA�αb�K����
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);
			System.out.println("��������");

			// ���o�X���]�q����
			EthPump contract = EthPump.load(contractAddr, web3, credentials, EthPump.GAS_PRICE, EthPump.GAS_LIMIT);
			System.out.println("���o�X��");

			// �]�w���~�k��
			contract.doGoodsReturn(stickName, new BigInteger("" + inx)).send();
			System.out.println("�]�w���~�k�٧���");

		} catch (Exception e) {
			System.out.println("�]�w���~�k��,���~:" + e);
		}
	}

}

// �B�z��ƨ㦳��Ʀ^�ǭ�
class AddGoodsCallBack implements Callback {
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
		System.out.println("�s�W���~�D��(�Ǹ�):" + newsKey.longValue());
	}

	public void exception(Exception exception) {
		System.out.println("�s�W���~�D��(�Ǹ�)�^��, err:" + exception);
	}
}
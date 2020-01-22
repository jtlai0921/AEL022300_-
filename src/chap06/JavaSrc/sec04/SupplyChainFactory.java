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

	// �϶���`�I��}
	private static String blockchainNode = "http://127.0.0.1:8080/";

	// ����X����}
	private static String contractAddr = "0x069ce65305532f6e125366a9f98b90de511ff4e1";

	// �Ȧ���_��
	private String bankKey = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";

	// �Ȧ�EOA
	String bank = "0x4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";

	// �s�y�Ӫ��_��
	private String factoryKey = "C:\\MyGeth\\node01\\keystore\\UTC--2018-11-25T03-41-09.743521200Z--576b11fb5d5c380fcf973b62c3ab59f19f9300fe";

	// �s�y��EOA
	String factory = "0x576B11Fb5D5C380fCF973b62C3aB59f19f9300fE";

	// ������EOA
	String supplier = "0xDa85610910365341D3372fa350F865Ce50224a91";

	public SupplyChainFactory() {
		// step1. �W�Ǥ@������������T
		insSupplyTrans(factoryKey, "16888", "ABC888", "�ʶR�����]��", supplier, 200);
	}

	// �s�W��������
	private void insSupplyTrans(String keyFile, String myPWD, String transNo, String transMemo, String supplier,
			long transValue) {
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
					new InsTransCallBack(), attemptsPerTxHash, frequency);

			// �إߥ���޲z��
			TransactionManager transactionManager = new RawTransactionManager(web3, credentials, ChainId.NONE,
					myProcessor);
			System.out.println("�إߥ���޲z��");

			// ���o�X���]�q����
			SupplyChainContract contract = SupplyChainContract.load(contractAddr, web3, transactionManager,
					SupplyChainContract.GAS_PRICE, SupplyChainContract.GAS_LIMIT);
			System.out.println("���o�X��");

			// �[�J�@����������
			contract.insSupplyTrans(transNo, transMemo, supplier, new BigInteger("" + transValue)).sendAsync();			
			System.out.println("�s�W��������");
		} catch (Exception e) {
			System.out.println("�s�W�����������~,���~:" + e);
		}
	}	
}

// �B�z��ƨ㦳��Ʀ^�ǭ�
class InsTransCallBack implements Callback {
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
		System.out.println("���������D��:" + newsKey.intValue());
	}

	public void exception(Exception exception) {
		System.out.println("�������, err:" + exception);
	}
}
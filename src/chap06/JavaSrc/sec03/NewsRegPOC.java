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

	// �϶���`�I��}
	private static String blockchainNode = "http://127.0.0.1:8080/";

	// ����X����}
	private static String contractAddr = "0x7d9a6b46ea40683393ffa40327f5fbd5ceeaab3f";

	// �q�u�b�������_��
	private String keyFilbase = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";

	long startTime = 0;
	long endTime = 0;

	public NewsRegPOC() {
		//���U�@�ӷs�D�W�D
		//regNews(keyFilbase, "16888", "0x298a71b8d049ccf6ee8cb6c9d5c31136e47f9e96", "�u�۷s�D��");
		
		// �d�߫��w���s�D�W�D
		queryContract(keyFilbase, "16888", "0x9a8512326b0c74ec0fd066e17eb34877d361f790"); 
	}

	// ���U�@�h�s�D
	private void regNews(String keyFile, String myPWD, String newsContractAddr, String newsContractName) {
		try {
			// �s���϶���`�I
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���w���_�ɡA�αb�K����
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);

			int attemptsPerTxHash = 30;
			long frequency = 1000;

			// �O������}�l�ɶ�
			startTime = System.currentTimeMillis();

			// �إߥ���B�z��
			TransactionReceiptProcessor myProcessor = new QueuingTransactionReceiptProcessor(web3,
					new NewsRegCallBack(), attemptsPerTxHash, frequency);

			// �إߥ���޲z��
			TransactionManager transactionManager = new RawTransactionManager(web3, credentials, ChainId.NONE,
					myProcessor);

			// ���o�X���]�q����
			RegisterContract contract = RegisterContract.load(contractAddr, web3, transactionManager,
					RegisterContract.GAS_PRICE, RegisterContract.GAS_LIMIT);

			// �[�J�@�h�s���s�D
			contract.regContract(newsContractAddr, newsContractName).sendAsync();
			
		} catch (Exception e) {
			System.out.println("�إ߷s�D���~,���~:" + e);
		}
	}

	// �d�߷s�D�W�D
	private void queryContract(String keyFile, String myPWD, String newsContractAddr) {
		try {
			// �s���϶���`�I
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���w���_�ɡA�αb�K����
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);

			// ���o�X���]�q����
			RegisterContract contract = RegisterContract.load(contractAddr, web3, credentials,
					RegisterContract.GAS_PRICE, RegisterContract.GAS_LIMIT);

			// �d�߬O�_�s�b
			Boolean isExist = contract.isContractExist(newsContractAddr).send();
			System.out.println("�s�D�W�D�O�_�s�b:" + isExist.booleanValue());

			// �d�߷s�D�ƶq
			BigInteger newsCnt = contract.contractCnt().send();
			System.out.println("���U�s�D�ƶq:" + newsCnt);

			// �d�߷s�D�W�D
			String newsName = contract.addrToNameMapping(newsContractAddr).send();
			System.out.println("�s�D�W�D�W��:" + newsName);

		} catch (Exception e) {
			System.out.println("�d�߷s�D�W�D���~,���~:" + e);
		}
	}
}

// �B�z��ƨ㦳��Ʀ^�ǭ�
class NewsRegCallBack implements Callback {
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
		System.out.println("news reg ID:" + nonIndexedValues.get(0).getValue());
	}

	public void exception(Exception exception) {
		System.out.println("�������, err:" + exception);
	}
}
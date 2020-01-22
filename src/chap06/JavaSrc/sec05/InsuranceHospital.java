package chap06.sec05;

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

import chap06.com.alc.InsuranceContract;
import chap06.com.alc.SupplyChainContract;

public class InsuranceHospital {

	public static void main(String[] args) {
		new InsuranceHospital();
	}

	// �϶���`�I��}
	private static String blockchainNode = "http://127.0.0.1:8080/";

	// ����X����}
	private static String contractAddr = "0xc6a3fb214038e574fff84d358eb080d3200c5fe3";

	// �O�I���q���_��
	private String insuranceCorpKey = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";

	// �O�I���qEOA
	String insuranceCorp = "0x4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";

	// ��|���_��
	private String hospitalKey = "C:\\MyGeth\\node01\\keystore\\UTC--2018-11-25T03-41-09.743521200Z--576b11fb5d5c380fcf973b62c3ab59f19f9300fe";

	// ��|EOA
	String hospital = "0x576B11Fb5D5C380fCF973b62C3aB59f19f9300fE";

	// �O�I�Ȥ�EOA
	String patient = "0xDa85610910365341D3372fa350F865Ce50224a91";

	public InsuranceHospital() {
		// step1. �s�W�f�H��T
		//insPatient(hospitalKey, "16888", patient, "Jackie", "TPE");

		// step 2. �s�W�f��
		insRecord(hospitalKey, "16888", patient, "�ݴe", "��Ŧ�f�}�M", 10, 36000);
	}

	// �s�W�f�H��T
	private void insPatient(String keyFile, String myPWD, String patient, String name, String addr) {
		try {
			// �s���϶���`�I
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���w���_�ɡA�αb�K����
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);
			System.out.println("��������");

			// ���o�X���]�q����
			InsuranceContract contract = InsuranceContract.load(contractAddr, web3, credentials,
					InsuranceContract.GAS_PRICE, InsuranceContract.GAS_LIMIT);
			System.out.println("���o�X��");

			// �[�J�@���f�H���
			contract.insPatient(patient, name, addr).send();

			System.out.println("�s�W�f�H��T");
		} catch (Exception e) {
			System.out.println("�s�W�f�H��T���~,���~:" + e);
		}
	}

	// �s�W��������
	private void insRecord(String keyFile, String myPWD, String patient, String symptom, String cause, int day, int money) {
		try {
			// �s���϶���`�I
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���w���_�ɡA�αb�K����
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);
			System.out.println("��������");

			// ���o�X���]�q����
			InsuranceContract contract = InsuranceContract.load(contractAddr, web3, credentials,
					InsuranceContract.GAS_PRICE, InsuranceContract.GAS_LIMIT);
			System.out.println("���o�X��");

			// �[�J�@�����|��T
			contract.insRecord(patient, symptom, cause, new BigInteger("" + day), new BigInteger("" + money)).send();
			System.out.println("�s�W���|��T");
			
		} catch (Exception e) {
			System.out.println("�s�W���|��T���~,���~:" + e);
		}
	}
}

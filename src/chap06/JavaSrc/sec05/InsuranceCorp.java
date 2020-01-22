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
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import chap06.com.alc.InsuranceContract;
import rx.Subscription;
import rx.functions.Action1;

public class InsuranceCorp {

	public static void main(String[] args) {
		new InsuranceCorp();
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

	public InsuranceCorp() {
		// step1. �O�I���q�]�w��|EOA
		initHospital(insuranceCorpKey, "16888", hospital);

		// step2. ��ť���|�ӽШƥ�
		startOracle(contractAddr);		
	}

	// �]�w��|
	private void initHospital(String keyFile, String myPWD, String hospital) {
		try {
			// �s���϶���`�I
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���w���_�ɡA�αb�K����
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);

			// ���o�X���]�q����
			InsuranceContract contract = InsuranceContract.load(contractAddr, web3, credentials,
					InsuranceContract.GAS_PRICE, InsuranceContract.GAS_LIMIT);

			// �]�w��|��}
			contract.setHospital(hospital).send();
			System.out.println("�]�w��|,����");

		} catch (Exception e) {
			System.out.println("�]�w��|���~,���~:" + e);
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
			BigInteger gasLimit = new BigInteger("" + 30000);

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

	// �Ұ�Oracle�A��
	public void startOracle(String contractAddr) {
		try {
			// �s�u�϶���`�I

			Admin web3 = Admin.build(new HttpService(blockchainNode));

			// �]�w�L�o����
			EthFilter filter = new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST,
					contractAddr);

			// ���o�ƥ�topic��hash code
			String eventTopicHash = Hash.sha3String("insRecord");

			// ����ƥ�Log
			Function transLog = new Function("", Collections.<Type>emptyList(),
					Arrays.asList(new TypeReference<Uint>() {
					}, new TypeReference<Uint>() {
					}, new TypeReference<Uint>() {
					}, new TypeReference<Uint>() {
					}));

			System.out.println("Oracle service start...");
			
			// ���򰻴��ƥ�
			Subscription subscription = web3.ethLogObservable(filter).subscribe(new Action1<Log>() {
				public void call(Log log) {
					List<String> list = log.getTopics();
					// ���ߨƥ󤤪�Topic
					for (String topic : list) {
						if (topic.equals(eventTopicHash)) {
							System.out.println("�B�z����ƥ�");
							handleTransEvent(log, transLog);
						}
					}
				}
			});
		} catch (Exception e) {
			System.out.println("Oracle�������~:" + e);
		}
	}

	// �B�z�ƥ󤺮e
	private void handleTransEvent(Log log, Function function) {
		try {
			List<Type> nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), function.getOutputParameters());
			int inx = 0;
			String address = ""; // �O�I�Ȥ�EOA
			BigInteger recordInx = BigInteger.ZERO; // �f���Ǹ�
			BigInteger days = BigInteger.ZERO; // ��|�Ѽ�
			BigInteger money = BigInteger.ZERO; // ��|���B

			for (Type type : nonIndexedValues) {
				// �Ĥ@�ӰѼƬOaddress
				if (inx == 0) {
					try {
						// �N��}�A�ഫ��16�i��r��
						address = Numeric.toHexStringWithPrefix((BigInteger) type.getValue());
					} catch (Exception e) {
					}
				}

				// �ĤG�ӰѼƬO�f���Ǹ�
				if (inx == 1) {
					recordInx = (BigInteger) type.getValue();
				}

				// �ĤT�ӰѼƬO��|�Ѽ�
				if (inx == 2) {
					days = (BigInteger) type.getValue();
				}

				// �ĥ|�ӰѼƬO��|���B
				if (inx == 3) {
					money = (BigInteger) type.getValue();
				}
				inx++;
			}
			
			System.out.println("�O�I���q�H:" + address);
			System.out.println("�f���Ǹ�:" + recordInx.longValueExact());
			System.out.println("��|�Ѽ�:" + days.longValueExact());
			System.out.println("��|���B:" + money.longValueExact());
			
			// ���ڲz�ߪ�
			String payMoney = "" + (money.longValueExact()/1000);
			transferETH(insuranceCorp, address, insuranceCorpKey, "16888", payMoney);

		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}
}
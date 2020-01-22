package chap06.sec04;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
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
import org.web3j.tuples.generated.Tuple8;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import chap06.com.alc.SupplyChainContract;
import rx.Subscription;
import rx.functions.Action1;

public class SupplyChainBank {

	public static void main(String[] args) {
		new SupplyChainBank();
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

	//������EOA
	String supplier = "0xDa85610910365341D3372fa350F865Ce50224a91";
	
	public SupplyChainBank() {
		// step1. �Ȧ�]�w�s�y��
		initFactory(bankKey, "16888", factory);

		// step2. �s��l�B�󴼯�X��
		transferETH(bank, contractAddr, bankKey, "16888", "200");

		// step3. �Ȧ�i��ƥ��ť
		// step4. ��ť�ƥ��A���o�����T
		// step5. �i���ڪ��ʧ@
		startOracle(contractAddr);

		// step6. �����ӽT�{����
	}

	// �]�w�s�y��
	private void initFactory(String keyFile, String myPWD, String factory) {
		try {
			// �s���϶���`�I
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���w���_�ɡA�αb�K����
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);

			// ���o�X���]�q����
			SupplyChainContract contract = SupplyChainContract.load(contractAddr, web3, credentials,
					SupplyChainContract.GAS_PRICE, SupplyChainContract.GAS_LIMIT);

			// �]�w�s�y�Ӧ�}
			contract.setFactory(factory).send();
			System.out.println("�]�w�s�y��,����");

		} catch (Exception e) {
			System.out.println("�]�w�s�y�ӿ��~,���~:" + e);
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
			EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST,
					contractAddr);

			// ���o�ƥ�topic��hash code
			String eventTopicHash = Hash.sha3String("TransIns");

			// ����ƥ�Log
			Function transLog = new Function("", Collections.<Type>emptyList(),
					Arrays.asList(new TypeReference<Uint>() {
					}));

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

	// �B�z����ƥ�
	private void handleTransEvent(Log log, Function function) {
		try {
			List<Type> nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), function.getOutputParameters());
			int inx = 0;
			long transKey = 0l; // ���ID
			for (Type type : nonIndexedValues) {
				System.out.println("Type String:" + type.getTypeAsString());
				System.out.println("Type Value:" + type.getValue());
				if (inx == 0) {
					// �^�Ǫ��ѼơA�D�O����s��
					try {
						transKey = ((BigInteger) type.getValue()).longValue();
					} catch (Exception e) {
						System.out.println("convert error:" + e);
					}
				}
				inx++;
			}

			// �P�_���������O�_�s�b
			Long transValueObj = querySupplyChainTrans(bankKey, "16888", transKey);
			if (transValueObj != null && transValueObj.longValue() > 0) {
				// ������
				System.out.println("�ǳƶi����");
				executeLoan(bankKey, "16888", transKey, transValueObj.longValue());
			} else {
				// ��������
				System.out.println("������s�b,���i����");
			}

		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}

	// �d�ߨ�������
	private Long querySupplyChainTrans(String keyFile, String myPWD, long transKey) {
		Long transValueObj = null;
		try {
			// �s���϶���`�I
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���w���_�ɡA�αb�K����
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);

			// ���o�X���]�q����
			SupplyChainContract contract = SupplyChainContract.load(contractAddr, web3, credentials,
					SupplyChainContract.GAS_PRICE, SupplyChainContract.GAS_LIMIT);

			// �d�ߥ���O�_�s�b
			if (contract.isTransExist(new BigInteger("" + transKey)).send()) {
				System.out.println("���������s�b");

				// ���o�������
				Tuple8 transData = contract.transData(new BigInteger("" + transKey)).send();

				// ����̳�s��
				String transNo = (String) transData.getValue1();

				// �������
				String transMemo = (String) transData.getValue2();

				// ������
				String supplier = (String) transData.getValue3();

				// ����ɶ�
				BigInteger transTime = (BigInteger) transData.getValue4();

				// ���������B
				BigInteger transValue = (BigInteger) transData.getValue5();
				transValueObj = transValue.longValue();

				// ��ڮɶ�
				BigInteger loanTime = (BigInteger) transData.getValue6();

				// ��ڪ��B
				BigInteger loanValue = (BigInteger) transData.getValue7();

				// ����s�b�X��
				Boolean exist = (Boolean) transData.getValue8();

				// �ɶ��e�{�榡
				SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Calendar bolckTimeCal = Calendar.getInstance();

				System.out.println("����̳�s��:" + transNo);
				System.out.println("�������:" + transMemo);
				System.out.println("������:" + supplier);

				bolckTimeCal.setTimeInMillis(transTime.longValueExact() * 1000);
				System.out.println("����ɶ�:" + timeFormat.format(bolckTimeCal.getTime()));

				System.out.println("���������B:" + transValue);

				bolckTimeCal.setTimeInMillis(loanTime.longValueExact() * 1000);
				System.out.println("��ڮɶ�:" + timeFormat.format(bolckTimeCal.getTime()));

				System.out.println("��ڪ��B:" + loanValue.longValue());
				System.out.println("����s�b�X��:" + exist);
			} else {
				System.out.println("�����������s�b");
				transValueObj = null;
			}

		} catch (Exception e) {
			System.out.println("�d�ߨ����������~,���~:" + e);
		}
		return transValueObj;
	}

	// ������
	private void executeLoan(String keyFile, String myPWD, long transKey, long transValue) {
		try {
			// �s���϶���`�I
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���w���_�ɡA�αb�K����
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);

			// ���o�X���]�q����
			SupplyChainContract contract = SupplyChainContract.load(contractAddr, web3, credentials,
					SupplyChainContract.GAS_PRICE, SupplyChainContract.GAS_LIMIT);

			// �d�ߥ���O�_�s�b
			Boolean isExist = contract.isTransExist(new BigInteger("" + transKey)).send();
			if (isExist) {
				System.out.println("���������s�b�A�ǳƶi����");

				// �p�����B��
				long loanValue = transValue / 10;
				BigInteger weiValue = Convert.toWei("" + loanValue, Convert.Unit.ETHER).toBigInteger();
				
				// ������
				contract.loanEth(new BigInteger("" + transKey), new BigInteger("" + weiValue)).send();
				System.out.println("�������");

			} else {
				System.out.println("�����������s�b");
			}

		} catch (Exception e) {
			System.out.println("�����ڿ��~,���~:" + e);
		}
	}
}
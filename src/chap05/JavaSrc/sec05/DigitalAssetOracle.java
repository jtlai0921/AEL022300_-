package chap05.sec05;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import chap05.com.alc.DigitalAssetContract;
import rx.Subscription;
import rx.functions.Action1;

public class DigitalAssetOracle {

	public static void main(String[] args) {
		new DigitalAssetOracle();
	}

	private static String blockchainNode = "http://127.0.0.1:8080/";

	// ���o�X���]�q����
	private static String contractAddr = "0x0a60d53892adefcbe216178c27dfb43164491c79";

	public DigitalAssetOracle() {
		try {
			// �s�u�϶���`�I

			Admin web3 = Admin.build(new HttpService(blockchainNode));

			// �]�w�L�o����
			EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST,
					contractAddr);

			// �ثe�|�L�k�ϥ�
			// filter.addSingleTopic(topicStrHash);
			// filter.addOptionalTopics(topicStrHash);

			// �uť��topic �� buy ���ƥ�
			String buy_topicHash = Hash.sha3String("buy");
			System.out.println("�L�otopic(buy) hash:" + buy_topicHash);

			// Buy�ƥ�Log���e�A�Ĥ@�ӬOaddress�A�ĤG�ӬOuint256
			Function buyLog = new Function("", Collections.<Type>emptyList(), Arrays.asList(new TypeReference<Uint>() {
			}, new TypeReference<Uint>() {
			}));

			String err_topicHash = Hash.sha3String("err");
			System.out.println("�L�otopic(err) hash:" + err_topicHash);

			Function errLog = new Function("", Collections.<Type>emptyList(), Arrays.asList(new TypeReference<Uint>() {
			}, new TypeReference<Uint>() {
			}));

			String set_topicHash = Hash.sha3String("set");
			System.out.println("�L�otopic(set) hash:" + set_topicHash);

			// Set�ƥ�Log���e�A�Ĥ@�ӬOaddress�A�ĤG�ӬOuint256
			Function setLog = new Function("", Collections.<Type>emptyList(), Arrays.asList(new TypeReference<Uint>() {
			}, new TypeReference<Utf8String>() {
			}));
			
			// ���򰻴��ƥ�
			Subscription subscription = web3.ethLogObservable(filter).subscribe(new Action1<Log>() {
				public void call(Log log) {
					List<String> list = log.getTopics();

					// ���ߨƥ󤤪�Topic
					for (String topic : list) {
						System.out.println("topic:" + topic);
						System.out.println("�X����}:" + log.getAddress() + "," + log.getData());

						if (topic.equals(buy_topicHash)) {
							System.out.println("�B�zBuy�ƥ�");
							handleBuyEvent(log, buyLog);
						}

						if (topic.equals(err_topicHash)) {
							System.out.println("�B�zError�ƥ�");
							handleErrEvent(log, errLog);
						}

						if (topic.equals(set_topicHash)) {
							System.out.println("�]�w�ƥ�");
							handleSetEvent(log, setLog);
						}
					}
					
					System.out.println("=========================");
				}
			});

			// ������ť
			subscription.unsubscribe();
		} catch (Exception e) {
			System.out.println("Oracle���~:" + e);
		}
	}

	// �B�zBuy�ƥ�
	private void handleBuyEvent(Log log, Function buyLog) {
		try {
			List<Type> nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), buyLog.getOutputParameters());
			int inx = 0;
			String address = ""; // �ʶR�b��EOA
			BigInteger money = BigInteger.ZERO; // �ʶR���B

			for (Type type : nonIndexedValues) {
				System.out.println("Type String:" + type.getTypeAsString());
				System.out.println("Type Value:" + type.getValue());
				if (inx == 0) {
					// �Ĥ@�ӰѼƬOaddress
					try {
						// �N��}�A�ഫ��16�i��r��
						address = Numeric.toHexStringWithPrefix((BigInteger) type.getValue());
					} catch (Exception e) {
						System.out.println("convert error:" + e);
					}
				} else {
					// �ĤG�ӰѼƬO�ǤJ�����B
					money = (BigInteger) type.getValue();
				}
				inx++;
			}

			System.out.println("Buy�ƥ�A�ʶREOA:" + address);
			System.out.println("Buy�ƥ�A�ʶR���B:" + money);

			// �g�J���ʶR���Ʀ�겣
			callbackContract(address, "PO_ABC16888");

		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}

	// �B�zSet�ƥ�
	private void handleSetEvent(Log log, Function setLog) {
		try {
			List<Type> nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), setLog.getOutputParameters());
			int inx = 0;
			String address = ""; // �ʶR�b��EOA
			String license = ""; // �Ʀ�겣

			for (Type type : nonIndexedValues) {
				System.out.println("Type String:" + type.getTypeAsString());
				System.out.println("Type Value:" + type.getValue());
				if (inx == 0) {
					// �Ĥ@�ӰѼƬOaddress
					try {
						// �N��}�A�ഫ��16�i��r��
						address = Numeric.toHexStringWithPrefix((BigInteger) type.getValue());
					} catch (Exception e) {
						System.out.println("convert error:" + e);
					}
				} else {
					// �ĤG�ӰѼƬO�Ʀ�겣
					license = (String) type.getValue();
				}
				inx++;
			}

			System.out.println("Set�ƥ�A�ʶREOA:" + address);
			System.out.println("Set�ƥ�A�Ʀ�겣:" + license);
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}

	// �B�zErr�ƥ�
	private void handleErrEvent(Log log, Function errLog) {
		try {
			List<Type> nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), errLog.getOutputParameters());
			int inx = 0;
			String address = ""; // �ʶR�b��EOA
			BigInteger money = BigInteger.ZERO; // �ʶR���B

			for (Type type : nonIndexedValues) {
				System.out.println("Type String:" + type.getTypeAsString());
				System.out.println("Type Value:" + type.getValue());
				if (inx == 0) {
					// �Ĥ@�ӰѼƬOaddress
					try {
						// �N��}�A�ഫ��16�i��r��
						address = Numeric.toHexStringWithPrefix((BigInteger) type.getValue());
					} catch (Exception e) {
						System.out.println("convert error:" + e);
					}
				} else {
					// �ĤG�ӰѼƬO�ǤJ�����B
					money = (BigInteger) type.getValue();
				}
				inx++;
			}

			System.out.println("Err�ƥ�A�ʶREOA:" + address);
			System.out.println("Err�ƥ�A�ʶR���B:" + money);
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}

	private void callbackContract(String owner, String license) {
		try {
			// �s���϶���`�I
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���w���_�ɡA�αb�K����
			String coinBaseFile = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";
			String myPWD = "16888";
			Credentials credentials = WalletUtils.loadCredentials(myPWD, coinBaseFile);

			// �ǥѦX���]�q����A�i��G�p
			long startTime = System.currentTimeMillis();

			// ���o�X���]�q����
			DigitalAssetContract contract = DigitalAssetContract.load(contractAddr, web3, credentials,
					DigitalAssetContract.GAS_PRICE, DigitalAssetContract.GAS_LIMIT);

			// �]�w�����H�P�Ʀ�겣
			contract.setOwner(owner, license).send();

			long endTime = System.currentTimeMillis();
			System.out.println("�X������ɶ�:" + (endTime - startTime) + " ms");

		} catch (Exception e) {
			System.out.println("������~:" + e);
		}
	}
}

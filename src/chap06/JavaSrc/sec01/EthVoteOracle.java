package chap06.sec01;

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
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import chap06.com.alc.EthVoting;
import rx.Subscription;
import rx.functions.Action1;

public class EthVoteOracle {
	public static void main(String[] args) {
		new EthVoteOracle();
	}

	private static String blockchainNode = "http://127.0.0.1:8080/";

	// ���o�X���]�q����
	private static String contractAddr = "0x969df30e59d0ac27a012145a3d15627611a9c82e";

	public EthVoteOracle() {
		try {
			// �s�u�϶���`�I

			Admin web3 = Admin.build(new HttpService(blockchainNode));

			// �]�w�L�o����
			EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST,
					contractAddr);

			String vote_topicHash = Hash.sha3String("vote");
			System.out.println("�L�otopic(vote) hash:" + vote_topicHash);

			// ��ĳ�ƥ�Log�AEOA�Ptimestamp
			Function voteLog = new Function("", Collections.<Type>emptyList(), Arrays.asList(new TypeReference<Uint>() {
			}, new TypeReference<Uint>() {
			}));

			String propose_topicHash = Hash.sha3String("propose");
			System.out.println("�L�otopic(propose) hash:" + propose_topicHash);

			// ���רƥ�Log�AEOA�Ptimestamp
			Function proposeLog = new Function("", Collections.<Type>emptyList(),
					Arrays.asList(new TypeReference<Uint>() {
					}, new TypeReference<Uint>() {
					}));

			// ���򰻴��ƥ�
			Subscription subscription = web3.ethLogObservable(filter).subscribe(new Action1<Log>() {
				public void call(Log log) {
					List<String> list = log.getTopics();

					// ���ߨƥ󤤪�Topic
					for (String topic : list) {
						System.out.println("topic:" + topic);
						System.out.println("�X����}:" + log.getAddress() + "," + log.getData());

						if (topic.equals(vote_topicHash)) {
							System.out.println("�B�z��ĳ�ƥ�");
							handleVoteEvent(log, voteLog);
						}

						if (topic.equals(propose_topicHash)) {
							System.out.println("�B�z���רƥ�");
							handleProposeEvent(log, proposeLog);
						}
					}

					System.out.println("=========================");
				}
			});
		} catch (Exception e) {
			System.out.println("������~:" + e);
		}
	}

	// �B�z��ĳ�ƥ�
	private void handleVoteEvent(Log log, Function function) {
		try {
			List<Type> nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), function.getOutputParameters());
			int inx = 0;
			String address = ""; // ��ĳEOA
			BigInteger timestamp = BigInteger.ZERO; // ��ĳ�ɶ�

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
					// �ĤG�ӰѼƬO��ĳ�ɶ�
					timestamp = (BigInteger) type.getValue();
				}
				inx++;
			}

			// �N���o���϶��ɶ��A�ഫ����Ū���ɶ��榡
			Calendar bolckTimeCal = Calendar.getInstance();
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

			// �N�H����쪺���G�A���W1000�A���ܦ��ʸU�D���@��e�{
			bolckTimeCal.setTimeInMillis(timestamp.longValueExact() * 1000);

			// ��ܰ��浲�G
			System.out.println("��ĳ�ƥ�AEOA:" + address);
			System.out.println("�϶��ɶ�(UNIX):" + timestamp.longValueExact());
			System.out.println("�϶��ɶ�(���iŪ��):" + timeFormat.format(bolckTimeCal.getTime()));
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}

	// �B�z���רƥ�
	private void handleProposeEvent(Log log, Function function) {
		try {
			List<Type> nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), function.getOutputParameters());
			int inx = 0;
			BigInteger pId = BigInteger.ZERO; // ���ץD��
			BigInteger timestamp = BigInteger.ZERO; // ��ĳ�ɶ�

			for (Type type : nonIndexedValues) {
				System.out.println("Type String:" + type.getTypeAsString());
				System.out.println("Type Value:" + type.getValue());
				if (inx == 0) {
					// �Ĥ@�ӰѼƬO����PID
					pId = (BigInteger) type.getValue();
				} else {
					// �ĤG�ӰѼƬO���״���
					timestamp = (BigInteger) type.getValue();
				}
				inx++;
			}

			// �d�� ���פ��e
			System.out.println("���רƥ�APID:" + pId.longValueExact());
	
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}	

}

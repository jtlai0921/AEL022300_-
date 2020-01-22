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

public class EthVoteQuery {
	public static void main(String[] args) {
		new EthVoteQuery();
	}

	private static String blockchainNode = "http://127.0.0.1:8080/";

	// ���o�X���]�q����
	private static String contractAddr = "0x969df30e59d0ac27a012145a3d15627611a9c82e";

	public EthVoteQuery() {
		try {
			// �s���϶���`�I
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���w���_�ɡA�αb�K����
			String coinBaseFile = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";
			String myPWD = "16888";
			Credentials credentials = WalletUtils.loadCredentials(myPWD, coinBaseFile);

			// �ǥѦX���]�q����A�i��G�p
			long startTime = System.currentTimeMillis();

			// ���w���פ��d��
			BigInteger pId = new BigInteger("" + 1540916078);
			
			// ���o�X���]�q����
			EthVoting contract = EthVoting.load(contractAddr, web3, credentials, EthVoting.GAS_PRICE,
					EthVoting.GAS_LIMIT);

			// �]�w�����H�P�Ʀ�겣
			System.out.println("���׼��D:" + contract.getProposalName(pId).send());
			System.out.println("���פ��e:" + contract.getProposalCtx(pId).send());
			System.out.println("��ĳ�H��:" + contract.getProposalVCnt(pId).send());

			// ���o��ĳ�ɶ�
			BigInteger blockTime = contract.getProposalLimit(pId).send();

			// �N���o���϶��ɶ��A�ഫ����Ū���ɶ��榡
			Calendar bolckTimeCal = Calendar.getInstance();
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

			// �N�H����쪺���G�A���W1000�A���ܦ��ʸU�D���@��e�{
			bolckTimeCal.setTimeInMillis(blockTime.longValueExact() * 1000);

			// ��ܰ��浲�G
			System.out.println("�϶��ɶ�(UNIX):" + blockTime.longValueExact());
			System.out.println("�϶��ɶ�(���iŪ��):" + timeFormat.format(bolckTimeCal.getTime()));
		} catch (Exception e) {
			System.out.println("������~:" + e);
		}
	}
}

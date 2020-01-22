package chap06.sec01;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import chap06.com.alc.TimeContract;

public class TimeChecker {
	public static void main(String[] args) {
		try {
			// �s���϶���`�I
			String blockchainNode = "http://127.0.0.1:8080/";
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���w���_�ɡA�αb�K����
			String coinBaseFile = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";
			String myPWD = "16888";
			Credentials credentials = WalletUtils.loadCredentials(myPWD, coinBaseFile);

			// �]�w�X����}
			String contractAddr = "0x5c4eb42dcce95f883155b0edca702c9ceb7563e0";

			// ���o�X���]�q����
			TimeContract contract = TimeContract.load(contractAddr, web3, credentials, TimeContract.GAS_PRICE,
					TimeContract.GAS_LIMIT);

			//�ϥδ���X�������
			BigInteger time = contract.getBlockTime().send();
			
			//�N���o���϶��ɶ��A�ഫ����Ū���ɶ��榡
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			
			//�N�H����쪺���G�A���W1000�A���ܦ��ʸU�D���@��e�{
			cal.setTimeInMillis(time.longValueExact() * 1000);
			
			//��ܰ��浲�G
			System.out.println("seconds since the epoch:" + time.longValueExact());
			System.out.println("after format:" + dateFormat.format(cal.getTime()));
			
		} catch (Exception e) {
			System.out.println("������~:" + e);
		}
	}
}

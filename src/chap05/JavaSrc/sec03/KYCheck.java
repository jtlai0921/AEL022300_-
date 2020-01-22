package chap05.sec03;

import java.math.BigInteger;
import java.util.List;

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

import chap05.com.alc.KYC;

public class KYCheck {

	public static void main(String[] args) {
		try {
			// �s���϶���`�I
			String blockchainNode = "http://127.0.0.1:8080/";
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���w���_�ɡA�αb�K����
			String coinBaseFile = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";
			String myPWD = "16888";
			Credentials credentials = WalletUtils.loadCredentials(myPWD, coinBaseFile);

			// �ǥѦX���]�q����A�i��G�p
			long startTime = System.currentTimeMillis();
			// ���o�X���]�q����
			String contractAddr = "0xfd0781353653b77c58117a55a80aef2ba7c36070";
			KYC contract = KYC.load(contractAddr, web3, credentials, KYC.GAS_PRICE, KYC.GAS_LIMIT);
			long endTime = System.currentTimeMillis();
			System.out.println("�X������ɶ�:" + (endTime - startTime) + " ms");
			
			System.out.println("�X�����ĩ�:" + contract.isValid());
			
		} catch (Exception e) {
			System.out.println("������~:" + e);
		}

		// System.exit(0);
	}
}

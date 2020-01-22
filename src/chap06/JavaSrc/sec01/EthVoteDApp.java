package chap06.sec01;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import chap06.com.alc.EthVoting;

public class EthVoteDApp {
	public static void main(String[] args) {
		new EthVoteDApp();
	}

	private static String blockchainNode = "http://127.0.0.1:8080/";

	// 取得合約包裹物件
	private static String contractAddr = "0x969df30e59d0ac27a012145a3d15627611a9c82e";

	public EthVoteDApp() {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			String coinBaseFile = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";
			String myPWD = "16888";
			Credentials credentials = WalletUtils.loadCredentials(myPWD, coinBaseFile);

			// 取得合約包裹物件
			EthVoting contract = EthVoting.load(contractAddr, web3, credentials, EthVoting.GAS_PRICE,
					EthVoting.GAS_LIMIT);

			// 指定提案之附議
			BigInteger pId = new BigInteger("" + 1540916078);
			contract.doVoting(pId).send();

		} catch (Exception e) {
			System.out.println("交易錯誤:" + e);
		}
	}

}

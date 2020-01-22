package chap05.sec04;

import java.math.BigInteger;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import chap05.com.alc.DeployHelloWorld;

public class DeployTest {

	public static void main(String[] args) {
		try {
			// 連接區塊鏈節點
			String blockchainNode = "http://127.0.0.1:8080/";
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			String coinBaseFile = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";
			String myPWD = "16888";
			Credentials credentials = WalletUtils.loadCredentials(myPWD, coinBaseFile);

			// 藉由合約包裹物件，進行佈署
			long startTime = System.currentTimeMillis();
			DeployHelloWorld contract = DeployHelloWorld
					.deploy(web3, credentials, DeployHelloWorld.GAS_PRICE, DeployHelloWorld.GAS_LIMIT).send();
			long endTime = System.currentTimeMillis();

			// 取得合約位址
			String addr = contract.getContractAddress();
			System.out.println("合約佈署位址:" + addr + ",花費:" + (endTime - startTime) + " ms");

			//DeployHelloWorld contract2 = DeployHelloWorld.load(addr, web3, credentials, DeployHelloWorld.GAS_PRICE, DeployHelloWorld.GAS_LIMIT);
			System.out.println("合約有效性:" + contract.isValid());

		} catch (Exception e) {
			System.out.println("交易錯誤:" + e);
		}

		// System.exit(0);
	}
}
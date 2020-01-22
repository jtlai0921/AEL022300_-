package chap05.sec04;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

public class NodeVersionChk {
	public static void main(String[] args) {
		try {
			// �s���϶���`�I
			String blockchainNode = "http://127.0.0.1:8080/";
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// �D�P�B�覡�d��
			long startTime = System.currentTimeMillis();
			Web3ClientVersion nodeVer = web3.web3ClientVersion().sendAsync().get();
			long endTime = System.currentTimeMillis();
			System.out.println("�����d��(���B)�A��O:" + (endTime - startTime) + " ms. ver:" + nodeVer.getWeb3ClientVersion());

			// �P�B�覡�d��
			startTime = System.currentTimeMillis();
			nodeVer = web3.web3ClientVersion().send();
			endTime = System.currentTimeMillis();
			System.out.println("�����d��(�P�B)�A��O:" + (endTime - startTime) + " ms. ver:" + nodeVer.getWeb3ClientVersion());

		} catch (Exception e) {
			System.out.println("������~:" + e);
		}
	}
}

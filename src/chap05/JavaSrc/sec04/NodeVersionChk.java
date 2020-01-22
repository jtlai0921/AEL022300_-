package chap05.sec04;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

public class NodeVersionChk {
	public static void main(String[] args) {
		try {
			// 連接區塊鏈節點
			String blockchainNode = "http://127.0.0.1:8080/";
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 非同步方式查詢
			long startTime = System.currentTimeMillis();
			Web3ClientVersion nodeVer = web3.web3ClientVersion().sendAsync().get();
			long endTime = System.currentTimeMillis();
			System.out.println("版本查詢(異步)，花費:" + (endTime - startTime) + " ms. ver:" + nodeVer.getWeb3ClientVersion());

			// 同步方式查詢
			startTime = System.currentTimeMillis();
			nodeVer = web3.web3ClientVersion().send();
			endTime = System.currentTimeMillis();
			System.out.println("版本查詢(同步)，花費:" + (endTime - startTime) + " ms. ver:" + nodeVer.getWeb3ClientVersion());

		} catch (Exception e) {
			System.out.println("交易錯誤:" + e);
		}
	}
}

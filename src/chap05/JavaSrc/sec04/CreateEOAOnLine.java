package chap05.sec04;

import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.http.HttpService;

public class CreateEOAOnLine {
	public static void main(String[] args) {
		try {
			// 連接區塊鏈節點
			String blockchainNode = "http://127.0.0.1:8080/";
			Admin web3 = Admin.build(new HttpService(blockchainNode));

			// 設定新EOA的密碼
			NewAccountIdentifier newEOA = web3.personalNewAccount("16888").send();

			// 取得新EOA的位址
			System.out.println("新帳號位址:" + newEOA.getAccountId());
		} catch (Exception e) {
			System.out.println("交易錯誤:" + e);
		}
	}
}

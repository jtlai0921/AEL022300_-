package chap05.sec04;

import java.io.File;

import org.web3j.crypto.WalletUtils;

public class CreateWalletFile {
	public static void main(String[] args) {
		try {
			// 連接區塊鏈節點
			String fileName = WalletUtils.generateNewWalletFile("888", new File("c:\\temp\\"), true);

		} catch (Exception e) {
			System.out.println("交易錯誤:" + e);
		}
	}
}

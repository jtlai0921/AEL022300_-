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
			// 連接區塊鏈節點
			String blockchainNode = "http://127.0.0.1:8080/";
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			String coinBaseFile = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";
			String myPWD = "16888";
			Credentials credentials = WalletUtils.loadCredentials(myPWD, coinBaseFile);

			// 設定合約位址
			String contractAddr = "0x5c4eb42dcce95f883155b0edca702c9ceb7563e0";

			// 取得合約包裹物件
			TimeContract contract = TimeContract.load(contractAddr, web3, credentials, TimeContract.GAS_PRICE,
					TimeContract.GAS_LIMIT);

			//使用智能合約的函數
			BigInteger time = contract.getBlockTime().send();
			
			//將取得的區塊時間，轉換成易讀的時間格式
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			
			//將以秒為單位的結果，乘上1000，轉變成百萬非之一秒呈現
			cal.setTimeInMillis(time.longValueExact() * 1000);
			
			//顯示執行結果
			System.out.println("seconds since the epoch:" + time.longValueExact());
			System.out.println("after format:" + dateFormat.format(cal.getTime()));
			
		} catch (Exception e) {
			System.out.println("交易錯誤:" + e);
		}
	}
}

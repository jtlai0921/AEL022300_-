package chap05.sec04;

import java.math.BigDecimal;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

public class OffLineSignEasyVer {
	public static void main(String[] args) {
		try {
			// 連接區塊鏈節點
			String blockchainNode = "http://127.0.0.1:8080/";
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 設定出金帳號
			String pwd = "16888";
			//String keyFile = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";
			String keyFile = "C:\\MyGeth\\node01\\keystore\\UTC--2018-11-13T13-18-52.868020400Z--5b27969b0f9778d3e04bfa5dbd06b7c55fa60277";
			Credentials credentials = WalletUtils.loadCredentials(pwd, keyFile);

			// 設定入金帳號
			//String toEOA = "0x8F6EFEE826a64350dC18BFB87B935886AC5C4ABC";
			String toEOA = "0x4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";
			
			// 發送ETH
			double eth = 10.0;
			TransactionReceipt recp = Transfer
					.sendFunds(web3, credentials, toEOA, BigDecimal.valueOf(eth), Convert.Unit.ETHER).send();

		} catch (Exception e) {
			System.out.println("交易錯誤:" + e);
		}
	}
}

package chap06.sec02;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import chap06.com.alc.PayContract;

public class TransDApp {

	public static void main(String[] args) {
		new TransDApp();
	}

	//區塊鏈節點URL
	private static String blockchainNode = "http://127.0.0.1:8080/";

	// 智能合約位址
	private static String contractAddr = "0x36154dc6ad23eea0436aa9e09a0bdd50b2a15cac";

	//三個EOA的金鑰檔
	private String keyFile01 = "C:\\MyGeth\\node01\\keystore\\UTC--2018-11-25T03-41-09.743521200Z--576b11fb5d5c380fcf973b62c3ab59f19f9300fe";
	private String keyFile02 = "C:\\MyGeth\\node01\\keystore\\UTC--2018-11-14T13-37-02.324633700Z--da85610910365341d3372fa350f865ce50224a91";
	private String keyFile03 = "C:\\MyGeth\\node01\\keystore\\UTC--2018-11-14T13-38-25.785341700Z--acf34ee2ea0eeaca037b8fb9b64d5361f053da9a";

	//礦工帳號的金鑰檔
	private String keyFilbase = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";
	
	//三個EOA的區塊鏈位址
	String user1 = "0x576B11Fb5D5C380fCF973b62C3aB59f19f9300fE";
	String user2 = "0xDa85610910365341D3372fa350F865Ce50224a91";
	String user3 = "0xacf34EE2EA0EeaCa037b8fB9B64D5361f053DA9a";

	public TransDApp() {	
		//User 2 傳輸加密貨幣智能合約
		transferETH(user2, contractAddr, keyFile02, "16888", "20.0");
		queryBalance();
		System.out.println("User 2 to contract done");
		
		//User 3 傳輸加密貨幣智能合約
		transferETH(user3, contractAddr, keyFile03, "16888", "10.0");
		queryBalance();
		System.out.println("User 3 to contract done");
		
		//智能合約將餘額傳輸給User 1
		contractToEOA(user1);
		queryBalance();		
	}

	// 傳送ETH
	private void transferETH(String fromEOA, String toEOA, String keyFile, String pwd, String eth) {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 驗證加簽物件
			Credentials credentials = WalletUtils.loadCredentials(pwd, keyFile);

			// 設定ETH數量
			BigInteger ethValue = Convert.toWei(eth, Convert.Unit.ETHER).toBigInteger();

			// 設定nonce亂數
			EthGetTransactionCount ethGetTransactionCount = web3
					.ethGetTransactionCount(fromEOA, DefaultBlockParameterName.LATEST).sendAsync().get();
			BigInteger nonce = ethGetTransactionCount.getTransactionCount();

			// 設定Gas
			BigInteger gasPrice = new BigInteger("" + 1);
			BigInteger gasLimit = new BigInteger("" + 30000);

			// 建立RawTransaction物件
			RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, toEOA,
					ethValue);

			// 對交易進行加簽與加密
			byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
			String hexValue = Numeric.toHexString(signedMessage);

			// 提出交易
			EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();

			String txnHash = ethSendTransaction.getTransactionHash();
			System.out.println("傳送ETH交易序號:" + txnHash);

		} catch (Exception e) {
			System.out.println("transferETH,錯誤:" + e);
		}
	}

	// 查詢合約的餘額
	private void queryBalance() {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定由礦工帳號執行合約函數
			String coinBaseFile = keyFilbase;
			String myPWD = "16888";
			Credentials credentials = WalletUtils.loadCredentials(myPWD, coinBaseFile);

			// 取得合約包裹物件
			PayContract contract = PayContract.load(contractAddr, web3, credentials, PayContract.GAS_PRICE,
					PayContract.GAS_LIMIT);

			// 查詢合約餘額
			BigInteger balance = contract.queryBalance().send();
			System.out.println("合約ETH餘額:" + balance.doubleValue());

		} catch (Exception e) {
			System.out.println("合約ETH餘額,錯誤:" + e);
		}
	}

	// 合約傳送ETH給指定EOA
	private void contractToEOA(String toEOA) {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			String coinBaseFile = keyFilbase;
			String myPWD = "16888";
			Credentials credentials = WalletUtils.loadCredentials(myPWD, coinBaseFile);

			// 取得合約包裹物件
			PayContract contract = PayContract.load(contractAddr, web3, credentials, PayContract.GAS_PRICE,
					PayContract.GAS_LIMIT);

			// 合約傳送ETH給指定
			contract.transEth(toEOA).send();
			System.out.println("合約傳送ETH");

		} catch (Exception e) {
			System.out.println("合約傳送ETH,錯誤:" + e);
		}
	}

}

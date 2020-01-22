package chap06.sec02;

import java.math.BigInteger;
import java.util.List;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import chap06.com.alc.OpenAuction;

public class AuctionDApp {

	public static void main(String[] args) {
		new AuctionDApp();
	}

	//區塊鏈節點位址
	private static String blockchainNode = "http://127.0.0.1:8080/";

	// 智能合約位址
	private static String contractAddr = "0xa95eaac45799954c6c362d733c53b1440a035519";

	// 金鑰檔儲存路徑
	private String keyFile01 = "C:\\MyGeth\\node01\\keystore\\UTC--2018-11-25T03-41-09.743521200Z--576b11fb5d5c380fcf973b62c3ab59f19f9300fe";
	private String keyFile02 = "C:\\MyGeth\\node01\\keystore\\UTC--2018-11-14T13-37-02.324633700Z--da85610910365341d3372fa350f865ce50224a91";
	private String keyFile03 = "C:\\MyGeth\\node01\\keystore\\UTC--2018-11-14T13-38-25.785341700Z--acf34ee2ea0eeaca037b8fb9b64d5361f053da9a";

	// 礦工帳號的金鑰檔
	private String keyFilbase = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";

	// 測試用EOA
	String user1 = "0x576B11Fb5D5C380fCF973b62C3aB59f19f9300fE";
	String user2 = "0xDa85610910365341D3372fa350F865Ce50224a91";
	String user3 = "0xacf34EE2EA0EeaCa037b8fB9B64D5361f053DA9a";

	long startTime = 0;
	long endTime = 0;

	public AuctionDApp() {
		// 啟動拍賣
		openAuction(120);

		long startTime2 = 0;
		long endTime2 = 0;

		startTime2 = System.currentTimeMillis();

		// 等待到開始拍賣
		boolean isAuctionOpen = queryAuction();
		while (!isAuctionOpen) {
			isAuctionOpen = queryAuction();
		}

		// 第一位競標者進行出價
		transferETH(user1, contractAddr, keyFile01, "16888", "10.0");

		endTime2 = System.currentTimeMillis();
		System.out.println("user 1 出價結束:" + (endTime2 - startTime2) + " ms");

		try {
			Thread.sleep(1000 * 20 * 1);
		} catch (Exception e) {
		}

		startTime2 = System.currentTimeMillis();

		// 第二位競標者進行出價
		transferETH(user2, contractAddr, keyFile02, "16888", "20.0");

		endTime2 = System.currentTimeMillis();
		System.out.println("user 2出價結束:" + (endTime2 - startTime2) + " ms");

		try {
			Thread.sleep(1000 * 120 * 1);
		} catch (Exception e) {
		}

		// 結束拍賣活動
		endAuction();
		
		//查詢拍賣結果
		queryAuction();
	}

	// 啟動拍賣活動
	private void openAuction(int timeLimit) {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			String coinBaseFile = keyFile03;
			String myPWD = "16888";
			Credentials credentials = WalletUtils.loadCredentials(myPWD, coinBaseFile);

			// 取得合約包裹物件
			OpenAuction contract = OpenAuction.load(contractAddr, web3, credentials, OpenAuction.GAS_PRICE,
					OpenAuction.GAS_LIMIT);

			// 設定拍賣期間為180秒
			BigInteger _timeLimit = new BigInteger("" + timeLimit);
			contract.setAuctionStart(_timeLimit).send();
			System.out.println("設定拍賣開始");

		} catch (Exception e) {
			System.out.println("設定拍賣開始,錯誤:" + e);
		}
	}

	// 結束拍賣
	private void endAuction() {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			String coinBaseFile = keyFile03;
			String myPWD = "16888";
			Credentials credentials = WalletUtils.loadCredentials(myPWD, coinBaseFile);

			startTime = System.currentTimeMillis();

			// 取得合約包裹物件
			OpenAuction contract = OpenAuction.load(contractAddr, web3, credentials, OpenAuction.GAS_PRICE,
					OpenAuction.GAS_LIMIT);

			// 結束拍賣
			contract.setAuctionEnd().sendAsync();
			System.out.println("設定拍賣結束");

		} catch (Exception e) {
			System.out.println("設定拍賣結束,錯誤:" + e);
		}
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
			BigInteger gasLimit = new BigInteger("" + 80000);

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

	// 查詢拍賣情況
	public boolean queryAuction() {
		boolean isAuctionOpen = false;
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			String coinBaseFile = keyFilbase;
			String myPWD = "16888";
			Credentials credentials = WalletUtils.loadCredentials(myPWD, coinBaseFile);

			// 取得合約包裹物件
			OpenAuction contract = OpenAuction.load(contractAddr, web3, credentials, OpenAuction.GAS_PRICE,
					OpenAuction.GAS_LIMIT);

			System.out.println("拍賣開始時間:" + contract.auctionStart().send());
			isAuctionOpen = contract.startFlg().send();
			System.out.println("拍賣開始旗標:" + isAuctionOpen);

			System.out.println("拍賣結束時間:" + contract.auctionLimit().send());
			System.out.println("拍賣結束旗標:" + contract.endFlg().send());

			System.out.println("受益人:" + contract.highestBidder().send());
			System.out.println("最高標金:" + contract.highestBid().send());

		} catch (Exception e) {
			System.out.println("queryAuction 錯誤:" + e);
		}
		return isAuctionOpen;
	}

}

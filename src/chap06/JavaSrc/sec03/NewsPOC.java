package chap06.sec03;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ChainId;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.response.Callback;
import org.web3j.tx.response.QueuingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;
import org.web3j.utils.Convert;

import chap06.com.alc.NewsContract;

public class NewsPOC {

	public static void main(String[] args) {
		new NewsPOC();
	}

	// 區塊鏈節點位址
	private static String blockchainNode = "http://127.0.0.1:8080/";

	// 智能合約位址
	private static String contractAddr = "0x9a8512326b0c74ec0fd066e17eb34877d361f790";

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

	public NewsPOC() {
		// createNews(keyFilbase, "16888", "油價重挫與中美貿易戰，營收持續衰退");

		// 多位閱聽者對新聞進行獎勵
		// rewardNews(keyFile01, "16888", 5, 2);

		// 第二位閱聽者進行獎勵
		// rewardNews(keyFile02, "16888", 5, 10);

		// 查詢指定的新聞
		queryNews(keyFilbase, "16888", 5);
	}

	// 建立一則新聞
	private void createNews(String keyFile, String myPWD, String newCtx) {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);
			System.out.println("身份驗證");

			int attemptsPerTxHash = 30;
			long frequency = 1000;

			// 記錄交易開始時間
			startTime = System.currentTimeMillis();

			// 建立交易處理器
			TransactionReceiptProcessor myProcessor = new QueuingTransactionReceiptProcessor(web3,
					new NewsCreateCallBack(), attemptsPerTxHash, frequency);

			// 建立交易管理器
			TransactionManager transactionManager = new RawTransactionManager(web3, credentials, ChainId.NONE,
					myProcessor);
			System.out.println("建立交易管理器");

			// 取得合約包裹物件
			NewsContract contract = NewsContract.load(contractAddr, web3, transactionManager, NewsContract.GAS_PRICE,
					NewsContract.GAS_LIMIT);
			System.out.println("取得合約");

			// 加入一則新的新聞
			contract.addNews(newCtx).sendAsync();
			System.out.println("新增新聞");
		} catch (Exception e) {
			System.out.println("建立新聞錯誤,錯誤:" + e);
		}
	}

	// 獎勵一則新聞
	private void rewardNews(String keyFile, String myPWD, int newsKey, int eth) {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);

			// 取得合約包裹物件
			NewsContract contract = NewsContract.load(contractAddr, web3, credentials, NewsContract.GAS_PRICE,
					NewsContract.GAS_LIMIT);

			// 獎勵一則新聞
			BigInteger weiValue = Convert.toWei("" + eth, Convert.Unit.ETHER).toBigInteger();
			contract.rewardNews(new BigInteger("" + newsKey), weiValue).send();

		} catch (Exception e) {
			System.out.println("獎勵新聞錯誤,錯誤:" + e);
		}
	}

	// 查詢一則新聞
	private void queryNews(String keyFile, String myPWD, int newsKey) {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);
			System.out.println("身份驗證");

			// 記錄交易開始時間
			startTime = System.currentTimeMillis();

			// 取得合約包裹物件
			NewsContract contract = NewsContract.load(contractAddr, web3, credentials, NewsContract.GAS_PRICE,
					NewsContract.GAS_LIMIT);
			System.out.println("取得合約");

			// 查詢新聞是否存在
			Boolean isExist = contract.isNewsExist(new BigInteger("" + newsKey)).send();
			System.out.println("新聞是否存在:" + isExist.booleanValue());

			// 查詢新聞內容
			String newsCxt = contract.queryCtx(new BigInteger("" + newsKey)).send();
			System.out.println("新聞內容:" + newsCxt);

			// 查詢新聞累積獎勵
			BigInteger reward = contract.queryReward(new BigInteger("" + newsKey)).send();
			System.out.println("新聞的累積獎勵:" + reward);

		} catch (Exception e) {
			System.out.println("查詢新聞錯誤,錯誤:" + e);
		}
	}
}

// 處理函數具有整數回傳值
class NewsCreateCallBack implements Callback {
	// 交易被接受的回叫函數
	public void accept(TransactionReceipt recp) {

		// 定義函數回傳值
		Function function = new Function("", Collections.<Type>emptyList(), Arrays.asList(new TypeReference<Uint>() {
		}));

		// 取得回傳值
		List<Log> list = recp.getLogs();
		List<Type> nonIndexedValues = FunctionReturnDecoder.decode(list.get(0).getData(),
				function.getOutputParameters());

		// 第一個回傳值是uint
		BigInteger newsKey = (BigInteger) nonIndexedValues.get(0).getValue();
		System.out.println("news ID:" + newsKey.intValue());
	}

	public void exception(Exception exception) {
		System.out.println("交易失敗, err:" + exception);
	}
}
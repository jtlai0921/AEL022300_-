package chap06.sec06;

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

import chap06.com.alc.EthPump;

public class PumpOwner {

	public static void main(String[] args) {
		new PumpOwner();
	}

	// 區塊鏈節點位址
	private static String blockchainNode = "http://127.0.0.1:8080/";

	// 智能合約位址
	private static String contractAddr = "0xe42481327a9a4386eb7cbabf495794ca897fdedd";

	// 合約主持人EOA
	private String host = "0x4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";

	// 合約主持人金鑰檔
	private String hostKey = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";

	// 出借人EOA
	private String owner = "0x576B11Fb5D5C380fCF973b62C3aB59f19f9300fE";

	// 出借人金鑰檔
	private String ownerKey = "C:\\MyGeth\\node01\\keystore\\UTC--2018-11-25T03-41-09.743521200Z--576b11fb5d5c380fcf973b62c3ab59f19f9300fe";

	// 借用人EOA
	String borrower = "0xDa85610910365341D3372fa350F865Ce50224a91";

	// 借用人金鑰檔
	private String borrowerKey = "C:\\MyGeth\\node01\\keystore\\UTC--2018-11-14T13-37-02.324633700Z--da85610910365341d3372fa350f865ce50224a91";

	public PumpOwner() {
		// 新增物品
		//insertGoods(ownerKey, "16888", "bicycle", "10", true);

		// 設定物品歸還
		doGoodsReturn(ownerKey, "16888", "bicycle", 1);
	}

	// 新增物品資訊
	private void insertGoods(String keyFile, String myPWD, String stickName, String eth, boolean available) {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);
			System.out.println("身份驗證");

			int attemptsPerTxHash = 30;
			long frequency = 1000;

			// 建立交易處理器
			TransactionReceiptProcessor myProcessor = new QueuingTransactionReceiptProcessor(web3,
					new AddGoodsCallBack(), attemptsPerTxHash, frequency);

			// 建立交易管理器
			TransactionManager transactionManager = new RawTransactionManager(web3, credentials, ChainId.NONE,
					myProcessor);
			System.out.println("建立交易管理器");

			// 取得合約包裹物件
			EthPump contract = EthPump.load(contractAddr, web3, transactionManager, EthPump.GAS_PRICE,
					EthPump.GAS_LIMIT);
			System.out.println("取得合約");

			// 設定ETH數量
			BigInteger weiValue = Convert.toWei(eth, Convert.Unit.ETHER).toBigInteger();

			// 加入一筆物品
			contract.addGoods(stickName, weiValue, available).send();
			System.out.println("新增物品完成");

		} catch (Exception e) {
			System.out.println("新增物品,錯誤:" + e);
		}
	}

	// 歸還物品
	private void doGoodsReturn(String keyFile, String myPWD, String stickName, int inx) {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);
			System.out.println("身份驗證");

			// 取得合約包裹物件
			EthPump contract = EthPump.load(contractAddr, web3, credentials, EthPump.GAS_PRICE, EthPump.GAS_LIMIT);
			System.out.println("取得合約");

			// 設定物品歸還
			contract.doGoodsReturn(stickName, new BigInteger("" + inx)).send();
			System.out.println("設定物品歸還完成");

		} catch (Exception e) {
			System.out.println("設定物品歸還,錯誤:" + e);
		}
	}

}

// 處理函數具有整數回傳值
class AddGoodsCallBack implements Callback {
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
		System.out.println("新增物品主鍵(序號):" + newsKey.longValue());
	}

	public void exception(Exception exception) {
		System.out.println("新增物品主鍵(序號)回應, err:" + exception);
	}
}
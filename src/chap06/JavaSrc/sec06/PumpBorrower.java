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

public class PumpBorrower {

	public static void main(String[] args) {
		new PumpBorrower();
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

	public PumpBorrower() {
		// 借用物品
		borrowGoods(borrowerKey, "16888", "bicycle", 1, "10");
	}

	// 登記物品借用
	private void borrowGoods(String keyFile, String myPWD, String stickName, int inx, String eth) {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);
			System.out.println("身份驗證");

			// 取得合約包裹物件
			EthPump contract = EthPump.load(contractAddr, web3, credentials, EthPump.GAS_PRICE, EthPump.GAS_LIMIT);
			System.out.println("取得合約");

			// 設定ETH數量
			BigInteger weiValue = Convert.toWei(eth, Convert.Unit.ETHER).toBigInteger();

			// 加入一筆貼紙(物品分類)
			contract.borrowGoods(stickName, new BigInteger("" + inx), weiValue).send();
			System.out.println("借用物品完成");

		} catch (Exception e) {
			System.out.println("借用物品錯誤,錯誤:" + e);
		}
	}
}

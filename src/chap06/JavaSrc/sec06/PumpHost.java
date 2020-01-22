package chap06.sec06;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import chap06.com.alc.EthPump;

public class PumpHost {

	public static void main(String[] args) {
		new PumpHost();
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

	public PumpHost() {
		// 新增貼紙
		insertNewSticker(hostKey, "16888", "bicycle");
	}

	// 新增貼紙資訊
	private void insertNewSticker(String keyFile, String myPWD, String stickName) {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);
			System.out.println("身份驗證");

			// 取得合約包裹物件
			EthPump contract = EthPump.load(contractAddr, web3, credentials, EthPump.GAS_PRICE, EthPump.GAS_LIMIT);
			System.out.println("取得合約");

			// 加入一筆貼紙(物品分類)
			contract.addSticker(stickName).send();
			System.out.println("新增貼紙完成");

		} catch (Exception e) {
			System.out.println("新增貼紙錯誤,錯誤:" + e);
		}
	}
}

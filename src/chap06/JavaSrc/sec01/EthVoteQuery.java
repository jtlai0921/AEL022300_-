package chap06.sec01;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import chap06.com.alc.EthVoting;
import rx.Subscription;
import rx.functions.Action1;

public class EthVoteQuery {
	public static void main(String[] args) {
		new EthVoteQuery();
	}

	private static String blockchainNode = "http://127.0.0.1:8080/";

	// 取得合約包裹物件
	private static String contractAddr = "0x969df30e59d0ac27a012145a3d15627611a9c82e";

	public EthVoteQuery() {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			String coinBaseFile = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";
			String myPWD = "16888";
			Credentials credentials = WalletUtils.loadCredentials(myPWD, coinBaseFile);

			// 藉由合約包裹物件，進行佈署
			long startTime = System.currentTimeMillis();

			// 指定提案之查詢
			BigInteger pId = new BigInteger("" + 1540916078);
			
			// 取得合約包裹物件
			EthVoting contract = EthVoting.load(contractAddr, web3, credentials, EthVoting.GAS_PRICE,
					EthVoting.GAS_LIMIT);

			// 設定持有人與數位資產
			System.out.println("提案標題:" + contract.getProposalName(pId).send());
			System.out.println("提案內容:" + contract.getProposalCtx(pId).send());
			System.out.println("附議人數:" + contract.getProposalVCnt(pId).send());

			// 取得附議時間
			BigInteger blockTime = contract.getProposalLimit(pId).send();

			// 將取得的區塊時間，轉換成易讀的時間格式
			Calendar bolckTimeCal = Calendar.getInstance();
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

			// 將以秒為單位的結果，乘上1000，轉變成百萬非之一秒呈現
			bolckTimeCal.setTimeInMillis(blockTime.longValueExact() * 1000);

			// 顯示執行結果
			System.out.println("區塊時間(UNIX):" + blockTime.longValueExact());
			System.out.println("區塊時間(高可讀性):" + timeFormat.format(bolckTimeCal.getTime()));
		} catch (Exception e) {
			System.out.println("交易錯誤:" + e);
		}
	}
}

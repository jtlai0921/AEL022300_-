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
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import chap06.com.alc.EthVoting;
import rx.Subscription;
import rx.functions.Action1;

public class EthVotePropose {
	public static void main(String[] args) {
		new EthVotePropose();
	}

	private static String blockchainNode = "http://127.0.0.1:8080/";

	// 取得合約包裹物件
	private static String contractAddr = "0x969df30e59d0ac27a012145a3d15627611a9c82e";

	public EthVotePropose() {

		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			String coinBaseFile = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";
			String myPWD = "16888";
			Credentials credentials = WalletUtils.loadCredentials(myPWD, coinBaseFile);
			
			// 取得合約包裹物件
			EthVoting contract = EthVoting.load(contractAddr, web3, credentials, EthVoting.GAS_PRICE,
					EthVoting.GAS_LIMIT);

			// 取得區塊時間
			BigInteger blockTime = contract.getBlockTime().send();

			// 將取得的區塊時間，轉換成易讀的時間格式
			Calendar bolckTimeCal = Calendar.getInstance();
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

			// 將以秒為單位的結果，乘上1000，轉變成百萬非之一秒呈現
			bolckTimeCal.setTimeInMillis(blockTime.longValueExact() * 1000);

			// 顯示執行結果
			System.out.println("區塊時間(UNIX):" + blockTime.longValueExact());
			System.out.println("區塊時間(高可讀性):" + timeFormat.format(bolckTimeCal.getTime()));

			// 設定附議時間為5分鐘後
			bolckTimeCal.add(Calendar.MINUTE, 5);
			long proposalLimit = bolckTimeCal.getTimeInMillis();

			// 新增提案
			contract.createProposal("廣增公車專用道", "避免與機車爭道", new BigInteger("" + (long) (proposalLimit / 1000))).send();
			
		} catch (Exception e) {
			System.out.println("交易錯誤:" + e);
		}
	}
}

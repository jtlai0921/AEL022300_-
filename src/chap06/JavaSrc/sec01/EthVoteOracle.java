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

public class EthVoteOracle {
	public static void main(String[] args) {
		new EthVoteOracle();
	}

	private static String blockchainNode = "http://127.0.0.1:8080/";

	// 取得合約包裹物件
	private static String contractAddr = "0x969df30e59d0ac27a012145a3d15627611a9c82e";

	public EthVoteOracle() {
		try {
			// 連線區塊鏈節點

			Admin web3 = Admin.build(new HttpService(blockchainNode));

			// 設定過濾條件
			EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST,
					contractAddr);

			String vote_topicHash = Hash.sha3String("vote");
			System.out.println("過濾topic(vote) hash:" + vote_topicHash);

			// 附議事件Log，EOA與timestamp
			Function voteLog = new Function("", Collections.<Type>emptyList(), Arrays.asList(new TypeReference<Uint>() {
			}, new TypeReference<Uint>() {
			}));

			String propose_topicHash = Hash.sha3String("propose");
			System.out.println("過濾topic(propose) hash:" + propose_topicHash);

			// 提案事件Log，EOA與timestamp
			Function proposeLog = new Function("", Collections.<Type>emptyList(),
					Arrays.asList(new TypeReference<Uint>() {
					}, new TypeReference<Uint>() {
					}));

			// 持續偵測事件
			Subscription subscription = web3.ethLogObservable(filter).subscribe(new Action1<Log>() {
				public void call(Log log) {
					List<String> list = log.getTopics();

					// 輪詢事件中的Topic
					for (String topic : list) {
						System.out.println("topic:" + topic);
						System.out.println("合約位址:" + log.getAddress() + "," + log.getData());

						if (topic.equals(vote_topicHash)) {
							System.out.println("處理附議事件");
							handleVoteEvent(log, voteLog);
						}

						if (topic.equals(propose_topicHash)) {
							System.out.println("處理提案事件");
							handleProposeEvent(log, proposeLog);
						}
					}

					System.out.println("=========================");
				}
			});
		} catch (Exception e) {
			System.out.println("交易錯誤:" + e);
		}
	}

	// 處理附議事件
	private void handleVoteEvent(Log log, Function function) {
		try {
			List<Type> nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), function.getOutputParameters());
			int inx = 0;
			String address = ""; // 附議EOA
			BigInteger timestamp = BigInteger.ZERO; // 附議時間

			for (Type type : nonIndexedValues) {
				System.out.println("Type String:" + type.getTypeAsString());
				System.out.println("Type Value:" + type.getValue());
				if (inx == 0) {
					// 第一個參數是address
					try {
						// 將位址，轉換成16進制的字串
						address = Numeric.toHexStringWithPrefix((BigInteger) type.getValue());
					} catch (Exception e) {
						System.out.println("convert error:" + e);
					}
				} else {
					// 第二個參數是附議時間
					timestamp = (BigInteger) type.getValue();
				}
				inx++;
			}

			// 將取得的區塊時間，轉換成易讀的時間格式
			Calendar bolckTimeCal = Calendar.getInstance();
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

			// 將以秒為單位的結果，乘上1000，轉變成百萬非之一秒呈現
			bolckTimeCal.setTimeInMillis(timestamp.longValueExact() * 1000);

			// 顯示執行結果
			System.out.println("附議事件，EOA:" + address);
			System.out.println("區塊時間(UNIX):" + timestamp.longValueExact());
			System.out.println("區塊時間(高可讀性):" + timeFormat.format(bolckTimeCal.getTime()));
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}

	// 處理提案事件
	private void handleProposeEvent(Log log, Function function) {
		try {
			List<Type> nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), function.getOutputParameters());
			int inx = 0;
			BigInteger pId = BigInteger.ZERO; // 提案主鍵
			BigInteger timestamp = BigInteger.ZERO; // 附議時間

			for (Type type : nonIndexedValues) {
				System.out.println("Type String:" + type.getTypeAsString());
				System.out.println("Type Value:" + type.getValue());
				if (inx == 0) {
					// 第一個參數是提案PID
					pId = (BigInteger) type.getValue();
				} else {
					// 第二個參數是提案期限
					timestamp = (BigInteger) type.getValue();
				}
				inx++;
			}

			// 查詢 提案內容
			System.out.println("提案事件，PID:" + pId.longValueExact());
	
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}	

}

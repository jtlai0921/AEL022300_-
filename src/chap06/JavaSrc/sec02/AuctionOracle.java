package chap06.sec02;

import java.util.List;

import org.web3j.crypto.Hash;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;

import rx.Subscription;
import rx.functions.Action1;

public class AuctionOracle {
	public static void main(String[] args) {
		new AuctionOracle();
	}

	private static String blockchainNode = "http://127.0.0.1:8080/";

	// 取得合約包裹物件
	private static String contractAddr = "0xbe345136765beeb324c1d2be583dfd199067a287";

	public AuctionOracle() {
		try {
			// 連線區塊鏈節點

			Admin web3 = Admin.build(new HttpService(blockchainNode));

			// 設定過濾條件
			EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST,
					contractAddr);

			String evn01 = Hash.sha3String("bidEvn");
			String evn02 = Hash.sha3String("HighBid");
			String evn03 = Hash.sha3String("AuctionSS");
			String evn04 = Hash.sha3String("AuctionEE");

			// 持續偵測事件
			Subscription subscription = web3.ethLogObservable(filter).subscribe(new Action1<Log>() {
				public void call(Log log) {
					List<String> list = log.getTopics();

					// 輪詢事件中的Topic
					for (String topic : list) {
						System.out.println("topic:" + topic);
						
						if (topic.equals(evn01)) {
							System.out.println("出價");
						}

						if (topic.equals(evn02)) {
							System.out.println("最高價");
						}

						if (topic.equals(evn03)) {
							System.out.println("拍賣開始");
						}

						if (topic.equals(evn04)) {
							System.out.println("拍賣結束");
						}
						
						System.out.println("合約位址:" + log.getAddress() + "," + log.getData());
					}

					System.out.println("=========================");
				}
			});
		} catch (Exception e) {
			System.out.println("交易錯誤:" + e);
		}
	}
}

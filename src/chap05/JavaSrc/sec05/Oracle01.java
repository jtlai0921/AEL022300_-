package chap05.sec05;

import java.util.List;

import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;

import rx.Subscription;
import rx.functions.Action1;

public class Oracle01 {
	public static void main(String[] args) {
		try {
			// 連線區塊鏈節點
			String blockchainNode = "http://127.0.0.1:8080/";
			Admin web3 = Admin.build(new HttpService(blockchainNode));

			// 指定合約位址
			String contractAddr = "0xeb1da6170755d8a60b045cde6181ecddc8dd81b0";

			// 設定過濾條件
			EthFilter filter = new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST,
					contractAddr);

			// filter.addSingleTopic("MyEvent"); //web3j 無法work

			// 抓取Event
			Subscription subscription = web3.ethLogObservable(filter).subscribe(new Action1<Log>() {
				public void call(Log log) {
					List<String> list = log.getTopics();
					for (String topic : list) {
						System.out.println("topic:" + topic);
					}
					
					System.out.println("data:" + log.getData());
				}
			});

		} catch (Exception e) {
			System.out.println("交易錯誤:" + e);
		}
	}
}

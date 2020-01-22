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

	// ���o�X���]�q����
	private static String contractAddr = "0xbe345136765beeb324c1d2be583dfd199067a287";

	public AuctionOracle() {
		try {
			// �s�u�϶���`�I

			Admin web3 = Admin.build(new HttpService(blockchainNode));

			// �]�w�L�o����
			EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST,
					contractAddr);

			String evn01 = Hash.sha3String("bidEvn");
			String evn02 = Hash.sha3String("HighBid");
			String evn03 = Hash.sha3String("AuctionSS");
			String evn04 = Hash.sha3String("AuctionEE");

			// ���򰻴��ƥ�
			Subscription subscription = web3.ethLogObservable(filter).subscribe(new Action1<Log>() {
				public void call(Log log) {
					List<String> list = log.getTopics();

					// ���ߨƥ󤤪�Topic
					for (String topic : list) {
						System.out.println("topic:" + topic);
						
						if (topic.equals(evn01)) {
							System.out.println("�X��");
						}

						if (topic.equals(evn02)) {
							System.out.println("�̰���");
						}

						if (topic.equals(evn03)) {
							System.out.println("���}�l");
						}

						if (topic.equals(evn04)) {
							System.out.println("��浲��");
						}
						
						System.out.println("�X����}:" + log.getAddress() + "," + log.getData());
					}

					System.out.println("=========================");
				}
			});
		} catch (Exception e) {
			System.out.println("������~:" + e);
		}
	}
}

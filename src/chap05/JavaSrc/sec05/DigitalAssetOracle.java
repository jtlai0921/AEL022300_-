package chap05.sec05;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
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

import chap05.com.alc.DigitalAssetContract;
import rx.Subscription;
import rx.functions.Action1;

public class DigitalAssetOracle {

	public static void main(String[] args) {
		new DigitalAssetOracle();
	}

	private static String blockchainNode = "http://127.0.0.1:8080/";

	// 取得合約包裹物件
	private static String contractAddr = "0x0a60d53892adefcbe216178c27dfb43164491c79";

	public DigitalAssetOracle() {
		try {
			// 連線區塊鏈節點

			Admin web3 = Admin.build(new HttpService(blockchainNode));

			// 設定過濾條件
			EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST,
					contractAddr);

			// 目前尚無法使用
			// filter.addSingleTopic(topicStrHash);
			// filter.addOptionalTopics(topicStrHash);

			// 只聽取topic 為 buy 的事件
			String buy_topicHash = Hash.sha3String("buy");
			System.out.println("過濾topic(buy) hash:" + buy_topicHash);

			// Buy事件的Log內容，第一個是address，第二個是uint256
			Function buyLog = new Function("", Collections.<Type>emptyList(), Arrays.asList(new TypeReference<Uint>() {
			}, new TypeReference<Uint>() {
			}));

			String err_topicHash = Hash.sha3String("err");
			System.out.println("過濾topic(err) hash:" + err_topicHash);

			Function errLog = new Function("", Collections.<Type>emptyList(), Arrays.asList(new TypeReference<Uint>() {
			}, new TypeReference<Uint>() {
			}));

			String set_topicHash = Hash.sha3String("set");
			System.out.println("過濾topic(set) hash:" + set_topicHash);

			// Set事件的Log內容，第一個是address，第二個是uint256
			Function setLog = new Function("", Collections.<Type>emptyList(), Arrays.asList(new TypeReference<Uint>() {
			}, new TypeReference<Utf8String>() {
			}));
			
			// 持續偵測事件
			Subscription subscription = web3.ethLogObservable(filter).subscribe(new Action1<Log>() {
				public void call(Log log) {
					List<String> list = log.getTopics();

					// 輪詢事件中的Topic
					for (String topic : list) {
						System.out.println("topic:" + topic);
						System.out.println("合約位址:" + log.getAddress() + "," + log.getData());

						if (topic.equals(buy_topicHash)) {
							System.out.println("處理Buy事件");
							handleBuyEvent(log, buyLog);
						}

						if (topic.equals(err_topicHash)) {
							System.out.println("處理Error事件");
							handleErrEvent(log, errLog);
						}

						if (topic.equals(set_topicHash)) {
							System.out.println("設定事件");
							handleSetEvent(log, setLog);
						}
					}
					
					System.out.println("=========================");
				}
			});

			// 取消傾聽
			subscription.unsubscribe();
		} catch (Exception e) {
			System.out.println("Oracle錯誤:" + e);
		}
	}

	// 處理Buy事件
	private void handleBuyEvent(Log log, Function buyLog) {
		try {
			List<Type> nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), buyLog.getOutputParameters());
			int inx = 0;
			String address = ""; // 購買帳號EOA
			BigInteger money = BigInteger.ZERO; // 購買金額

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
					// 第二個參數是傳入的金額
					money = (BigInteger) type.getValue();
				}
				inx++;
			}

			System.out.println("Buy事件，購買EOA:" + address);
			System.out.println("Buy事件，購買金額:" + money);

			// 寫入所購買的數位資產
			callbackContract(address, "PO_ABC16888");

		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}

	// 處理Set事件
	private void handleSetEvent(Log log, Function setLog) {
		try {
			List<Type> nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), setLog.getOutputParameters());
			int inx = 0;
			String address = ""; // 購買帳號EOA
			String license = ""; // 數位資產

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
					// 第二個參數是數位資產
					license = (String) type.getValue();
				}
				inx++;
			}

			System.out.println("Set事件，購買EOA:" + address);
			System.out.println("Set事件，數位資產:" + license);
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}

	// 處理Err事件
	private void handleErrEvent(Log log, Function errLog) {
		try {
			List<Type> nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), errLog.getOutputParameters());
			int inx = 0;
			String address = ""; // 購買帳號EOA
			BigInteger money = BigInteger.ZERO; // 購買金額

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
					// 第二個參數是傳入的金額
					money = (BigInteger) type.getValue();
				}
				inx++;
			}

			System.out.println("Err事件，購買EOA:" + address);
			System.out.println("Err事件，購買金額:" + money);
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}

	private void callbackContract(String owner, String license) {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			String coinBaseFile = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";
			String myPWD = "16888";
			Credentials credentials = WalletUtils.loadCredentials(myPWD, coinBaseFile);

			// 藉由合約包裹物件，進行佈署
			long startTime = System.currentTimeMillis();

			// 取得合約包裹物件
			DigitalAssetContract contract = DigitalAssetContract.load(contractAddr, web3, credentials,
					DigitalAssetContract.GAS_PRICE, DigitalAssetContract.GAS_LIMIT);

			// 設定持有人與數位資產
			contract.setOwner(owner, license).send();

			long endTime = System.currentTimeMillis();
			System.out.println("合約執行時間:" + (endTime - startTime) + " ms");

		} catch (Exception e) {
			System.out.println("交易錯誤:" + e);
		}
	}
}

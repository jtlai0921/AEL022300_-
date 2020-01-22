package chap06.sec05;

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

import chap06.com.alc.InsuranceContract;
import chap06.com.alc.SupplyChainContract;

public class InsuranceHospital {

	public static void main(String[] args) {
		new InsuranceHospital();
	}

	// 區塊鏈節點位址
	private static String blockchainNode = "http://127.0.0.1:8080/";

	// 智能合約位址
	private static String contractAddr = "0xc6a3fb214038e574fff84d358eb080d3200c5fe3";

	// 保險公司金鑰檔
	private String insuranceCorpKey = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";

	// 保險公司EOA
	String insuranceCorp = "0x4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";

	// 醫院金鑰檔
	private String hospitalKey = "C:\\MyGeth\\node01\\keystore\\UTC--2018-11-25T03-41-09.743521200Z--576b11fb5d5c380fcf973b62c3ab59f19f9300fe";

	// 醫院EOA
	String hospital = "0x576B11Fb5D5C380fCF973b62C3aB59f19f9300fE";

	// 保險客戶EOA
	String patient = "0xDa85610910365341D3372fa350F865Ce50224a91";

	public InsuranceHospital() {
		// step1. 新增病人資訊
		//insPatient(hospitalKey, "16888", patient, "Jackie", "TPE");

		// step 2. 新增病歷
		insRecord(hospitalKey, "16888", patient, "胸悶", "心臟病開刀", 10, 36000);
	}

	// 新增病人資訊
	private void insPatient(String keyFile, String myPWD, String patient, String name, String addr) {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);
			System.out.println("身份驗證");

			// 取得合約包裹物件
			InsuranceContract contract = InsuranceContract.load(contractAddr, web3, credentials,
					InsuranceContract.GAS_PRICE, InsuranceContract.GAS_LIMIT);
			System.out.println("取得合約");

			// 加入一筆病人資料
			contract.insPatient(patient, name, addr).send();

			System.out.println("新增病人資訊");
		} catch (Exception e) {
			System.out.println("新增病人資訊錯誤,錯誤:" + e);
		}
	}

	// 新增供應鏈交易
	private void insRecord(String keyFile, String myPWD, String patient, String symptom, String cause, int day, int money) {
		try {
			// 連接區塊鏈節點
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// 指定金鑰檔，及帳密驗證
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);
			System.out.println("身份驗證");

			// 取得合約包裹物件
			InsuranceContract contract = InsuranceContract.load(contractAddr, web3, credentials,
					InsuranceContract.GAS_PRICE, InsuranceContract.GAS_LIMIT);
			System.out.println("取得合約");

			// 加入一筆離院資訊
			contract.insRecord(patient, symptom, cause, new BigInteger("" + day), new BigInteger("" + money)).send();
			System.out.println("新增離院資訊");
			
		} catch (Exception e) {
			System.out.println("新增離院資訊錯誤,錯誤:" + e);
		}
	}
}

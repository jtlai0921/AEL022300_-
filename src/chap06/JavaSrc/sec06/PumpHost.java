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

	// �϶���`�I��}
	private static String blockchainNode = "http://127.0.0.1:8080/";

	// ����X����}
	private static String contractAddr = "0xe42481327a9a4386eb7cbabf495794ca897fdedd";

	// �X���D���HEOA
	private String host = "0x4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";

	// �X���D���H���_��
	private String hostKey = "C:\\MyGeth\\node01\\keystore\\UTC--2018-05-12T05-36-09.868221900Z--4cd063815f7f7a26504ae42a3693b4bbdf0b9b1a";

	// �X�ɤHEOA
	private String owner = "0x576B11Fb5D5C380fCF973b62C3aB59f19f9300fE";

	// �X�ɤH���_��
	private String ownerKey = "C:\\MyGeth\\node01\\keystore\\UTC--2018-11-25T03-41-09.743521200Z--576b11fb5d5c380fcf973b62c3ab59f19f9300fe";

	// �ɥΤHEOA
	String borrower = "0xDa85610910365341D3372fa350F865Ce50224a91";

	// �ɥΤH���_��
	private String borrowerKey = "C:\\MyGeth\\node01\\keystore\\UTC--2018-11-14T13-37-02.324633700Z--da85610910365341d3372fa350f865ce50224a91";

	public PumpHost() {
		// �s�W�K��
		insertNewSticker(hostKey, "16888", "bicycle");
	}

	// �s�W�K�ȸ�T
	private void insertNewSticker(String keyFile, String myPWD, String stickName) {
		try {
			// �s���϶���`�I
			Web3j web3 = Web3j.build(new HttpService(blockchainNode));

			// ���w���_�ɡA�αb�K����
			Credentials credentials = WalletUtils.loadCredentials(myPWD, keyFile);
			System.out.println("��������");

			// ���o�X���]�q����
			EthPump contract = EthPump.load(contractAddr, web3, credentials, EthPump.GAS_PRICE, EthPump.GAS_LIMIT);
			System.out.println("���o�X��");

			// �[�J�@���K��(���~����)
			contract.addSticker(stickName).send();
			System.out.println("�s�W�K�ȧ���");

		} catch (Exception e) {
			System.out.println("�s�W�K�ȿ��~,���~:" + e);
		}
	}
}

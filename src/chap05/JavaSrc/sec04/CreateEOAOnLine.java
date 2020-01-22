package chap05.sec04;

import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.http.HttpService;

public class CreateEOAOnLine {
	public static void main(String[] args) {
		try {
			// �s���϶���`�I
			String blockchainNode = "http://127.0.0.1:8080/";
			Admin web3 = Admin.build(new HttpService(blockchainNode));

			// �]�w�sEOA���K�X
			NewAccountIdentifier newEOA = web3.personalNewAccount("16888").send();

			// ���o�sEOA����}
			System.out.println("�s�b����}:" + newEOA.getAccountId());
		} catch (Exception e) {
			System.out.println("������~:" + e);
		}
	}
}

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class AddContract {

	public static void main(String[] args) {
		// 建立HTTP客户端
		CloseableHttpClient httpClient = HttpClients.createDefault();

		// 使用POST
		HttpPost httpPost = new HttpPost("http://127.0.0.1:8080");

		// 執行與取得結果
		CloseableHttpResponse response = null;
		try {
			// RPC內容
			String method = "eth_call";
			String from = "0x4CD063815f7f7a26504AE42a3693B4BBDf0B9B1A";
			String to = "0xa29dd279719017783c8fa4b03e7995f15fabc7cb";
			String data = "0xdcc721d200000000000000000000000000000000000000000000000000000000000000060000000000000000000000000000000000000000000000000000000000000007";

			String json = "{\"jsonrpc\": \"2.0\",\"id\": 67," + "  \"method\": \"" + method + "\","
					+ "\"params\": [{\"from\":\"" + from + "\",\"to\": \"" + to + "\"," + "\"data\": \"" + data + "\""
					+ "},\"latest\"]}";

			StringEntity entity = new StringEntity(json);
			httpPost.setEntity(entity);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			response = httpClient.execute(httpPost);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 獲取結果
		HttpEntity entity = response.getEntity();
		try {
			System.out.println(EntityUtils.toString(entity));
			EntityUtils.consume(entity);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
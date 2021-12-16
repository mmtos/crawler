package localSupProJ.jsoup;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupExecutor {

	private final static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";

	public static Document makeJsoupDocument(String url, String httpMethod) throws Exception{
		Document doc = null;
		Connection conn = getJsoupConnection(url);

		if(httpMethod == null || httpMethod.equals("") || httpMethod.equals("get")){
			doc = conn.get();
		}else{
			doc = conn.post();
		}
		return doc;
	}

	private static Connection getJsoupConnection(String url) throws Exception{
		Connection conn = null;
		if(url != null && !url.equals("")){
			// SSL 체크
			if(url.indexOf("https://") >= 0){
				/* ssl 우회처리를 하여 https 사이트의 문서를 가져오도록 준비 */
				setSSL();
			}

			// HTML 가져오기
			conn = Jsoup.connect(url)
					.header("Content-Type", "application/json;charset=UTF-8")
					.userAgent(USER_AGENT)
					.method(Connection.Method.GET)
					.ignoreContentType(true);
			return conn;
		}
		throw new Exception("JSoup 접속에 실패했습니다.");
	}

	// SSL 우회 등록
	private static void setSSL() throws NoSuchAlgorithmException, KeyManagementException {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());

		HttpsURLConnection.setDefaultHostnameVerifier(
				new HostnameVerifier() {
					@Override
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				}
		);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}
}

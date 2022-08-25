package com.petya136900.rcebot.raccoonremote;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.tools.SHA;
import com.petya136900.rcebot.vk.VK;
@Service
public class RaccoonRemote {
	private static final SSLSocketFactory SOCKET_FACTORY = getSocketFactory();
	private static RaccoonRemote instance = new RaccoonRemote();
	public static RaccoonRemote getInstance() {
		return instance;
	}
	private static SSLSocketFactory getSocketFactory() {
		try {
			TrustManager trm = new X509TrustManager() {
			    public X509Certificate[] getAcceptedIssuers() {return null;}
			    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
			    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
			};
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, new TrustManager[] { trm }, null);
			return sc.getSocketFactory();
		} catch (Exception e) {
			return (SSLSocketFactory) SSLSocketFactory.getDefault();
		}
	}
	private boolean connected=false;
	public boolean isConnected() {
		return connected;
	}
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	private String login;
	private String password;
	private String host;
	private boolean useHttps;
	private Integer port;
	private String token;
	private static final String API_VERSION = "v1";
	private Long userId;
	public void connect(String host, Integer port, String login, String pass, boolean useHttps, VK vkContent) throws Exception {
		try {
			Connection connection = Jsoup.connect((useHttps?"https://":"http://")+host+":"+port+"/api/"+API_VERSION+
					"/login?login="+login+"&passwordHash="+SHA.hashSHA256(pass))
					.sslSocketFactory(SOCKET_FACTORY);
			Response response = JsonParser.fromJson(connection.execute().body(), Response.class);
			if(!response.getError()) {
				this.token = response.getToken();
				this.connected=true;
				this.login=login;
				this.password=pass;
				this.host = host;
				this.port = port;
				this.useHttps=useHttps;
				this.userId=response.getUserId();
				vkContent.reply("Подключен!");
			} else {
				vkContent.reply("Ошибка: "+response.getCodeDescRu());
			}
		} catch (Exception e) {
			throw e;
		}
	}
	public String getWebAddress() {
		return (useHttps?"https://":"http://")+host+":"+port;
	}
	public DeviceGson[] getDevices() throws Exception {
		try {
			Connection connection = Jsoup.connect((useHttps?"https://":"http://")+host+":"+port+"/api/"+API_VERSION+
					"/devices/getbyuserid?id="+userId)
					.sslSocketFactory(SOCKET_FACTORY);
			connection.cookie("raccoontoken", token);
			Response response = JsonParser.fromJson(connection.execute().body(), Response.class);
			if(!response.getError()) {
				return response.getDevices();
			} else {
				throw new Exception(response.getCodeDescRu());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	public void disconnect() {
		try {
			Connection connection = Jsoup.connect((useHttps?"https://":"http://")+host+":"+port+"/api/"+API_VERSION+
					"/logout")
					.sslSocketFactory(SOCKET_FACTORY);
			connection.cookie("raccoontoken", token);
			connection.execute();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		this.token=null;
		this.connected=false;
		this.login=null;
		this.password=null;
		this.host=null;
		this.port=null;
		this.useHttps=false;
		this.userId=null;
	}
	public DeviceGson getDevice(long id) throws Exception {
		try {
			Connection connection = Jsoup.connect((useHttps?"https://":"http://")+host+":"+port+"/api/"+API_VERSION+
					"/devices/getbyid?id="+id)
					.sslSocketFactory(SOCKET_FACTORY);
			connection.cookie("raccoontoken", token);
			Response response = JsonParser.fromJson(connection.execute().body(), Response.class);
			if(!response.getError()) {
				return response.getDevice();
			} else {
				throw new Exception(response.getCodeDescRu());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	public Integer getFreePort() throws Exception {
		try {
			Connection connection = Jsoup.connect((useHttps?"https://":"http://")+host+":"+port+"/api/"+API_VERSION+
					"/tools/getfreeport")
					.sslSocketFactory(SOCKET_FACTORY);
			connection.cookie("raccoontoken", token);
			Response response = JsonParser.fromJson(connection.execute().body(), Response.class);
			if(!response.getError()) {
				return Integer.parseInt(response.getData());
			} else {
				throw new Exception(response.getCodeDescRu());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	public LoadedCondition addCond(LoadedCondition cond) throws Exception {
		System.out.println("Добавляю: "+JsonParser.toJson(cond));
		try {
			Connection connection = Jsoup.connect((useHttps?"https://":"http://")+host+":"+port+"/api/"+API_VERSION+
					"/conditions/add")
					.sslSocketFactory(SOCKET_FACTORY);
			connection.cookie("raccoontoken", token);
			Response response = JsonParser.fromJson(connection.ignoreContentType(true)
		        .method(Connection.Method.POST)
		        .requestBody(JsonParser.toJson(cond))
		        .header("Content-Type", "application/json; charset=utf-8")
		        .execute()
		        .body(), Response.class);
			if(!response.getError()) {
				return response.getConditions()[0];
			} else {
				throw new Exception(response.getCodeDescRu());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}		
	}

}

package com.haothink.utils;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author wanghao
 * 对httpClient的封装
 */
public class ApacheHttpClient {


	public static String defaultEncoding = "utf-8";     //默认字符集！可修改
	private PoolingHttpClientConnectionManager connManager;
	public static Map<String,String> cookieMap = new HashMap<String, String>(64);
	//获取HttpClient实例,http和https各种进！~.~！留下cookie的参数设计！应对验证码用户交互的登陆过程
	public HttpClient buildClient(BasicCookieStore cookieStore) {
		//设置连接参数
		ConnectionConfig connConfig = ConnectionConfig.custom().setCharset(Charset.forName(defaultEncoding)).build();
		SocketConfig socketConfig = SocketConfig.custom().build();
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
		ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
		registryBuilder.register("http", plainSF);
		try {
			//指定信任密钥存储对象和连接套接字工厂
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore,
					new TrustSelfSignedStrategy()).build();
			LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			registryBuilder.register("https", sslSF);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		} catch (KeyManagementException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

		Registry<ConnectionSocketFactory> registry = registryBuilder.build();
		//设置连接管理器
		connManager = new PoolingHttpClientConnectionManager(registry);
		connManager.setDefaultConnectionConfig(connConfig);
		connManager.setDefaultSocketConfig(socketConfig);
		// 将最大连接数增加到500
		connManager.setMaxTotal(500);
		// 将每个路由基础的连接增加到50
		connManager.setDefaultMaxPerRoute(50);
		//LaxRedirectStrategy可以自动重定向所有的HEAD，GET，POST请求，解除了http规范对post请求重定向的限制。
		//LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();
		//构建客户端
		return HttpClientBuilder.
				create().
				setDefaultCookieStore(cookieStore).
				setConnectionManager(connManager).
				//setRedirectStrategy(redirectStrategy).
						build();
	}

	/**
	 *  post方式请求获取文本数据
	 *  @param httpClient           httpClient对象（主要为了保证同一cookie的请求）
	 *  @param requestConfig        主要超时设置
	 *  @param url                  请求的地址
	 *  @param encoding             数据的字符集
	 *  @param params               请求参数
	 *  @return String              网页的html || 如果请求超时返回"TIMEOUT"
	 **/
	public String post(HttpClient httpClient, RequestConfig requestConfig, String url,
					   String encoding, List<NameValuePair> params)
			throws SocketTimeoutException, ConnectTimeoutException
	{
		return post(httpClient, requestConfig, url, encoding, params, null, null);
	}

	/**
	 *  post方式请求获取文本数据
	 *  @param httpClient           httpClient对象（主要为了保证同一cookie的请求）
	 *  @param requestConfig        主要超时设置
	 *  @param url                  请求的地址
	 *  @param encoding             数据的字符集
	 *  @param params               请求参数
	 *  @param headers              请求头
	 *  @return String              网页的html || 如果请求超时返回"TIMEOUT"
	 **/
	public String post(HttpClient httpClient, RequestConfig requestConfig, String url,
					   String encoding, List<NameValuePair> params, Map<String, String> headers)
			throws SocketTimeoutException, ConnectTimeoutException
	{
		return post(httpClient, requestConfig, url, encoding, params, headers, null);
	}

	/**
	 *  post方式请求获取文本数据
	 *  @param httpClient           httpClient对象（主要为了保证同一cookie的请求）
	 *  @param requestConfig        主要超时设置
	 *  @param url                  请求的地址
	 *  @param encoding             数据的字符集
	 *  @param headers              请求头
	 *  @param requestBody          请求body
	 *  @return String              网页的html || 如果请求超时返回"TIMEOUT"
	 **/
	public String post(HttpClient httpClient, RequestConfig requestConfig, String url,
					   String encoding, Map<String, String> headers, String requestBody)
			throws SocketTimeoutException, ConnectTimeoutException
	{
		return post(httpClient, requestConfig, url, encoding, null, headers, requestBody);
	}

	/**
	 *  post方式请求获取文本数据
	 *  @param httpClient           httpClient对象（主要为了保证同一cookie的请求）
	 *  @param requestConfig        主要超时设置
	 *  @param url                  请求的地址
	 *  @param encoding             数据的字符集
	 *  @param params               请求参数
	 *  @param headers              请求头
	 *  @param requestBody          请求body
	 *  @return String              网页的html || 如果请求超时返回"TIMEOUT"
	 **/
	public String post(HttpClient httpClient, RequestConfig requestConfig, String url,
					   String encoding, List<NameValuePair> params,
					   Map<String, String> headers, String requestBody)
			throws ConnectTimeoutException, SocketTimeoutException
	{
		ByteArrayOutputStream data = null;
		HttpPost httpPost = null;
		HttpResponse response = null;
		String newUrl = checkUrl(url);
		try {
			if (httpClient != null && newUrl != null) {

				httpPost = new HttpPost(newUrl);
				if (headers != null) {
					setHeader(httpPost, headers);
				}

				if (params != null) {
					if (encoding != null) {
						httpPost.setEntity(new UrlEncodedFormEntity(params, encoding));
					} else {
						httpPost.setEntity(new UrlEncodedFormEntity(params));
					}
				}

				if (requestConfig != null) {
					httpPost.setConfig(requestConfig);
				}

				if (requestBody != null) {      //默认使用json的格式
					StringEntity entity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
					httpPost.setEntity(entity);
				}

				response = httpClient.execute(httpPost);
				data = new ByteArrayOutputStream(1024);
				response.getEntity().writeTo(data);
				return encoding == null ? data.toString() : data.toString(encoding);
			}
		} catch (ConnectTimeoutException e) {
			throw new ConnectTimeoutException(url);
		} catch (SocketTimeoutException e) {
			throw new SocketTimeoutException(url);
		} catch (IOException e) {
			//log error
		} finally {
			close(null, httpPost, response, data);
		}

		return null;
	}

	/**
	 * post 方式获取HttpResponse
	 * @param httpClient        httpClient对象（主要为了保证同一cookie的请求）
	 * @param requestConfig     主要超时设置
	 * @param url               请求的url
	 * @param encoding          数据的字符集
	 * @param params            请求参数
	 * @param headers           请求头
	 * @return HttpResponse
	 * @throws ConnectTimeoutException
	 * @throws SocketTimeoutException
	 */
	public HttpResponse postResponse(HttpClient httpClient, RequestConfig requestConfig, String url,
									 String encoding, List<NameValuePair> params,
									 Map<String, String> headers)
			throws IOException {
		HttpPost httpPost = null;
		HttpResponse response = null;
		String newUrl = checkUrl(url);
		try {
			if (httpClient != null && newUrl != null) {
				httpPost = new HttpPost(newUrl);
				if (headers != null) {
					setHeader(httpPost, headers);
				}

				if (params != null) {
					if (encoding != null) {
						httpPost.setEntity(new UrlEncodedFormEntity(params, encoding));
					} else {
						httpPost.setEntity(new UrlEncodedFormEntity(params));
					}
				}

				if (requestConfig != null) {
					httpPost.setConfig(requestConfig);
				}

				return httpClient.execute(httpPost);

			}
		} catch (ConnectTimeoutException e) {
			throw new ConnectTimeoutException(url);
		} catch (SocketTimeoutException e) {
			throw new SocketTimeoutException(url);
		} catch (IOException e) {
			//log error
		} finally {
			if (null != response) {
				response.getEntity().getContent().close();
			}
		}

		return null;
	}

	/**
	 *  get方式请求获取文本数据
	 *  @param httpClient httpClient对象（主要为了保证同一cookie的请求）
	 *  @param requestConfig    主要超时设置
	 *  @param url 请求的地址
	 *  @param encoding  数据的字符集
	 *  @return String 网页的html
	 **/
	public String get(HttpClient httpClient, RequestConfig requestConfig, String url,
					  String encoding) throws ConnectTimeoutException, SocketTimeoutException {
		return get(httpClient, requestConfig, url, encoding, null);
	}

	/**
	 *  get方式请求获取文本数据
	 *  @param httpClient httpClient对象（主要为了保证同一cookie的请求）
	 *  @param requestConfig    主要超时设置
	 *  @param url              请求的地址
	 *  @param encoding         数据的字符集
	 *  @param headers          请求头
	 *  @return String 网页的html
	 **/
	public String get(HttpClient httpClient, RequestConfig requestConfig, String url,
					  String encoding, Map<String, String> headers)
			throws SocketTimeoutException, ConnectTimeoutException
	{
		ByteArrayOutputStream data = null;
		HttpGet httpGet = null;
		HttpResponse response = null;
		String newUrl = checkUrl(url);
		try {
			if (httpClient != null && newUrl != null) {

				httpGet = new HttpGet(newUrl);
				if (headers != null) {
					setHeader(httpGet, headers);
				}

				if (requestConfig != null) {
					httpGet.setConfig(requestConfig);
				}

				response = httpClient.execute(httpGet);
				data = new ByteArrayOutputStream(1024);
				response.getEntity().writeTo(data);
				return encoding == null ? data.toString() : data.toString(encoding);
			}
		} catch (ConnectTimeoutException e) {
			throw new ConnectTimeoutException(url);
		} catch (SocketTimeoutException e) {
			throw new SocketTimeoutException(url);
		} catch (IOException e) {
			//log error
		} finally {
			close(httpGet, null, response, data);
		}

		return null;
	}

	//获取验证码
	protected byte[] getCaptcha(HttpClient httpClient, RequestConfig requestConfig,
								Map<String, String> imageHeaders,String url)
			throws SocketTimeoutException, ConnectTimeoutException
	{
		HttpGet httpGet = null;
		HttpResponse response = null;
		ByteArrayOutputStream outStream = null;
		try {
			if (httpClient != null) {
				httpGet = new HttpGet(url);
				if (requestConfig != null) {
					httpGet.setConfig(requestConfig);
				}

				if (imageHeaders != null) {
					setHeader(httpGet, imageHeaders);
				}

				response = httpClient.execute(httpGet);
				Header[] headers = response.getHeaders("Content-Encoding");
				HttpEntity gzipEntity = response.getEntity();
				for(Header header : headers){
					if(header.getValue().contains("gzip")){
						gzipEntity = new GzipDecompressingEntity(gzipEntity);
						break;
					}
				}

				InputStream inputStream = gzipEntity.getContent();
				if (inputStream == null) {
					HttpClientUtils.closeQuietly(response);
				} else {
					outStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int length;
					while ((length = inputStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, length);
					}

					return outStream.toByteArray();
				}
			}
		} catch (ConnectTimeoutException e) {
			throw new ConnectTimeoutException(url);
		} catch (SocketTimeoutException e) {
			throw new SocketTimeoutException(url);
		} catch (IOException e) {
			//log error
		} finally {
			close(httpGet, null, response, outStream);
		}

		return null;
	}

	//设置请求头
	public void setHeader(AbstractHttpMessage message, Map<String, String> headers) {
		Set<String> keys = headers.keySet();
		for (String key : keys) {
			message.setHeader(key, headers.get(key));
		}
	}

	//关闭释放资源
	public void close(HttpGet get, HttpPost post, HttpResponse response, ByteArrayOutputStream data) {
		if (null != get) {
			get.releaseConnection();
		}

		if (null != post) {
			post.releaseConnection();
		}

		if (null != response) {
			try {
				response.getEntity().getContent().close();
			} catch (IOException e) {
				//log error
			}

			HttpClientUtils.closeQuietly(response);
		}

		if (null != data) {
			try {
				data.close();
			} catch (IOException e) {
				//log error
			}
		}
	}


	@Override
	protected void finalize() throws Throwable {
		closeConnManager();
		super.finalize();
	}

	private void closeConnManager() {
		// 关闭失效的连接
		connManager.closeExpiredConnections();
		// 可选的, 关闭500秒内不活动的连接
		connManager.closeIdleConnections(500, TimeUnit.SECONDS);
	}

	//检查数据合法性
	private boolean checkData(String urlString) {
		return urlString != null && urlString.startsWith("http://") ||
				urlString != null && urlString.startsWith("https://");
	}

	private String checkUrl(String url) {
		if (url == null) {
			return null;
		}

		String newUrl = url;
		if (!checkData(url)) {
			if (url.startsWith("//")) {
				newUrl = "http:" + url;
			}
		}

		return newUrl;
	}

	/**
	 *
	 * @param httpResponse
	 * @return   获取cookie字符串
	 */
	public static String getCookie(HttpResponse httpResponse)
	{
		System.out.println("----getCookieStore");
		Header headers[] = httpResponse.getHeaders("Set-Cookie");
		if (headers == null || headers.length==0)
		{
			System.out.println("----there are no cookies");
			return null;
		}
		String cookie = "";
		for (int i = 0; i < headers.length; i++) {
			cookie += headers[i].getValue();
			if(i != headers.length-1)
			{
				System.out.println(cookie);
				cookie += ";";
			}
		}

		String cookies[] = cookie.split(";");
		for (String c : cookies)
		{
			c = c.trim();
			if(cookieMap.containsKey(c.split("=")[0]))
			{
				cookieMap.remove(c.split("=")[0]);
			}
			cookieMap.put(c.split("=")[0], c.split("=").length == 1 ? "":(c.split("=").length ==2?c.split("=")[1]:c.split("=",2)[1]));
		}
		System.out.println("----setCookieStore success");
		String cookiesTmp = "";
		for (String key :cookieMap.keySet())
		{
			cookiesTmp +=key+"="+cookieMap.get(key)+";";
		}

		return cookiesTmp.substring(0,cookiesTmp.length()-2);
	}

	/**
	 * 打印成功的信息
	 * @param httpResponse http的响应实体
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void printResponse(HttpResponse httpResponse)
			throws ParseException, IOException {
		// 获取响应消息实体
		HttpEntity entity = httpResponse.getEntity();
		// 响应状态
		System.out.println("status:" + httpResponse.getStatusLine());
		System.out.println("headers:");
		HeaderIterator iterator = httpResponse.headerIterator();
		while (iterator.hasNext()) {
			System.out.println("\t" + iterator.next());
		}
		// 判断响应实体是否为空
		if (entity != null) {
			String responseString = EntityUtils.toString(entity);
			System.out.println("response length:" + responseString.length());
			System.out.println("response content:"
					+ responseString.replace("\r\n", ""));
		}
	}
}

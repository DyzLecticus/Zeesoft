package nl.zeesoft.zdk.http;

import java.net.URL;

public class HttpRequest extends HttpIO {
	public static String	HEAD		= "HEAD";
	public static String	GET			= "GET";
	public static String	PUT			= "PUT";
	public static String	POST		= "POST";
	
	public String			method		= "";
	public String			path		= "";
	public StringBuilder	query		= new StringBuilder();
	
	public HttpRequest() {
		
	}
	
	public HttpRequest(String method, String path) {
		this.method = method;
		this.path = path;
	}
	
	public void setDefaultHeaders(URL url) {
		if (head.get(HttpHeader.HOST)==null) {
			head.add(HttpHeader.HOST, getHostString(url));
		}
		if (head.get(HttpHeader.CONNECTION)==null) {
			setConnectionClose();
		}
		setContentLength();
	}
	
	protected static String getHostString(URL url) {
		String r = url.getHost();
		int port = getPort(url);
		if (port!=80) {
			r = ":" + port;
		}
		return r;
	}
	
	protected static int getPort(URL url) {
		int r = url.getPort();
		if (r==-1) {
			r = 80;
		}
		return r;
	}
}

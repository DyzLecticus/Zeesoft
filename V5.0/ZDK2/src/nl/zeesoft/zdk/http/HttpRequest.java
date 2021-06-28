package nl.zeesoft.zdk.http;

public class HttpRequest extends HttpIO {
	public static String	HEAD		= "HEAD";
	public static String	GET			= "GET";
	public static String	PUT			= "PUT";
	public static String	POST		= "POST";
	
	public String			method		= "";
	public String			path		= "";
	
	public HttpRequest() {
		
	}
	
	public HttpRequest(String method, String path) {
		this.method = method;
		this.path = path;
	}
}

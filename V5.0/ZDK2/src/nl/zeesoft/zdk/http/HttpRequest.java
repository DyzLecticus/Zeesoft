package nl.zeesoft.zdk.http;

public class HttpRequest extends HttpIO {
	public String			method		= "";
	public String			path		= "";
	
	public HttpRequest() {
		
	}
	
	public HttpRequest(String method, String path) {
		this.method = method;
		this.path = path;
	}
}

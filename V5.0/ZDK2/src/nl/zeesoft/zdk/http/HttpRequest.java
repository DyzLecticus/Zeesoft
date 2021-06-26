package nl.zeesoft.zdk.http;

public class HttpRequest {
	public String			method		= "";
	public String			path		= "";
	public String			protocol	= "HTTP/1.1";
	public HttpHeaders		head		= new HttpHeaders();
	public StringBuilder	body		= new StringBuilder();
	
	public HttpRequest() {
		
	}
	
	public HttpRequest(String method, String path) {
		this.method = method;
		this.path = path;
	}
}

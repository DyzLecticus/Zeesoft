package nl.zeesoft.zdk.http;

public class HttpContextHandler {
	public String	path	= "";
	
	public HttpContextHandler() {
		
	}
	
	public HttpContextHandler(String path) {
		this.path = path;
	}
	
	public void handleRequest(HttpRequest request, HttpResponse response) {
		// Override to implement
	}
}

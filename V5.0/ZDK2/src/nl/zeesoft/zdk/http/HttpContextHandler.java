package nl.zeesoft.zdk.http;

import java.util.ArrayList;
import java.util.List;

public class HttpContextHandler {
	public String			path				= "";
	public List<String>		allowedMethods	= new ArrayList<String>();
	
	public HttpContextHandler() {
		
	}
	
	public HttpContextHandler(String path) {
		this.path = path;
	}
	
	public void handleRequest(HttpRequest request, HttpResponse response) {
		// Override to implement
	}
}

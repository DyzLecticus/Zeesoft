package nl.zeesoft.zdk.http;

import nl.zeesoft.zdk.Logger;

public class HttpRequestHandler {
	public final HttpResponse handleRequest(HttpRequest request) {
		HttpResponse response = new HttpResponse();
		response.head.add(HttpHeader.CONNECTION, request.getConnection());
		try {
			handleRequest(request, response);
		} catch(Exception e) {
			Logger.error(this, "Caught exception in HTTP request handler", e);
			response.setInternalError();
		}
		return response;
	}
	
	public void handleRequest(HttpRequest request, HttpResponse response) {
		// Override to implement
	}
}

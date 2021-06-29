package nl.zeesoft.zdk.http;

public class HttpRequestHandler {
	public final HttpResponse handleRequest(HttpRequest request) {
		HttpResponse response = new HttpResponse();
		response.head.add(HttpHeader.CONNECTION, request.getConnection());
		handleRequest(request, response);
		return response;
	}
	
	public void handleRequest(HttpRequest request, HttpResponse response) {
		// Override to implement
	}
}

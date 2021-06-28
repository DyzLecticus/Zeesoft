package nl.zeesoft.zdk.http;

import java.net.HttpURLConnection;

public class HttpResponse extends HttpIO {
	public int		code		= HttpURLConnection.HTTP_OK;
	public String	message		= "OK";
	
	public HttpResponse() {
		
	}
	
	public HttpResponse(int code, String message) {
		this.code = code;
		this.message = message;
	}
}

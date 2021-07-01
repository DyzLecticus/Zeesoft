package nl.zeesoft.zdk.http;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class HttpResponse extends HttpIO {
	public int		code		= HttpURLConnection.HTTP_OK;
	public String	message		= "OK";
	public Date		modified	= new Date();
	
	public HttpResponse() {
		
	}
	
	public HttpResponse(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public void setDefaultHeaders() {
		setContentLength();
		head.add("Cache-Control", "no-cache, no-store, must-revalidate");
		head.add("Pragma", "no-cache");
		head.add("Last-Modified", getLastModifiedDateString(modified));
	}
	
	public void setNotFound() {
		code = HttpURLConnection.HTTP_NOT_FOUND;
		message = "Not Found";
	}
	
	public void setNotImplemented() {
		code = HttpURLConnection.HTTP_NOT_IMPLEMENTED;
		message = "Not Implemented";
	}
	
	protected final String getLastModifiedDateString(Date modified) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormat.format(modified);
	}
}

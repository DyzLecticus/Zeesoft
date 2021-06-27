package nl.zeesoft.zdk.http;

public abstract class HttpIO {
	public String			protocol	= "HTTP/1.1";
	public HttpHeaders		head		= new HttpHeaders();
	public StringBuilder	body		= new StringBuilder();
}

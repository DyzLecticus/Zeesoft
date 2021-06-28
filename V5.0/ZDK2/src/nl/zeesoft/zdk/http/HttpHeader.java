package nl.zeesoft.zdk.http;

public class HttpHeader {
	public static final String		CONTENT_LENGTH		= "Content-Length";
	public static final String		CONTENT_TYPE		= "Content-Type";
	public static final String		HOST				= "Host";
	public static final String		CONNECTION			= "Connection";
	public static final String		PROXY_CONNECTION	= "Proxy-Connection";

	public String					name				= "";
	public String					value				= "";
	
	public HttpHeader() {
		
	}
	
	public HttpHeader(String name, String value) {
		this.name = name;
		this.value = value;
	}
}

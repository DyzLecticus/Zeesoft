package nl.zeesoft.zdk.http;

public class HttpHeader {
	public String	name	= "";
	public String	value	= "";
	
	public HttpHeader() {
		
	}
	
	public HttpHeader(String name, String value) {
		this.name = name;
		this.value = value;
	}
}

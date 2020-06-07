package nl.zeesoft.zdk.http;

import java.util.List;

public class HttpHeader {
	public static final String		CONTENT_LENGTH	= "Content-Length";
	
	public String 					name			= "";
	public String					value			= "";
	
	public HttpHeader() {
		
	}
	
	public HttpHeader(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public static HttpHeader getHeader(List<HttpHeader> headers, String name) {
		HttpHeader r = null;
		for (HttpHeader header: headers) {
			if (header.name.equals(name)) {
				r = header;
				break;
			}
		}
		return r;
	}
	
	public static void addContentLengthHeader(List<HttpHeader> headers, int length) {
		HttpHeader contentLength = HttpHeader.getHeader(headers,HttpHeader.CONTENT_LENGTH);
		if (contentLength==null) {
			headers.add(new HttpHeader(HttpHeader.CONTENT_LENGTH,"" + length));
		}
	}
}

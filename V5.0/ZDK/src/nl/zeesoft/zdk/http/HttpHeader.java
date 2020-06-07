package nl.zeesoft.zdk.http;

import java.util.List;

public class HttpHeader {
	public static final String		CONTENT_LENGTH	= "Content-Length";
	public static final String		CONTENT_TYPE	= "Content-Type";
	public static final String		HOST			= "Host";
	public static final String		CONNECTION		= "Connection";
	
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
		HttpHeader contentLength = HttpHeader.getHeader(headers,CONTENT_LENGTH);
		if (contentLength==null) {
			headers.add(new HttpHeader(CONTENT_LENGTH,"" + length));
		}
	}
	
	public static void addContentTypeHeader(List<HttpHeader> headers, String type) {
		HttpHeader contentLength = HttpHeader.getHeader(headers,CONTENT_TYPE);
		if (contentLength==null) {
			headers.add(new HttpHeader(CONTENT_TYPE,type));
		}
	}
	
	public static void addHostHeader(List<HttpHeader> headers, String host) {
		HttpHeader contentLength = HttpHeader.getHeader(headers,HOST);
		if (contentLength==null) {
			headers.add(new HttpHeader(HOST,host));
		}
	}
	
	public static void addConnectionHeader(List<HttpHeader> headers, String connection) {
		HttpHeader contentLength = HttpHeader.getHeader(headers,CONNECTION);
		if (contentLength==null) {
			headers.add(new HttpHeader(CONNECTION,connection));
		}
	}
}

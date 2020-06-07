package nl.zeesoft.zdk.http;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Str;

public class HttpHeaderList {
	public static final String		CONTENT_LENGTH	= "Content-Length";
	public static final String		CONTENT_TYPE	= "Content-Type";
	public static final String		HOST			= "Host";
	public static final String		CONNECTION		= "Connection";
	
	private List<HttpHeader>		headers			= new ArrayList<HttpHeader>();
	
	public HttpHeaderList copy() {
		HttpHeaderList r = new HttpHeaderList();
		r.addAll(this);
		return r;
	}
	
	public void add(String name, String value) {
		headers.add(new HttpHeader(name,value));
	}
	
	public void addAll(HttpHeaderList headerList) {
		for (HttpHeader header: headerList.list()) {
			add(header.name,header.value);
		}
	}
	
	public List<HttpHeader> list() {
		return headers;
	}
	
	public HttpHeader get(String name) {
		HttpHeader r = null;
		for (HttpHeader header: headers) {
			if (header.name.equals(name)) {
				r = header;
				break;
			}
		}
		return r;
	}
	
	public void set(String name, String value) {
		HttpHeader header = get(name);
		if (header!=null) {
			header.value = value;
		}
	}
	
	public String getValue(String name) {
		String r = "";
		for (HttpHeader header: headers) {
			if (header.name.equals(name)) {
				r = header.value;
				break;
			}
		}
		return r;
	}
	
	public int getIntegerValue(String name) {
		int r = 0;
		String value = getValue(name);
		if (value.length()>0) {
			try {
				r = Integer.parseInt(value);
			} catch (NumberFormatException e) {
				// Ignore
			}
		}
		return r;
	}
	
	public void clear() {
		headers.clear();
	}
	
	public Str toStr() {
		Str r = new Str();
		for (HttpHeader header: headers) {
			if (r.length()>0) {
				r.sb().append("\r\n");
			}
			r.sb().append(header.name);
			r.sb().append(": ");
			r.sb().append(header.value);
		}
		return r;

	}
	
	public void addContentLengthHeader(int length) {
		addHeaderIfNotInHeaders(HttpHeader.CONTENT_LENGTH, "" + length);
	}
	
	public void addContentTypeHeader(String type) {
		addHeaderIfNotInHeaders(HttpHeader.CONTENT_TYPE, type);
	}
	
	public void addHostHeader(String host) {
		addHeaderIfNotInHeaders(HttpHeader.HOST, host);
	}
	
	public void addPathHeader(String path) {
		addHeaderIfNotInHeaders(HttpHeader.HOST, path);
	}
	
	public void addConnectionHeader(String connection) {
		addHeaderIfNotInHeaders(HttpHeader.CONNECTION, connection);
	}
	
	private void addHeaderIfNotInHeaders(String name, String value) {
		HttpHeader header = get(name);
		if (header==null) {
			add(name,value);
		}
	}
}

package nl.zeesoft.zdk.http;

import java.util.ArrayList;
import java.util.List;

public class HttpHeaders {
	public List<HttpHeader>		headers		= new ArrayList<HttpHeader>();
	
	public HttpHeader add(String name, String value) {
		HttpHeader r = new HttpHeader(name, value);
		headers.add(r);
		return r;
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
	
	public HttpHeader remove(String name) {
		HttpHeader r = get(name);
		if (r!=null) {
			headers.remove(r);
		}
		return r;
	}
}

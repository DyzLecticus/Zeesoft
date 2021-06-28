package nl.zeesoft.zdk.http;

import nl.zeesoft.zdk.Util;

public abstract class HttpIO {
	public String			protocol	= "HTTP/1.1";
	public HttpHeaders		head		= new HttpHeaders();
	public byte[]			body		= new byte[0];
	
	public void setBody(StringBuilder str) {
		body = new byte[str.length()];
		for (int i = 0; i < str.length(); i++) {
			body[i] = str.substring(i,i+1).getBytes()[0];
		}
	}
	
	public StringBuilder getBody() {
		StringBuilder r = new StringBuilder();
		for (int i = 0; i < body.length; i++) {
			r.append((char)body[i]);
		}
		return r;
	}
	
	public void setContentLength() {
		HttpHeader header = head.get(HttpHeader.CONTENT_LENGTH);
		if (header!=null) {
			header.value = "" + body.length;
		} else {
			head.add(HttpHeader.CONTENT_LENGTH, "" + body.length);
		}
	}
	
	public int getContentLength() {
		int r = 0;
		HttpHeader header = head.get(HttpHeader.CONTENT_LENGTH);
		if (header!=null) {
			r = Util.parseInt(header.value);
		}
		return r;
	}
}

package nl.zeesoft.zdk.http;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.json.Json;

public abstract class HttpIO {
	public String			protocol	= "HTTP/1.1";
	public HttpHeaders		head		= new HttpHeaders();
	public byte[]			body		= new byte[0];
	
	public void setBody(Json json) {
		setBody(json.toStringBuilder());
		setContentTypeJson();
	}
		
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
	
	public void setContentType(String type) {
		head.set(HttpHeader.CONTENT_TYPE, type);
	}
	
	public void setContentTypeHtml() {
		setContentType("text/html");
	}
	
	public void setContentTypeJavaScript() {
		setContentType("application/javascript");
	}
	
	public void setContentTypeCss() {
		setContentType("text/css");
	}
	
	public void setContentTypeJson() {
		setContentType("application/json");
	}
	
	public void setContentTypeText() {
		setContentType("text/plain");
	}
	
	public void setContentLength() {
		head.set(HttpHeader.CONTENT_LENGTH, "" + body.length);
	}
	
	public int getContentLength() {
		int r = 0;
		HttpHeader header = head.get(HttpHeader.CONTENT_LENGTH);
		if (header!=null) {
			r = Util.parseInt(header.value);
		}
		return r;
	}
	
	public void setConnection(String connection) {
		head.set(HttpHeader.CONNECTION, connection);
	}
	
	public String getConnection() {
		String r = "close";
		HttpHeader header = head.get(HttpHeader.CONNECTION);
		if (header!=null) {
			r = header.value;
		}
		return r;
	}
	
	public void setConnectionClose() {
		setConnection("close");
	}
	
	public void setConnectionKeepAlive() {
		setConnection("keep-alive");
	}
	
	public boolean isConnectionClose() {
		return getConnection().equals("close");
	}
}

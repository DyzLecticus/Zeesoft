package nl.zeesoft.zdk.http;

import java.net.HttpURLConnection;

import nl.zeesoft.zdk.Str;

public class HttpResponse {
	public String				protocol		= "HTTP/1.1";
	public int					code			= HttpURLConnection.HTTP_OK;
	public String				message			= "OK";
	public HttpHeaderList		headers			= new HttpHeaderList();
	public Str					body			= new Str();
	public byte[]				bytes			= null;
	
	public boolean				close			= true;
	
	public Str toHeaderStr() {
		addContentLengthHeader();
		Str r = new Str();
		r.sb().append(protocol);
		r.sb().append(" ");
		r.sb().append(code);
		r.sb().append(" ");
		r.sb().append(message);
		Str header = headers.toStr();
		if (header.length()>0) {
			r.sb().append("\r\n");
			r.sb().append(header);
		}
		return r;
	}
	
	public Str toStr() {
		Str r = toHeaderStr();
		r.sb().append("\r\n");
		r.sb().append("\r\n");
		r.sb().append(body.sb());
		return r;
	}
	
	private void addContentLengthHeader() {
		if (body!=null && body.length()>0) {
			headers.addContentLengthHeader(body.length());
		} else if (bytes!=null && bytes.length>0) {
			headers.addContentLengthHeader(bytes.length);
		}
	}
}

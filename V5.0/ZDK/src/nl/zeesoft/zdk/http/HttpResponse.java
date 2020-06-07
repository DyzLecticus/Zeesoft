package nl.zeesoft.zdk.http;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Str;

public class HttpResponse {
	public String				protocol		= "HTTP/1.1";
	public int					code			= HttpURLConnection.HTTP_OK;
	public String				message	= "OK";
	public List<HttpHeader>		headers			= new ArrayList<HttpHeader>();
	public Str					body			= new Str();
	public byte[]				bytes			= null;
	
	public boolean				close			= true;
	
	public void addContentLengthHeader() {
		if (body!=null && body.length()>0) {
			HttpHeader.addContentLengthHeader(headers,body.length());
		}
	}
	
	public Str toStr() {
		Str r = new Str();
		r.sb().append(protocol);
		r.sb().append(" ");
		r.sb().append(code);
		r.sb().append(" ");
		r.sb().append(message);
		r.sb().append("\r\n");
		for (HttpHeader header: headers) {
			r.sb().append(header.name);
			r.sb().append(": ");
			r.sb().append(header.value);
			r.sb().append("\r\n");
		}
		r.sb().append("\r\n");
		r.sb().append(body.sb());
		r.sb().append("\r\n");
		return r;
	}
}

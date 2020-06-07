package nl.zeesoft.zdk.http;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import nl.zeesoft.zdk.Str;

public class HttpRequest {
	protected HttpServerConfig			config		= null;
	
	public String						method		= "";
	public String						path		= "";
	public String						protocol	= "";
	public SortedMap<String,String>		headerMap	= new TreeMap<String,String>();
	public Str							body		= new Str();
	
	protected HttpRequest(HttpServerConfig config) {
		this.config = config.copy();
	}
	
	public String getPath() {
		String r = path;
		if (r.startsWith("/")) {
			r = r.substring(1);
		}
		if (r.length()==0) {
			r = config.getIndexFileName();
		}
		r = config.getFilePath() + r;
		return r;
	}
	
	public int getContentLength() {
		int r = 0;
		String contentLength = headerMap.get(HttpHeader.CONTENT_LENGTH);
		if (contentLength!=null) {
			try {
				r = Integer.parseInt(contentLength);
			} catch (NumberFormatException ex) {
				config.error(this,new Str("Exception occurred while parsing content length header"),ex);
			}
		}
		return r;
	}

	public String getHost() {
		return headerMap.get("Host");
	}

	public List<HttpHeader> getHeaders() {
		List<HttpHeader> r = new ArrayList<HttpHeader>();
		for (Entry<String,String> entry: headerMap.entrySet()) {
			r.add(new HttpHeader(entry.getKey(),entry.getValue()));
		}
		return r;
	}
	
	public boolean closeConnection() {
		String connection = headerMap.get("Connection");
		return connection==null || connection.equals("close");
	}
	
	public boolean keepConnectionAlive() {
		String connection = headerMap.get("Connection");
		return connection!=null && connection.equals("keep-alive");
	}
}

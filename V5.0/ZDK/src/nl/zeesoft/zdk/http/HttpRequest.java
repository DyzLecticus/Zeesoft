package nl.zeesoft.zdk.http;

import nl.zeesoft.zdk.Str;

public class HttpRequest {
	protected HttpServerConfig			config		= null;
	
	public String						method		= "";
	public String						path		= "";
	public String						protocol	= "";
	public HttpHeaderList				headers		= new HttpHeaderList();
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
		return headers.getIntegerValue(HttpHeader.CONTENT_LENGTH);
	}

	public String getHost() {
		return headers.getValue(HttpHeader.HOST);
	}

	public boolean isSelfHost() {
		String host = getHost();
		return isSelf(host);
	}
	
	public boolean isSelfPath() {
		return isSelf(path);
	}

	public boolean isSelf(String host) {
		boolean r = false;
		if (host.startsWith("http://")) {
			host = host.substring(7);
		} else if (host.startsWith("https://")) {
			host = host.substring(8);
		}
		if (host.startsWith("127.0.0.1:" + config.getPort()) ||
			host.startsWith("localhost:" + config.getPort())	
			) {
			r = true;
		}
		return r;
	}
	
	public boolean closeConnection() {
		return headers.getValue(HttpHeader.CONNECTION).equals("close");
	}
	
	public boolean keepConnectionAlive() {
		return headers.getValue(HttpHeader.CONNECTION).equals("keep-alive");
	}
}

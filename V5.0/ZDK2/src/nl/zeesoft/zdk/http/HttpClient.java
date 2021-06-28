package nl.zeesoft.zdk.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.str.ObjectStringConvertors;

public class HttpClient extends HttpConnection {
	protected URL						url					= null;
	
	public HttpRequestStringConvertor	requestConvertor	= (HttpRequestStringConvertor) ObjectStringConvertors.getConvertor(HttpRequest.class);
	public HttpResponseStringConvertor	responseConvertor	= (HttpResponseStringConvertor) ObjectStringConvertors.getConvertor(HttpResponse.class);
	
	public synchronized boolean connect(String urlString) {
		boolean r = false;
		try {
			url = new URL(urlString);
			r = connect(url);
		} catch (MalformedURLException e) {
			Logger.error(this, "Malformed URL exception", e);
		}
		return r;
	}

	public synchronized boolean connect(URL url) {
		boolean r = false;
		this.url = url;
		try {
			socket = new Socket(url.getHost(),getPort());
			r = open(socket);
		} catch(IOException e) {
			Logger.error(this, "IO exception", e);
		}
		return r;
	}
	
	public synchronized boolean isConnected() {
		return super.isOpen();
	}
	
	public synchronized boolean disconnect() {
		boolean r = super.close();
		return r;
	}
	
	public synchronized HttpResponse sendRequest(HttpRequest request) {
		HttpResponse r = null;
		if (isOpen()) {
			request.setContentLength();
			writeHead(requestConvertor.toStringBuilder(request));
			if (request.getContentLength()>0) {
				writeBody(request.body, false);
			}
			StringBuilder res = readHead();
			r = responseConvertor.fromStringBuilder(res);
			if (r.getContentLength()>0) {
				r.body = readBody(r.getContentLength(), false);
			}
		}
		return r;
	}
	
	protected int getPort() {
		int r = url.getPort();
		if (r==-1) {
			r = 80;
		}
		return r;
	}
}

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
	public boolean						errorLogIO			= true;
	
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
			socket = new Socket(url.getHost(),HttpRequest.getPort(url));
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
		boolean r = false;
		if (isOpen()) {
			r = super.close();
		}
		return r;
	}

	public synchronized HttpResponse sendRequest(HttpRequest request, String urlString) {
		connect(urlString);
		return sendRequest(request);
	}

	public synchronized HttpResponse sendRequest(HttpRequest request) {
		HttpResponse r = null;
		if (isOpen()) {
			request.setDefaultHeaders(url);
			writer.writeHead(requestConvertor.toStringBuilder(request));
			if (request.getContentLength()>0) {
				writer.writeBody(request.body, errorLogIO, false);
			}
			r = readResponse();
		}
		return r;
	}
	
	protected HttpResponse readResponse() {
		HttpResponse r = null;
		StringBuilder res = readHead(errorLogIO);
		r = responseConvertor.fromStringBuilder(res);
		if (r!=null) {
			if (r.getContentLength()>0) {
				r.body = readBody(r.getContentLength(), errorLogIO, false);
			}
			if (r.isConnectionClose()) {
				disconnect();
			}
		}
		return r;
	}
}

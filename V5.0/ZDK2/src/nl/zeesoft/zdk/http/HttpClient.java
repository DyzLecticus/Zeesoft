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
	
	public synchronized StringBuilder sendRequest(HttpRequest request) {
		StringBuilder r = new StringBuilder();
		if (isOpen()) {
			writeOutput(requestConvertor.toStringBuilder(request));
			r = readInput();
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

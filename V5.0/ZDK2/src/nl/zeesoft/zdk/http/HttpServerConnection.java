package nl.zeesoft.zdk.http;

import java.net.Socket;

import nl.zeesoft.zdk.Logger;

public class HttpServerConnection extends HttpConnection implements Runnable {
	public HttpServerConfig				config				= null;
	public Thread						thread				= null;

	public HttpServerConnection(HttpServerConfig config) {
		this.config = config;
	}
	
	@Override
	public synchronized boolean open(Socket socket) {
		boolean r = super.open(socket);
		startThread(r);
		return r;
	}

	@Override
	public synchronized boolean close() {
		thread = null;
		return super.close();
	}
	
	@Override
	protected synchronized boolean isOpen() {
		return thread!=null;
	}
	
	@Override
	public void run() {
		handleIO();
	}
	
	protected void startThread(boolean open) {
		if (open) {
			thread = new Thread(this);
			thread.start();
		}
	}
	
	protected void handleIO() {
		while(isOpen()) {
			StringBuilder input = readHead();
			if (input.length()==0) {
				if (isOpen()) {
					close();
				}
			} else {
				handleInput(input);
			}
		}
		stopped();
	}
	
	protected void handleInput(StringBuilder input) {
		HttpRequest request = config.getRequestConvertor().fromStringBuilder(input);
		debugLogRequestHeaders(request);
		if (request.method.equals(HttpRequest.PUT) || request.method.equals(HttpRequest.POST)) {
			request.body = readBody(request.getContentLength(), false);
		}
		HttpResponse response = config.getRequestHandler().handleRequest(request);
		response.setContentLength();
		debugLogResponseHeaders(response);
		writeHead(config.getResponseConvertor().toStringBuilder(response));
		writeBody(response.body, false);
	}
	
	protected void debugLogRequestHeaders(HttpRequest request) {
		if (config.isDebugLogHeaders()) {
			StringBuilder str = getRequestString(request);
			str.append(getHeadersString(request, "\n<<< "));
			Logger.debug(this, str);
		}
	}
	
	protected void debugLogResponseHeaders(HttpResponse response) {
		if (config.isDebugLogHeaders()) {
			StringBuilder str = getResponseString(response);
			str.append(getHeadersString(response, "\n>>> "));
			Logger.debug(this, str);
		}
	}
	
	protected StringBuilder getRequestString(HttpRequest request) {
		StringBuilder r = new StringBuilder("\n<<< ");
		r.append(request.method);
		r.append(" ");
		r.append(request.path);
		return r;
	}
	
	protected StringBuilder getResponseString(HttpResponse request) {
		StringBuilder r = new StringBuilder("\n>>> ");
		r.append(request.code);
		r.append(" ");
		r.append(request.message);
		return r;
	}
	
	protected StringBuilder getHeadersString(HttpIO io, String prefix) {
		StringBuilder r = new StringBuilder();
		for (HttpHeader header: io.head.headers) {
			r.append(prefix);
			r.append(header.name);
			r.append(": ");
			r.append(header.value);
		}
		return r;
	}

	protected void stopped() {
		// Override to implement
	}
}

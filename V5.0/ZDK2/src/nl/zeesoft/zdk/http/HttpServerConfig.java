package nl.zeesoft.zdk.http;

import nl.zeesoft.zdk.Instantiator;
import nl.zeesoft.zdk.str.ObjectStringConvertors;

public class HttpServerConfig {
	protected int							port				= 8080;
	protected HttpRequestStringConvertor	requestConvertor	= (HttpRequestStringConvertor) ObjectStringConvertors.getConvertor(HttpRequest.class);
	protected HttpResponseStringConvertor	responseConvertor	= (HttpResponseStringConvertor) ObjectStringConvertors.getConvertor(HttpResponse.class);
	protected HttpRequestHandler			requestHandler		= new HttpRequestHandler();
	protected boolean						debugLogHeaders		= false;

	public HttpServerConfig copy() {
		HttpServerConfig r = (HttpServerConfig) Instantiator.getNewClassInstance(this.getClass());
		r.port = this.port;
		r.requestConvertor = this.requestConvertor;
		r.responseConvertor = this.responseConvertor;
		r.requestHandler = this.requestHandler;
		r.debugLogHeaders = this.debugLogHeaders;
		return r;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public HttpRequestStringConvertor getRequestConvertor() {
		return requestConvertor;
	}

	public void setRequestConvertor(HttpRequestStringConvertor requestConvertor) {
		this.requestConvertor = requestConvertor;
	}

	public HttpResponseStringConvertor getResponseConvertor() {
		return responseConvertor;
	}

	public void setResponseConvertor(HttpResponseStringConvertor responseConvertor) {
		this.responseConvertor = responseConvertor;
	}

	public HttpRequestHandler getRequestHandler() {
		return requestHandler;
	}

	public void setRequestHandler(HttpRequestHandler requestHandler) {
		this.requestHandler = requestHandler;
	}

	public boolean isDebugLogHeaders() {
		return debugLogHeaders;
	}

	public void setDebugLogHeaders(boolean debugLogHeaders) {
		this.debugLogHeaders = debugLogHeaders;
	}
}

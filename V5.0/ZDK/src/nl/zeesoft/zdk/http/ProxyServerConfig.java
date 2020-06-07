package nl.zeesoft.zdk.http;

import nl.zeesoft.zdk.Logger;

public class ProxyServerConfig extends HttpServerConfig {
	private int connectTimeoutMs	= 1000;
	private int readTimeoutMs		= 3000;
	
	public ProxyServerConfig() {
		setPort(9090);
	}

	public ProxyServerConfig(Logger logger) {
		super(logger);
		setPort(9090);
	}

	public int getConnectTimeoutMs() {
		return connectTimeoutMs;
	}
	
	public void setConnectTimeoutMs(int timeoutMs) {
		this.connectTimeoutMs = timeoutMs;
	}

	public int getReadTimeoutMs() {
		return readTimeoutMs;
	}
	
	public void setReadTimeoutMs(int timeoutMs) {
		this.readTimeoutMs = timeoutMs;
	}
	
	@Override
	protected HttpServerConfig copy() {
		ProxyServerConfig r = (ProxyServerConfig) super.copy();
		r.setConnectTimeoutMs(getConnectTimeoutMs());
		r.setReadTimeoutMs(getReadTimeoutMs());
		return r;
	}
	
	@Override
	protected HttpRequestHandler getNewHttpRequestHandler() {
		return new ProxyRequestHandler(this);
	}
}

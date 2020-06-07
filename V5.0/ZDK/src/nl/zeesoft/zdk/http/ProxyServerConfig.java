package nl.zeesoft.zdk.http;

import nl.zeesoft.zdk.Logger;

public class ProxyServerConfig extends HttpServerConfig {
	private int		readTimeoutMs	= 3000;
	
	public ProxyServerConfig() {
		setPort(9090);
		setAllowConnect(true);
	}

	public ProxyServerConfig(Logger logger) {
		super(logger);
		setPort(9090);
		setAllowConnect(true);
	}

	public int getReadTimeoutMs() {
		return readTimeoutMs;
	}

	public void setReadTimeoutMs(int readTimeoutMs) {
		this.readTimeoutMs = readTimeoutMs;
	}
	
	@Override
	protected HttpRequestHandler getNewHttpRequestHandler() {
		return new ProxyRequestHandler(this);
	}

	@Override
	protected HttpServerConfig copy() {
		ProxyServerConfig r = (ProxyServerConfig) super.copy();
		r.setReadTimeoutMs(getReadTimeoutMs());
		return r;
	}
}

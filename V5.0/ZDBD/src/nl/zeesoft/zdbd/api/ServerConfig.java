package nl.zeesoft.zdbd.api;

import nl.zeesoft.zdbd.ThemeController;
import nl.zeesoft.zdk.http.HttpRequestHandler;
import nl.zeesoft.zdk.http.HttpServerConfig;

public class ServerConfig extends HttpServerConfig {
	protected ThemeController controller = null;
	
	public ServerConfig(ThemeController controller) {
		this.controller = controller;
		this.setAllowPost(true);
	}
	
	public ServerConfig() {
		// To be used for copy function only
	}

	@Override
	protected HttpRequestHandler getNewHttpRequestHandler() {
		return new RequestHandler(this,controller);
	}
	
	@Override
	protected HttpServerConfig copy() {
		ServerConfig r = (ServerConfig) super.copy();
		r.controller = this.controller;
		return r;
	}
}

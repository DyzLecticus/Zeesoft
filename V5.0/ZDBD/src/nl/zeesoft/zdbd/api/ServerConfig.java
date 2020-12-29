package nl.zeesoft.zdbd.api;

import nl.zeesoft.zdbd.theme.ThemeController;
import nl.zeesoft.zdk.http.HttpRequestHandler;
import nl.zeesoft.zdk.http.HttpServerConfig;

public class ServerConfig extends HttpServerConfig {
	protected ThemeController		controller	= null;
	protected ControllerMonitor		monitor		= null;
	
	public ServerConfig(ThemeController controller, ControllerMonitor monitor) {
		this.controller = controller;
		this.monitor = monitor;
		this.setAllowPost(true);
	}
	
	public ServerConfig() {
		// To be used for copy function only
	}

	@Override
	protected HttpRequestHandler getNewHttpRequestHandler() {
		return new RequestHandler(this,controller,monitor);
	}
	
	@Override
	protected HttpServerConfig copy() {
		ServerConfig r = (ServerConfig) super.copy();
		r.controller = this.controller;
		r.monitor = this.monitor;
		return r;
	}
}

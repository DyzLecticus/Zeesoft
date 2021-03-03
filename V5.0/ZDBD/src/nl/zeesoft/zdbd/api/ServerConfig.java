package nl.zeesoft.zdbd.api;

import nl.zeesoft.zdbd.theme.ThemeController;
import nl.zeesoft.zdbd.theme.ThemeSequenceController;
import nl.zeesoft.zdk.http.HttpRequestHandler;
import nl.zeesoft.zdk.http.HttpServerConfig;

public class ServerConfig extends HttpServerConfig {
	protected ThemeController			controller	= null;
	protected ControllerMonitor			monitor		= null;
	protected ThemeSequenceController	sequencer	= null;
	
	public ServerConfig(ThemeController controller, ControllerMonitor monitor, ThemeSequenceController sequencer) {
		this.controller = controller;
		this.monitor = monitor;
		this.sequencer = sequencer;
		this.setAllowPost(true);
	}
	
	public ServerConfig() {
		// To be used for copy function only
	}

	@Override
	protected HttpRequestHandler getNewHttpRequestHandler() {
		return new RequestHandler(this.copy(),controller,monitor,sequencer);
	}
	
	@Override
	protected HttpServerConfig copy() {
		ServerConfig r = (ServerConfig) super.copy();
		r.controller = this.controller;
		r.monitor = this.monitor;
		r.sequencer = this.sequencer;
		return r;
	}
}

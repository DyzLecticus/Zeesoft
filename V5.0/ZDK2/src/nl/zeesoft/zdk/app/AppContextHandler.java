package nl.zeesoft.zdk.app;

import nl.zeesoft.zdk.http.HttpContextHandler;
import nl.zeesoft.zdk.http.HttpServer;

public class AppContextHandler extends HttpContextHandler {
	protected App	app		= null;
	
	public AppContextHandler(App app) {
		this.app = app;
	}
	
	public HttpServer getServer() {
		return app.server;
	}
}

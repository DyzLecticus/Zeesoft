package nl.zeesoft.zdk.app;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.http.HttpServerConfig;

public class AppConfig {
	public HttpServerConfig loadHttpServerConfig(App app) {
		return getNewHttpServerConfig(app);
	}
	
	public void onAppStart() {
		// Override to implement
	}

	public void onAppStop() {
		// Override to implement
	}
	
	public HttpServerConfig getNewHttpServerConfig(App app) {
		HttpServerConfig r = new HttpServerConfig();
		if (Logger.isLoggerDebug()) {
			r.setPort(1234);
			r.setDebugLogHeaders(Logger.isLoggerDebug());
		}
		return r;
	}
}

package nl.zeesoft.zdk.app;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.http.HttpContextRequestHandler;
import nl.zeesoft.zdk.http.HttpServerConfig;

public class AppConfig {
	public HttpServerConfig loadHttpServerConfig(App app) {
		HttpServerConfig r = getNewHttpServerConfig(app);
		HttpContextRequestHandler handler = (HttpContextRequestHandler) r.getRequestHandler();
		Logger.debug(this, handler.getPathHandlersStringBuilder());
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
		AppContextRequestHandler handler = new AppContextRequestHandler(app);
		handler.initializeContextHandlers();
		r.setRequestHandler(handler);
		return r;
	}
	
	public StringBuilder getParseBodyJsonError(Class<?> cls) {
		StringBuilder r = new StringBuilder("Failed to parse ");
		r.append(cls.getName());
		r.append(" from JSON");
		return r;
	}
}

package nl.zeesoft.zdk.app;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.http.HttpServerConfig;

public class AppConfig {
	public String				name				= "[AppConfig.name]";
	public int					port				= 1234;
	public int					destroyMessageMs	= 3000;
	
	public HttpServerConfig loadHttpServerConfig(App app) {
		return getNewHttpServerConfig(app);
	}
	
	public AppTrayIcon getNewAppTrayIcon(App app) {
		AppActionHandler actionHandler = getNewAppActionHandler();
		actionHandler.initialize(app);
		AppTrayIcon r = new AppTrayIcon();
		r.initialize(actionHandler, name);
		return r;
	}

	public AppActionHandler getNewAppActionHandler() {
		return new AppActionHandler();
	}
	
	public void onAppStart() {
		// Override to implement
	}

	public void onAppStop() {
		// Override to implement
	}
	
	public HttpServerConfig getNewHttpServerConfig(App app) {
		HttpServerConfig r = new HttpServerConfig();
		r.setPort(port);
		if (Logger.isLoggerDebug()) {
			r.setErrorLogIO(true);
			r.setDebugLogHeaders(true);
		}
		AppContextRequestHandler handler = new AppContextRequestHandler(app);
		handler.initializeContextHandlers();
		r.setRequestHandler(handler);
		return r;
	}
	
	public String getWelcomeMessage() {
		return "Click here for options";
	}
	
	public String getGoodbyeMessage() {
		return "Hope you had fun!";
	}
	
	public String getFailedToStartAppServerError() {
		return "Failed to start app server";
	}
	
	public StringBuilder getParseBodyJsonError(Class<?> cls) {
		StringBuilder r = new StringBuilder("Failed to parse ");
		r.append(cls.getName());
		r.append(" from JSON");
		return r;
	}
	
	public String getSelfUrl() {
		StringBuilder r = new StringBuilder("http://127.0.0.1");
		if (port!=80) {
			r.append(":");
			r.append(port);
		}
		r.append("/");
		return r.toString();
	}
}

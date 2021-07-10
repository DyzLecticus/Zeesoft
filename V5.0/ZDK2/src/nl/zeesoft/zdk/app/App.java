package nl.zeesoft.zdk.app;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.http.HttpContextRequestHandler;
import nl.zeesoft.zdk.http.HttpServer;
import nl.zeesoft.zdk.http.HttpServerConfig;
import nl.zeesoft.zdk.str.StrUtil;

public class App {
	protected AppConfig			config			= null;
	protected AppStateManager	state			= new AppStateManager();
	protected HttpServer		server			= null;
	
	public App(AppConfig config) {
		this.config = config;
	}
	
	public boolean start() {
		boolean r = state.ifSetState(AppStateManager.STARTING);
		if (r) {
			HttpServerConfig serverConfig = config.loadHttpServerConfig(this);
			logAppServerStarting(serverConfig);
			server = new HttpServer(serverConfig);
			if (server.open()) {
				logAppServerPathHandlers(serverConfig);
				config.onAppStart();
				r = state.ifSetState(AppStateManager.STARTED);
			} else {
				r = !state.ifSetState(AppStateManager.STOPPED);
			}
		}
		return r;
	}
	
	public boolean isStarted() {
		return state.isStarted();
	}
	
	public String getState() {
		return state.getState();
	}
	
	public boolean stop() {
		boolean r = state.ifSetState(AppStateManager.STOPPING);
		if (r) {
			config.onAppStop();
			server.close();
		}
		return r;
	}
	
	public StringBuilder getParseBodyJsonError(Class<?> cls) {
		return config.getParseBodyJsonError(cls);
	}
	
	protected void logAppServerStarting(HttpServerConfig serverConfig) {
		StringBuilder msg = new StringBuilder("Starting app server on port ");
		msg.append(serverConfig.getPort());
		msg.append(" ...");
		Logger.debug(this, msg);
	}
	
	protected void logAppServerPathHandlers(HttpServerConfig serverConfig) {
		StringBuilder msg = new StringBuilder("Started app server. Path handlers;");
		HttpContextRequestHandler handler = (HttpContextRequestHandler) serverConfig.getRequestHandler();
		StrUtil.appendLine(msg, handler.getPathHandlersStringBuilder());
		Logger.debug(this, msg);
	}
}

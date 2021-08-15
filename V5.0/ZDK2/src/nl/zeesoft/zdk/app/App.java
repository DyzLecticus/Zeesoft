package nl.zeesoft.zdk.app;

import java.awt.TrayIcon;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.http.HttpContextRequestHandler;
import nl.zeesoft.zdk.http.HttpServer;
import nl.zeesoft.zdk.http.HttpServerConfig;
import nl.zeesoft.zdk.str.StrUtil;

public class App {
	protected AppConfig			config			= null;
	protected AppStateManager	state			= new AppStateManager();
	protected AppTrayIcon		trayIcon		= null;
	protected HttpServer		server			= null;

	public App(AppConfig config) {
		this.config = config;
	}
	
	public String getName() {
		return config.name;
	}
	
	public String getSelfUrl() {
		return config.getSelfUrl();
	}
	
	public int getPort() {
		return config.port;
	}
	
	public boolean start() {
		boolean r = state.ifSetState(AppStateManager.STARTING);
		if (r) {
			initializeTrayIcon();
			HttpServerConfig serverConfig = initializeHttpServer();
			if (server.open()) {
				logAppServerPathHandlers(serverConfig);
				config.onAppStart();
				r = state.ifSetState(AppStateManager.STARTED);
			} else {
				state.ifSetState(AppStateManager.STOPPED);
				destroyTrayIcon(config.getFailedToStartAppServerError(),TrayIcon.MessageType.ERROR);
				r = false;
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
			r = server.close();
			destroyTrayIcon(config.getGoodbyeMessage(),TrayIcon.MessageType.INFO);
		}
		return r;
	}
	
	public AppTrayIcon getTrayIcon() {
		return trayIcon;
	}
	
	public StringBuilder getParseBodyJsonError(Class<?> cls) {
		return config.getParseBodyJsonError(cls);
	}
	
	protected HttpServerConfig initializeHttpServer() {
		HttpServerConfig r = config.loadHttpServerConfig(this);
		logAppServerStarting(r);
		server = new HttpServer(r);
		return r;
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
	
	protected void initializeTrayIcon() {
		trayIcon = config.getNewAppTrayIcon(this);
		if (config.getWelcomeMessage().length()>0) {
			trayIcon.displayMessage(config.name, config.getWelcomeMessage(), TrayIcon.MessageType.INFO);
		}
	}
	
	protected void destroyTrayIcon(String message, TrayIcon.MessageType type) {
		if (message.length()>0) {
			trayIcon.displayMessage(config.name, message, type);
			Util.sleep(config.destroyMessageMs);
		}
		trayIcon.destroy();
	}
}

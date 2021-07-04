package nl.zeesoft.zdk.app;

import nl.zeesoft.zdk.http.HttpServer;

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
			server = new HttpServer(config.loadHttpServerConfig(this));
			if (server.open()) {
				config.onAppStart();
				r = state.ifSetState(AppStateManager.STARTED);
			} else {
				state.ifSetState(AppStateManager.STOPPED);
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
			server.close();
		}
		return r;
	}
	
	public StringBuilder getParseBodyJsonError(Class<?> cls) {
		return config.getParseBodyJsonError(cls);
	}
}

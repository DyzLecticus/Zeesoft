package nl.zeesoft.zdk.app;

import nl.zeesoft.zdk.http.HttpServer;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;

public class App {
	protected AppConfig			config			= null;
	
	protected AppStateManager	state			= new AppStateManager();
	
	protected HttpServer		server			= null;
	protected NetworkConfig		networkConfig	= null;
	protected Network			network			= new Network();
	
	public App(AppConfig config) {
		this.config = config;
	}
	
	public boolean start() {
		boolean r = state.ifSetState(AppStateManager.STARTING);
		if (r) {
			server = new HttpServer(config.loadHttpServerConfig(this));
			networkConfig = config.loadNetworkConfig();
			network.initialize(networkConfig);
			if (!config.loadNetwork(network)) {
				network.reset();
			}
			server.open();
			r = state.ifSetState(AppStateManager.STARTED);
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
			server.close();
			config.saveNetworkConfig(networkConfig);
			config.saveNetwork(network);
			r = state.ifSetState(AppStateManager.STOPPED);
		}
		return r;
	}
}

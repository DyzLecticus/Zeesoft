package nl.zeesoft.zdk.app.neural;

import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;
import nl.zeesoft.zdk.neural.network.config.NetworkConfigFactory;

public class NetworkManager {
	protected NetworkStateManager 	stateManager	= new NetworkStateManager();
	protected NetworkConfig			config			= null;
	protected Network				network			= new Network();

	public synchronized NetworkConfig getConfig() {
		return config;
	}

	public synchronized void setConfig(NetworkConfig config) {
		this.config = config;
	}

	public boolean isReady() {
		return stateManager.isReady();
	}

	public String getState() {
		return stateManager.getState();
	}
	
	public boolean loadNetwork() {
		boolean r = false;
		if (stateManager.ifSetState(NetworkStateManager.LOADING)) {
			if (!loadNetwork(network)) {
				network.reset();
			}
			r = stateManager.ifSetState(NetworkStateManager.READY);
		}
		return r;
	}

	public boolean resetNetwork() {
		boolean r = false;
		if (stateManager.ifSetState(NetworkStateManager.RESETTING)) {
			r = network.initialize(config);
			if (r) {
				r = network.reset();
			}
			r = stateManager.ifSetState(NetworkStateManager.READY);
		}
		return r;
	}

	public boolean saveNetwork() {
		boolean r = false;
		if (stateManager.ifSetState(NetworkStateManager.SAVING)) {
			saveNetworkConfig(config);
			saveNetwork(network);
			r = stateManager.ifSetState(NetworkStateManager.READY);
		}
		return r;
	}
	
	protected void onAppStart() {
		initializeNetwork();
	}
	
	protected void onAppStop() {
		saveNetwork();
	}

	protected void initializeNetwork() {
		config = loadNetworkConfig();
		if (stateManager.ifSetState(NetworkStateManager.INITIALIZING)) {
			network.initialize(config);
			loadNetwork();
		}
	}
	
	protected NetworkConfig loadNetworkConfig() {
		NetworkConfigFactory factory = new NetworkConfigFactory();
		return factory.getSimpleConfig("input1", "input2");
	}
	
	protected boolean loadNetwork(Network network) {
		// Override to implement; return true if loaded
		return false;
	}
	
	protected void saveNetworkConfig(NetworkConfig config) {
		// Override to implement
	}
	
	protected void saveNetwork(Network network) {
		// Override to implement
	}
}

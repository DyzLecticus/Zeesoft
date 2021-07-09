package nl.zeesoft.zdk.app.neural;

import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;
import nl.zeesoft.zdk.neural.network.config.NetworkConfigFactory;

public class NetworkManager {
	protected NetworkStateManager 	stateManager	= new NetworkStateManager();
	protected NetworkConfig			config			= null;
	
	protected int					workers			= Runtime.getRuntime().availableProcessors();
	protected int					initTimeoutMs	= 10000;
	protected int					resetTimeoutMs	= 10000;

	protected Network				network			= new Network();
	
	public synchronized NetworkConfig getConfig() {
		return config;
	}

	public synchronized void setConfig(NetworkConfig config) {
		this.config = config;
	}

	public synchronized int getWorkers() {
		return workers;
	}

	public synchronized void setWorkers(int workers) {
		this.workers = workers;
		network.setNumberOfWorkers(workers);
	}

	public synchronized int getInitTimeoutMs() {
		return initTimeoutMs;
	}

	public synchronized void setInitTimeoutMs(int initTimeoutMs) {
		this.initTimeoutMs = initTimeoutMs;
	}

	public synchronized int getResetTimeoutMs() {
		return resetTimeoutMs;
	}

	public synchronized void setResetTimeoutMs(int resetTimeoutMs) {
		this.resetTimeoutMs = resetTimeoutMs;
	}
	
	public boolean isReady() {
		return stateManager.isReady();
	}

	public String getState() {
		return stateManager.getState();
	}
	
	public boolean processNetworkIO(NetworkIO io) {
		boolean r = false;
		if (stateManager.ifSetState(NetworkStateManager.PROCESSING)) {
			network.processIO(io);
			r = stateManager.ifSetState(NetworkStateManager.READY);
		}
		return r;
	}
	
	public boolean loadNetwork() {
		boolean r = false;
		if (stateManager.ifSetState(NetworkStateManager.LOADING)) {
			if (!loadNetwork(network)) {
				network.reset(getResetTimeoutMs());
			}
			r = stateManager.ifSetState(NetworkStateManager.READY);
		}
		return r;
	}

	public boolean resetNetwork() {
		boolean r = false;
		if (stateManager.ifSetState(NetworkStateManager.RESETTING)) {
			r = network.initialize(getConfig());
			if (r) {
				network.reset(getResetTimeoutMs());
			}
			r = stateManager.ifSetState(NetworkStateManager.READY);
		}
		return r;
	}

	public boolean saveNetwork() {
		boolean r = false;
		if (stateManager.ifSetState(NetworkStateManager.SAVING)) {
			saveNetworkConfig(getConfig());
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
		network.setNumberOfWorkers(0);
	}

	protected synchronized void initializeNetwork() {
		config = loadNetworkConfig();
		if (stateManager.ifSetState(NetworkStateManager.INITIALIZING)) {
			network.initialize(config, getInitTimeoutMs());
			loadNetwork();
			network.setNumberOfWorkers(getWorkers());
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

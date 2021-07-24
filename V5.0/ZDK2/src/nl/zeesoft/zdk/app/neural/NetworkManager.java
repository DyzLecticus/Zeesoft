package nl.zeesoft.zdk.app.neural;

import java.util.Map.Entry;
import java.util.SortedMap;

import nl.zeesoft.zdk.neural.model.CellStats;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.network.config.HotGymConfigFactory;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;

public class NetworkManager extends NetworkManagerSettings {
	protected NetworkStateManager 	stateManager	= new NetworkStateManager();
	protected NetworkConfig			config			= null;

	protected Network				network			= new Network();
	
	public synchronized NetworkConfig getConfig() {
		return config;
	}

	public synchronized void setConfig(NetworkConfig config) {
		this.config = config;
	}

	@Override
	public synchronized void setWorkers(int workers) {
		super.setWorkers(workers);
		network.setNumberOfWorkers(workers);
	}
	
	public boolean isReady() {
		return stateManager.isReady();
	}

	public String getState() {
		return stateManager.getState();
	}
	
	public boolean processNetworkIO(NetworkIO io, NetworkRecorder recorder) {
		boolean r = false;
		if (stateManager.ifSetState(NetworkStateManager.PROCESSING)) {
			network.processIO(io);
			recorder.add(io);
			r = stateManager.ifSetState(NetworkStateManager.READY);
		}
		return r;
	}
	
	public boolean setProcessorLearning(SortedMap<String,Boolean> processorLearning) {
		boolean r = false;
		if (stateManager.ifSetState(NetworkStateManager.PROCESSING)) {
			for (Entry<String,Boolean> entry: processorLearning.entrySet()) {
				network.setLearn(Network.ALL_LAYERS, entry.getKey(), entry.getValue());
			}
			r = stateManager.ifSetState(NetworkStateManager.READY);
		}
		return r;
	}
	
	public SortedMap<String,Boolean> getProcessorLearning() {
		SortedMap<String,Boolean> r = null;
		if (stateManager.ifSetState(NetworkStateManager.PROCESSING)) {
			r = getProcessorLearningForNetwork(network);
			stateManager.ifSetState(NetworkStateManager.READY);
		}
		return r;
	}
	
	public CellStats getCellStats() {
		CellStats r = null;
		if (stateManager.ifSetState(NetworkStateManager.PROCESSING)) {
			r = network.getCellStats();
			stateManager.ifSetState(NetworkStateManager.READY);
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

	protected synchronized boolean initializeNetwork() {
		boolean r = false;
		config = loadNetworkConfig();
		if (stateManager.ifSetState(NetworkStateManager.INITIALIZING)) {
			r = network.initialize(config, getInitTimeoutMs());
			loadNetwork();
			network.setNumberOfWorkers(getWorkers());
		}
		return r;
	}
	
	protected NetworkConfig loadNetworkConfig() {
		return HotGymConfigFactory.getNewHotGymNetworkConfig();
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

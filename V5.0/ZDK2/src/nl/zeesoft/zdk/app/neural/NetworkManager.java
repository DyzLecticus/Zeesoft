package nl.zeesoft.zdk.app.neural;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import nl.zeesoft.zdk.app.neural.handlers.api.NetworkSettings;
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
		super.getProcessorWorkers().clear();
		this.config = config;
	}

	@Override
	public synchronized void setWorkers(int workers) {
		if (workers>=0) {
			super.setWorkers(workers);
			network.setNumberOfWorkers(workers);
		}
	}
	
	@Override
	public synchronized void setProcessorWorkers(SortedMap<String, Integer> processorWorkers) {
		super.setProcessorWorkers(processorWorkers);
		for (Entry<String,Integer> entry: getProcessorWorkers().entrySet()) {
			network.setNumberOfWorkersForProcessor(Network.ALL_LAYERS, entry.getKey(), entry.getValue());
		}
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
	
	public boolean setProcessorLearningAndWorkers(SortedMap<String,Boolean> processorLearning,SortedMap<String,Integer> processorWorkers) {
		boolean r = false;
		if (stateManager.ifSetState(NetworkStateManager.PROCESSING)) {
			for (Entry<String,Boolean> entry: processorLearning.entrySet()) {
				network.setLearn(Network.ALL_LAYERS, entry.getKey(), entry.getValue());
			}
			setProcessorWorkers(processorWorkers);
			r = stateManager.ifSetState(NetworkStateManager.READY);
		}
		return r;
	}

	public boolean setProcessorLearningAndWorkers(NetworkSettings settings) {
		boolean r = false;
		if (stateManager.ifSetState(NetworkStateManager.PROCESSING)) {
			settings.processorLearning = getProcessorLearningForNetwork(network);
			if (getProcessorWorkers().size()==0) {
				List<String> names = getExecutorProcessorsForNetwork(network);
				for (String name: names) {
					getProcessorWorkers().put(name, 0);
				}
			}
			settings.processorWorkers = getProcessorWorkers();
			r = stateManager.ifSetState(NetworkStateManager.READY);
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
			if (getProcessorWorkers().size()>0) {
				setProcessorWorkers(getProcessorWorkers());
			}
		}
		return r;
	}
	
	protected NetworkConfig loadNetworkConfig() {
		getProcessorWorkers().put("SpatialPooler", getWorkers());
		getProcessorWorkers().put("TemporalMemory", getWorkers());
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

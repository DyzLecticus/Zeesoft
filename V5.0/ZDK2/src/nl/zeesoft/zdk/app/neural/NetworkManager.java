package nl.zeesoft.zdk.app.neural;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.network.NetworkProcessor;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;
import nl.zeesoft.zdk.neural.network.config.NetworkConfigFactory;
import nl.zeesoft.zdk.neural.processor.LearningProcessor;

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
			r = new TreeMap<String,Boolean>();
			List<NetworkProcessor> processors = network.getProcessors(Network.ALL_LAYERS, Network.ALL_PROCESSORS);
			for (NetworkProcessor processor: processors) {
				if (processor.processor instanceof LearningProcessor) {
					r.put(processor.name, ((LearningProcessor)processor.processor).isLearn());
				}
			}
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

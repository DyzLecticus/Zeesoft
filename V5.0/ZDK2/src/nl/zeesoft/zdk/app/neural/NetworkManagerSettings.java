package nl.zeesoft.zdk.app.neural;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkProcessor;
import nl.zeesoft.zdk.neural.processor.LearningProcessor;

public class NetworkManagerSettings {
	protected int	workers			= Runtime.getRuntime().availableProcessors();
	protected int	initTimeoutMs	= 10000;
	protected int	resetTimeoutMs	= 10000;

	public synchronized int getWorkers() {
		return workers;
	}

	public synchronized void setWorkers(int workers) {
		this.workers = workers;
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
	
	protected SortedMap<String,Boolean> getProcessorLearningForNetwork(Network network) {
		SortedMap<String,Boolean> r = new TreeMap<String,Boolean>();
		List<NetworkProcessor> processors = network.getProcessors(Network.ALL_LAYERS, Network.ALL_PROCESSORS);
		for (NetworkProcessor processor: processors) {
			if (processor.processor instanceof LearningProcessor) {
				r.put(processor.name, ((LearningProcessor)processor.processor).isLearn());
			}
		}
		return r;
	}

}

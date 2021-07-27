package nl.zeesoft.zdk.app.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkProcessor;
import nl.zeesoft.zdk.neural.processor.ExecutorProcessor;
import nl.zeesoft.zdk.neural.processor.LearningProcessor;

public class NetworkManagerSettings {
	private int							workers				= Runtime.getRuntime().availableProcessors();
	private int							initTimeoutMs		= 10000;
	private int							resetTimeoutMs		= 10000;
	private SortedMap<String,Integer>	processorWorkers	= new TreeMap<String,Integer>();

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
		if (initTimeoutMs>=1000) {
			this.initTimeoutMs = initTimeoutMs;
		}
	}

	public synchronized int getResetTimeoutMs() {
		return resetTimeoutMs;
	}

	public synchronized void setResetTimeoutMs(int resetTimeoutMs) {
		if (resetTimeoutMs>=1000) {
			this.resetTimeoutMs = resetTimeoutMs;
		}
	}

	public synchronized SortedMap<String, Integer> getProcessorWorkers() {
		return processorWorkers;
	}

	public synchronized void setProcessorWorkers(SortedMap<String, Integer> processorWorkers) {
		for (Entry<String,Integer> entry: processorWorkers.entrySet()) {
			if (entry.getValue() >=0 && this.processorWorkers.containsKey(entry.getKey())) {
				this.processorWorkers.put(entry.getKey(), entry.getValue());
			}
		}
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
	
	protected List<String> getExecutorProcessorsForNetwork(Network network) {
		List<String> r = new ArrayList<String>();
		List<NetworkProcessor> processors = network.getProcessors(Network.ALL_LAYERS, Network.ALL_PROCESSORS);
		for (NetworkProcessor processor: processors) {
			if (processor.processor instanceof ExecutorProcessor) {
				r.add(processor.name);
			}
		}
		return r;
	}
}

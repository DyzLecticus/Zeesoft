package nl.zeesoft.zdk.app.neural.handlers.api;

import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.app.neural.NetworkManager;

public class NetworkSettings {
	public int							workers				= -1;
	public int							initTimeoutMs		= -1;
	public int							resetTimeoutMs		= -1;
	public SortedMap<String,Boolean>	processorLearning	= new TreeMap<String,Boolean>();
	public SortedMap<String,Integer>	processorWorkers	= new TreeMap<String,Integer>();
	
	public NetworkSettings() {
		
	}
	
	public NetworkSettings(NetworkManager manager) {
		this.workers = manager.getWorkers();
		this.initTimeoutMs = manager.getInitTimeoutMs();
		this.resetTimeoutMs = manager.getResetTimeoutMs();
		if (!manager.setProcessorLearningAndWorkers(this)) {
			processorLearning = null;
			processorWorkers = null;
		}
	}
	
	public boolean configure(NetworkManager manager) {
		boolean r = (processorLearning.size()==0 && processorWorkers.size()==0)
			|| manager.setProcessorLearningAndWorkers(processorLearning, processorWorkers);
		if (r) {
			if (workers>=0) {
				manager.setWorkers(workers);
			}
			if (initTimeoutMs>=1000) {
				manager.setInitTimeoutMs(initTimeoutMs);
			}
			if (resetTimeoutMs>=1000) {
				manager.setResetTimeoutMs(resetTimeoutMs);
			}
		}
		return r;
	}
}

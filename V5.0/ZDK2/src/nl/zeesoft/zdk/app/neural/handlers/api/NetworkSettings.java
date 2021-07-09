package nl.zeesoft.zdk.app.neural.handlers.api;

import nl.zeesoft.zdk.app.neural.NetworkManager;

public class NetworkSettings {
	public int		workers				= Runtime.getRuntime().availableProcessors();
	public int		initTimeoutMs		= 10000;
	public int		resetTimeoutMs		= 10000;
	
	public NetworkSettings() {
		
	}
	
	public NetworkSettings(NetworkManager manager) {
		this.workers = manager.getWorkers();
		this.initTimeoutMs = manager.getInitTimeoutMs();
		this.resetTimeoutMs = manager.getResetTimeoutMs();
	}
	
	public void configure(NetworkManager manager) {
		if (manager.getWorkers()!=workers) {
			manager.setWorkers(workers);
		}
		manager.setInitTimeoutMs(initTimeoutMs);
		manager.setResetTimeoutMs(resetTimeoutMs);
	}
}

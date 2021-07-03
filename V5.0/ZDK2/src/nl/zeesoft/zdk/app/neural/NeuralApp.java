package nl.zeesoft.zdk.app.neural;

import nl.zeesoft.zdk.app.App;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;

public class NeuralApp extends App {
	public NeuralApp(NeuralAppConfig config) {
		super(config);
	}
	
	public synchronized void setNetworkConfig(NetworkConfig config) {
		if (isStarted()) {
			((NeuralAppConfig) this.config).networkConfig = config;
		}
	}
	
	public synchronized NetworkConfig getNetworkConfig() {
		NetworkConfig r = null;
		if (isStarted()) {
			r = ((NeuralAppConfig) this.config).networkConfig;
		}
		return r;
	}
	
	public synchronized Network getNetwork() {
		Network r = null;
		if (isStarted()) {
			r = ((NeuralAppConfig) this.config).network;
		}
		return r;
	}
}

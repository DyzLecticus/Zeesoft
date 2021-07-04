package nl.zeesoft.zdk.app.neural;

import nl.zeesoft.zdk.app.App;

public class NeuralApp extends App {
	public NeuralApp(NeuralAppConfig config) {
		super(config);
	}
	
	public synchronized NetworkManager getNetworkManager() {
		return ((NeuralAppConfig) this.config).getNetworkManager();
	}
}

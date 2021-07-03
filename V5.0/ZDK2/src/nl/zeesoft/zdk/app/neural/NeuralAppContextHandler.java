package nl.zeesoft.zdk.app.neural;

import nl.zeesoft.zdk.app.AppContextHandler;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;

public class NeuralAppContextHandler extends AppContextHandler {
	public NeuralAppContextHandler(NeuralApp app) {
		super(app);
	}
	
	public void setNetworkConfig(NetworkConfig config) {
		((NeuralApp)app).setNetworkConfig(config);
	}
	
	public NetworkConfig getNetworkConfig() {
		return ((NeuralApp)app).getNetworkConfig();
	}
	
	public Network getNetwork() {
		return ((NeuralApp)app).getNetwork();
	}
}

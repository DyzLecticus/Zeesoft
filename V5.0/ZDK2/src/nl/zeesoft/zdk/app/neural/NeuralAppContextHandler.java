package nl.zeesoft.zdk.app.neural;

import nl.zeesoft.zdk.app.AppContextHandler;

public class NeuralAppContextHandler extends AppContextHandler {
	public NeuralAppContextHandler(NeuralApp app) {
		super(app);
	}
	
	public NetworkManager getNetworkManager() {
		return ((NeuralApp)app).getNetworkManager();
	}
}

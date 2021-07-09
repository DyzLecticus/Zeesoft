package nl.zeesoft.zdk.test.app;

import nl.zeesoft.zdk.app.neural.NetworkManager;
import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppConfig;

public class MockNeuralApp extends NeuralApp {
	public MockNeuralApp(NeuralAppConfig config) {
		super(config);
	}

	@Override
	public NetworkManager getNetworkManager() {
		return new MockNetworkManager();
	}
}

package nl.zeesoft.zdk.test.app;

import nl.zeesoft.zdk.app.neural.NetworkManager;

public class MockNetworkManager extends NetworkManager {
	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public boolean resetNetwork() {
		return false;
	}
}

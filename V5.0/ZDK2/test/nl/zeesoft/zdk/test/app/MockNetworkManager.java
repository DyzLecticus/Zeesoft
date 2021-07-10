package nl.zeesoft.zdk.test.app;

import nl.zeesoft.zdk.app.neural.NetworkManager;
import nl.zeesoft.zdk.app.neural.NetworkStateManager;

public class MockNetworkManager extends NetworkManager {
	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public boolean resetNetwork() {
		return false;
	}

	@Override
	public boolean initializeNetwork() {
		stateManager.ifSetState(NetworkStateManager.INITIALIZING);
		return super.initializeNetwork();
	}
}

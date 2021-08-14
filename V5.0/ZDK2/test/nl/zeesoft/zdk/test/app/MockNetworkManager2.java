package nl.zeesoft.zdk.test.app;

import nl.zeesoft.zdk.app.neural.NetworkManager;
import nl.zeesoft.zdk.app.neural.NetworkStateManager;

public class MockNetworkManager2 extends NetworkManager {
	public void setProcessing() {
		stateManager.ifSetState(NetworkStateManager.PROCESSING);
	}
	public void unsetProcessing() {
		stateManager.ifSetState(NetworkStateManager.READY);
	}
}

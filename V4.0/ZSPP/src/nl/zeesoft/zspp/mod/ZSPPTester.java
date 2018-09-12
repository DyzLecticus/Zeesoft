package nl.zeesoft.zspp.mod;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.TesterObject;

public class ZSPPTester extends TesterObject {
	public ZSPPTester(Config config, String url) {
		super(config, url);
	}

	@Override
	protected void initializeRequestsNoLock() {
		// TODO: Add requests
	}
}

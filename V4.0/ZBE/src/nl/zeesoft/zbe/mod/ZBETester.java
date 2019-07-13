package nl.zeesoft.zbe.mod;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.TesterObject;
import nl.zeesoft.zodb.mod.TesterRequest;

public class ZBETester extends TesterObject {
	public ZBETester(Config config, String url) {
		super(config, url);
	}

	@Override
	protected void initializeRequestsNoLock() {
	}
	
	@Override
	protected void addLogLineForRequest(TesterRequest request) {
	}
}

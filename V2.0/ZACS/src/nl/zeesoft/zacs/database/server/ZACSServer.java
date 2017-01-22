package nl.zeesoft.zacs.database.server;

import nl.zeesoft.zacs.simulator.SimStateHistoryWorker;
import nl.zeesoft.zodb.database.server.SvrHTTPResourceFactory;
import nl.zeesoft.zodb.database.server.SvrServer;

public class ZACSServer extends SvrServer {
	@Override
	public boolean install() {
		boolean r = super.install();
		if (r) {
			SimStateHistoryWorker.getInstance().generateInitialImages();
		}
		return r;
	}
	@Override
	public SvrHTTPResourceFactory getNewHTTPResourceFactory() {
		return new ZACSHTTPResourceFactory();
	}
}

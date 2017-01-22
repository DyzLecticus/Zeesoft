package nl.zeesoft.zdsm.database.server;

import nl.zeesoft.zodb.database.server.SvrHTTPResourceFactory;
import nl.zeesoft.zodb.database.server.SvrServer;

public class ZDSMServer extends SvrServer {
	@Override
	public SvrHTTPResourceFactory getNewHTTPResourceFactory() {
		return new ZDSMHTTPResourceFactory();
	}
}

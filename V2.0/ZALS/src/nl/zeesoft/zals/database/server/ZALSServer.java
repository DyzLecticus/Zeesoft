package nl.zeesoft.zals.database.server;

import nl.zeesoft.zodb.database.server.SvrHTTPResourceFactory;
import nl.zeesoft.zodb.database.server.SvrServer;

public class ZALSServer extends SvrServer {
	@Override
	public SvrHTTPResourceFactory getNewHTTPResourceFactory() {
		return new ZALSHTTPResourceFactory();
	}
}

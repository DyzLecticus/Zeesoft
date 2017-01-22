package nl.zeesoft.zac.database.server;

import nl.zeesoft.zodb.database.server.SvrHTTPResourceFactory;
import nl.zeesoft.zodb.database.server.SvrServer;

public class ZACServer extends SvrServer {
	@Override
	public SvrHTTPResourceFactory getNewHTTPResourceFactory() {
		return new ZACHTTPResourceFactory();
	}
}

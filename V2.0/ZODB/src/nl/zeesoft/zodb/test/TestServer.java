package nl.zeesoft.zodb.test;

import nl.zeesoft.zodb.database.server.SvrHTTPResourceFactory;
import nl.zeesoft.zodb.database.server.SvrServer;

public class TestServer extends SvrServer {
	@Override
	public SvrHTTPResourceFactory getNewHTTPResourceFactory() {
		return new TestHTTPResourceFactory();
	}
}

package nl.zeesoft.zodd.demo;

import nl.zeesoft.zodb.database.server.SvrHTTPResourceFactory;
import nl.zeesoft.zodb.database.server.SvrServer;

public class DemoServer extends SvrServer {
	@Override
	public SvrHTTPResourceFactory getNewHTTPResourceFactory() {
		return new DemoHTTPResourceFactory();
	}
}

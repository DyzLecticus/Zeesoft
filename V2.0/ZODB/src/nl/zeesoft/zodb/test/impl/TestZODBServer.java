package nl.zeesoft.zodb.test.impl;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.server.SvrConfig;
import nl.zeesoft.zodb.test.TestConfig;

public class TestZODBServer {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestConfig.getInstance();

		TestConfig.startDatabase();
		
		if (!TestConfig.openServer()) {
			System.err.println("!!! Failed to start server");
		} else {
			String url = "http://localhost:" + SvrConfig.getInstance().getPort();
			if (!Generic.startBrowserAtUrl(url)) {
				System.out.println("Start a browser and navigate to: " + url);
			}
			TestConfig.sleep(6000000);
		}
		
		DbIndex.getInstance().debug(TestConfig.getInstance());

		TestConfig.stopDatabase();
	}
	
}

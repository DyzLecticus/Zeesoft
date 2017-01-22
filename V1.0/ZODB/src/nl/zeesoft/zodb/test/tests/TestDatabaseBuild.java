package nl.zeesoft.zodb.test.tests;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.client.ClFactory;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbFactory;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.test.model.TestConfig;

public class TestDatabaseBuild {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DbConfig c = DbConfig.getInstance();
		c.setDebug(true);
		c.setInstallDir(new TestConfig().getTestDir());
		c.setEncryptionKey(Generic.generateNewKey(128));
		c.setModelClassName(TestConfig.class.getName());
		
		DbFactory dbfac = new DbFactory();
		dbfac.buildDatabase(dbfac);
		
		ClFactory clFac = new ClFactory();
		clFac.buildClient();
		
		System.out.println("Index size: " + DbIndex.getInstance().getSize());
	}

}

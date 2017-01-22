package nl.zeesoft.zodb.test.tests;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbFactory;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.test.model.TestConfig;

public class DefaultDatabaseBuild {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DbConfig c = DbConfig.getInstance();
		c.setDebug(true);
		c.setInstallDir(new TestConfig().getTestDir());
		c.setEncryptionKey(Generic.generateNewKey(128));
		
		DbFactory fac = new DbFactory();
		fac.buildDatabase(fac);

		System.out.println("Index size: " + DbIndex.getInstance().getSize());
	}

}

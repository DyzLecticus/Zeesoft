package nl.zeesoft.zodb.test.tests;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbControlServerWorker;
import nl.zeesoft.zodb.database.DbFactory;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.DbSessionServerWorker;
import nl.zeesoft.zodb.test.model.TestConfig;

public class TestDatabaseSessionServerWorker {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DbConfig c = DbConfig.getInstance();
		c.setInstallDir(new TestConfig().getTestDir());
		
		DbFactory fac = new DbFactory();
		fac.loadDatabase(fac);
		
		System.out.println("Index size: " + DbIndex.getInstance().getSize());

		DbControlServerWorker.getInstance().start();
		DbSessionServerWorker.getInstance().start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		DbSessionServerWorker.getInstance().stop();
		DbControlServerWorker.getInstance().stop();
	}

}

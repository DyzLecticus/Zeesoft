package nl.zeesoft.zodb.test.tests;

import nl.zeesoft.zodb.batch.BtcBatchWorker;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbFactory;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.DbIndexSaveWorker;
import nl.zeesoft.zodb.test.model.TestConfig;

public class TestDatabaseBatchWorker {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DbConfig c = DbConfig.getInstance();
		c.setDebug(true);
		c.setInstallDir(new TestConfig().getTestDir());
		c.setModelClassName(TestConfig.class.getName());
		
		DbFactory fac = new DbFactory();
		fac.buildDatabase(fac);
		
		System.out.println("Index size: " + DbIndex.getInstance().getSize());

		DbIndexSaveWorker.getInstance().start();

		BtcBatchWorker.getInstance().start();

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		BtcBatchWorker.getInstance().stop();

		DbIndexSaveWorker.getInstance().stop();
	}

}

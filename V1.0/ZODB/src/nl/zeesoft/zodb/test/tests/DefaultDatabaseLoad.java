package nl.zeesoft.zodb.test.tests;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbFactory;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.file.XMLFile;
import nl.zeesoft.zodb.test.model.TestConfig;

public class DefaultDatabaseLoad {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dir = new TestConfig().getTestDir();
		
		DbConfig c = DbConfig.getInstance();
		c.setInstallDir(dir);
		
		DbFactory fac = new DbFactory();
		fac.loadDatabase(fac);
		
		XMLFile f = DbConfig.getInstance().toXml();
		System.out.println("Configuration:");
		System.out.println(f.toStringReadFormat());

		System.out.println("Index size: " + DbIndex.getInstance().getSize());
	}

}

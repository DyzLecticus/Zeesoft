package nl.zeesoft.zodb.test.tests;

import nl.zeesoft.zodb.client.ClConfig;
import nl.zeesoft.zodb.client.ClFactory;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.test.model.TestConfig;

public class TestClientLoad {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DbConfig c = DbConfig.getInstance();
		c.setInstallDir(new TestConfig().getTestDir());
		
		ClFactory fac = new ClFactory();
		fac.loadClient();
		
		System.out.println("Client config:");
		System.out.println(ClConfig.getInstance().toXml().toStringReadFormat());
		System.out.println("Database config:");
		System.out.println(DbConfig.getInstance().toXml().toStringReadFormat());
		
		System.out.println("Decoded password: " + DbConfig.getInstance().decodePassword(new StringBuffer(ClConfig.getInstance().getUserPassword())));
	}

}

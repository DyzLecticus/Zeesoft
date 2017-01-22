package nl.zeesoft.zodb.test.tests;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.test.model.TestConfig;

public class TestModel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DbConfig c = DbConfig.getInstance();
		c.setDebug(true);
		c.setModelClassName(TestConfig.class.getName());
		
		TestConfig.showModel();
	}

}

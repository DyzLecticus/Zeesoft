package nl.zeesoft.zodb.test.impl;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.test.TestConfig;
import nl.zeesoft.zodb.test.TestModel;

public class TestZODB {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestConfig.getInstance();

		TestConfig.startDatabase();
		
		boolean ok = DbConfig.getInstance().getModelClassName().equals(TestModel.class.getName()); 
		if (!ok) {
			System.err.println("!!! " + DbConfig.getInstance().getModelClassName() + "!=" + TestModel.class.getName());
		}
		
		if (ok) {
			ok = DbConfig.getInstance().getModel().getPackages().size() == 2; 
			if (!ok) {
				System.err.println("!!! packages " + DbConfig.getInstance().getModel().getPackages().size() + "!=" + 2);
			}
		}

		if (ok) {
			ok = DbConfig.getInstance().getModel().getPackages().get(0).getClasses().size() == 7; 
			if (!ok) {
				System.err.println("!!! classes " + DbConfig.getInstance().getModel().getPackages().get(0).getClasses().size() + "!=7");
			}
		}
		
		if (ok) {
			ok = DbConfig.getInstance().getModel().getPackages().get(1).getClasses().size() == 2; 
			if (!ok) {
				System.err.println("!!! classes " + DbConfig.getInstance().getModel().getPackages().get(1).getClasses().size() + "!=2");
			}
		}
		
		TestConfig.sleep(1000);

		DbIndex.getInstance().debug(TestConfig.getInstance());

		TestConfig.stopDatabase();
	}
	
}

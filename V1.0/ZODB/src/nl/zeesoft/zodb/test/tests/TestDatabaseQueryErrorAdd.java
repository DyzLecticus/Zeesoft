package nl.zeesoft.zodb.test.tests;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbFactory;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryObjectList;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.model.impl.DbUser;
import nl.zeesoft.zodb.test.model.TestConfig;
import nl.zeesoft.zodb.test.model.impl.TestA;

public class TestDatabaseQueryErrorAdd {

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

		DbUser admin = DbConfig.getInstance().getModel().getAdminUser(fac);
		
		TestA testA = new TestA();
		testA.getName().setValue("TestA:19");
		testA.getTestString().setValue("Test:11");
		testA.getTestLong().setValue(new Long(7));
		
		QryAdd qa = new QryAdd(testA);

		QryTransaction t = new QryTransaction(admin);
		t.addQuery(qa);
		DbIndex.getInstance().executeTransaction(t, fac);
		System.out.println("Executed add query");
		System.out.println(QryObjectList.toXml(t).toStringReadFormat());
	}

}

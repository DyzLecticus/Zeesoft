package nl.zeesoft.zodb.test.tests;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbFactory;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.DbIndexSaveWorker;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.database.query.QryRemove;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.MdlObject;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.impl.DbUser;
import nl.zeesoft.zodb.test.model.TestConfig;
import nl.zeesoft.zodb.test.model.impl.TestA;

public class TestDatabaseIndexWorker {

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
		
		DbUser admin = DbConfig.getInstance().getModel().getAdminUser(fac);
		
		QryFetch q = null;

		QryTransaction t = new QryTransaction(admin);

		q = new QryFetch(new TestA().getClassName().getValue());
		DtString name = new DtString();
		name.setValue("TestA:10");
		q.addCondition(new QryFetchCondition(MdlObject.PROPERTY_NAME,QryFetchCondition.OPERATOR_EQUALS,name));
		DbIndex.getInstance().executeFetch(q, DbConfig.getInstance().getModel().getAdminUser(fac), fac);
		if (q.getMainResults().getReferences().size()>0) {
			TestA testA = (TestA) q.getMainResults().getReferences().get(0).getDataObject(); 
			
			testA.getTestBoolean().setValue(false);
			testA.getTestFloat().setValue(new Float(1.922));
			testA.getTestLong().setValue(new Long(1231123));
			testA.getTestString().setValue("Updated value!!!");
			
			t.addQuery(new QryUpdate(testA));
		}

		q = new QryFetch(new TestA().getClassName().getValue());
		name.setValue("TestA:15");
		q.addCondition(new QryFetchCondition(MdlObject.PROPERTY_NAME,QryFetchCondition.OPERATOR_EQUALS,name));
		t.addQuery(new QryRemove(q));
		
		DbIndex.getInstance().executeTransaction(t, fac);
		System.out.println("Executed transaction");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		DbIndexSaveWorker.getInstance().stop();
	}

}

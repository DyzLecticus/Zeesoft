package nl.zeesoft.zodb.test.tests;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbFactory;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.database.query.QryObjectList;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.file.XMLFile;
import nl.zeesoft.zodb.model.MdlObject;
import nl.zeesoft.zodb.model.datatypes.DtLong;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.impl.DbUser;
import nl.zeesoft.zodb.test.model.TestConfig;
import nl.zeesoft.zodb.test.model.impl.TestA;
import nl.zeesoft.zodb.test.model.impl.TestC;

public class TestDatabaseQueryUpdate {

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
		
		QryFetch q = null;

		q = new QryFetch(new TestA().getClassName().getValue());
		DtString name = new DtString();
		name.setValue("TestA:10");
		q.addCondition(new QryFetchCondition(MdlObject.PROPERTY_NAME,QryFetchCondition.OPERATOR_EQUALS,name));
		DbIndex.getInstance().executeFetch(q, admin, fac);
		if (q.getMainResults().getReferences().size()>0) {
			TestA testA = (TestA) q.getMainResults().getReferences().get(0).getDataObject(); 
			
			q = new QryFetch(new TestC().getClassName().getValue());
			name.setValue("TestC:1 for TestA:4");
			q.addCondition(new QryFetchCondition(MdlObject.PROPERTY_NAME,QryFetchCondition.OPERATOR_EQUALS,name));
			DbIndex.getInstance().executeFetch(q, admin, fac);

			if (q.getMainResults().getReferences().size()>0) {
				TestC testC = (TestC) q.getMainResults().getReferences().get(0).getDataObject(); 
				
				testC.getName().setValue("TestC:2 for TestA:10");
				testC.getTestReference().setValue(testA.getId().getValue());
				
				QryUpdate qu = new QryUpdate(testC);

				QryTransaction t = new QryTransaction(admin);
				t.addQuery(qu);
				DbIndex.getInstance().executeTransaction(t, fac);
				System.out.println("Executed update query");
				
				XMLFile xml = null;
				xml = QryObjectList.toXml(t);
				t = (QryTransaction) QryObjectList.fromXml(xml);
				System.out.println(QryObjectList.toXml(t).toStringReadFormat());
				
				q = new QryFetch(new TestC().getClassName().getValue());
				DtLong id = new DtLong();
				id.setValue(testA.getId().getValue());
				q.addCondition(new QryFetchCondition("testReference",QryFetchCondition.OPERATOR_CONTAINS,id));
				DbIndex.getInstance().executeFetch(q, admin, fac);
				System.out.println("Executed fetch query");
				System.out.println("Results:" + q.getResults());
			}
		}
	}

}

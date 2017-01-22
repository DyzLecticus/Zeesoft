package nl.zeesoft.zodb.test.tests;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbFactory;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.database.query.QryObjectList;
import nl.zeesoft.zodb.database.query.QryRemove;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.file.XMLFile;
import nl.zeesoft.zodb.model.MdlObject;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.impl.DbUser;
import nl.zeesoft.zodb.test.model.TestConfig;
import nl.zeesoft.zodb.test.model.impl.TestA;

public class TestDatabaseQueryRemove {

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
			
		QryRemove qr = new QryRemove(q);
		QryTransaction t = new QryTransaction(admin);
		t.addQuery(qr);
		DbIndex.getInstance().executeTransaction(t, fac);
		System.out.println("Executed remove query");
		
		XMLFile xml = null;
		xml = QryObjectList.toXml(t);
		t = (QryTransaction) QryObjectList.fromXml(xml);
		System.out.println(QryObjectList.toXml(t).toStringReadFormat());

		DbIndex.getInstance().executeFetch(q, admin, fac);
		System.out.println("Executed fetch query");
		System.out.println("Results:" + q.getResults());
	}

}

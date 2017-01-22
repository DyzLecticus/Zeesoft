package nl.zeesoft.zodb.test.tests;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbFactory;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.file.XMLFile;
import nl.zeesoft.zodb.model.MdlObject;
import nl.zeesoft.zodb.model.datatypes.DtLong;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.test.model.TestConfig;
import nl.zeesoft.zodb.test.model.impl.TestA;
import nl.zeesoft.zodb.test.model.impl.TestC;

public class TestDatabaseQueryFetch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dir = new TestConfig().getTestDir();
		
		DbConfig c = DbConfig.getInstance();
		c.setInstallDir(dir);
		
		DbFactory fac = new DbFactory();
		fac.loadDatabase(fac);
		
		System.out.println("Index size: " + DbIndex.getInstance().getSize());

		QryFetch q = null;
		XMLFile xml = null;

		q = new QryFetch(new TestA().getClassName().getValue());
		q.setType(QryFetch.TYPE_FETCH_REFERENCES);
		DbIndex.getInstance().executeFetch(q, DbConfig.getInstance().getModel().getAdminUser(fac), fac);
		System.out.println("Executed fetch query");
		System.out.println("Results:" + q.getResults());

		xml = QryObject.toXml(q);
		q = (QryFetch) QryObject.fromXml(xml);
		System.out.println(QryObject.toXml(q).toStringReadFormat());
		
		q = new QryFetch(new TestA().getClassName().getValue());
		q.setOrderBy("testFloat", true);
		q.setStartLimit(5, 3);
		DbIndex.getInstance().executeFetch(q, DbConfig.getInstance().getModel().getAdminUser(fac), fac);
		System.out.println("Executed fetch query");
		System.out.println("Results:" + q.getResults());

		xml = QryObject.toXml(q);
		q = (QryFetch) QryObject.fromXml(xml);
		System.out.println(QryObject.toXml(q).toStringReadFormat());
		
		q = new QryFetch(new TestA().getClassName().getValue());
		DtString name = new DtString();
		name.setValue("TestA:10");
		q.addCondition(new QryFetchCondition(MdlObject.PROPERTY_NAME,QryFetchCondition.OPERATOR_EQUALS,name));
		DbIndex.getInstance().executeFetch(q, DbConfig.getInstance().getModel().getAdminUser(fac), fac);
		System.out.println("Executed fetch query");
		System.out.println("Results:" + q.getResults());

		xml = QryObject.toXml(q);
		q = (QryFetch) QryObject.fromXml(xml);
		System.out.println(QryObject.toXml(q).toStringReadFormat());

		q = new QryFetch(new TestC().getClassName().getValue());
		DtLong id = new DtLong();
		id.setValue(new Long(15));
		q.addCondition(new QryFetchCondition("testReference",QryFetchCondition.OPERATOR_CONTAINS,id));
		DbIndex.getInstance().executeFetch(q, DbConfig.getInstance().getModel().getAdminUser(fac), fac);
		System.out.println("Executed fetch query");
		System.out.println("Results:" + q.getResults());

		q = new QryFetch(new TestA().getClassName().getValue());
		DtString str = new DtString();
		str.setValue("Test:10");
		DtLong lng = new DtLong();
		lng.setValue(new Long(7));
		q.addCondition(new QryFetchCondition("testString",QryFetchCondition.OPERATOR_EQUALS,str));
		q.addCondition(new QryFetchCondition("testLong",QryFetchCondition.OPERATOR_EQUALS,lng));
		DbIndex.getInstance().executeFetch(q, DbConfig.getInstance().getModel().getAdminUser(fac), fac);
		System.out.println("Executed fetch query");
		System.out.println("Results:" + q.getResults());
	}

}

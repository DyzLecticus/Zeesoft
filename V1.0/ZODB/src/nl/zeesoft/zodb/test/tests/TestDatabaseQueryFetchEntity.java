package nl.zeesoft.zodb.test.tests;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbFactory;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.file.XMLFile;
import nl.zeesoft.zodb.test.model.TestConfig;
import nl.zeesoft.zodb.test.model.impl.TestA;
import nl.zeesoft.zodb.test.model.impl.TestB;
import nl.zeesoft.zodb.test.model.impl.TestC;

public class TestDatabaseQueryFetchEntity {

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

		q = new QryFetch(TestA.class.getName());
		q.setType(QryFetch.TYPE_FETCH_OBJECTS_ENTITY);
		q.getEntities().add(TestB.class.getName());
		q.getEntities().add(TestC.class.getName());
		DbIndex.getInstance().executeFetch(q, DbConfig.getInstance().getModel().getAdminUser(fac), fac);
		System.out.println("Executed fetch query");
		System.out.println("Results:" + q.getResults());

		xml = QryObject.toXml(q);
		q = (QryFetch) QryObject.fromXml(xml);
		System.out.println(QryObject.toXml(q).toStringReadFormat());
		
	}

}

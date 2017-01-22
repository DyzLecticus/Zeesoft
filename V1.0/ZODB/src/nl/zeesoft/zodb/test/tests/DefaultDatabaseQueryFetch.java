package nl.zeesoft.zodb.test.tests;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbFactory;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.model.MdlObject;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtLong;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.impl.DbUser;
import nl.zeesoft.zodb.test.model.TestConfig;

public class DefaultDatabaseQueryFetch {

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
		
		q = new QryFetch(new DbUser().getClassName().getValue());
		DtLong id = new DtLong();
		id.setValue(new Long(1));
		q.addCondition(new QryFetchCondition(MdlObject.PROPERTY_ID,QryFetchCondition.OPERATOR_EQUALS,id));
		DbIndex.getInstance().executeFetch(q, DbConfig.getInstance().getModel().getAdminUser(fac), fac);
		System.out.println("Executed fetch query");
		System.out.println("Results:" + q.getResults());

		q = new QryFetch(new DbUser().getClassName().getValue());
		DtString name = new DtString();
		name.setValue("admin");
		q.addCondition(new QryFetchCondition(MdlObject.PROPERTY_NAME,QryFetchCondition.OPERATOR_EQUALS,name));
		DbIndex.getInstance().executeFetch(q, DbConfig.getInstance().getModel().getAdminUser(fac), fac);
		System.out.println("Executed fetch query");
		System.out.println("Results:" + q.getResults());
		
		q = new QryFetch(new DbUser().getClassName().getValue());
		DtBoolean admin = new DtBoolean();
		admin.setValue(false);
		q.addCondition(new QryFetchCondition("admin",QryFetchCondition.OPERATOR_EQUALS,admin));
		DbIndex.getInstance().executeFetch(q, DbConfig.getInstance().getModel().getAdminUser(fac), fac);
		System.out.println("Executed fetch query");
		System.out.println("Results:" + q.getResults());

		q = new QryFetch(new DbUser().getClassName().getValue());
		q.addCondition(new QryFetchCondition(MdlObject.PROPERTY_ID,QryFetchCondition.OPERATOR_GREATER_OR_EQUALS,id));
		q.setOrderBy("name", false);
		q.setStartLimit(1, 1);
		DbIndex.getInstance().executeFetch(q, DbConfig.getInstance().getModel().getAdminUser(fac), fac);
		System.out.println("Executed fetch query");
		System.out.println("Results:" + q.getResults());

		q = new QryFetch(new DbUser().getClassName().getValue());
		q.setType(QryFetch.TYPE_FETCH_REFERENCES);
		DbIndex.getInstance().executeFetch(q, DbConfig.getInstance().getModel().getAdminUser(fac), fac);
		System.out.println("Executed fetch query");
		System.out.println("Results:" + q.getResults());

	}

}

package nl.zeesoft.zodb.database.query;

import nl.zeesoft.zodb.model.impl.DbUser;


public class QryFetchList extends QryObjectList {
	public QryFetchList(DbUser usr) {
		super(usr);
	}
	public QryFetchList(DbUser usr,QryObject qry) {
		super(usr);
		addQuery(qry);
	}
	@Override
	public void addQuery(QryObject qry) {
		if (qry instanceof QryFetch) {
			super.addQuery(qry);
		}
	}
}

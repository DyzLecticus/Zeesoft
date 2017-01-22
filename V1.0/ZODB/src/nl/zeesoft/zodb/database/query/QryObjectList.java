package nl.zeesoft.zodb.database.query;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.file.XMLElem;
import nl.zeesoft.zodb.file.XMLFile;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.impl.DbUser;

public abstract class QryObjectList {
	private DbUser 			user	= null;
	private List<QryObject>	queries	= new ArrayList<QryObject>(); 

	public QryObjectList(DbUser usr) {
		user = usr;
	}
	
	public void addQuery(QryObject qry) {
		queries.add(qry);
	}

	public static XMLFile toXml(QryObjectList qryList) {
		XMLFile f = new XMLFile();
		if (qryList instanceof QryTransaction) {
			f.setRootElement(new XMLElem("transaction",null,null));
			QryTransaction transaction = (QryTransaction) qryList;
			if (transaction.getQueries().size()>0) {
				XMLElem queriesElem = new XMLElem("queries",null,f.getRootElement());
				for (QryObject qry: transaction.getQueries()) {
					XMLFile qXml = QryObject.toXml(qry);
					qXml.getRootElement().setParent(queriesElem);
				}
				if (transaction.getAddedIdList().size()>0) {
					XMLElem addedElem = new XMLElem("added",null,f.getRootElement());
					for (long id: transaction.getAddedIdList()) {
						StringBuffer sb = new StringBuffer();
						sb.append(id);
						new XMLElem("id",sb,addedElem);
					}
				}
				if (transaction.getUpdatedIdList().size()>0) {
					XMLElem updatedElem = new XMLElem("updated",null,f.getRootElement());
					for (long id: transaction.getUpdatedIdList()) {
						StringBuffer sb = new StringBuffer();
						sb.append(id);
						new XMLElem("id",sb,updatedElem);
					}
				}
				if (transaction.getRemovedIdList().size()>0) {
					XMLElem removedElem = new XMLElem("removed",null,f.getRootElement());
					for (long id: transaction.getRemovedIdList()) {
						StringBuffer sb = new StringBuffer();
						sb.append(id);
						new XMLElem("id",sb,removedElem);
					}
				}
			}
		} else if (qryList instanceof QryFetchList) {
			f.setRootElement(new XMLElem("fetches",null,null));
			QryFetchList fetchList = (QryFetchList) qryList;
			if (fetchList.getQueries().size()>0) {
				XMLElem queriesElem = new XMLElem("queries",null,f.getRootElement());
				for (QryObject qry: fetchList.getQueries()) {
					XMLFile qXml = QryObject.toXml(qry);
					qXml.getRootElement().setParent(queriesElem);
				}
			}
		}
		return f;
	}

	public static QryObjectList fromXml(XMLFile f) {
		QryObjectList r = null;
		if (f.getRootElement().getName().equals("transaction")) {
			QryTransaction t = new QryTransaction(null);
			for (XMLElem tElem: f.getRootElement().getChildren()) {
				if (tElem.getName().equals("queries")) {
					for (XMLElem qElem: tElem.getChildren()) {
						XMLFile qXml = new XMLFile();
						qXml.setRootElement(qElem);
						QryObject qry = QryObject.fromXml(qXml);
						t.addQuery(qry);
					}
				} else if (tElem.getName().equals("added")) {
					for (XMLElem idElem: tElem.getChildren()) {
						t.addAddedId(Long.parseLong(idElem.getValue().toString()));
					}
				} else if (tElem.getName().equals("updated")) {
					for (XMLElem idElem: tElem.getChildren()) {
						t.addUpdatedId(Long.parseLong(idElem.getValue().toString()));
					}
				} else if (tElem.getName().equals("removed")) {
					for (XMLElem idElem: tElem.getChildren()) {
						t.addRemovedId(Long.parseLong(idElem.getValue().toString()));
					}
				}
			}
			r = t;
		} else if (f.getRootElement().getName().equals("fetches")) {
			XMLElem queriesElem = f.getRootElement().getChildByName("queries");
			if (queriesElem!=null) {
				r = new QryFetchList(null);
				for (XMLElem qElem: queriesElem.getChildren()) {
					XMLFile qXml = new XMLFile();
					qXml.setRootElement(qElem);
					QryObject qry = QryObject.fromXml(qXml);
					r.addQuery(qry);
				}
			}
		}
		return r;
	}
	
	public static boolean checkFetchList(QryFetchList fetchList, DbUser user) {
		boolean ok = true;
		for (QryObject qry: fetchList.getQueries()) {
			if (!QryObject.checkFetch((QryFetch) qry,user)) {
				ok = false;
				break;
			}
		}
		return ok;
	}
		
	public static boolean checkTransaction(QryTransaction transaction) {
		boolean ok = true;
		if (transaction.getQueries().size()==0) {
			ok = false;
		} else {
			for (QryObject query: transaction.getQueries()) {
				if (query instanceof QryUpdate) {
					QryUpdate qry = (QryUpdate) query;
					if (qry.getFetch()==null) {
						qry.addError("0101","Missing fetch for update");
						ok = false;
						break;
					} else if (!QryObject.checkFetch(qry.getFetch(),transaction.getUser())) {
						qry.addError("0102","Fetch query for update is invalid");
						ok = false;
						break;
					} else if (qry.getDataObject()==null) {
						qry.addError("0103","Missing model object for update");
						ok = false;
						break;
					} else {
						MdlDataObject obj = qry.getDataObject();
						ok = QryObject.checkQueryCollectionAccess(qry,transaction.getUser(),obj.getClassName().getValue());
						if (!ok) {
							break;
						}
					}
				} else if (query instanceof QryAdd) {
					QryAdd qry = (QryAdd) query;
					if (qry.getDataObject()==null) {
						qry.addError("0104","Missing model object for add");
						ok = false;
						break;
					} else {
						MdlDataObject obj = qry.getDataObject();
						ok = QryObject.checkQueryCollectionAccess(qry,transaction.getUser(),obj.getClassName().getValue());
						if (!ok) {
							break;
						}
					}					
				} else if (query instanceof QryRemove) {
					QryRemove qry = (QryRemove) query;
					if (qry.getFetch()==null) {
						qry.addError("0105","Missing fetch for remove");
						ok = false;
						break;
					} else if (!QryObject.checkFetch(qry.getFetch(),transaction.getUser())) {
						qry.addError("0106","Fetch query for remove is invalid");
						ok = false;
						break;
					} else {
						ok = QryObject.checkQueryCollectionAccess(qry,transaction.getUser(),qry.getFetch().getClassName());
						if (!ok) {
							break;
						}
					}
				}
			}
		}
		return ok;
	}

	/**
	 * @return the user
	 */
	public DbUser getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(DbUser user) {
		this.user = user;
	}

	/**
	 * @return the queries
	 */
	public List<QryObject> getQueries() {
		return new ArrayList<QryObject>(queries);
	}
}

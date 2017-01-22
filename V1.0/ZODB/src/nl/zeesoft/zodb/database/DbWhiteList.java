package nl.zeesoft.zodb.database;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.impl.DbSession;
import nl.zeesoft.zodb.model.impl.DbWhiteListItem;

public final class DbWhiteList {
	private static DbWhiteList list = null;
	
	private List<DbWhiteListItem>	whiteList		= new ArrayList<DbWhiteListItem>();
	
	private DbWhiteList() {
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	public static DbWhiteList getInstance() {
		if (list==null) {
			list = new DbWhiteList();
			list.updateWhiteListItems();
		}
		return list;
	}
	
	public void updateWhiteListAndKillSessions() {
		if (updateWhiteListItems()) {

			List<DbWhiteListItem> controlList = new ArrayList<DbWhiteListItem>();
			for (DbWhiteListItem item: getWhiteList()) {
				if (item.getControl().getValue()) {
					controlList.add(item);
				}
			}
			DbControlServer.getInstance().setNewWhiteList(controlList);
			DbSessionServer.getInstance().setNewWhiteList(new ArrayList<DbWhiteListItem>(whiteList));

			QryFetch fetch = new QryFetch(DbSession.class.getName());
			fetch.addCondition(new QryFetchCondition("ended",QryFetchCondition.OPERATOR_EQUALS,new DtDateTime()));
			DbIndex.getInstance().executeFetch(fetch, DbConfig.getInstance().getModel().getAdminUser(this), this);
			if (fetch.getMainResults().getReferences().size()>0) {
				for (MdlObjectRef ref: fetch.getMainResults().getReferences()) {
					DbSession session = (DbSession) ref.getDataObject();
					boolean ok = (getWhiteList().size() == 0);
					for (DbWhiteListItem item: getWhiteList()) {
						if (session.getIpAndPort().getValue().startsWith(item.getStartsWith().getValue())) {
							ok = true;
							break;
						}
					}
					if (!ok) {
						if (
							(DbControlServer.getInstance().stopSession(session.getId().getValue())) ||
							(DbSessionServer.getInstance().stopSession(session.getId().getValue()))
							) {
							Messenger.getInstance().debug(this, "Killed session: " + session.getId().getValue());
						} else {
							Messenger.getInstance().error(this, "Failed to kill session: " + session.getId().getValue());
						}
					}
				}
			}
		}
	}
	
	private boolean updateWhiteListItems() {
		boolean changed = false;
		List<DbWhiteListItem> newList= new ArrayList<DbWhiteListItem>();
		QryFetch fetch = new QryFetch(DbWhiteListItem.class.getName());
		fetch.addCondition(new QryFetchCondition("active",QryFetchCondition.OPERATOR_EQUALS,new DtBoolean(true)));
		DbIndex.getInstance().executeFetch(fetch, DbConfig.getInstance().getModel().getAdminUser(this), this);
		if (fetch.getMainResults().getReferences().size()>0) {
			for (MdlObjectRef ref: fetch.getMainResults().getReferences()) {
				DbWhiteListItem item = (DbWhiteListItem) ref.getDataObject();
				newList.add(item);
			}
		}
		if (!stringWhiteList(whiteList).equals(stringWhiteList(newList))) {
			whiteList = newList;
			changed = true;
		}
		return changed;
	}
	
	private static String stringWhiteList(List<DbWhiteListItem> whiteList) {
		StringBuffer str = new StringBuffer();
		for (DbWhiteListItem item: whiteList) {
			str.append(item.getStartsWith().getValue());
			str.append(Generic.SEP_STR);
			str.append(item.getControl().getValue());
			str.append(Generic.SEP_OBJ);
		}
		return str.toString();
	}

	/**
	 * @return the whiteList
	 */
	protected List<DbWhiteListItem> getWhiteList() {
		return whiteList;
	}
}	

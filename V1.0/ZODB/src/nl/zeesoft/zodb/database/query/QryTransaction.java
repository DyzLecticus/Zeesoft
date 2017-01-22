package nl.zeesoft.zodb.database.query;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.model.impl.DbUser;

public class QryTransaction extends QryObjectList {
	private List<Long>					addedIdList 	= new ArrayList<Long>();
	private List<Long>					updatedIdList 	= new ArrayList<Long>();
	private List<Long>					removedIdList 	= new ArrayList<Long>();
	
	public QryTransaction(DbUser usr) {
		super(usr);
	}

	@Override
	public void addQuery(QryObject qry) {
		if (!(qry instanceof QryFetch)) {
			super.addQuery(qry);
		}
	}

	public void addAddedId(long id) {
		addedIdList.add(id);
	}

	public void addUpdatedId(long id) {
		updatedIdList.add(id);
	}

	public void addRemovedId(long id) {
		removedIdList.add(id);
	}
	
	/**
	 * @return the addedIdList
	 */
	public List<Long> getAddedIdList() {
		return new ArrayList<Long>(addedIdList);
	}

	/**
	 * @return the updatedIdList
	 */
	public List<Long> getUpdatedIdList() {
		return new ArrayList<Long>(updatedIdList);
	}

	/**
	 * @return the removedIdList
	 */
	public List<Long> getRemovedIdList() {
		return new ArrayList<Long>(removedIdList);
	}
	
	public void debugErrors() {
		if (DbConfig.getInstance().isDebug()) {
			for (QryObject qry: getQueries()) {
				for (QryError err: qry.getErrors()) {
					String props = "";
					for (String prop: err.getProperties()) {
						if (props.length()>0) {
							props = props + ", ";
						}
						props = props + prop;
					}
					Messenger.getInstance().debug(this, "Error: " + err.toString() + ", properties: " + props);
				}
			}
		}
	}
}

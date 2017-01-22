package nl.zeesoft.zadf.format;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zadf.model.impl.DbFetch;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventPublisher;
import nl.zeesoft.zodb.model.MdlObjectRefListMap;

/**
 * This class is designed to be extended.
 */
public abstract class FmtObject extends EvtEventPublisher {
	public static final String								FORMATTED				= "FORMATTED";
	
	private List<DbFetch>									fetches 				= null;
	private List<DbCollection> 								collections 			= null;
	private SortedMap<String,List<DbCollectionProperty>> 	collectionProperties 	= null;
	private MdlObjectRefListMap 							results 				= null;
	private int												done					= 0;

	public void setData(List<DbFetch> fetches, List<DbCollection> collections, SortedMap<String,List<DbCollectionProperty>> collectionProperties, MdlObjectRefListMap results) {
		this.fetches = fetches;
		this.collections = collections;
		this.collectionProperties = collectionProperties;
		this.results = results;
		removeExcludedProperties();
		done = 0;
	}
	
	/**
	 * Override to implement
	 */
	public abstract StringBuffer format();

	/**
	 *  Signal to listeners that some work has been done
	 */
	protected void formatted(int done) {
		publishEvent(new EvtEvent(FORMATTED,this,done));
	}
	
	/**
	 *  Signal to listeners that some work has been done
	 */
	protected void formatted() {
		done++;
		formatted(done);
	}

	protected boolean getIncludeCollectionProperty(DbCollection dbCol, DbCollectionProperty dbProp) {
		boolean add = true;
		if (dbProp.getName().getValue().equals("className")) {
			add = false;
		}
		return add;
	}

	protected List<DbCollectionProperty> getReportCollectionProperties(DbCollection dbCol, List<DbCollectionProperty> dbProps) {
		List<DbCollectionProperty> lstCopy = new ArrayList<DbCollectionProperty>(dbProps);
		for (DbCollectionProperty dbProp: lstCopy) {
			if (!getIncludeCollectionProperty(dbCol,dbProp)) {
				dbProps.remove(dbProp);
			}
		}
		return dbProps;
	}
	
	private void removeExcludedProperties() { 		
		for (DbCollection dbCol: collections) {
			List<DbCollectionProperty> colProps = collectionProperties.get(dbCol.getName().getValue());
			List<DbCollectionProperty> colPropsNew = new ArrayList<DbCollectionProperty>();
			List<DbCollectionProperty> colPropsAdd = getReportCollectionProperties(dbCol,colProps);
			for (DbCollectionProperty dbProp: colPropsAdd) {
				colPropsNew.add(dbProp);
			}
			colProps.clear();
			for (DbCollectionProperty dbProp: colPropsNew) {
				colProps.add(dbProp);
			}
		}
	}

	/**
	 * @return the dbFetch
	 */
	protected List<DbFetch> getFetches() {
		return fetches;
	}

	/**
	 * @return the collections
	 */
	protected List<DbCollection> getCollections() {
		return collections;
	}

	/**
	 * @return the collectionProperties
	 */
	protected SortedMap<String, List<DbCollectionProperty>> getCollectionProperties() {
		return collectionProperties;
	}

	/**
	 * @return the results
	 */
	protected MdlObjectRefListMap getResults() {
		return results;
	}
	
	protected DbCollection getCollectionById(long id) {
		DbCollection obj = null;
		for (DbCollection tst: collections) {
			if (tst.getId().getValue().equals(id)) {
				obj = tst;
				break;
			}
		}
		return obj;
	}

	protected DbCollection getCollectionByName(String name) {
		DbCollection obj = null;
		for (DbCollection tst: collections) {
			if (tst.getName().getValue().equals(name)) {
				obj = tst;
				break;
			}
		}
		return obj;
	}

	protected DbCollectionProperty getCollectionPropertyById(long id) {
		DbCollectionProperty obj = null;
		for (Entry<String, List<DbCollectionProperty>> entry: collectionProperties.entrySet()) {
			for (DbCollectionProperty tst: entry.getValue()) {
				if (tst.getId().getValue().equals(id)) {
					obj = tst;
					break;
				}
			}
		}
		return obj;
	}

	protected DbCollectionProperty getCollectionPropertyByName(String collection, String property) {
		DbCollectionProperty obj = null;
		List<DbCollectionProperty> dbProps = new ArrayList<DbCollectionProperty>(collectionProperties.get(collection));
		if (dbProps!=null) {
			for (DbCollectionProperty tst: dbProps) {
				if (tst.getName().getValue().equals(property)) {
					obj = tst;
					break;
				}
			}
		}
		return obj;
	}

	protected List<DbCollectionProperty> getReferencesByCollectionId(long id, boolean entityOnly) {
		List<DbCollectionProperty> objs = new ArrayList<DbCollectionProperty>();
		for (Entry<String, List<DbCollectionProperty>> entry: collectionProperties.entrySet()) {
			for (DbCollectionProperty tst: entry.getValue()) {
				if ((tst.getEntity().getValue()) &&
					(tst.getReferenceCollection().getValue().equals(id))
					) {
					objs.add(tst);
				}
			}
		}
		return objs;
	}

	/**
	 * @return the done
	 */
	protected int getDone() {
		return done;
	}

	/**
	 * @param done the done to set
	 */
	protected void setDone(int done) {
		this.done = done;
	}
	
}

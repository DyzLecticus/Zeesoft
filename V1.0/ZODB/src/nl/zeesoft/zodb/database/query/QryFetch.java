package nl.zeesoft.zodb.database.query;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.model.MdlObject;
import nl.zeesoft.zodb.model.MdlObjectRefList;
import nl.zeesoft.zodb.model.MdlObjectRefListMap;
import nl.zeesoft.zodb.model.datatypes.DtLong;

public class QryFetch extends QryObject {
	public static final String 		MAIN_RESULTS				= "MAIN_RESULTS";

	public static final String		TYPE_COUNT_REFERENCES		= "TYPE_COUNT_REFERENCES";
	public static final String		TYPE_FETCH_REFERENCES 		= "TYPE_FETCH_REFERENCES";
	public static final String		TYPE_FETCH_OBJECTS 			= "TYPE_FETCH_OBJECTS";
	public static final String		TYPE_FETCH_OBJECTS_EXTEND	= "TYPE_FETCH_OBJECTS_EXTEND";
	public static final String		TYPE_FETCH_OBJECTS_ENTITY	= "TYPE_FETCH_OBJECTS_ENTITY";
	
	private String					type						= TYPE_FETCH_OBJECTS;
	private String 					className 					= "";
	private List<QryFetchCondition> conditions 					= new ArrayList<QryFetchCondition>();
	private String 					orderBy						= "";
	private boolean					orderAscending				= true; 
	private int						start						= 0;
	private int						limit						= 0;
	private boolean					useCache					= true;
	private List<String>			entities					= new ArrayList<String>();

	private int						count						= 0;
	private boolean					resultsIncomplete			= false;
	private MdlObjectRefListMap		results						= new MdlObjectRefListMap();
	private SortedMap<Long,String> 	extendedReferences			= new TreeMap<Long,String>();

	// Used by DbIndex
	private boolean					limitedInitialResults		= false;
	private boolean					orderedInitialResults		= false;

	public QryFetch(String className) {
		this.className = className;
	}
	
	public void initializeResults() {
		setLog(new StringBuffer());
		count = 0;
		limitedInitialResults = false;
		orderedInitialResults = false;
		resultsIncomplete = false;
		results = new MdlObjectRefListMap();
		extendedReferences.clear();
	}

	public QryFetch(String className, long id) {
		this.className = className;
		DtLong v = new DtLong();
		v.setValue(id);
		addCondition(new QryFetchCondition(MdlObject.PROPERTY_ID,QryFetchCondition.OPERATOR_EQUALS,v));
	}

	public QryFetch(MdlObject ref) {
		this.className = ref.getClassName().getValue();
		if (ref.getId().getValue()>0) {
			addCondition(new QryFetchCondition(MdlObject.PROPERTY_ID,QryFetchCondition.OPERATOR_EQUALS,ref.getId()));
		}
	}
	
	public void addCondition(QryFetchCondition c) {
		conditions.add(c);
	}

	public void setOrderBy(String property,boolean ascending) {
		orderBy = property;
		orderAscending = ascending;
	}

	public void setStartLimit(int s, int l) {
		if (s<0) {
			s = 0;
		}
		if (l<0) {
			l = 0;
		}
		start = s;
		limit = l;
	}
	
	public MdlObjectRefList getMainResults() {
		return results.getReferenceListForCollection(MAIN_RESULTS);
	}

	public void setMainResults(MdlObjectRefList mainResults) {
		results.getReferenceListForCollection(MAIN_RESULTS).getReferences().clear();
		results.addReferenceList(MAIN_RESULTS, mainResults);
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return the conditions
	 */
	public List<QryFetchCondition> getConditions() {
		return conditions;
	}

	/**
	 * @return the orderBy
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @return the orderAscending
	 */
	public boolean isOrderAscending() {
		return orderAscending;
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @return the results
	 */
	public MdlObjectRefListMap getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(MdlObjectRefListMap results) {
		this.results = results;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the extendedReferences
	 */
	public SortedMap<Long, String> getExtendedReferences() {
		return extendedReferences;
	}

	/**
	 * @return the entities
	 */
	public List<String> getEntities() {
		return entities;
	}

	/**
	 * @return the useCache
	 */
	public boolean isUseCache() {
		return useCache;
	}

	/**
	 * @param useCache the useCache to set
	 */
	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}

	/**
	 * @return the resultsIncomplete
	 */
	public boolean isResultsIncomplete() {
		return resultsIncomplete;
	}

	/**
	 * @param resultsIncomplete the resultsIncomplete to set
	 */
	public void setResultsIncomplete(boolean resultsIncomplete) {
		this.resultsIncomplete = resultsIncomplete;
	}

	/**
	 * @return the limitedInitialResults
	 */
	public boolean isLimitedInitialResults() {
		return limitedInitialResults;
	}

	/**
	 * @param limitedInitialResults the limitedInitialResults to set
	 */
	public void setLimitedInitialResults(boolean limitedInitialResults) {
		this.limitedInitialResults = limitedInitialResults;
	}

	/**
	 * @return the orderedInitialResults
	 */
	public boolean isOrderedInitialResults() {
		return orderedInitialResults;
	}

	/**
	 * @param orderedInitialResults the orderedInitialResults to set
	 */
	public void setOrderedInitialResults(boolean orderedInitialResults) {
		this.orderedInitialResults = orderedInitialResults;
	}
}

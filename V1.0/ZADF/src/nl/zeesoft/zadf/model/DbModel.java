package nl.zeesoft.zadf.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zadf.controller.GuiController;
import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionFilter;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zadf.model.impl.DbFilterOperator;
import nl.zeesoft.zadf.model.impl.DbModule;
import nl.zeesoft.zadf.model.impl.DbPropertyConstraint;
import nl.zeesoft.zadf.model.impl.DbReferenceFilter;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.client.ClEntityLoader;
import nl.zeesoft.zodb.client.ClRequest;
import nl.zeesoft.zodb.database.query.QryError;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.MdlObjectRefList;
import nl.zeesoft.zodb.model.MdlObjectRefListMap;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtIdRefList;
import nl.zeesoft.zodb.model.datatypes.DtObject;

public class DbModel extends ClEntityLoader {
	public static final String 			LOADED_MODEL					= "LOADED_MODEL";
	
	public static final String			SESSION_PREFIX					= "@SESSION";
	public static final String			SESSION_ID						= SESSION_PREFIX + ".id";
	public static final String			SESSION_USER_ID					= SESSION_PREFIX + ".user.id";
	public static final String			SESSION_USER_NAME				= SESSION_PREFIX + ".user.name";
	public static final String			SESSION_USER_ADMIN				= SESSION_PREFIX + ".user.admin";
	public static final String			SESSION_USER_LEVEL				= SESSION_PREFIX + ".user.level";
	public static final String			SESSION_DESCRIPTION				= 
		SESSION_ID + ": The ID of the current session.\n" + 
		SESSION_USER_ID + ": The ID of the current session user.\n" + 
		SESSION_USER_NAME + ": The name of the current session user.\n" + 
		SESSION_USER_ADMIN + ": The administrator privilege indicator of the current session user.\n" + 
		SESSION_USER_LEVEL + ": The access level of the current session user.\n" + 
		"";
	
	public static final String			DATE_PREFIX						= "@DATE";
	public static final String			DATE_EMPTY						= DATE_PREFIX + ".empty";
	public static final String			DATE_NOW						= DATE_PREFIX + ".now";
	public static final String			DATE_TODAY						= DATE_PREFIX + ".today";
	public static final String			DATE_TOMORROW					= DATE_PREFIX + ".tomorrow";
	public static final String			DATE_PREV_YEAR					= DATE_PREFIX + ".prevYear";
	public static final String			DATE_THIS_YEAR					= DATE_PREFIX + ".thisYear";
	public static final String			DATE_NEXT_YEAR					= DATE_PREFIX + ".nextYear";
	public static final String			DATE_DESCRIPTION				= 
			DATE_EMPTY + ": Emty date/time value.\n" + 
			DATE_NOW + ": The current date/time.\n" + 
			DATE_TODAY + ": Today at 00:00:00.\n" + 
			DATE_TOMORROW + ": Tomorrow at 00:00:00.\n" + 
			DATE_PREV_YEAR + ": The 1st of januari of the previous year at 00:00:00.\n" + 
			DATE_THIS_YEAR + ": The 1st of januari of this year at 00:00:00.\n" + 
			DATE_NEXT_YEAR + ": The 1st of januari of the next year at 00:00:00.\n" + 
			"";
	
	public static final String			CONTEXT_OBJECT_PREFIX			= "@OBJECT";
	public static final String			CONTEXT_OBJECT_DESCRIPTION		= 
			"The prefix '" + CONTEXT_OBJECT_PREFIX + "' can be used to access any property value and referenced object property value from the context object.\n" + 
			"For instance; '" + CONTEXT_OBJECT_PREFIX + ".reference.name' will be replaced with the value of the name of the object referenced by the the property 'reference'.\n" + 
			"";
	
	private static DbModel 				model 							= null;
	
	private long						time							= 0; 

	private DbModel() {
		// Singleton
		super(GuiController.getInstance().getSession());
		String[] entities = {
			DbModule.class.getName(),
			DbCollection.class.getName(),
			DbCollectionProperty.class.getName(),
			DbFilterOperator.class.getName(),
			DbCollectionFilter.class.getName(),
			DbReferenceFilter.class.getName(),
			DbPropertyConstraint.class.getName()
		};
		initialize(entities);
	}

	public static DbModel getInstance() {
		if (model==null) {
			model = new DbModel();
		}
		return model;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}
	
	@Override
	public void handleEvent(EvtEvent e) {
		super.handleEvent(e);
		if (e.getType().equals(ClRequest.RECEIVED_REQUEST_RESPONSE)) {
			for (DbCollection dbCol: getCollections()) {
				dbCol.initializeOrderedPropertyLists(GuiController.getInstance().getSession().getUserLevel());
			}
			for (DbCollectionProperty dbProp: getProperties()) {
				if (dbProp.getRefColl()!=null) {
					String childCollectionAndPropertyName = DbCollectionProperty.class.getName() + Generic.SEP_STR + "referenceCollection";
					dbProp.getRefColl().addChildObject(childCollectionAndPropertyName, dbProp);
				}
			}
			for (DbCollectionFilter filter: getCollectionFilters()) {
				if (
					(filter.getValue().getValue().startsWith(SESSION_PREFIX)) ||
					(filter.getValue().getValue().startsWith(DATE_PREFIX))
					) {
					Object value = evaluateExpressionToValue(filter.getValue().getValue(),null);
					String stringValue = evaluateExpressionToStringValue(filter.getValue().getValue(),null);
					filter.getValue().setValue("" + value);
					filter.setStringValue(stringValue);
				}
			}
			for (DbReferenceFilter filter: getReferenceFilters()) {
				if (
					(filter.getValue().getValue().startsWith(SESSION_PREFIX)) ||
					(filter.getValue().getValue().startsWith(DATE_PREFIX))
					) {
					Object value = evaluateExpressionToValue(filter.getValue().getValue(),null);
					String stringValue = evaluateExpressionToStringValue(filter.getValue().getValue(),null);
					filter.getValue().setValue("" + value);
					filter.setStringValue(stringValue);
				}
			}
			GuiModel.getInstance().unserialize();
			Messenger.getInstance().debug(this, "Loaded model in " + (new Date().getTime() - time) + " ms");
			this.publishEvent(new EvtEvent(LOADED_MODEL,this,LOADED_MODEL));
		}
	}

	@Override
	protected void resultsIncomplete() {
		Messenger.getInstance().warn(this, "Model results are incomplete. Try increasing the maximum fetch load and/or maximum fetch results.");
	}

	@Override
	public void loadEntities() {
		time = new Date().getTime();
		super.loadEntities();
	}

	public DbModule getModuleByName(String modName) {
		return (DbModule) this.getCollectionObjectByName(DbModule.class.getName(), modName);
	}

	public DbCollection getCollectionByName(String colName) {
		return (DbCollection) this.getCollectionObjectByName(DbCollection.class.getName(), colName);
	}
	
	/**
	 * @return the modules
	 */
	public List<DbModule> getModules() {
		List<DbModule> list = new ArrayList<DbModule>();
		for (MdlDataObject obj: this.getCollectionObjects(DbModule.class.getName())) {
			list.add((DbModule) obj);
		}
		return list;
	}

	/**
	 * @return the collections
	 */
	private List<DbCollection> getCollections() {
		List<DbCollection> list = new ArrayList<DbCollection>();
		for (MdlDataObject obj: this.getCollectionObjects(DbCollection.class.getName())) {
			list.add((DbCollection) obj);
		}
		return list;
	}

	/**
	 * @return the properties
	 */
	private List<DbCollectionProperty> getProperties() {
		List<DbCollectionProperty> list = new ArrayList<DbCollectionProperty>();
		for (MdlDataObject obj: this.getCollectionObjects(DbCollectionProperty.class.getName())) {
			list.add((DbCollectionProperty) obj);
		}
		return list;
	}

	/**
	 * @return the collection filters
	 */
	private List<DbCollectionFilter> getCollectionFilters() {
		List<DbCollectionFilter> list = new ArrayList<DbCollectionFilter>();
		for (MdlDataObject obj: this.getCollectionObjects(DbCollectionFilter.class.getName())) {
			list.add((DbCollectionFilter) obj);
		}
		return list;
	}
	
	/**
	 * @return the reference filters
	 */
	private List<DbReferenceFilter> getReferenceFilters() {
		List<DbReferenceFilter> list = new ArrayList<DbReferenceFilter>();
		for (MdlDataObject obj: this.getCollectionObjects(DbReferenceFilter.class.getName())) {
			list.add((DbReferenceFilter) obj);
		}
		return list;
	}

	public Object evaluateExpressionToValue(String expression, MdlObjectRefListMap referenceLists) {
		Object returnValue = expression;

		MdlObjectRefListMap refLists = new MdlObjectRefListMap();
		if (referenceLists!=null) {
			for (String collectionName: referenceLists.getCollectionList()) {
				MdlObjectRefList refList = MdlObjectRefList.copy(referenceLists.getReferenceListForCollection(collectionName));
				refLists.addReferenceList(collectionName, refList);
			}
		}

		if (
			(expression.startsWith(SESSION_PREFIX)) ||
			(expression.startsWith(DATE_PREFIX)) ||
			(expression.startsWith(CONTEXT_OBJECT_PREFIX))
			) {
			expression = expression.trim();
		}
		
		boolean evaluated = false;
		if (expression.startsWith(SESSION_PREFIX)) {
			if (expression.equals(SESSION_ID)) {
				returnValue = getSession().getSessionId();
				evaluated = true;
			} else if (expression.equals(SESSION_USER_ID)) {
				returnValue = getSession().getUserId();
				evaluated = true;
			} else if (expression.equals(SESSION_USER_NAME)) {
				returnValue = getSession().getUserName();
				evaluated = true;
			} else if (expression.equals(SESSION_USER_ADMIN)) {
				returnValue = getSession().isUserAdmin();
				evaluated = true;
			} else if (expression.equals(SESSION_USER_LEVEL)) {
				returnValue = getSession().getUserLevel();
				evaluated = true;
			}
		} else if (expression.startsWith(DATE_PREFIX)) {
			if (expression.equals(DATE_EMPTY)) {
				returnValue = DtObject.NULL_VALUE_STRING;
				evaluated = true;
			} else if (expression.equals(DATE_NOW)) {
				Date d = new Date();
				returnValue = d.getTime();
				evaluated = true;
			} else if (expression.equals(DATE_TODAY)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				returnValue = cal.getTime().getTime();
				evaluated = true;
			} else if (expression.equals(DATE_TOMORROW)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.DATE,1);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				returnValue = cal.getTime().getTime();
				evaluated = true;
			} else if (expression.equals(DATE_PREV_YEAR)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.YEAR,-1);
				cal.set(Calendar.DATE,1);
				cal.set(Calendar.MONTH,0);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				returnValue = cal.getTime().getTime();
				evaluated = true;
			} else if (expression.equals(DATE_THIS_YEAR)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.set(Calendar.DATE,1);
				cal.set(Calendar.MONTH,0);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				returnValue = cal.getTime().getTime();
				evaluated = true;
			} else if (expression.equals(DATE_NEXT_YEAR)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.YEAR,1);
				cal.set(Calendar.DATE,1);
				cal.set(Calendar.MONTH,0);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				returnValue = cal.getTime().getTime();
				evaluated = true;
			}
		} else if (expression.startsWith(CONTEXT_OBJECT_PREFIX)) {
			if (referenceLists!=null) {
				MdlObjectRefList contextObjectList = refLists.getReferenceListForCollection(CONTEXT_OBJECT_PREFIX);
				if (contextObjectList!=null) {
					MdlDataObject contextObject = contextObjectList.getReferences().get(0).getDataObject();
					DbCollection dbCol = getCollectionByName(contextObject.getClassName().getValue());
					String[] elements = expression.split("\\.");
					if (elements.length>1) {
						DbCollectionProperty dbProp = dbCol.getPropertyByName(elements[1]);
						if (dbProp!=null) {
							returnValue = contextObject.getPropertyValue(dbProp.getName().getValue());
							evaluated = true;
							
							String remainder = ""; 
							for (int i = 2; i<elements.length; i++) {
								if (!remainder.equals("")) {
									remainder = remainder + ".";
								}
								remainder = remainder + elements[i]; 
							}

							if ((returnValue instanceof DtIdRef) && (!remainder.equals(""))) {
								long id = ((DtIdRef) returnValue).getValue();
								MdlObjectRef ref = referenceLists.getMdlObjectRefById(id);
								if (ref!=null) {
									contextObjectList.getReferences().clear();
									contextObjectList.getReferences().add(ref);
									returnValue = CONTEXT_OBJECT_PREFIX + "." + remainder;
								} else {
									returnValue = expression;
									evaluated = false;
								}
							}
							
						}
					}
				}
			}
		}
		
		if ((evaluated) &&
			(returnValue instanceof String) &&
			(
				((String) returnValue).startsWith(SESSION_PREFIX) ||
				((String) returnValue).startsWith(DATE_PREFIX) ||
				((String) returnValue).startsWith(CONTEXT_OBJECT_PREFIX)
			)
			) {
			returnValue = evaluateExpressionToValue(((String) returnValue),refLists);
		}
		
		return returnValue;
	}
	
	public String evaluateExpressionToStringValue(String expression, MdlObjectRefListMap referenceLists) {
		String returnValue = "";
		
		if (expression.startsWith(SESSION_PREFIX)) {
			if (expression.equals(SESSION_USER_ID)) {
				returnValue = getSession().getUserName();
			}
		} else if (expression.startsWith(DATE_PREFIX)) {
			if (expression.equals(SESSION_USER_ID)) {
				returnValue = getSession().getUserName();
			}
			if (expression.equals(DATE_EMPTY)) {
				returnValue = DtObject.NULL_VALUE_STRING;
			} else if (expression.equals(DATE_NOW)) {
				returnValue = "" + new Date();
			} else if (expression.equals(DATE_TODAY)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				returnValue = "" + cal.getTime();
			} else if (expression.equals(DATE_TOMORROW)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.DATE,1);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				returnValue = "" + cal.getTime();
			} else if (expression.equals(DATE_PREV_YEAR)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.YEAR,-1);
				cal.set(Calendar.DATE,1);
				cal.set(Calendar.MONTH,0);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				returnValue = "" + cal.getTime();
			} else if (expression.equals(DATE_THIS_YEAR)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.set(Calendar.DATE,1);
				cal.set(Calendar.MONTH,0);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				returnValue = "" + cal.getTime();
			} else if (expression.equals(DATE_NEXT_YEAR)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.YEAR,1);
				cal.set(Calendar.DATE,1);
				cal.set(Calendar.MONTH,0);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				returnValue = "" + cal.getTime();
			}
		} else if (expression.startsWith(CONTEXT_OBJECT_PREFIX)) {
			Object valObj = evaluateExpressionToValue(expression,referenceLists);
			if (valObj instanceof DtIdRef) {
				long id = ((DtIdRef) valObj).getValue();
				MdlObjectRef ref = referenceLists.getMdlObjectRefById(id);
				if (ref!=null) {
					returnValue = ref.getName().getValue();
				}
			} else if (valObj instanceof DtIdRefList) {
				List<String> nameList = new ArrayList<String>();
				for (long id: ((DtIdRefList) valObj).getValue()) {
					MdlObjectRef ref = referenceLists.getMdlObjectRefById(id);
					if (ref!=null) {
						nameList.add(ref.getName().getValue());
						if (nameList.size()>=3) {
							break;
						}
					}
				}
				if (nameList.size()>0) {
					returnValue = getStringValueForNameList(nameList);
				}
			}
		}
		
		return returnValue;
	}
	
	public static String getStringValueForNameList(List<String> nameList) {
		int added = 0;
		String names = "";
		for (String name: nameList) {
			if (added>=2) {
				names = names + ", ...";
				break;
			}
			if (added<2) {
				if (!names.equals("")) {
					names = names + ", ";
				}
				names = names + name;
				added++;
			}
		}
		return names;
	}

	public static String getQueryErrorMessageForCollection(QryError error, String collectionName) {
		DbCollection dbCol = DbModel.getInstance().getCollectionByName(collectionName);
		return getQueryErrorMessageForCollection(error,dbCol);
	}

	public static String getQueryErrorMessageForCollection(QryError error, DbCollection dbCol) {
		String properties = "";
		for (String propName: error.getProperties()) {
			DbCollectionProperty dbProp = dbCol.getPropertyByName(propName);
			if (dbProp!=null) {
				if (!properties.equals("")) {
					properties = properties + ", ";
				}
				properties = properties + dbProp.getLabel().getValue();
			}
		}
		if (!properties.equals("")) {
			properties = properties + ": ";
		}
		return properties + error.toString();
	}
}

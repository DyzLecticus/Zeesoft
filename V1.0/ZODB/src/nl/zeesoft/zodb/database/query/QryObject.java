package nl.zeesoft.zodb.database.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.file.XMLElem;
import nl.zeesoft.zodb.file.XMLFile;
import nl.zeesoft.zodb.model.MdlCollection;
import nl.zeesoft.zodb.model.MdlCollectionProperty;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.MdlObjectRefList;
import nl.zeesoft.zodb.model.MdlObjectRefListMap;
import nl.zeesoft.zodb.model.ZODBModel;
import nl.zeesoft.zodb.model.datatypes.DtObject;
import nl.zeesoft.zodb.model.impl.DbUser;

public abstract class QryObject {
	private StringBuffer		log						= new StringBuffer();
	private long				time					= 0;
	private List<QryError>		errors					= new ArrayList<QryError>();

	private Object				source					= null;

	public void addLogLine(String line) {
		log.append(line);
		log.append("\n");
		if (DbConfig.getInstance().isDebug()) {
			Messenger.getInstance().debug(this, line);
		}
	}
	
	public QryError addError(String code, String msg) {
		return addError(code,"",msg,"");
	}
	public QryError addError(String code, String property, String msg) {
		return addError(code,property,msg,"");
	}
	public QryError addError(String code, String property, String msg, String value) {
		QryError error = new QryError(code,property,msg);
		if (value.length()>0) {
			error.getValues().add(value);
		}
		String line = "ERROR: [" + code + "] " + error.toString();
		if (property.length()>0) {
			line = line + " (" + property + ")";
		}
		addLogLine(line);
		errors.add(error);
		return error;
	}

	public static XMLFile toXml(QryObject qry) {
		return toXml(qry,false);
	}
	
	public static XMLFile toXml(QryObject qry,boolean referenceOnly) {
		XMLFile f = new XMLFile();
		if (qry instanceof QryFetch) {
			QryFetch fetch = (QryFetch) qry;
			f.setRootElement(new XMLElem("fetch",null,null));
			new XMLElem("type",new StringBuffer(fetch.getType()),f.getRootElement());
			new XMLElem("className",new StringBuffer(fetch.getClassName()),f.getRootElement());
			if (fetch.getLog().length()>0) {
				StringBuffer sb = new StringBuffer();
				sb.append(fetch.getTime());
				new XMLElem("time",sb,f.getRootElement());
				XMLElem e = new XMLElem("log",fetch.getLog(),f.getRootElement());
				e.setCData(true);
				sb = new StringBuffer();
				sb.append(fetch.getCount());
				new XMLElem("count",sb,f.getRootElement());
			}
			if (fetch.getOrderBy().length()>0) {
				StringBuffer sb = new StringBuffer();
				sb.append(fetch.getOrderBy());
				new XMLElem("orderBy",sb,f.getRootElement());
				sb = new StringBuffer();
				sb.append(fetch.isOrderAscending());
				new XMLElem("orderAscending",sb,f.getRootElement());
			}
			if (fetch.getLimit()>0) {
				StringBuffer sb = new StringBuffer();
				sb.append(fetch.getStart());
				new XMLElem("start",sb,f.getRootElement());
				sb = new StringBuffer();
				sb.append(fetch.getLimit());
				new XMLElem("limit",sb,f.getRootElement());
			}
			if (!fetch.isUseCache()) {
				StringBuffer sb = new StringBuffer();
				sb.append(fetch.isUseCache());
				new XMLElem("useCache",sb,f.getRootElement());
			}
			if (fetch.getConditions().size()>0) {
				XMLElem conditionsElem = new XMLElem("conditions",null,f.getRootElement());
				for (QryFetchCondition c: fetch.getConditions()) {
					XMLElem conditionElem = new XMLElem("condition",null,conditionsElem);
					StringBuffer sb = new StringBuffer();
					sb.append(c.getProperty());
					new XMLElem("property",sb,conditionElem);
					sb = new StringBuffer();
					sb.append(c.isInvert());
					new XMLElem("invert",sb,conditionElem);
					sb = new StringBuffer();
					sb.append(c.getOperator());
					new XMLElem("operator",sb,conditionElem);
					sb = new StringBuffer();
					sb.append(c.getValue().getClass().getName());
					new XMLElem("valueClassName",sb,conditionElem);
					XMLElem e = new XMLElem("value",c.getValue().toStringBuffer(),conditionElem);
					e.setCData(true);
				}
			}
			if (fetch.getEntities().size()>0) {
				XMLElem entitiesElem = new XMLElem("entities",null,f.getRootElement());
				for (String entity: fetch.getEntities()) {
					new XMLElem("entity",new StringBuffer(entity),entitiesElem);
				}
			}
			if (fetch.isResultsIncomplete()) {
				StringBuffer sb = new StringBuffer();
				sb.append(fetch.isResultsIncomplete());
				new XMLElem("resultsIncomplete",sb,f.getRootElement());
			}
			List<String> collectionNames = fetch.getResults().getCollectionList();
			if (collectionNames.size()>0) {
				XMLElem resultsElem = new XMLElem("results",null,f.getRootElement());
				for (String collectionName: collectionNames) {
					MdlObjectRefList refList = fetch.getResults().getReferenceListForCollection(collectionName);
					if (refList.getReferences().size()>0) {
						XMLElem colElem = new XMLElem("collection",null,resultsElem);
						new XMLElem("name",new StringBuffer(collectionName),colElem);
						XMLElem objsElem = new XMLElem("objects",null,colElem);
						for (MdlObjectRef ref: refList.getReferences()) {
							if ((!referenceOnly) && (!fetch.getType().equals(QryFetch.TYPE_FETCH_REFERENCES))) {
								XMLFile oXml = MdlDataObject.toXml(ref.getDataObject());
								oXml.getRootElement().setParent(objsElem);
								oXml.setRootElement(null);
								oXml = null;
							} else {
								new XMLElem("reference",ref.toStringBuffer(referenceOnly),objsElem);
							}
						}
					}
				}
			}
			if (fetch.getExtendedReferences().size()>0) {
				XMLElem refElem = new XMLElem("extendedReferences",null,f.getRootElement());
				for (Entry<Long,String> entry: fetch.getExtendedReferences().entrySet()) {
					XMLElem valueElem = new XMLElem("value",null,refElem);
					StringBuffer sb = new StringBuffer();
					sb.append(entry.getKey());
					new XMLElem("id",sb,valueElem);
					sb = new StringBuffer();
					sb.append(entry.getValue());
					new XMLElem("name",sb,valueElem);
				}
			}
		} else if (qry instanceof QryUpdate) {
			f.setRootElement(new XMLElem("update",null,null));
			if (qry.getLog().length()>0) {
				new XMLElem("log",qry.getLog(),f.getRootElement());
				StringBuffer sb = new StringBuffer();
				sb.append(qry.getTime());
				new XMLElem("time",sb,f.getRootElement());
			}
			QryUpdate update = (QryUpdate) qry;
			if ((update.getDataObject()!=null) && (update.getFetch()!=null)) {
				XMLFile oXml = null; 
				oXml = QryObject.toXml(update.getFetch());
				oXml.getRootElement().setParent(f.getRootElement());
				oXml = MdlDataObject.toXml(update.getDataObject());
				oXml.getRootElement().setParent(f.getRootElement());
				oXml.setRootElement(null);
				oXml=null;
			}
		} else if (qry instanceof QryAdd) {
			f.setRootElement(new XMLElem("add",null,null));
			if (qry.getLog().length()>0) {
				new XMLElem("log",qry.getLog(),f.getRootElement());
				StringBuffer sb = new StringBuffer();
				sb.append(qry.getTime());
				new XMLElem("time",sb,f.getRootElement());
			}
			QryAdd add = (QryAdd) qry;
			if (add.getDataObject()!=null) {
				XMLFile oXml = MdlDataObject.toXml(add.getDataObject());
				oXml.getRootElement().setParent(f.getRootElement());
				oXml.setRootElement(null);
				oXml=null;
			}
		} else if (qry instanceof QryRemove) {
			f.setRootElement(new XMLElem("remove",null,null));
			if (qry.getLog().length()>0) {
				new XMLElem("log",qry.getLog(),f.getRootElement());
				StringBuffer sb = new StringBuffer();
				sb.append(qry.getTime());
				new XMLElem("time",sb,f.getRootElement());
			}
			QryRemove remove = (QryRemove) qry;
			if (remove.getFetch()!=null) {
				XMLFile oXml = QryObject.toXml(remove.getFetch());
				oXml.getRootElement().setParent(f.getRootElement());
			}
		}
		if (qry.getErrors().size()>0) {
			XMLElem errorsElem = new XMLElem("errors",null,f.getRootElement());
			for (QryError error: qry.getErrors()) {
				XMLFile errorFile = QryError.toXml(error);
				errorFile.getRootElement().setParent(errorsElem);
			}
		}
		return f;
	}
	
	public static QryObject fromXml(XMLFile f) {
		QryObject r = null;
		if (f.getRootElement().getName().equals("fetch")) {
			String className = f.getRootElement().getChildByName("className").getValue().toString();
			QryFetch fetch = new QryFetch(className); 
			String orderBy = "";
			boolean orderAscending = true;
			int start = 0;
			int limit = 0;
			for (XMLElem cElem: f.getRootElement().getChildren()) {
				if (cElem.getName().equals("type")) {
					fetch.setType(cElem.getValue().toString());
				}
				if (cElem.getName().equals("count")) {
					fetch.setCount(Integer.parseInt(cElem.getValue().toString()));
				}
				if (cElem.getName().equals("time")) {
					fetch.setTime(Long.parseLong(cElem.getValue().toString()));
				}
				if (cElem.getName().equals("orderBy")) {
					orderBy = cElem.getValue().toString();
				}
				if (cElem.getName().equals("orderAscending")) {
					orderAscending = Boolean.parseBoolean(cElem.getValue().toString());
				}
				if (cElem.getName().equals("start")) {
					start = Integer.parseInt(cElem.getValue().toString());
				}
				if (cElem.getName().equals("limit")) {
					limit = Integer.parseInt(cElem.getValue().toString());
				}
				if (cElem.getName().equals("useCache")) {
					fetch.setUseCache(Boolean.parseBoolean(cElem.getValue().toString()));
				}
				if (cElem.getName().equals("log")) {
					fetch.setLog(cElem.getValue());
				}
				if (cElem.getName().equals("conditions")) {
					for (XMLElem conElem: cElem.getChildren()) {
						String property = "";
						boolean invert = false;
						String operator = "";
						String valueClassName = "";
						StringBuffer valueString = new StringBuffer();
						for (XMLElem cvElem: conElem.getChildren()) {
							if (cvElem.getName().equals("property")) {
								property = cvElem.getValue().toString();
							}
							if (cvElem.getName().equals("invert")) {
								invert = Boolean.parseBoolean(cvElem.getValue().toString());
							}
							if (cvElem.getName().equals("operator")) {
								operator = cvElem.getValue().toString();
							}
							if (cvElem.getName().equals("value")) {
								valueString.append(cvElem.getValue());
							}
							if (cvElem.getName().equals("valueClassName")) {
								valueClassName = cvElem.getValue().toString();
							}
						}
						DtObject value = (DtObject) Generic.instanceForName(valueClassName);
						value.fromString(valueString);
						fetch.addCondition(new QryFetchCondition(property,invert,operator,value));
					}
				}
				if (cElem.getName().equals("entities")) {
					for (XMLElem entityElem: cElem.getChildren()) {
						fetch.getEntities().add(entityElem.getValue().toString());
					}
				}
				if (cElem.getName().equals("errors")) {
					for (XMLElem errElem: cElem.getChildren()) {
						XMLFile errorFile = new XMLFile();
						errorFile.setRootElement(errElem);
						fetch.getErrors().add(QryError.fromXml(errorFile));
					}
				}
				if (cElem.getName().equals("resultsIncomplete")) {
					fetch.setResultsIncomplete(Boolean.parseBoolean(cElem.getValue().toString()));
				}
				if (cElem.getName().equals("results")) {
					MdlObjectRefListMap results = new MdlObjectRefListMap(); 
					for (XMLElem colElem: cElem.getChildren()) {
						String name = colElem.getChildByName("name").getValue().toString();
						for (XMLElem objElem: colElem.getChildByName("objects").getChildren()) {
							if (objElem.getName().equals("reference")) {
								results.addReference(name,MdlObjectRef.parseMdlObjectRef(objElem.getValue().toString()));
							} else {
								XMLFile oXml = new XMLFile();
								oXml.setRootElement(objElem);
								MdlDataObject obj = MdlDataObject.fromXml(oXml);
								results.addReference(name,new MdlObjectRef(obj));
							}
						}
					}
					fetch.setResults(results);
				}
				if (cElem.getName().equals("extendedReferences")) {
					for (XMLElem valElem: cElem.getChildren()) {
						long id = 0;
						String name = "";
						for (XMLElem kvElem: valElem.getChildren()) {
							if (kvElem.getName().equals("id")) {
								id = Long.parseLong(kvElem.getValue().toString());
							} 
							if (kvElem.getName().equals("name")) {
								name = kvElem.getValue().toString();
							} 
						}
						if (id>0) {
							fetch.getExtendedReferences().put(id, name);
						}
					}
				}
			}
			fetch.setOrderBy(orderBy, orderAscending);
			fetch.setStartLimit(start, limit);
			r = fetch;
		} else if (f.getRootElement().getName().equals("update")) {
			XMLFile oXml = new XMLFile();
			oXml.setRootElement(f.getRootElement().getChildByName("object"));
			MdlDataObject obj = MdlDataObject.fromXml(oXml);
			oXml.setRootElement(f.getRootElement().getChildByName("fetch"));
			QryFetch fetch = (QryFetch) QryObject.fromXml(oXml);
			QryUpdate update = new QryUpdate(fetch,obj);
			for (XMLElem cElem: f.getRootElement().getChildren()) {
				if (cElem.getName().equals("time")) {
					update.setTime(Long.parseLong(cElem.getValue().toString()));
				}
				if (cElem.getName().equals("log")) {
					update.setLog(cElem.getValue());
				}
				if (cElem.getName().equals("errors")) {
					for (XMLElem errElem: cElem.getChildren()) {
						XMLFile errorFile = new XMLFile();
						errorFile.setRootElement(errElem);
						update.getErrors().add(QryError.fromXml(errorFile));
					}
				}
			}
			r = update;
		} else if (f.getRootElement().getName().equals("add")) {
			XMLFile oXml = new XMLFile();
			oXml.setRootElement(f.getRootElement().getChildByName("object"));
			MdlDataObject obj = MdlDataObject.fromXml(oXml);
			QryAdd add = new QryAdd(obj);
			for (XMLElem cElem: f.getRootElement().getChildren()) {
				if (cElem.getName().equals("time")) {
					add.setTime(Long.parseLong(cElem.getValue().toString()));
				}
				if (cElem.getName().equals("log")) {
					add.setLog(cElem.getValue());
				}
				if (cElem.getName().equals("errors")) {
					for (XMLElem errElem: cElem.getChildren()) {
						XMLFile errorFile = new XMLFile();
						errorFile.setRootElement(errElem);
						add.getErrors().add(QryError.fromXml(errorFile));
					}
				}
			}
			r = add;
		} else if (f.getRootElement().getName().equals("remove")) {
			XMLFile oXml = new XMLFile();
			oXml.setRootElement(f.getRootElement().getChildByName("fetch"));
			QryFetch fetch = (QryFetch) QryObject.fromXml(oXml);
			QryRemove remove = new QryRemove(fetch);
			for (XMLElem cElem: f.getRootElement().getChildren()) {
				if (cElem.getName().equals("time")) {
					remove.setTime(Long.parseLong(cElem.getValue().toString()));
				}
				if (cElem.getName().equals("log")) {
					remove.setLog(cElem.getValue());
				}
				if (cElem.getName().equals("errors")) {
					for (XMLElem errElem: cElem.getChildren()) {
						XMLFile errorFile = new XMLFile();
						errorFile.setRootElement(errElem);
						remove.getErrors().add(QryError.fromXml(errorFile));
					}
				}
			}
			r = remove;
		}
		return r;
	}

	public static boolean checkFetch(QryFetch fetch, DbUser user) {
		boolean ok = true;
		ZODBModel model = DbConfig.getInstance().getModel();
		MdlCollection c = model.getCollectionByName(fetch.getClassName());
		if (checkQueryCollectionAccess(fetch,user,fetch.getClassName())) { 
			for (String entity: fetch.getEntities()) {
				ok = checkQueryCollectionAccess(fetch,user,entity);
				if (!ok) {
					break;
				}
			}
			if (ok) {
				for (QryFetchCondition cond: fetch.getConditions()) {
					// Check operator
					boolean operatorValid = false;
					for (String operator: Generic.getValuesFromString(QryFetchCondition.OPERATORS)) {
						if (cond.getOperator().equals(operator)) {
							operatorValid = true;
							break;
						}
					}
					if (!operatorValid) {
						fetch.addError("0003","","Invalid operator: @1",cond.getOperator());
						ok = false;
						break;
					}
					// Check properties
					MdlCollectionProperty p = model.getCollectionPropertyByName(c.getName(), cond.getProperty());
					if (p==null) {
						fetch.addError("0004",cond.getProperty(),"Collection property not found");
						ok = false;
						break;
					}
				}
			}
			if (ok) {
				if (fetch.getOrderBy().length()>0) {
					MdlCollectionProperty m = model.getCollectionPropertyByName(c.getName(), fetch.getOrderBy());
					if (m==null) {
						fetch.addError("0005",fetch.getOrderBy(),"Collection property not found");
						ok = false;
					}
				}
			}
		}
		return ok;
	}

	protected static boolean checkQueryCollectionAccess(QryObject qry,DbUser user, String collectionName) {
		boolean ok = true;
		MdlCollection c = DbConfig.getInstance().getModel().getCollectionByName(collectionName);
		int userLevel = DbUser.USER_LEVEL_MIN;
		if (user!=null) {
			user.getLevel().getValue();
		}
		if (c==null) {
			qry.addError("0001","","Collection not found: @1",collectionName);
			ok = false;
		} else if (
			(user!=null) &&
			(
				((qry instanceof QryFetch) && (userLevel>c.getUserLevelFetch())) ||
				((qry instanceof QryUpdate) && (userLevel>c.getUserLevelUpdate())) ||
				((qry instanceof QryAdd) && (userLevel>c.getUserLevelAdd())) ||
				((qry instanceof QryRemove) && (userLevel>c.getUserLevelRemove()))
			)
			) {
			String msg = "";
			String val = "";
			if (qry instanceof QryFetch) {
				msg = "User level is insufficient to fetch collection: @1";
				msg = msg + " (@2 > @3)";
				val = "" + c.getUserLevelFetch();
			} else if (qry instanceof QryUpdate) {
				msg = "User level is insufficient to update collection: @1";
				msg = msg + " (@2 > @3)";
				val = "" + c.getUserLevelUpdate();
			} else if (qry instanceof QryAdd) {
				msg = "User level is insufficient to add to collection: @1";
				msg = msg + " (@2 > @3)";
				val = "" + c.getUserLevelAdd();
			} else if (qry instanceof QryRemove) {
				msg = "User level is insufficient to update collection: @1";
				msg = msg + " (@2 > @3)";
				val = "" + c.getUserLevelRemove();
			}
			QryError err = qry.addError("0002","",msg,c.getName());
			err.getValues().add("" + userLevel);
			err.getValues().add(val);
			ok = false;
		}
		return ok;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * @return the log
	 */
	public StringBuffer getLog() {
		return log;
	}

	/**
	 * @param log the log to set
	 */
	public void setLog(StringBuffer log) {
		this.log = log;
	}

	/**
	 * @return the source
	 * 
	 * Remember; the DbFactory must be able to update objects
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(Object source) {
		this.source = source;
	}

	/**
	 * @return the errors
	 */
	public List<QryError> getErrors() {
		return errors;
	}
}

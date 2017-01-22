package nl.zeesoft.zodb.database.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlLink;
import nl.zeesoft.zodb.database.model.MdlNumber;
import nl.zeesoft.zodb.database.model.MdlObject;
import nl.zeesoft.zodb.database.model.MdlProperty;
import nl.zeesoft.zodb.database.model.MdlString;
import nl.zeesoft.zodb.database.model.MdlUniqueConstraint;
import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

public class ReqGet extends ReqObject {
	public static final int							MAX_INDEXES			= 5;
	public static final String						ALL_PROPERTIES		= "*";
	public static final String						ALL_CHILD_INDEXES	= "*";

	private List<String>							properties 			= new ArrayList<String>();
	private String									orderBy				= "";
	private boolean									orderAscending		= true;
	private int										start				= 0;
	private int										limit				= 0;
	private List<String>							childIndexes		= new ArrayList<String>(); // Adds a map for [child links > child id lists] to the request data objects
	private List<ReqGetFilter>						filters 			= new ArrayList<ReqGetFilter>();
	private boolean									filterStrict		= false; // Slower but more accurate

	private int										count				= 0;
	
	private MdlObject[]								index				= new MdlObject[MAX_INDEXES];
	private SortedMap<Integer,List<ReqGetFilter>>	indexFilters		= new TreeMap<Integer,List<ReqGetFilter>>();
	private List<ReqGetFilter>						remainingFilters 	= new ArrayList<ReqGetFilter>();
	private List<Long>								initialIdList		= null;
	
	public ReqGet(String className) {
		super(className);
	}

	public ReqGet(String className,long id) {
		super(className);
		addFilter(MdlProperty.ID,ReqGetFilter.EQUALS,""+id);
	}

	protected void copy(ReqGet original, ReqGet copy) {
		super.copy(original, copy);
		copy.getProperties().clear();
		for (String prop: original.getProperties()) {
			copy.getProperties().add(prop);
		}
		copy.setOrderBy(new String(original.getOrderBy()));
		copy.setOrderAscending(new Boolean(original.isOrderAscending()));
		copy.setStart(new Integer(original.getStart()));
		copy.setLimit(new Integer(original.getLimit()));
		copy.getChildIndexes().clear();
		for (String idx: original.getChildIndexes()) {
			copy.getChildIndexes().add(idx);
		}
		for (ReqGetFilter filter: original.getFilters()) {
			copy.getFilters().add(filter.copy());
		}
		copy.setFilterStrict(new Boolean(original.isFilterStrict()));
		copy.setCount(new Integer(original.getCount()));
	}
	
	@Override
	protected String getXmlRootElementName() {
		return GET;
	}

	@Override
	public XMLFile toXML() {
		XMLFile file = super.toXML();
		if (properties.size()>0) {
			XMLElem propsElem = new XMLElem("properties",null,file.getRootElement());
			for (String prop: properties) {
				new XMLElem("name",new StringBuilder(prop),propsElem);
			}
		}
		if (orderBy.length()>0) {
			new XMLElem("orderBy",new StringBuilder(orderBy),file.getRootElement());
			new XMLElem("orderAscending",new StringBuilder("" + orderAscending),file.getRootElement());
		}
		new XMLElem("start",new StringBuilder("" + start),file.getRootElement());
		new XMLElem("limit",new StringBuilder("" + limit),file.getRootElement());
		if (childIndexes.size()>0) {
			XMLElem cidxsElem = new XMLElem("childIndexes",null,file.getRootElement());
			for (String cidx: childIndexes) {
				new XMLElem("name",new StringBuilder(cidx),cidxsElem);
			}
		}
		if (filters.size()>0) {
			XMLElem filtsElem = new XMLElem("filters",null,file.getRootElement());
			for (ReqGetFilter filter: filters) {
				filter.toXML().getRootElement().setParent(filtsElem);
			}
			if (filterStrict) {
				new XMLElem("filterStrict",new StringBuilder("" + filterStrict),file.getRootElement());
			}
		}
		new XMLElem("count",new StringBuilder("" + count),file.getRootElement());
		return file;
	}

	@Override
	public void fromXML(XMLElem rootElem) {
		super.fromXML(rootElem);
		if (rootElem.getName().equals(getXmlRootElementName())) {
			for (XMLElem cElem: rootElem.getChildren()) {
				//Messenger.getInstance().debug(this,"element name: " + cElem.getName() + ", value: " + cElem.getValue());
				if (cElem.getName().equals("properties")) {
					for (XMLElem propElem: cElem.getChildren()) {
						if (propElem.getValue()!=null && propElem.getValue().length()>0) {
							properties.add(propElem.getValue().toString());
						}
					}
				}
				if (cElem.getName().equals("orderBy") && cElem.getValue()!=null && cElem.getValue().length()>0) {
					orderBy = cElem.getValue().toString();
				}
				if (cElem.getName().equals("orderAscending") && cElem.getValue()!=null && cElem.getValue().length()>0) {
					orderAscending = Boolean.parseBoolean(cElem.getValue().toString());
				}
				if (cElem.getName().equals("start") && cElem.getValue()!=null && cElem.getValue().length()>0) {
					try {
						start = Integer.parseInt(cElem.getValue().toString());
					} catch (NumberFormatException e) {
						Messenger.getInstance().error(this,"Unable to parse start: " + cElem.getValue());
					}
				}
				if (cElem.getName().equals("limit") && cElem.getValue()!=null && cElem.getValue().length()>0) {
					try {
						limit = Integer.parseInt(cElem.getValue().toString());
					} catch (NumberFormatException e) {
						Messenger.getInstance().error(this,"Unable to parse limit: " + cElem.getValue());
					}
				}
				if (cElem.getName().equals("childIndexes")) {
					for (XMLElem cidxElem: cElem.getChildren()) {
						if (cidxElem.getValue()!=null && cidxElem.getValue().length()>0) {
							childIndexes.add(cidxElem.getValue().toString());
						}
					}
				}
				if (cElem.getName().equals("filters")) {
					for (XMLElem filtElem: cElem.getChildren()) {
						ReqGetFilter filter = new ReqGetFilter();
						filter.fromXML(filtElem);
						filters.add(filter);
					}
				}
				if (cElem.getName().equals("filterStrict") && cElem.getValue()!=null && cElem.getValue().length()>0) {
					filterStrict = Boolean.parseBoolean(cElem.getValue().toString());
				}
				if (cElem.getName().equals("count") && cElem.getValue()!=null && cElem.getValue().length()>0) {
					try {
						count = Integer.parseInt(cElem.getValue().toString());
					} catch (NumberFormatException e) {
						Messenger.getInstance().error(this,"Unable to parse count: " + cElem.getValue());
					}
				}
			}
		}
	}
	
	/**
	 * Initializes properties and determines indexes
	 */
	@Override
	public void prepare() {
		super.prepare();

		MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(getClassName());

		remainingFilters = new ArrayList<ReqGetFilter>(filters);

		if (properties.size()==0) {
			properties.add(MdlProperty.ID);
		} else if (properties.size() == 1 && properties.get(0).equals(ALL_PROPERTIES)) {
			properties.clear();
			for (MdlProperty prop: cls.getPropertiesExtended()) {
				properties.add(prop.getName());
			}
		}

		if (childIndexes.size() == 1 && childIndexes.get(0).equals(ALL_CHILD_INDEXES)) {
			childIndexes.clear();
			for (MdlLink cLink: cls.getChildLinks()) {
				childIndexes.add(cLink.getFullName());
			}
		}
		
		for (ReqGetFilter filter: filters) {
			if (filter.isInvert()) {
				if (filter.getOperator().equals(ReqGetFilter.GREATER)) {
					filter.setInvert(false);
					filter.setOperator(ReqGetFilter.LESS_OR_EQUALS);
				} else if (filter.getOperator().equals(ReqGetFilter.GREATER_OR_EQUALS)) {
					filter.setInvert(false);
					filter.setOperator(ReqGetFilter.LESS);
				} else if (filter.getOperator().equals(ReqGetFilter.LESS)) {
					filter.setInvert(false);
					filter.setOperator(ReqGetFilter.GREATER_OR_EQUALS);
				} else if (filter.getOperator().equals(ReqGetFilter.LESS_OR_EQUALS)) {
					filter.setInvert(false);
					filter.setOperator(ReqGetFilter.GREATER);
				}
			}
		}

		for (int i = 0; i<index.length; i++) {
			determineIndex(i,cls);
			if (index[i]==null || remainingFilters.size()==0) {
				break;
			}
		}
		
		if (filterStrict) {
			remainingFilters = new ArrayList<ReqGetFilter>(filters);
		}
	}
	
	public boolean finishedGetIdListRequest() {
		boolean finished = false;
		if (properties.size() == 1 && properties.get(0).equals(MdlProperty.ID) &&
			(
				(remainingFilters.size()==0 && orderBy.length()==0) ||
				(initialIdList.size()==0)
			)
			) {
			if (initialIdList.size()!=getObjects().size()) {
				for (Long id: initialIdList) {
					DbDataObject obj = new DbDataObject();
					obj.setId(id);
					getObjects().add(new ReqDataObject(obj));
				}
			}
			finished = true;
		}
		return finished;
	}
	
	public boolean applyStartLimitToInitialIdList() {
		boolean applied = false;
		if (orderBy.length()==0 && initialIdList.size()>0 && remainingFilters.size()==0 && (limit>0 || start>0)) {
			if (getCount()==0) {
				setCount(initialIdList.size());
			}
			addLogLine("Start: " + start + ", limit: " + limit + " (size: " + initialIdList.size() + ")");
			List<Long> newIdList = new ArrayList<Long>();
			int lim = limit;
			if (lim==0) {
				lim = (initialIdList.size() - start);
			}
			int end = (start + lim); 
			if (end>=initialIdList.size()) {
				end = initialIdList.size();
			}
			for (int i = start; i < end; i++) {
				newIdList.add(initialIdList.get(i));
			}
			initialIdList = newIdList;
			start = 0;
			limit = 0;
			applied = true;
		}
		return applied;
	}

	public void applyRemainingFiltersToObjectList() {
		if (remainingFilters.size()>0 && getObjects().size()>0) {
			MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(getClassName());
			for (ReqGetFilter filter: remainingFilters) {
				int removed = 0;
				List<ReqDataObject> remainingObjects = new ArrayList<ReqDataObject>(getObjects());
				for (ReqDataObject obj: remainingObjects) {
					boolean remove = false;
					MdlProperty prop = cls.getPropertyByName(filter.getProperty());
					if (prop instanceof MdlLink) {
						List<Long> idList = obj.getDataObject().getLinkValue(prop.getName());
						if (idList!=null && !idList.contains(Long.parseLong(filter.getValue()))) {
							if ((filter.getOperator().equals(ReqGetFilter.EQUALS)) && (idList.size()>1)) {
								remove = true;
							}
						}
					} else if (prop instanceof MdlNumber) {
						StringBuilder stringVal = obj.getDataObject().getPropertyValue(prop.getName());
						if (stringVal!=null) {
							BigDecimal value = new BigDecimal(stringVal.toString());
							BigDecimal filterValue = new BigDecimal(filter.getValue());
							if (filter.getOperator().equals(ReqGetFilter.EQUALS)) {
								if (value.compareTo(filterValue)!=0) {
									remove = true;
								}
							} else if (filter.getOperator().equals(ReqGetFilter.GREATER)) {
								if (value.compareTo(filterValue)<=0) {
									remove = true;
								}
							} else if (filter.getOperator().equals(ReqGetFilter.GREATER_OR_EQUALS)) {
								if (value.compareTo(filterValue)<0) {
									remove = true;
								}
							} else if (filter.getOperator().equals(ReqGetFilter.LESS)) {
								if (value.compareTo(filterValue)>=0) {
									remove = true;
								}
							} else if (filter.getOperator().equals(ReqGetFilter.LESS_OR_EQUALS)) {
								if (value.compareTo(filterValue)>0) {
									remove = true;
								}
							}
						}
					} else {
						StringBuilder stringVal = obj.getDataObject().getPropertyValue(prop.getName());
						if (stringVal!=null) {
							if (filter.getOperator().equals(ReqGetFilter.EQUALS)) {
								StringBuilder filterVal = new StringBuilder(filter.getValue());
								if (!Generic.stringBuilderEquals(stringVal,filterVal)) {
									remove = true;
								}
							} else if (filter.getOperator().equals(ReqGetFilter.CONTAINS)) {
								if (stringVal.indexOf(filter.getValue())<0) {
									remove = true;
								}
							}
						}
					}
					if ((remove && !filter.isInvert()) || 
						(!remove && filter.isInvert())
						) {
						getObjects().remove(obj);
						removed++;
					}
				}
				if (removed>0) {
					String invert = "";
					if (filter.isInvert()) {
						invert = "not ";
					}
					addLogLine("Removed " + removed + " objects for filter: " + filter.getProperty() +  " " + invert + filter.getOperator() + " " + filter.getValue());
				}
			}
		}
	}
	
	public void applyOrderByToObjectList() {
		if (orderBy.length()>0 && getObjects().size()>0) {
			if (orderAscending) {
				addLogLine("Order by: " + orderBy + " (ascending)");
			} else {
				addLogLine("Order by: " + orderBy + " (descending)");
			}
			MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(getClassName());
			MdlProperty prop = cls.getPropertyByName(orderBy);
			SortedMap<String,List<ReqDataObject>> orderedStringMap = new TreeMap<String,List<ReqDataObject>>();
			SortedMap<BigDecimal,List<ReqDataObject>> orderedNumberMap = new TreeMap<BigDecimal,List<ReqDataObject>>();
			for (ReqDataObject obj: getObjects()) {
				if (prop instanceof MdlLink) {
					long id = 0;
					List<Long> idList = obj.getDataObject().getLinkValue(prop.getName());
					if (idList!=null && idList.size()>0) {
						id = idList.get(0);
					}
					BigDecimal key = new BigDecimal("" + id);
					List<ReqDataObject> objs = orderedNumberMap.get(key);
					if (objs==null) {
						objs = new ArrayList<ReqDataObject>();
						orderedNumberMap.put(key,objs);
					}
					objs.add(obj);
				} else if (prop instanceof MdlNumber) {
					StringBuilder val = obj.getDataObject().getPropertyValue(prop.getName());
					BigDecimal key = null;
					if (val==null) {
						key = new BigDecimal("0");
					} else {
						key = new BigDecimal(val.toString());
					}
					List<ReqDataObject> objs = orderedNumberMap.get(key);
					if (objs==null) {
						objs = new ArrayList<ReqDataObject>();
						orderedNumberMap.put(key,objs);
					}
					objs.add(obj);
				} else {
					StringBuilder stringVal = obj.getDataObject().getPropertyValue(prop.getName());
					if (stringVal==null) {
						stringVal = new StringBuilder();
					}
					String key = "";
					key = stringVal.toString();
					List<ReqDataObject> objs = orderedStringMap.get(key);
					if (objs==null) {
						objs = new ArrayList<ReqDataObject>();
						orderedStringMap.put(key,objs);
					}
					objs.add(obj);
				}
			}
			getObjects().clear();
			for (Entry<String,List<ReqDataObject>> entry: orderedStringMap.entrySet()) {
				if (orderAscending) {
					for (ReqDataObject obj: entry.getValue()) {
						getObjects().add(obj);
					}
				} else {
					for (ReqDataObject obj: entry.getValue()) {
						getObjects().add(0,obj);
					}
				}
			}
			for (Entry<BigDecimal,List<ReqDataObject>> entry: orderedNumberMap.entrySet()) {
				if (orderAscending) {
					for (ReqDataObject obj: entry.getValue()) {
						getObjects().add(obj);
					}
				} else {
					for (ReqDataObject obj: entry.getValue()) {
						getObjects().add(0,obj);
					}
				}
			}
		}
	}

	public void applyStartLimitToObjectList() {
		if (getCount()==0) {
			setCount(getObjects().size());
		}
		if ((limit>0 || start>0) && getObjects().size()>0) {
			addLogLine("Start: " + start + ", limit: " + limit + " (size: " + getObjects().size() + ")");
			List<ReqDataObject> newObjs = new ArrayList<ReqDataObject>();
			int lim = limit;
			if (lim==0) {
				lim = (getObjects().size() - start);
			}
			int end = (start + lim); 
			if (end>=getObjects().size()) {
				end = getObjects().size();
			}
			for (int i = start; i < end; i++) {
				newObjs.add(getObjects().get(i));
			}
			setObjects(newObjs);
		}
	}

	public void applyPropertiesToObjectList() {
		MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(getClassName());
		if (properties.size()!=cls.getPropertiesExtended().size() && (properties.size()!=1 || !properties.get(0).equals(MdlProperty.ID))) {
			StringBuilder props = new StringBuilder();
			for (MdlProperty prop: cls.getPropertiesExtended()) {
				if (!properties.contains(prop.getName())) {
					if (props.length()>0) {
						props.append(", ");
					}
					props.append(prop.getName());
				}
			}
			addLogLine("Remove property values for properties: " + props);
			for (ReqDataObject obj: getObjects()) {
				for (String property: obj.getDataObject().getProperties()) {
					if (!properties.contains(property)) {
						obj.getDataObject().removePropertyValue(property);
					}
				}
				for (String property: obj.getDataObject().getLinks()) {
					if (!properties.contains(property)) {
						obj.getDataObject().removePropertyValue(property);
					}
				}
			}
		}
	}
	
	@Override
	public boolean check(Object source) {
		boolean ok = super.check(source);
		if (ok) {
			ok = checkProperties(source);
		}
		if (ok) {
			ok = checkFilters(source);
		}
		if (ok) {
			ok = checkOrderBy(source);
		}
		return ok;
	}


	public void addFilter(String prop, String op,String val) {
		filters.add(new ReqGetFilter(prop,op,val));
	}

	public void addFilter(String prop, boolean inv, String op,String val) {
		filters.add(new ReqGetFilter(prop,inv,op,val));
	}

	private boolean checkOrderBy(Object source) {
		boolean ok = true;
		if (getOrderBy().length()>0) {
			MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(getClassName());
			MdlProperty prop = cls.getPropertyByName(getOrderBy());
			if (prop==null) {
				addError(ERROR_CODE_CLASS_PROPERTY_NOT_FOUND,"Class property not found: " + getClassName() + ":" + getOrderBy());
				ok = false;
			}
		}
		return ok;
	}

	private boolean checkProperties(Object source) {
		boolean ok = true;
		MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(getClassName());
		for (String property: properties) {
			if (!property.equals(ALL_PROPERTIES)) {
				MdlProperty prop = cls.getPropertyByName(property);
				if (prop==null) {
					addError(ERROR_CODE_CLASS_PROPERTY_NOT_FOUND,"Class property not found: " + getClassName() + ":" + property);
					ok = false;
					break;
				}
			}
		}
		return ok;
	}
	
	private boolean checkFilters(Object source) {
		boolean ok = true;
		MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(getClassName());
		for (ReqGetFilter filter: filters) {
			MdlProperty prop = cls.getPropertyByName(filter.getProperty());
			if (prop==null) {
				addError(ERROR_CODE_CLASS_PROPERTY_NOT_FOUND,"Class property not found: " + getClassName() + ":" + filter.getProperty());
				ok = false;
				break;
			} else if (prop instanceof MdlString && filter.getValue().length()>((MdlString) prop).getMaxLength()) {
				ReqError error = addError(ERROR_CODE_VALUE_TOO_LONG,"Property value is too long: " + prop.getFullName() + " (" + filter.getValue().length() + ">" + ((MdlString) prop).getMaxLength() + ")");
				error.getProperties().add(prop.getFullName());
				ok = false;
				break;
			} else if (prop instanceof MdlString && ((MdlString) prop).isEncode()) {
				ReqError error = addError(ERROR_CODE_INVALID_FILTER_PROPERTY,"Invalid filter property: " + prop.getFullName());
				error.getProperties().add(prop.getFullName());
				ok = false;
				break;
			} else if (prop instanceof MdlLink || prop instanceof MdlNumber) {
				try {
					new BigDecimal(filter.getValue());
					if (prop instanceof MdlLink) {
						Long.parseLong(filter.getValue());
					} else if (prop.getName().equals(MdlProperty.ID)) {
						Long.parseLong(filter.getValue());
					}
				} catch(NumberFormatException e) {
					ReqError error = null;
					if (prop instanceof MdlLink) {
						error = addError(ERROR_CODE_VALUE_NOT_A_NUMBER,"Link value is not a number: " + prop.getFullName() + ": " + filter.getValue());
					} else {
						error = addError(ERROR_CODE_VALUE_NOT_A_NUMBER,"Property value is not a number: " + prop.getFullName() + ": " + filter.getValue());
					}
					error.getProperties().add(prop.getFullName());
					ok = false;
					break;
				}
			}
			if (ok) {
				if (prop instanceof MdlNumber) {
					if (!filter.getOperator().equals(ReqGetFilter.EQUALS) &&
						!filter.getOperator().equals(ReqGetFilter.GREATER) &&
						!filter.getOperator().equals(ReqGetFilter.GREATER_OR_EQUALS) &&
						!filter.getOperator().equals(ReqGetFilter.LESS) &&
						!filter.getOperator().equals(ReqGetFilter.LESS_OR_EQUALS)
						) {
						ok = false;
					}
				} else if (prop instanceof MdlLink) {
					if (!filter.getOperator().equals(ReqGetFilter.CONTAINS) && 
						!filter.getOperator().equals(ReqGetFilter.EQUALS)
						) {
						ok = false;
					}
				} else {
					if (!filter.getOperator().equals(ReqGetFilter.CONTAINS) && 
						!filter.getOperator().equals(ReqGetFilter.EQUALS)
						) {
						ok = false;
					}
				}

				if (!ok) {
					ReqError error = addError(ERROR_CODE_DATATYPE_OPERATOR_MISMATCH,"Datatype operator mismatch: " + prop.getClass().getName() + " / " + filter.getOperator());
					error.getProperties().add(prop.getFullName());
				}
			}
			if (!ok) {
				break;
			}
		}
		return ok;
	}

	private void determineIndex(int indexNumber, MdlClass cls) {
		index[indexNumber] = null;
		List<ReqGetFilter> filtersForCurrentIndex = indexFilters.get(indexNumber);
		if (filtersForCurrentIndex==null) {
			filtersForCurrentIndex = new ArrayList<ReqGetFilter>();
			indexFilters.put(indexNumber,filtersForCurrentIndex);
		} else {
			filtersForCurrentIndex.clear();
		}
		
		List<MdlObject> selectedIndexes = new ArrayList<MdlObject>();
		List<ReqGetFilter> selectedIndexFilters = new ArrayList<ReqGetFilter>();
		for (int i = 0; i < indexNumber; i++) {
			selectedIndexes.add(index[i]);
			for (ReqGetFilter filter: indexFilters.get(i)) {
				selectedIndexFilters.add(filter);
			}
		}

		if (filters.size()>0) {
			if (indexNumber==0) {
				// Class index?
				for (ReqGetFilter filter: filters) {
					if (
						!filter.isInvert() && 
						filter.getProperty().equals(MdlProperty.ID) &&
						filter.getOperator().equals(ReqGetFilter.EQUALS) &&
						!selectedIndexFilters.contains(filter)
						) {
						index[indexNumber] = cls;
						filtersForCurrentIndex.add(filter);
						remainingFilters.remove(filter);
						break;
					}
				}
			}
			
			if (index[indexNumber]==null) {
				// Unique index?
				MdlUniqueConstraint uniqueConstraint = null;
				List<ReqGetFilter> uniqueConstraintFilters = null;
				for (MdlUniqueConstraint uc: cls.getUniqueConstraintList()) {
					if (!selectedIndexes.contains(uc)) {
						List<ReqGetFilter> testFilters = new ArrayList<ReqGetFilter>();
						int found = 0;
						for (MdlProperty indexProp: uc.getPropertiesListForClass(getClassName())) {
							for (ReqGetFilter filter: filters) {
								if (
									!filter.isInvert() && 
									filter.getProperty().equals(indexProp.getName()) &&
									filter.getOperator().equals(ReqGetFilter.EQUALS) &&
									!selectedIndexFilters.contains(filter)
									) {
									testFilters.add(filter);
									found++;
									break;
								}
							}
						}
						if (found == uc.getPropertiesListForClass(getClassName()).size()) {
							if (uniqueConstraint==null || 
								uniqueConstraint.getPropertiesListForClass(getClassName()).size()<uc.getPropertiesListForClass(getClassName()).size()) {
								uniqueConstraint = uc;
								uniqueConstraintFilters = testFilters;
							}
						}
					}
				}
				if (uniqueConstraint!=null) {
					index[indexNumber] = uniqueConstraint;
					for (ReqGetFilter filter: uniqueConstraintFilters) {
						filtersForCurrentIndex.add(filter);
						remainingFilters.remove(filter);
					}
				}
			}
			
			if (index[indexNumber]==null) {
				// String equals or contains index?
				MdlString stringIndex = null;
				ReqGetFilter stringIndexFilter = null;
				for (MdlProperty prop: cls.getPropertiesExtended()) {
					if (prop instanceof MdlString) {
						if (prop.isIndex()) {
							if (!selectedIndexes.contains(prop)) {
								for (ReqGetFilter filter: filters) {
									if (
										!filter.isInvert() &&
										filter.getProperty().equals(prop.getName()) &&
										(filter.getOperator().equals(ReqGetFilter.EQUALS) || filter.getOperator().equals(ReqGetFilter.CONTAINS))  && 
										!selectedIndexFilters.contains(filter)
										) {
										stringIndex = (MdlString) prop;
										stringIndexFilter = filter;
										break;
									}
								}
							}
						}
					}
					if (stringIndex!=null) {
						break;
					}
				}
				if (stringIndex!=null) {
					index[indexNumber] = stringIndex;
					filtersForCurrentIndex.add(stringIndexFilter);
					remainingFilters.remove(stringIndexFilter);
				}
			}

			if (index[indexNumber]==null) {
				// Number equals index?
				MdlNumber numberIndex = null;
				ReqGetFilter numberIndexFilter = null;
				for (MdlProperty prop: cls.getPropertiesExtended()) {
					if (prop instanceof MdlNumber) {
						if (prop.isIndex()) {
							if (!selectedIndexes.contains(prop)) {
								for (ReqGetFilter filter: filters) {
									if (
										!filter.isInvert() &&
										filter.getProperty().equals(prop.getName()) &&
										filter.getOperator().equals(ReqGetFilter.EQUALS) &&
										!selectedIndexFilters.contains(filter)
										) {
										numberIndex = (MdlNumber) prop;
										numberIndexFilter = filter;
										break;
									}
								}
							}
						}
					}
					if (numberIndex!=null) {
						break;
					}
				}
				if (numberIndex!=null) {
					index[indexNumber] = numberIndex;
					filtersForCurrentIndex.add(numberIndexFilter);
					remainingFilters.remove(numberIndexFilter);
				}
			}
			
			if (index[indexNumber]==null) {
				// Link contains index?
				MdlLink linkIndex = null;
				ReqGetFilter linkIndexFilter = null;
				for (MdlProperty prop: cls.getPropertiesExtended()) {
					if (prop instanceof MdlLink) {
						if (!selectedIndexes.contains(prop)) {
							for (ReqGetFilter filter: filters) {
								if (
									!filter.isInvert() &&
									filter.getProperty().equals(prop.getName()) &&
									filter.getOperator().equals(ReqGetFilter.CONTAINS) &&
									!selectedIndexFilters.contains(filter)
									) {
									linkIndex = (MdlLink) prop;
									linkIndexFilter = filter;
									break;
								}
							}
						}
					}
					if (linkIndex!=null) {
						break;
					}
				}
				if (linkIndex!=null) {
					index[indexNumber] = linkIndex;
					filtersForCurrentIndex.add(linkIndexFilter);
					remainingFilters.remove(linkIndexFilter);
				}
			}

			if (index[indexNumber]==null) {
				// String NOT equals or NOT contains index?
				MdlString stringIndex = null;
				ReqGetFilter stringIndexFilter = null;
				for (MdlProperty prop: cls.getPropertiesExtended()) {
					if (prop instanceof MdlString) {
						if (prop.isIndex()) {
							if (!selectedIndexes.contains(prop)) {
								for (ReqGetFilter filter: filters) {
									if (
										filter.isInvert() &&
										filter.getProperty().equals(prop.getName()) &&
										(filter.getOperator().equals(ReqGetFilter.EQUALS) || filter.getOperator().equals(ReqGetFilter.CONTAINS))  && 
										!selectedIndexFilters.contains(filter)
										) {
										stringIndex = (MdlString) prop;
										stringIndexFilter = filter;
										break;
									}
								}
							}
						}
					}
					if (stringIndex!=null) {
						break;
					}
				}
				if (stringIndex!=null) {
					index[indexNumber] = stringIndex;
					filtersForCurrentIndex.add(stringIndexFilter);
					remainingFilters.remove(stringIndexFilter);
				}
			}

			if (index[indexNumber]==null) {
				// Number subset index?
				MdlNumber numberIndex = null;
				List<ReqGetFilter> numberIndexFilters = null;
				for (MdlProperty prop: cls.getPropertiesExtended()) {
					if (prop instanceof MdlNumber) {
						if (prop.isIndex()) {
							if (!selectedIndexes.contains(prop)) {
								List<ReqGetFilter> testFilters = new ArrayList<ReqGetFilter>();
								for (ReqGetFilter filter: filters) {
									if (
										!filter.isInvert() &&
										filter.getProperty().equals(prop.getName()) &&
										(
												filter.getOperator().equals(ReqGetFilter.GREATER) || 
												filter.getOperator().equals(ReqGetFilter.GREATER_OR_EQUALS) || 
												filter.getOperator().equals(ReqGetFilter.LESS) || 
												filter.getOperator().equals(ReqGetFilter.LESS_OR_EQUALS) 
										) &&
										!selectedIndexFilters.contains(filter)
										) {
										if (filter.getOperator().equals(ReqGetFilter.GREATER) || filter.getOperator().equals(ReqGetFilter.GREATER_OR_EQUALS)) {
											testFilters.add(0,filter);
										} else if (filter.getOperator().equals(ReqGetFilter.LESS) || filter.getOperator().equals(ReqGetFilter.LESS_OR_EQUALS)) {
											testFilters.add(filter);
										}
									}
								}
								if (testFilters.size()>0 && (numberIndex == null || testFilters.size()>numberIndexFilters.size())) {
									numberIndex = (MdlNumber) prop;
									numberIndexFilters = testFilters;
								}
							}
						}
					}
				}
				if (numberIndex!=null) {
					index[indexNumber] = numberIndex;
					for (ReqGetFilter filter: numberIndexFilters) {
						filtersForCurrentIndex.add(filter);
						remainingFilters.remove(filter);
					}
				}
			}
		}
		if (index[indexNumber]==null && indexNumber==0) {
			index[indexNumber] = cls;
		}
		
		if (index[indexNumber]!=null) {
			if (indexNumber==0) {
				if (index[indexNumber] instanceof MdlString) {
					if (orderAscending && orderBy.length()>0 && orderBy.equals(((MdlString) index[indexNumber]).getName())) {
						orderBy = "";
					}
				} else if (index[indexNumber] instanceof MdlNumber) {
					if (orderAscending && orderBy.length()>0 && orderBy.equals(((MdlNumber) index[indexNumber]).getName())) {
						orderBy = "";
					}
				} else if (index[indexNumber] instanceof MdlLink) {
					if (orderAscending && orderBy.length()>0 && orderBy.equals(((MdlLink) index[indexNumber]).getName())) {
						orderBy = "";
					}
				} else if (index[indexNumber] instanceof MdlClass) {
					if (orderAscending && orderBy.length()>0 && orderBy.equals(MdlProperty.ID)) {
						orderBy = "";
					}
				}
			}
			String indexName = "";
			if (index[indexNumber] instanceof MdlUniqueConstraint) {
				indexName = ((MdlUniqueConstraint) index[indexNumber]).getFullName();
			} else if (index[indexNumber] instanceof MdlString) {
				indexName = ((MdlString) index[indexNumber]).getFullName();
			} else if (index[indexNumber] instanceof MdlNumber) {
				indexName = ((MdlNumber) index[indexNumber]).getFullName();
			} else if (index[indexNumber] instanceof MdlLink) {
				indexName = ((MdlLink) index[indexNumber]).getFullName();
			} else if (index[indexNumber] instanceof MdlClass) {
				indexName = ((MdlClass) index[indexNumber]).getFullName();
			}
			
			addLogLine("Selected index " + (indexNumber + 1) + ": " + indexName);
		}
	}

	@Override
	public List<String> getClassNames() {
		List<String> classNames = super.getClassNames();
		boolean uniqueIndexUsed = false;
		for (int i = 0; i<index.length; i++) {
			if (index[i] instanceof MdlUniqueConstraint) {
				uniqueIndexUsed = true;
				break;
			}
		}
		if (uniqueIndexUsed) {
			MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(getClassName());
			for (MdlUniqueConstraint uc: cls.getUniqueConstraintList()) {
				for (String className: uc.getClasses()) {
					if (!classNames.contains(className)) {
						classNames.add(className);
					}
				}
			}
		}
		return classNames;
	}

	/**
	 * @return the orderBy
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @return the orderAscending
	 */
	public boolean isOrderAscending() {
		return orderAscending;
	}

	/**
	 * @param orderAscending the orderAscending to set
	 */
	public void setOrderAscending(boolean orderAscending) {
		this.orderAscending = orderAscending;
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		if (start<0) {
			start = 0;
		}
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		if (limit<0) {
			limit = 0;
		}
		this.limit = limit;
	}

	/**
	 * @return the filters
	 */
	public List<ReqGetFilter> getFilters() {
		return filters;
	}

	/**
	 * @return the properties
	 */
	public List<String> getProperties() {
		return properties;
	}

	/**
	 * @return the index
	 */
	public MdlObject[] getIndex() {
		return index;
	}

	/**
	 * @return the indexFilters
	 */
	public SortedMap<Integer, List<ReqGetFilter>> getIndexFilters() {
		return indexFilters;
	}

	/**
	 * @return the initialIdList
	 */
	public List<Long> getInitialIdList() {
		return initialIdList;
	}

	/**
	 * @param initialIdList the initialIdList to set
	 */
	public void setInitialIdList(List<Long> initialIdList) {
		this.initialIdList = initialIdList;
	}

	/**
	 * @return the childIndexes
	 */
	public List<String> getChildIndexes() {
		return childIndexes;
	}

	/**
	 * @return the filterStrict
	 */
	public boolean isFilterStrict() {
		return filterStrict;
	}

	/**
	 * @param filterStrict the filterStrict to set
	 */
	public void setFilterStrict(boolean filterStrict) {
		this.filterStrict = filterStrict;
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
}

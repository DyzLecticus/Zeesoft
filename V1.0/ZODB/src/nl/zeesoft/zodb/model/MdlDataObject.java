package nl.zeesoft.zodb.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.database.query.QryRemove;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.file.XMLElem;
import nl.zeesoft.zodb.file.XMLFile;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.datatypes.DtDecimal;
import nl.zeesoft.zodb.model.datatypes.DtFloat;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtIdRefList;
import nl.zeesoft.zodb.model.datatypes.DtInteger;
import nl.zeesoft.zodb.model.datatypes.DtLong;
import nl.zeesoft.zodb.model.datatypes.DtObject;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;
import nl.zeesoft.zodb.model.impl.DbUser;

/**
 * This class defines the default properties for all model classes.
 * All model classes must extend this class.
 */
public class MdlDataObject extends MdlObject {
	private SortedMap<String,List<MdlDataObject>> 	referencedObjects 	= new TreeMap<String,List<MdlDataObject>>();
	private SortedMap<String,List<MdlDataObject>> 	childObjects 		= new TreeMap<String,List<MdlDataObject>>();
	
	/**
	 * Override to specify for implementation; must call super first!
	 * 
	 * This method is called before the new values of the object in the query are applied to the actual object.
	 * References and unique constraints are checked after this method is called.
	 * 
	 * @param user The user that is executing the query
	 * @param q The query (type QryAdd or QryUpdate)
	 * @return true if the values are valid.
	 */
	public boolean checkObjectConstraintsForUserQuery(DbUser user, QryObject q) {
		boolean ok = true;
		MdlDataObject changedObject = null; 
		if (q instanceof QryAdd) {
			changedObject = ((QryAdd) q).getDataObject();
		} else if (q instanceof QryUpdate) {
			changedObject = ((QryUpdate) q).getDataObject();
		} else if (q instanceof QryRemove) {
			changedObject = null; 
		}
		if (changedObject!=null) {
			if ((changedObject.getName().getValue()!=null) && (changedObject.getName().getValue().length()>0)) {
				changedObject.getName().setValue(Generic.getSerializableStringValue(changedObject.getName().getValue()));
			}
			for (MdlCollectionProperty prop: DbConfig.getInstance().getModel().getCollectionProperties(getClassName().getValue())) {
				if (prop.getConstraints().size()>0) {
					DtObject value = changedObject.getPropertyValue(prop.getName());
					if ((value instanceof DtIdRef) ||
						(value instanceof DtIdRefList)
						) {
						for (String constrain: prop.getConstraints()) {
							if (constrain.equals(DtObject.CONSTRAIN_PROPERTY_MANDATORY)) {
								if (value instanceof DtIdRef) {
									DtIdRef ref = (DtIdRef) value;
									if ((ref.getValue()==null) || (ref.getValue()==0)) {
										q.addError("1001",prop.getName(),"Value is mandatory");
										ok = false;
									}
								} else if (value instanceof DtIdRefList) {
									DtIdRefList refList = (DtIdRefList) value;
									if ((refList.getValue()==null) || (refList.getValue().size()==0)) {
										q.addError("1001",prop.getName(),"Value is mandatory");
										ok = false;
									}
								}
							}
						}
					} else if (value instanceof DtString) {
						DtString dtString = (DtString) value;
						if ((prop.getMinValue()>=0) && (prop.getMaxValue()<Integer.MAX_VALUE) && (prop.getMaxValue()>=prop.getMinValue())) { 
							if ((dtString.getValue()!=null) && (dtString.getValue().length()>prop.getMaxValue())) {
								dtString.setValue(dtString.getValue().substring(0,Integer.parseInt("" + prop.getMaxValue())));
							} else if ((dtString.getValue()==null) || (dtString.getValue().length()<prop.getMinValue())) {
								if (prop.getMinValue()>1) {
									q.addError("1002",prop.getName(),"Value must contain at least @1 characters","" + prop.getMinValue());
								} else {
									q.addError("1003",prop.getName(),"Value must contain at least one character");
								}
								ok = false;
							}
						}
						for (String constrain: prop.getConstraints()) {
							if (constrain.equals(DtObject.CONSTRAIN_STRING_ALPHABETIC)) { 
								if ((dtString.getValue()!=null) && (dtString.getValue().length()>0)) {
									dtString.setValue(Generic.removeNonAllowedCharactersFromString(dtString.getValue(),Generic.ALPHABETIC));
								}
							} else if (constrain.equals(DtObject.CONSTRAIN_STRING_ALPHABETIC_UNDERSCORE)) { 
								if ((dtString.getValue()!=null) && (dtString.getValue().length()>0)) {
									dtString.setValue(Generic.removeNonAllowedCharactersFromString(dtString.getValue(),"_" + Generic.ALPHABETIC));
								}
							} else if (constrain.equals(DtObject.CONSTRAIN_STRING_ALPHANUMERIC)) { 
								if ((dtString.getValue()!=null) && (dtString.getValue().length()>0)) {
									dtString.setValue(Generic.removeNonAllowedCharactersFromString(dtString.getValue(),Generic.ALPHANUMERIC));
								}
							} else if (constrain.equals(DtObject.CONSTRAIN_STRING_ALPHANUMERIC_UNDERSCORE)) { 
								if ((dtString.getValue()!=null) && (dtString.getValue().length()>0)) {
									dtString.setValue(Generic.removeNonAllowedCharactersFromString(dtString.getValue(),"_" + Generic.ALPHANUMERIC));
								}
							} else if (constrain.equals(DtObject.CONSTRAIN_STRING_NO_SPACE)) { 
								if ((dtString.getValue()!=null) && (dtString.getValue().contains(" "))) {
									dtString.setValue(dtString.getValue().replace(" ", ""));
								}
							} else if (constrain.equals(DtObject.CONSTRAIN_STRING_UPPER_CASE)) { 
								if ((dtString.getValue()!=null) && (dtString.getValue().length()>0)) {
									dtString.setValue(dtString.getValue().toUpperCase());
								}
							} else if (constrain.equals(DtObject.CONSTRAIN_STRING_LOWER_CASE)) { 
								if ((dtString.getValue()!=null) && (dtString.getValue().length()>0)) {
									dtString.setValue(dtString.getValue().toLowerCase());
								}
							} else if (constrain.equals(DtObject.CONSTRAIN_PROPERTY_MANDATORY)) {
								if (
										(dtString.getValue()==null) ||
										(dtString.getValue().length()==0)
									) {
									q.addError("1001",prop.getName(),"Value is mandatory");
									ok = false;
								}
							}
						}
					} else if (value instanceof DtStringBuffer) {
						DtStringBuffer dtString = (DtStringBuffer) value;
						if ((prop.getMinValue()>=0) && (prop.getMaxValue()<Integer.MAX_VALUE) && (prop.getMaxValue()>=prop.getMinValue())) { 
							if ((dtString.getValue()!=null) && (dtString.getValue().length()>prop.getMaxValue())) {
								dtString.setValue(dtString.getValue().substring(0,Integer.parseInt("" + prop.getMaxValue())));
							} else if ((dtString.getValue()==null) || (dtString.getValue().length()<prop.getMinValue())) {
								if (prop.getMinValue()>1) {
									q.addError("1002",prop.getName(),"Value must contain at least @1 characters","" + prop.getMinValue());
								} else {
									q.addError("1003",prop.getName(),"Value must contain at least one character");
								}
								ok = false;
							}
						}
						for (String constrain: prop.getConstraints()) {
							if (constrain.equals(DtObject.CONSTRAIN_PASSWORD)) {
								if (dtString.getValue().length()>0) {
									StringBuffer sVal = DbConfig.getInstance().decodePassword(dtString.getValue());
									if (sVal.length()<8) {
										q.addError("1004",prop.getName(),"Value must contain at least eight characters");
										ok = false;
									}
									if (
										(ok) &&
										(
											(!Generic.stringBufferContainsOneOfCharacters(sVal, Generic.ALPHABETIC)) ||
											(!Generic.stringBufferContainsOneOfCharacters(sVal, Generic.NUMERIC)) ||
											(!Generic.stringBufferContainsOneOfCharacters(sVal, Generic.PASSWORD))
										)
										) {
										q.addError("1005",prop.getName(),"Value must contain at least a letter, a number and a special character (" + Generic.PASSWORD + ")");
										ok = false;
									}
								}
							} else if (constrain.equals(DtObject.CONSTRAIN_PROPERTY_MANDATORY)) {
								if (
										(dtString.getValue()==null) ||
										(dtString.getValue().length()==0)
									) {
									q.addError("1001",prop.getName(),"Value is mandatory");
									ok = false;
								}
							}
						}
					} else if (value instanceof DtDateTime) {
						for (String constrain: prop.getConstraints()) {
							if (constrain.equals(DtObject.CONSTRAIN_PROPERTY_MANDATORY)) {
								DtDateTime dt = (DtDateTime) value;
								if (dt.getValue()==null) {
									q.addError("1001",prop.getName(),"Value is mandatory");
									ok = false;
								}
							} else if (
								(constrain.equals(DtObject.CONSTRAIN_DATETIME_FUTURE)) ||
								(constrain.equals(DtObject.CONSTRAIN_DATETIME_PAST))
								) {
								DtDateTime dt = (DtDateTime) value;
								if (dt.getValue()!=null) {
									Date now = new Date();
									Date val = dt.getValue();
									if ((constrain.equals(DtObject.CONSTRAIN_DATETIME_FUTURE)) && (val.getTime()<=now.getTime())) {
										q.addError("1006",prop.getName(),"Value must be a future date and/or time");
										ok = false;
									} else if ((constrain.equals(DtObject.CONSTRAIN_DATETIME_PAST)) && (val.getTime()>=now.getTime())) {
										q.addError("1007",prop.getName(),"Value must be a past date and/or time");
										ok = false;
									}
								}
							}
						}
					} else if (value instanceof DtDecimal) {
						if ((prop.getMinValue()>=Long.MIN_VALUE) && (prop.getMaxValue()<=Long.MAX_VALUE)) {
							BigDecimal minValue = new BigDecimal(prop.getMinValue());
							BigDecimal maxValue = new BigDecimal(prop.getMaxValue());
							if (((BigDecimal)value.getValue()).compareTo(minValue) == -1) {
								value.setValue(new BigDecimal(prop.getMinValue()));
							} else if (((BigDecimal)value.getValue()).compareTo(maxValue) == 1) {
								value.setValue(new BigDecimal(prop.getMaxValue()));
							}
						}
					} else if (value instanceof DtFloat) {
						if ((prop.getMinValue()>=Float.MIN_VALUE) && (prop.getMaxValue()<=Float.MAX_VALUE)) {
							float minValue = new Float(prop.getMinValue());
							float maxValue = new Float(prop.getMinValue());
							if ((Float)value.getValue()<minValue) {
								value.setValue(new Float(minValue));
							} else if ((Float)value.getValue()>maxValue) {
								value.setValue(new Float(maxValue));
							}
						}
					} else if (value instanceof DtInteger) {
						if ((prop.getMinValue()>=Integer.MIN_VALUE) && (prop.getMaxValue()<=Integer.MAX_VALUE)) {
							int minValue = Integer.parseInt("" + prop.getMinValue());
							int maxValue = Integer.parseInt("" + prop.getMaxValue());
							if ((Integer)value.getValue()<minValue) {
								value.setValue(new Integer(minValue));
							} else if ((Integer)value.getValue()>maxValue) {
								value.setValue(new Integer(maxValue));
							}
						}
					} else if (value instanceof DtLong) {
						if ((Long)value.getValue()<prop.getMinValue()) {
							value.setValue(new Long(prop.getMinValue()));
						} else if ((Long)value.getValue()>prop.getMaxValue()) {
							value.setValue(new Long(prop.getMaxValue()));
						}
					}
				}
			}
		}
		return ok;
	}
	
	/**
	 * Override to specify for implementation.
	 * 
	 * This method is called before the object is removed.
	 * 
	 * @param user The user that is executing the query
	 * @param q The remove query (type QryRemove)
	 * @return true if the remove is valid.
	 */
	public boolean checkObjectConstraintsForUserRemoveQuery(DbUser user,QryRemove q) {
		return true;
	}

	protected final MdlCollection getModelCollection() {
		return DbConfig.getInstance().getModel().getCollectionByName(getClassName().getValue());
	}

	protected final MdlCollectionProperty getModelCollectionProperty(String propName) {
		return DbConfig.getInstance().getModel().getCollectionPropertyByName(getClassName().getValue(),propName);
	}
	
	public final static MdlDataObject copy(MdlDataObject original) {
		MdlDataObject copy = (MdlDataObject) Generic.instanceForName(original.getClassName().getValue());
		for (MdlCollectionProperty prop: DbConfig.getInstance().getModel().getCollectionProperties(original.getClassName().getValue())) {
			DtObject value = original.getPropertyValue(prop.getName());
			copy.setPropertyValue(prop.getName(), DtObject.copy(value));
			if (prop instanceof MdlCollectionReference) {
				for (MdlDataObject obj: original.getReferencedObjects(prop.getName())) {
					copy.addReferencedObject(prop.getName(),obj);
				}
			}
		}
		for (MdlCollectionReference cColRef: DbConfig.getInstance().getModel().getCollectionReferencesByReferenceClass(original.getClassName().getValue())) {
			String childCollectionAndPropertyName = cColRef.getModelCollection().getName() + Generic.SEP_STR + cColRef.getName();
			for (MdlDataObject obj: original.getChildObjects(childCollectionAndPropertyName)) {
				copy.addChildObject(childCollectionAndPropertyName, obj);
			}
		}
		return copy;
	}
	
	public final static XMLFile toXml(MdlDataObject o) {
		XMLFile f = new XMLFile();
		f.setRootElement(new XMLElem("object", null, null));
		for (MdlCollectionProperty prop: DbConfig.getInstance().getModel().getCollectionProperties(o.getClassName().getValue())) {
			DtObject value = (DtObject) Generic.executeObjectClassMethod(o,prop.getMethod(),null);
			XMLElem e = new XMLElem(prop.getName(), value.toStringBuffer(), f.getRootElement());
			if (
				(!(value instanceof DtBoolean)) &&
				(!(value instanceof DtDecimal)) &&
				(!(value instanceof DtFloat)) &&
				(!(value instanceof DtInteger)) &&
				(!(value instanceof DtLong)) &&
				(!(value instanceof DtDateTime))
				) {
				e.setCData(true);
			}
		}
		return f;
	}
	
	public final static MdlDataObject fromXml(XMLFile f) {
		MdlDataObject o = null;
		if (
			(f.getRootElement()!=null) && 
			(f.getRootElement().getName().equals("object"))
			) {
			XMLElem classNameElem = f.getRootElement().getChildByName(PROPERTY_CLASSNAME);
			if (classNameElem!=null) {
				o = (MdlDataObject) Generic.instanceForName(classNameElem.getValue().toString());
				for (MdlCollectionProperty prop: DbConfig.getInstance().getModel().getCollectionProperties(o.getClassName().getValue())) {
					XMLElem valueElem = f.getRootElement().getChildByName(prop.getName());
					DtObject value = (DtObject) Generic.executeObjectClassMethod(o,prop.getMethod(),null);
					if ((valueElem!=null) && (value!=null)) {
						value.fromString(valueElem.getValue());
					}
				}
			}
		}
		return o;
	}

	/**
	 * This method is used by the client entity loader to set the loaded entity references
	 * 
	 * @param propertyName The name of the reference property
	 * @param object The referenced object to add to the list of referenced objects for the specified property
	 */
	public void addReferencedObject(String propertyName,MdlDataObject object) {
		List<MdlDataObject> references = referencedObjects.get(propertyName);
		if (references==null) {
			references = new ArrayList<MdlDataObject>();
			referencedObjects.put(propertyName, references);
		}
		if (!references.contains(object)) {
			references.add(object);
		}
	}
	
	public List<MdlDataObject> getReferencedObjects(String propertyName) {
		List<MdlDataObject> list = referencedObjects.get(propertyName);
		if (list==null) {
			list = new ArrayList<MdlDataObject>();
		}
		return list;
	}

	/**
	 * This method is used by the client entity loader to set the loaded child entity references
	 * 
	 * @param childCollectionAndPropertyName The name of collection combined with the reference property
	 * @param object The child object to add to the list of child objects for the specified property
	 */
	public void addChildObject(String childCollectionAndPropertyName,MdlDataObject object) {
		List<MdlDataObject> children = childObjects.get(childCollectionAndPropertyName);
		if (children==null) {
			children = new ArrayList<MdlDataObject>();
			childObjects.put(childCollectionAndPropertyName, children);
		}
		if (!children.contains(object)) {
			children.add(object);
		}
	}
	
	public List<MdlDataObject> getChildObjects(String childCollectionAndPropertyName) {
		List<MdlDataObject> list = childObjects.get(childCollectionAndPropertyName);
		if (list==null) {
			list = new ArrayList<MdlDataObject>();
		}
		return list;
	}
}

package nl.zeesoft.zodb.database.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlLink;
import nl.zeesoft.zodb.database.model.MdlModel;
import nl.zeesoft.zodb.database.model.MdlNumber;
import nl.zeesoft.zodb.database.model.MdlProperty;
import nl.zeesoft.zodb.database.model.MdlString;
import nl.zeesoft.zodb.database.model.MdlUniqueConstraint;
import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

public abstract class ReqObjectChange extends ReqObject {
	private List<Long>			impactedIds		= new ArrayList<Long>();

	public ReqObjectChange(String className) {
		super(className);
	}

	/**
	 * @return the impactedIds
	 */
	public List<Long> getImpactedIds() {
		return impactedIds;
	}

	protected void copy(ReqObjectChange original, ReqObjectChange copy) {
		super.copy(original, copy);
		copy.getImpactedIds().clear();
		for (Long id: original.getImpactedIds()) {
			copy.getImpactedIds().add(new Long(id));
		}
	}
	
	@Override
	public XMLFile toXML() {
		XMLFile file = super.toXML();
		if (impactedIds.size()>0) {
			XMLElem idsElem = new XMLElem("impactedIds",null,file.getRootElement());
			for (long id: impactedIds) {
				new XMLElem(MdlProperty.ID,new StringBuilder("" + id),idsElem);
			}
		}
		return file;
	}

	@Override
	public void fromXML(XMLElem rootElem) {
		super.fromXML(rootElem);
		if (rootElem.getName().equals(getXmlRootElementName())) {
			for (XMLElem cElem: rootElem.getChildren()) {
				if (cElem.getName().equals("impactedIds")) {
					for (XMLElem idElem: cElem.getChildren()) {
						impactedIds.add(Long.parseLong(idElem.getValue().toString()));
					}
				}
			}
		}
	}

	@Override
	public boolean check(Object source) {
		boolean ok = super.check(source);
		if (ok) {
			ok = checkObjectPropertyValues(source);
		}
		return ok;
	}

	private boolean checkObjectPropertyValues(Object source) {
		boolean ok = true;
		MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(getClassName());
		for (ReqDataObject object: getObjects()) {
			for (MdlProperty prop: cls.getPropertiesExtended()) {
				if (prop instanceof MdlString) {
					MdlString str = (MdlString) prop;
					StringBuilder value = object.getDataObject().getPropertyValue(prop.getName());
					if (value!=null) {
						if (value.length()>str.getMaxLength()) {
							ReqError error = addObjectError(object,ERROR_CODE_VALUE_TOO_LONG,"Value is too long: " + prop.getFullName() + ": " + value.length() + ">" + str.getMaxLength());
							error.getProperties().add(prop.getFullName());
							ok = false;
							break;
						}
					}
					// Model specific validations
					if ((this instanceof ReqAdd || object.getDataObject().hasPropertyValue(prop.getName())) && (
						cls.getFullName().equals(MdlModel.PACKAGE_CLASS_FULL_NAME) ||
						cls.getFullName().equals(MdlModel.CLASS_CLASS_FULL_NAME) ||
						cls.getFullName().equals(MdlModel.STRING_CLASS_FULL_NAME) ||
						cls.getFullName().equals(MdlModel.NUMBER_CLASS_FULL_NAME) ||
						cls.getFullName().equals(MdlModel.LINK_CLASS_FULL_NAME) ||
						cls.getFullName().equals(MdlModel.UNIQUE_CONSTRAINT_CLASS_FULL_NAME)
						)) {
						if (value!=null && value.length()>0) {
							if (prop.getName().equals("name") || prop.getName().equals("properties")) {
								String allowedCharacters = Generic.ALPHANUMERIC;
								if (prop.getName().equals("name")) {
									if (cls.getFullName().equals(MdlModel.PACKAGE_CLASS_FULL_NAME)) {
										allowedCharacters += ".";
									}
								} else if (prop.getName().equals("properties")) {
									allowedCharacters += ",";
								}
								if (Generic.stringBuilderContainsOneOfNonAllowedCharacters(value,allowedCharacters)) {
									ReqError error = addObjectError(object,ERROR_CODE_INVALID_PROPERTY_VALUE,"Invalid property value: " + prop.getFullName());
									error.getProperties().add(prop.getFullName());
									ok = false;
									break;
								}
							}
						}
						if (value==null || value.length()==0) {
							if (prop.getName().equals("name") || prop.getName().equals("properties")) {
								ReqError error = addObjectError(object,ERROR_CODE_VALUE_IS_MANDATORY,"Value is mandatory: " + prop.getFullName());
								error.getProperties().add(prop.getFullName());
								ok = false;
								break;
							}
							if (prop.getName().equals("abstract") || prop.getName().equals("index")) {
								value = new StringBuilder("false");
								object.getDataObject().setPropertyValue(prop.getName(),value);
							}
						} else {
							if (prop.getName().equals("abstract") || prop.getName().equals("index")) {
								boolean bool = Boolean.parseBoolean(value.toString());
								value = new StringBuilder("" + bool);
								object.getDataObject().setPropertyValue(prop.getName(),value);
							}
						}
					}
				} else if (prop instanceof MdlNumber) {
					StringBuilder value = object.getDataObject().getPropertyValue(prop.getName());
					if (value!=null) {
						// Model specific initial values for numbers
						if (value.length()==0 && (
							cls.getFullName().equals(MdlModel.PACKAGE_CLASS_FULL_NAME) ||
							cls.getFullName().equals(MdlModel.CLASS_CLASS_FULL_NAME) ||
							cls.getFullName().equals(MdlModel.STRING_CLASS_FULL_NAME) ||
							cls.getFullName().equals(MdlModel.NUMBER_CLASS_FULL_NAME) ||
							cls.getFullName().equals(MdlModel.LINK_CLASS_FULL_NAME) ||
							cls.getFullName().equals(MdlModel.UNIQUE_CONSTRAINT_CLASS_FULL_NAME)
							)) {
							if (prop.getName().equals("maxValue")) {
								value = new StringBuilder("9999");
							} else if (prop.getName().equals("maxLength")) {
								value = new StringBuilder("24");
							}
						}
						MdlNumber nmbr = (MdlNumber) prop;
						BigDecimal val = null;
						try {
							val = new BigDecimal(value.toString());
						} catch(NumberFormatException e) {
							ReqError error = addObjectError(object,ERROR_CODE_VALUE_NOT_A_NUMBER,"Value is not a number: " + prop.getFullName() + ": " + value);
							error.getProperties().add(nmbr.getFullName());
							ok = false;
							break;
						}
						if (ok) {
							if (val.compareTo(nmbr.getMinValue())<0) {
								object.getDataObject().setPropertyValue(prop.getName(),new StringBuilder(nmbr.getMinValue().toString()));
							} else if (val.compareTo(nmbr.getMaxValue())>0) {
								object.getDataObject().setPropertyValue(prop.getName(),new StringBuilder(nmbr.getMaxValue().toString()));
							}
						}
					}
				} else if (prop instanceof MdlLink) {
					MdlLink lnk = (MdlLink) prop;
					List<Long> idList = object.getDataObject().getLinkValue(lnk.getName());
					if (idList!=null) {
						if (idList.size()>lnk.getMaxSize()) {
							ReqError error = addObjectError(object,ERROR_CODE_LINK_VALUE_TOO_LARGE,"Link value is too large: " + lnk.getFullName() + " (" + idList.size() + ">" + lnk.getMaxSize() + ")");
							error.getProperties().add(lnk.getFullName());
							ok = false;
							break;
						}
					}
					// Model specific validations
					if (this instanceof ReqAdd || object.getDataObject().hasPropertyValue(prop.getName()) && (
						cls.getFullName().equals(MdlModel.PACKAGE_CLASS_FULL_NAME) ||
						cls.getFullName().equals(MdlModel.CLASS_CLASS_FULL_NAME) ||
						cls.getFullName().equals(MdlModel.STRING_CLASS_FULL_NAME) ||
						cls.getFullName().equals(MdlModel.NUMBER_CLASS_FULL_NAME) ||
						cls.getFullName().equals(MdlModel.LINK_CLASS_FULL_NAME) ||
						cls.getFullName().equals(MdlModel.UNIQUE_CONSTRAINT_CLASS_FULL_NAME)
						)) {
						if (idList==null || idList.size()==0) {
							if (prop.getName().equals("package") || 
								prop.getName().equals("class") || 
								prop.getName().equals("classTo") || 
								prop.getName().equals("classes")
								) {
								ReqError error = addObjectError(object,ERROR_CODE_VALUE_IS_MANDATORY,"Value is mandatory: " + prop.getFullName());
								error.getProperties().add(prop.getFullName());
								ok = false;
								break;
							}
						}
					}
				}
			}
			if (!ok) {
				break;
			}
		}
		return ok;
	}

	@Override
	public List<String> getClassNames() {
		List<String> classNames = super.getClassNames();
		MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(getClassName());
		for (MdlUniqueConstraint uc: cls.getUniqueConstraintList()) {
			for (String className: uc.getClasses()) {
				if (!classNames.contains(className)) {
					classNames.add(className);
				}
			}
		}
		return classNames;
	}
}

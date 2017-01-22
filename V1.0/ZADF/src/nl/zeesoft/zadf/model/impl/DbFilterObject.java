package nl.zeesoft.zadf.model.impl;

import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtString;

public abstract class DbFilterObject extends MdlDataObject {
	private DtIdRef			property			= new DtIdRef();
	private DtIdRef			operator			= new DtIdRef();
	private DtBoolean 		invert				= new DtBoolean(false);
	private DtString 		value				= new DtString();
	
	private String			stringValue			= "";
	
	/**
	 * @return the property
	 */
	@ConstrainPropertyMandatory
	@PersistReference(property = "property",label="Property",className="nl.zeesoft.zadf.model.impl.DbCollectionProperty",entity=false,entityLabel="Filters")
	public DtIdRef getProperty() {
		return property;
	}

	/**
	 * @return the operator
	 */
	@ConstrainPropertyMandatory
	@PersistReference(property = "operator",label="Operator",className="nl.zeesoft.zadf.model.impl.DbFilterOperator",entity=false,entityLabel="Filters")
	public DtIdRef getOperator() {
		return operator;
	}

	/**
	 * @return the invert
	 */
	@PersistProperty(property = "invert",label="Invert")
	public DtBoolean getInvert() {
		return invert;
	}
	
	/**
	 * @return the value
	 */
	@PersistProperty(property = "value",label="Value")
	public DtString getValue() {
		return value;
	}

	/**
	 * @return the prop
	 */
	public DbCollectionProperty getProp() {
		DbCollectionProperty prop = null;
		for (MdlDataObject obj: getReferencedObjects("property")) {
			prop = (DbCollectionProperty) obj;
			break;
		}
		return prop;
	}

	/**
	 * @return the oper
	 */
	public DbFilterOperator getOper() {
		DbFilterOperator oper = null;
		for (MdlDataObject obj: getReferencedObjects("operator")) {
			oper = (DbFilterOperator) obj;
			break;
		}
		return oper;
	}

	/**
	 * @return the stringValue
	 */
	public String getStringValue() {
		return stringValue;
	}

	/**
	 * @param stringValue the stringValue to set
	 */
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
}

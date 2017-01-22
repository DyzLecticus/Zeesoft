package nl.zeesoft.zodb.model.datatypes;

import java.math.BigDecimal;
import java.util.Date;

import nl.zeesoft.zodb.Generic;

/**
 * This class defines the default properties for all data types.
 * All data type must extend this class.
 */
public abstract class DtObject {
	public final static String		NULL_VALUE_STRING							= "[NULL]";

	public final static String		CONSTRAIN_PROPERTY_MANDATORY				= "mandatory";

	public final static String		CONSTRAIN_PASSWORD 							= "password";
	
	public final static String		CONSTRAIN_STRING_ALPHABETIC					= "alphabetic";
	public final static String		CONSTRAIN_STRING_ALPHABETIC_UNDERSCORE		= "alphabeticUnderscore";
	public final static String		CONSTRAIN_STRING_ALPHANUMERIC 				= "alphanumeric";
	public final static String		CONSTRAIN_STRING_ALPHANUMERIC_UNDERSCORE	= "alphanumericUnderscore";
	
	public final static String		CONSTRAIN_STRING_UPPER_CASE 				= "upperCase";
	public final static String		CONSTRAIN_STRING_LOWER_CASE 				= "lowerCase";
	
	public final static String		CONSTRAIN_STRING_NO_SPACE 					= "noSpace";

	public final static String		CONSTRAIN_DATETIME_FUTURE					= "future";
	public final static String		CONSTRAIN_DATETIME_PAST						= "past";

	public final static String[]	CONSTRAINTS									= {
		CONSTRAIN_PROPERTY_MANDATORY,
		
		CONSTRAIN_PASSWORD,
		
		CONSTRAIN_STRING_ALPHABETIC,
		CONSTRAIN_STRING_ALPHABETIC_UNDERSCORE,
		CONSTRAIN_STRING_ALPHANUMERIC,
		CONSTRAIN_STRING_ALPHANUMERIC_UNDERSCORE,
		
		CONSTRAIN_STRING_UPPER_CASE,
		CONSTRAIN_STRING_LOWER_CASE,
		
		CONSTRAIN_STRING_NO_SPACE,
		
		CONSTRAIN_DATETIME_FUTURE,
		CONSTRAIN_DATETIME_PAST
		};
	
	private Object 					value 										= null;
	
	public DtObject(Object value) {
		setValue(value);
	}
	
	@Override
	public String toString() {
		return toStringBuffer().toString();
	}
	
	/**
	 * Extend this method for custom data types.
	 * @return A string buffer representation of the value
	 */
	public StringBuffer toStringBuffer() {
		StringBuffer s = new StringBuffer();
		Object val = getValue();
		if (val!=null) {
			if (this instanceof DtDateTime) {
				Date d = (Date) val;
				s.append(d.getTime());
			} else {
				s.append(val);
			}
		} else {
			s.append(NULL_VALUE_STRING);
		}
		return s;
	}

	/**
	 * Extend this method for custom data types.
	 * @param stringValue A string buffer representation of the value
	 */
	public void fromString(StringBuffer stringValue) {
		Object v = null;
		if (
			(!Generic.stringBufferStartsWith(stringValue, NULL_VALUE_STRING)) || 
			((Generic.stringBufferStartsWith(stringValue, NULL_VALUE_STRING)) && (stringValue.length()!=NULL_VALUE_STRING.length()))
			) {
			if (stringValue==null) {
				stringValue = new StringBuffer();
			}
			if (this instanceof DtBoolean) {
				v = Boolean.parseBoolean(stringValue.toString());
			} else if (this instanceof DtDateTime) {
				Date d = new Date();
				d.setTime(Long.parseLong(stringValue.toString()));
				v = d;
			} else if (this instanceof DtDecimal) {
				v = new BigDecimal(stringValue.toString());
			} else if (this instanceof DtFloat) {
				v = Float.parseFloat(stringValue.toString());
			} else if (this instanceof DtInteger) {
				v = Integer.parseInt(stringValue.toString());
			} else if (this instanceof DtLong) {
				v = Long.parseLong(stringValue.toString());
			} else if (this instanceof DtString) {
				v = stringValue.toString();
			} else if (this instanceof DtStringBuffer) {
				v = stringValue;
			} else if (this instanceof DtIdRef) {
				v = Long.parseLong(stringValue.toString());
			} else if (this instanceof DtIdRefList) {
				v = DtIdRefList.parseDtIdRefList(stringValue.toString());
			}
		}
		setValue(v);
	}

	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Extend this method for custom data types.
	 * @return A copy of the data type object including a copy of the value
	 */
	public DtObject copyObject() {
		DtObject copy = (DtObject) Generic.instanceForName(this.getClass().getName());
		copy.setValue(this.getValue());
		return copy;
	}

	public static DtObject copy(DtObject original) {
		DtObject copy = (DtObject) Generic.instanceForName(original.getClass().getName());
		if (original instanceof DtBoolean) {
			copy.setValue(new Boolean((Boolean) original.getValue()));
		} else if (original instanceof DtDateTime) {
			if (original.getValue()!=null) {
				Date d = new Date();
				d.setTime(((Date) original.getValue()).getTime());
				copy.setValue(d);
			}
		} else if (original instanceof DtDecimal) {
			copy.setValue(new BigDecimal(((BigDecimal) original.getValue()).toString()));
		} else if (original instanceof DtFloat) {
			copy.setValue(new Float((Float) original.getValue()));
		} else if (original instanceof DtInteger) {
			copy.setValue(new Integer((Integer) original.getValue()));
		} else if (original instanceof DtLong) {
			copy.setValue(new Long((Long) original.getValue()));
		} else if (original instanceof DtString) {
			copy.setValue(new String((String) original.getValue()));
		} else if (original instanceof DtStringBuffer) {
			copy.setValue(new StringBuffer((StringBuffer) original.getValue()));
		} else if (original instanceof DtIdRef) {
			copy.setValue(new Long((Long) original.getValue()));
		} else if (original instanceof DtIdRefList) {
			copy.setValue(DtIdRefList.copy((DtIdRefList) original).getValue());
		} else {
			copy.setValue(original.copyObject().getValue());
		}
		return copy;
	}

	public boolean equals(Object obj) {
        return (
        	((this.getValue()==null) && (obj==null)) ||
        	((this.getValue()!=null) && (this.getValue().equals(obj))) ||
        	((obj!=null) && (obj instanceof DtObject) && (((DtObject)obj).getValue()==null) && (this.getValue()==null)) ||
        	((obj!=null) && (obj instanceof DtObject) && (((DtObject)obj).getValue()!=null) && (this.getValue()!=null) && (this.getValue().equals(((DtObject)obj).getValue()))) ||
        	((obj!=null) && (obj instanceof DtObject) && (((DtObject)obj).getValue()!=null) && (this.getValue()!=null) && (this.getValue() instanceof StringBuffer) && (((DtObject)obj).getValue() instanceof StringBuffer) && (this.getValue().toString().equals(((DtObject)obj).getValue().toString())))
        	);
    }

}

package nl.zeesoft.zodb.database.query;

import java.util.Date;

import nl.zeesoft.zodb.Generic;
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


public class QryFetchCondition {
	public static String OPERATOR_EQUALS					= "equals";
	public static String OPERATOR_CONTAINS 					= "contains";
	public static String OPERATOR_MATCHES 					= "matches";
	public static String OPERATOR_STARTSWITH 				= "startsWith";
	public static String OPERATOR_ENDSWITH 					= "endsWith";
	public static String OPERATOR_GREATER 					= "greater";
	public static String OPERATOR_GREATER_OR_EQUALS 		= "greaterOrEquals";
	public static String OPERATOR_LESS	 					= "less";
	public static String OPERATOR_LESS_OR_EQUALS 			= "lessOrEquals";
	
	public static String OPERATORS							=
		OPERATOR_EQUALS + Generic.SEP_STR +
		OPERATOR_CONTAINS + Generic.SEP_STR +
		OPERATOR_MATCHES + Generic.SEP_STR +
		OPERATOR_ENDSWITH + Generic.SEP_STR +
		OPERATOR_STARTSWITH + Generic.SEP_STR +
		OPERATOR_GREATER + Generic.SEP_STR +
		OPERATOR_GREATER_OR_EQUALS + Generic.SEP_STR +
		OPERATOR_LESS + Generic.SEP_STR +
		OPERATOR_LESS_OR_EQUALS;

	private String 		property 								= "";
	private boolean 	invert 									= false;
	private String 		operator 								= "";
	private DtObject 	value 									= null;
	
	public QryFetchCondition(String property, String operator, DtObject value) {
		this.property = property;
		this.operator = operator;
		this.value = value;
	}

	public QryFetchCondition(String property, boolean invert, String operator, DtObject value) {
		this.property = property;
		this.invert = invert;
		this.operator = operator;
		this.value = value;
	}

	public boolean valueValidForOperatorValue(DtObject objValue) {
		boolean r = false;
		
		if (objValue==null) {
			return r;
		}

		if (operator.equals(OPERATOR_EQUALS)) {
			if (objValue.getClass().equals(value.getClass())) {
				if (objValue.toString().equals(value.toString())) {
					r = true;
				}
			}
		} else if (operator.equals(OPERATOR_CONTAINS)) {
			if (
				(objValue instanceof DtIdRef) && 
				(value instanceof DtLong) 
				) {
				if (objValue.getValue().equals(value.getValue())) {
					r = true;
				}
			} else if ( 
				(objValue instanceof DtIdRefList) &&
				(value instanceof DtLong) 
				) {
				if (((DtLong) value).getValue().equals(0L)) {
					if (((DtIdRefList) objValue).getValue().size()==0) {
						r = true;
					}
				} else {
					for (long id: ((DtIdRefList) objValue).getValue()) {
						if (value.getValue().equals(id)) {
							r = true;
							break;
						}
					}
				}
			} else {
				if (objValue.toString().contains(value.toString())) {
					r = true;
				}
			}
		} else if (operator.equals(OPERATOR_MATCHES)) {
			if (objValue.toString().matches(value.toString())) {
				r = true;
			}
		} else if (operator.equals(OPERATOR_STARTSWITH)) {
			if (objValue.toString().startsWith(value.toString())) {
				r = true;
			}
		} else if (operator.equals(OPERATOR_ENDSWITH)) {
			if (objValue.toString().endsWith(value.toString())) {
				r = true;
			}
		} else if ( 
			(operator.equals(OPERATOR_GREATER)) ||
			(operator.equals(OPERATOR_GREATER_OR_EQUALS)) || 
			(operator.equals(OPERATOR_LESS)) ||
			(operator.equals(OPERATOR_LESS_OR_EQUALS)) 
			) {
			if (objValue.getClass().equals(value.getClass())) {
				if (value instanceof DtString) {
					String v = ((String) value.getValue());
					String o = ((String) objValue.getValue());
					if (operator.equals(OPERATOR_GREATER)) {
						if (o.length() > v.length()) {
							r = true;
						}
					} else if (operator.equals(OPERATOR_GREATER_OR_EQUALS)) {
						if (o.length() >= v.length()) {
							r = true;
						}
					} else if (operator.equals(OPERATOR_LESS)) {
						if (o.length() < v.length()) {
							r = true;
						}
					} else if (operator.equals(OPERATOR_LESS_OR_EQUALS)) {
						if (o.length() <= v.length()) {
							r = true;
						}
					}
				} else if (value instanceof DtStringBuffer) {
					StringBuffer v = ((StringBuffer) value.getValue());
					StringBuffer o = ((StringBuffer) objValue.getValue());
					if (operator.equals(OPERATOR_GREATER)) {
						if (o.length() > v.length()) {
							r = true;
						}
					} else if (operator.equals(OPERATOR_GREATER_OR_EQUALS)) {
						if (o.length() >= v.length()) {
							r = true;
						}
					} else if (operator.equals(OPERATOR_LESS)) {
						if (o.length() < v.length()) {
							r = true;
						}
					} else if (operator.equals(OPERATOR_LESS_OR_EQUALS)) {
						if (o.length() <= v.length()) {
							r = true;
						}
					}
				} else if (value instanceof DtDateTime) {
					if (
						(value.getValue()!=null) &&
						(objValue.getValue()!=null)
						) {
						long v = ((Date) value.getValue()).getTime();
						long o = ((Date) objValue.getValue()).getTime();
						if (operator.equals(OPERATOR_GREATER)) {
							if (o > v) {
								r = true;
							}
						} else if (operator.equals(OPERATOR_GREATER_OR_EQUALS)) {
							if (o >= v) {
								r = true;
							}
						} else if (operator.equals(OPERATOR_LESS)) {
							if (o < v) {
								r = true;
							}
						} else if (operator.equals(OPERATOR_LESS_OR_EQUALS)) {
							if (o <= v) {
								r = true;
							}
						}
					}
				} else if (value instanceof DtDecimal) {
					if (operator.equals(OPERATOR_GREATER)) {
						if ((((DtDecimal)objValue).getValue()).compareTo((((DtDecimal)value).getValue())) > 0) {
							r = true;
						}
					} else if (operator.equals(OPERATOR_GREATER_OR_EQUALS)) {
						if ((((DtDecimal)objValue).getValue()).compareTo((((DtDecimal)value).getValue())) >= 0) {
							r = true;
						}
					} else if (operator.equals(OPERATOR_LESS)) {
						if ((((DtDecimal)objValue).getValue()).compareTo((((DtDecimal)value).getValue())) < 0) {
							r = true;
						}
					} else if (operator.equals(OPERATOR_LESS_OR_EQUALS)) {
						if ((((DtDecimal)objValue).getValue()).compareTo((((DtDecimal)value).getValue())) <= 0) {
							r = true;
						}
					}
				} else if (value instanceof DtFloat) {
					if (operator.equals(OPERATOR_GREATER)) {
						if ((((DtFloat)objValue).getValue()) > (((DtFloat)value).getValue())) {
							r = true;
						}
					} else if (operator.equals(OPERATOR_GREATER_OR_EQUALS)) {
						if ((((DtFloat)objValue).getValue()) >= (((DtFloat)value).getValue())) {
							r = true;
						}
					} else if (operator.equals(OPERATOR_LESS)) {
						if ((((DtFloat)objValue).getValue()) < (((DtFloat)value).getValue())) {
							r = true;
						}
					} else if (operator.equals(OPERATOR_LESS_OR_EQUALS)) {
						if ((((DtFloat)objValue).getValue()) <= (((DtFloat)value).getValue())) {
							r = true;
						}
					}
				} else if (value instanceof DtInteger) {
					if (operator.equals(OPERATOR_GREATER)) {
						if ((((DtInteger)objValue).getValue()) > (((DtInteger)value).getValue())) {
							r = true;
						}
					} else if (operator.equals(OPERATOR_GREATER_OR_EQUALS)) {
						if ((((DtInteger)objValue).getValue()) >= (((DtInteger)value).getValue())) {
							r = true;
						}
					} else if (operator.equals(OPERATOR_LESS)) {
						if ((((DtInteger)objValue).getValue()) < (((DtInteger)value).getValue())) {
							r = true;
						}
					} else if (operator.equals(OPERATOR_LESS_OR_EQUALS)) {
						if ((((DtInteger)objValue).getValue()) <= (((DtInteger)value).getValue())) {
							r = true;
						}
					}
				} else if (value instanceof DtLong) {
					if (operator.equals(OPERATOR_GREATER)) {
						if ((((DtLong)objValue).getValue()) > (((DtLong)value).getValue())) {
							r = true;
						}
					} else if (operator.equals(OPERATOR_GREATER_OR_EQUALS)) {
						if ((((DtLong)objValue).getValue()) >= (((DtLong)value).getValue())) {
							r = true;
						}
					} else if (operator.equals(OPERATOR_LESS)) {
						if ((((DtLong)objValue).getValue()) < (((DtLong)value).getValue())) {
							r = true;
						}
					} else if (operator.equals(OPERATOR_LESS_OR_EQUALS)) {
						if ((((DtLong)objValue).getValue()) <= (((DtLong)value).getValue())) {
							r = true;
						}
					}
				}
			}
		}
		
		if (invert) {
			r = (!r);
		}
		
		return r;
	}
	
	@Override
	public String toString() {
		String not = "";
		if (invert) {
			not = "not ";
		}
		return property + " " + not + operator + " " + value;
	}
	
	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @return the value
	 */
	public DtObject getValue() {
		return value;
	}

	/**
	 * @return the invert
	 */
	public boolean isInvert() {
		return invert;
	}	
}

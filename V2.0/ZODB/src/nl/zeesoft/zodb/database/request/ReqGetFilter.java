package nl.zeesoft.zodb.database.request;

import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;


public class ReqGetFilter {
	public static final String 	EQUALS				= "equals";
	public static final String 	CONTAINS			= "contains";
	public static final String 	GREATER				= "greater";
	public static final String 	GREATER_OR_EQUALS	= "greaterOrEquals";
	public static final String 	LESS				= "less";
	public static final String 	LESS_OR_EQUALS		= "lessOrEquals";
	
	private String				property			= "";
	private boolean				invert				= false;
	private String				operator			= EQUALS;
	private String				value				= "";
	
	protected ReqGetFilter() {
		// Used by fromXML
	}

	public ReqGetFilter(String prop, String op,String val) {
		property = prop;
		operator = op;
		value = val;
	}

	public ReqGetFilter(String prop,boolean inv, String op,String val) {
		property = prop;
		operator = op;
		invert = inv;
		value = val;
	}
	
	protected ReqGetFilter copy() {
		return new ReqGetFilter(property,invert,operator,value);
	}

	protected XMLFile toXML() {
		XMLFile file = new XMLFile();
		file.setRootElement(new XMLElem("filter",null,null));
		new XMLElem("property",new StringBuilder(property),file.getRootElement());
		if (invert) {
			new XMLElem("invert",new StringBuilder("" + invert),file.getRootElement());
		}
		new XMLElem("operator",new StringBuilder(operator),file.getRootElement());
		XMLElem valElem = new XMLElem("value",new StringBuilder(value),file.getRootElement());
		valElem.setCData(true);
		return file;
	}

	protected void fromXML(XMLElem rootElem) {
		if (rootElem.getName().equals("filter")) {
			for (XMLElem cElem: rootElem.getChildren()) {
				if (cElem.getName().equals("property") && cElem.getValue()!=null && cElem.getValue().length()>0) {
					property = cElem.getValue().toString();
				}
				if (cElem.getName().equals("invert") && cElem.getValue()!=null && cElem.getValue().length()>0) {
					invert = Boolean.parseBoolean(cElem.getValue().toString());
				}
				if (cElem.getName().equals("operator") && cElem.getValue()!=null && cElem.getValue().length()>0) {
					operator = cElem.getValue().toString();
				}
				if (cElem.getName().equals("value") && cElem.getValue()!=null && cElem.getValue().length()>0) {
					value = cElem.getValue().toString();
				}
			}
		}
	}

	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator) {
		if (operator.equals(EQUALS) || 
			operator.equals(CONTAINS) ||
			operator.equals(GREATER) ||
			operator.equals(GREATER_OR_EQUALS) ||
			operator.equals(LESS) ||
			operator.equals(LESS_OR_EQUALS)
			) {
			this.operator = operator;
		}
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the invert
	 */
	public boolean isInvert() {
		return invert;
	}

	/**
	 * @param invert the invert to set
	 */
	public void setInvert(boolean invert) {
		this.invert = invert;
	}
}

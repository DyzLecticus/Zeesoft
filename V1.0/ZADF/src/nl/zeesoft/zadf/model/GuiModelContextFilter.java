package nl.zeesoft.zadf.model;

import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.model.datatypes.DtObject;

public final class GuiModelContextFilter extends QryFetchCondition {
	private boolean active 		= false;
	private String 	stringValue = "";

	public GuiModelContextFilter(boolean active, String property, boolean invert, String operator, DtObject value, String stringValue) {
		super(property, invert, operator, value);
		this.active = active;
		this.stringValue = stringValue;
	}
	
	@Override
	public String toString() {
		String act = "A";
		String not = "";
		if (isInvert()) {
			not = "not ";
		}
		if (!isActive()) {
			act = "I";
		}
		return act + ": " + getProperty() + " " + not + getOperator() + " " + getValue();
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
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

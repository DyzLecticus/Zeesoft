package nl.zeesoft.zdsm.database.model;

import nl.zeesoft.zodb.database.DbDataObject;

public class Process extends Task {
	public static final String	SELF_AUTHORIZE			= "SelfAuthorize";
	public static final String	SELF_AUTHORIZED			= "SelfAuthorized";
	
	private String 				className				= "";
	private StringBuilder		description				= new StringBuilder();
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("className")) {
			setClassName(obj.getPropertyValue("className").toString());
		}
		if (obj.hasPropertyValue("description")) {
			setDescription(obj.getPropertyValue("description"));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("className",new StringBuilder(getClassName()));
		r.setPropertyValue("description",new StringBuilder(getDescription()));
		return r;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the description
	 */
	public StringBuilder getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(StringBuilder description) {
		this.description = description;
	}
}

package nl.zeesoft.zids.database.model;

import nl.zeesoft.zodb.database.DbDataObject;

public class Human extends CognitiveEntity {
	private String		emailAddress	= "";
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("emailAddress")) {
			setEmailAddress(obj.getPropertyValue("emailAddress").toString());
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("emailAddress",new StringBuilder(getEmailAddress()));
		return r;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public static String getNameSingle(String typeSpecifier) {
		String r = "Human";
		if (typeSpecifier.equals("NED")) {
			r = "Mens";
		}
		return r;
	}
	
}

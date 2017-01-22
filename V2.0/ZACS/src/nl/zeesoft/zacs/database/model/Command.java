package nl.zeesoft.zacs.database.model;

import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class Command extends HlpObject {
	public final static String	ACTIVATE	= "Activate";
	public final static String	DEACTIVATE	= "Deactivate";
	public final static String	REACTIVATE	= "Reactivate";
	
	private String				code		= "";

	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("code")) {
			setCode(obj.getPropertyValue("code").toString());
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("code",new StringBuilder(getCode()));
		return r;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
}

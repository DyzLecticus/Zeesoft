package nl.zeesoft.zdsm.database.model;

import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public abstract class Task extends HlpObject  {
	private String 				name 					= "";
	private String 				domainNameContains		= "";
	private boolean				active					= true;
	private boolean				removeMe				= false;
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("name")) {
			setName(obj.getPropertyValue("name").toString());
		}
		if (obj.hasPropertyValue("domainNameContains")) {
			setDomainNameContains(obj.getPropertyValue("domainNameContains").toString());
		}
		if (obj.hasPropertyValue("active")) {
			setActive(Boolean.parseBoolean(obj.getPropertyValue("active").toString()));
		}
		if (obj.hasPropertyValue("removeMe")) {
			setRemoveMe(Boolean.parseBoolean(obj.getPropertyValue("removeMe").toString()));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("name",new StringBuilder(getName()));
		r.setPropertyValue("domainNameContains",new StringBuilder(getDomainNameContains()));
		r.setPropertyValue("active",new StringBuilder("" + isActive()));
		r.setPropertyValue("removeMe",new StringBuilder("" + isRemoveMe()));
		return r;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the removeMe
	 */
	public boolean isRemoveMe() {
		return removeMe;
	}

	/**
	 * @param removeMe the removeMe to set
	 */
	public void setRemoveMe(boolean removeMe) {
		this.removeMe = removeMe;
	}

	/**
	 * @return the domainNameContains
	 */
	public String getDomainNameContains() {
		return domainNameContains;
	}

	/**
	 * @param domainNameContains the domainNameContains to set
	 */
	public void setDomainNameContains(String domainNameContains) {
		this.domainNameContains = domainNameContains;
	}
}

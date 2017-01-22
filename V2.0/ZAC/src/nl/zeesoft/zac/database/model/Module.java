package nl.zeesoft.zac.database.model;

import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class Module extends HlpObject {
	private String 						name 					= "";
	private long						maxSequenceCount		= 1000;
	private int							maxSequenceDistance		= 8;
	private long						maxContextCount			= 2000;
	private boolean						active					= true;
	private boolean						removeMe				= false;
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("name")) {
			setName(obj.getPropertyValue("name").toString());
		}
		if (obj.hasPropertyValue("maxSequenceCount")) {
			setMaxSequenceCount(Long.parseLong(obj.getPropertyValue("maxSequenceCount").toString()));
		}
		if (obj.hasPropertyValue("maxSequenceDistance")) {
			setMaxSequenceDistance(Integer.parseInt(obj.getPropertyValue("maxSequenceDistance").toString()));
		}
		if (obj.hasPropertyValue("maxContextCount")) {
			setMaxContextCount(Long.parseLong(obj.getPropertyValue("maxContextCount").toString()));
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
		r.setPropertyValue("maxSequenceDistance",new StringBuilder("" + getMaxSequenceDistance()));
		r.setPropertyValue("maxSequenceCount",new StringBuilder("" + getMaxSequenceCount()));
		r.setPropertyValue("maxContextCount",new StringBuilder("" + getMaxContextCount()));
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
		return (active && !removeMe);
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
	 * @return the maxSequenceDistance
	 */
	public int getMaxSequenceDistance() {
		return maxSequenceDistance;
	}

	/**
	 * @param maxSequenceDistance the maxSequenceDistance to set
	 */
	public void setMaxSequenceDistance(int maxSequenceDistance) {
		if (maxSequenceDistance<2) {
			maxSequenceDistance = 2;
		} else if (maxSequenceDistance>99) {
			maxSequenceDistance = 99;
		}
		this.maxSequenceDistance = maxSequenceDistance;
	}

	/**
	 * @return the maxSequenceCount
	 */
	public long getMaxSequenceCount() {
		return maxSequenceCount;
	}

	/**
	 * @param maxSequenceCount the maxSequenceCount to set
	 */
	public void setMaxSequenceCount(long maxSequenceCount) {
		this.maxSequenceCount = maxSequenceCount;
	}

	/**
	 * @return the maxContextCount
	 */
	public long getMaxContextCount() {
		return maxContextCount;
	}

	/**
	 * @param maxContextCount the maxContextCount to set
	 */
	public void setMaxContextCount(long maxContextCount) {
		this.maxContextCount = maxContextCount;
	}
}

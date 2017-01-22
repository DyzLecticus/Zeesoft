package nl.zeesoft.zdsm.database.model;

import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public abstract class Log extends HlpObject  {
	private long 			dateTime		= 0;
	private long 			duration		= 0;
	private boolean			success			= false;

	private long 			domainId		= 0;
	private Domain 			domain			= null;

	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("dateTime")) {
			setDateTime(Long.parseLong(obj.getPropertyValue("dateTime").toString()));
		}
		if (obj.hasPropertyValue("duration")) {
			setDuration(Long.parseLong(obj.getPropertyValue("duration").toString()));
		}
		if (obj.hasPropertyValue("success")) {
			setSuccess(Boolean.parseBoolean(obj.getPropertyValue("success").toString()));
		}
		if (obj.hasPropertyValue("domain") && obj.getLinkValue("domain").size()>0) {
			setDomainId(obj.getLinkValue("domain").get(0));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("dateTime",new StringBuilder("" + getDateTime()));
		r.setPropertyValue("duration",new StringBuilder("" + getDuration()));
		r.setPropertyValue("success",new StringBuilder("" + isSuccess()));
		r.setLinkValue("domain",getDomainId());
		return r;
	}

	/**
	 * @return the dateTime
	 */
	public long getDateTime() {
		return dateTime;
	}

	/**
	 * @param dateTime the dateTime to set
	 */
	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}
	
	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the domainId
	 */
	public long getDomainId() {
		return domainId;
	}

	/**
	 * @param domainId the domainId to set
	 */
	public void setDomainId(long domainId) {
		this.domainId = domainId;
	}

	/**
	 * @return the domain
	 */
	public Domain getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(Domain domain) {
		this.domain = domain;
		if (domain!=null) {
			domainId = domain.getId();
		} else {
			domainId = 0;
		}
	}

	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}
}

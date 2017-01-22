package nl.zeesoft.zals.database.model;

import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public abstract class EnvironmentObject extends HlpObject {
	private int							posX					= 0;
	private int							posY					= 0;

	private Environment					environment				= null;
	private long						environmentId			= 0;

	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("posX")) {
			setPosX(Integer.parseInt(obj.getPropertyValue("posX").toString()));
		}
		if (obj.hasPropertyValue("posY")) {
			setPosY(Integer.parseInt(obj.getPropertyValue("posY").toString()));
		}
		if (obj.hasPropertyValue("environment") && obj.getLinkValue("environment").size()>0) {
			setEnvironmentId(obj.getLinkValue("environment").get(0));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("posX",new StringBuilder("" + getPosX()));
		r.setPropertyValue("posY",new StringBuilder("" + getPosY()));
		r.setLinkValue("environment",getEnvironmentId());
		return r;
	}

	/**
	 * @return the environment
	 */
	public Environment getEnvironment() {
		return environment;
	}

	/**
	 * @param environment the environment to set
	 */
	public void setEnvironment(Environment environment) {
		this.environment = environment;
		if (environment!=null) {
			environmentId = environment.getId();
		} else {
			environmentId = 0;
		}
	}

	/**
	 * @return the environmentId
	 */
	public long getEnvironmentId() {
		return environmentId;
	}

	/**
	 * @param environmentId the environmentId to set
	 */
	public void setEnvironmentId(long environmentId) {
		this.environmentId = environmentId;
	}

	/**
	 * @return the posX
	 */
	public int getPosX() {
		return posX;
	}

	/**
	 * @param posX the posX to set
	 */
	public void setPosX(int posX) {
		this.posX = posX;
	}

	/**
	 * @return the posY
	 */
	public int getPosY() {
		return posY;
	}

	/**
	 * @param posY the posY to set
	 */
	public void setPosY(int posY) {
		this.posY = posY;
	}
}

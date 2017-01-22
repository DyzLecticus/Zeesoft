package nl.zeesoft.zals.database.model;

import nl.zeesoft.zodb.database.DbDataObject;

public abstract class Organism extends EnvironmentObject {
	private int		energy			= 0;
	private long	dateTimeDied	= 0;
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("energy")) {
			setEnergy(Integer.parseInt(obj.getPropertyValue("energy").toString()));
		}
		if (obj.hasPropertyValue("dateTimeDied")) {
			setDateTimeDied(Long.parseLong(obj.getPropertyValue("dateTimeDied").toString()));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("energy",new StringBuilder("" + getEnergy()));
		r.setPropertyValue("dateTimeDied",new StringBuilder("" + getDateTimeDied()));
		return r;
	}

	/**
	 * @return the energy
	 */
	public int getEnergy() {
		return energy;
	}

	/**
	 * @param energy the energy to set
	 */
	public void setEnergy(int energy) {
		if (energy<0) {
			energy=0;
		}
		this.energy = energy;
	}

	/**
	 * @return the dateTimeDied
	 */
	public long getDateTimeDied() {
		return dateTimeDied;
	}

	/**
	 * @param dateTimeDied the dateTimeDied to set
	 */
	public void setDateTimeDied(long dateTimeDied) {
		this.dateTimeDied = dateTimeDied;
	}
}

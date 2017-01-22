package nl.zeesoft.zac.database.model;

import nl.zeesoft.zodb.database.DbDataObject;

public class SymbolLinkSequence extends SymbolLink {
	private long				distance				= 1;
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("distance")) {
			setDistance(Long.parseLong(obj.getPropertyValue("distance").toString()));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("distance",new StringBuilder("" + getDistance()));
		return r;
	}	

	/**
	 * @return the distance
	 */
	public long getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(long distance) {
		this.distance = distance;
	}
}

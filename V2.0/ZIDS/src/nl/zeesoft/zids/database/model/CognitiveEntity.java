package nl.zeesoft.zids.database.model;

import nl.zeesoft.zids.database.pattern.PtnDatabaseObjectInterface;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public abstract class CognitiveEntity extends HlpObject implements PtnDatabaseObjectInterface {
	private String		name		= "";
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("name")) {
			setName(obj.getPropertyValue("name").toString());
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("name",new StringBuilder(getName()));
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

	@Override
	public String getInstanceName() {
		return getName();
	}
}

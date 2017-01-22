package nl.zeesoft.zac.database.model;

import nl.zeesoft.zodb.database.DbDataObject;

public class SymbolLinkContext extends SymbolLink {
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		return r;
	}	
}

package nl.zeesoft.zdk.database;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.collection.PersistableObject;
import nl.zeesoft.zdk.collection.PersistableProperty;

@PersistableObject
public class IndexElement {
	@PersistableProperty
	protected Str		id			= null;
	@PersistableProperty
	protected int		blockNum	= 0;
}

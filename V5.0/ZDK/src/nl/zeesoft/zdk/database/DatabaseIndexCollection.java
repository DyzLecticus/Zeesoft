package nl.zeesoft.zdk.database;

import nl.zeesoft.zdk.collection.PartitionableCollection;

public class DatabaseIndexCollection extends PartitionableCollection {
	protected long getNextId() {
		return nextId;
	}
}

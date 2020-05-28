package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.database.DatabaseCollection;
import nl.zeesoft.zdk.database.DatabaseConfiguration;

public class DCTest extends DatabaseCollection {
	public DCTest(DatabaseConfiguration config) {
		super(config);
	}

	@Override
	public Str saveIndex(boolean force) {
		return super.saveIndex(force);
	}

	@Override
	public boolean triggerLoadIndex(boolean wait, int waitMs) {
		return super.triggerLoadIndex(wait, waitMs);
	}

	@Override
	public Str saveAllBlocks(boolean force) {
		return super.saveAllBlocks(force);
	}

	@Override
	public void loadAllBlocks(boolean wait, int waitMs) {
		super.loadAllBlocks(wait, waitMs);
	}
}

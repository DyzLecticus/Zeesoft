package nl.zeesoft.zdk.test.impl;

import java.util.List;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.database.DatabaseCollection;
import nl.zeesoft.zdk.database.DatabaseConfiguration;
import nl.zeesoft.zdk.thread.CodeRunner;

public class DCTest extends DatabaseCollection {
	public DCTest(DatabaseConfiguration config) {
		super(config);
	}

	@Override
	public Str saveIndex(boolean force, int minDiffMs) {
		return super.saveIndex(force, minDiffMs);
	}

	@Override
	public CodeRunner triggerLoadIndex() {
		return super.triggerLoadIndex();
	}

	@Override
	public Str saveAllBlocks(boolean force, int minDiffMs) {
		return super.saveAllBlocks(force, minDiffMs);
	}

	@Override
	public List<CodeRunner> triggerLoadAllBlocks() {
		return super.triggerLoadAllBlocks();
	}
}

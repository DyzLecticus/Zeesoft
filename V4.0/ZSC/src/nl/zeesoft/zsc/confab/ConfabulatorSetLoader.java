package nl.zeesoft.zsc.confab;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.InitializerDatabaseObject;
import nl.zeesoft.zodb.db.InitializerObject;
import nl.zeesoft.zsc.mod.ModZSC;

public class ConfabulatorSetLoader extends InitializerObject {
	private ConfabulatorSet		confabulatorSet		= null;

	public ConfabulatorSetLoader(Config config) {
		super(config,ModZSC.NAME + "/Configuration/");
	}

	public void setConfabulatorSet(ConfabulatorSet confabulatorSet) {
		this.confabulatorSet = confabulatorSet;
	}
	
	@Override
	protected void initializeDatabaseObjectsNoLock() {
		if (confabulatorSet.getConfabulators().size()==0) {
			confabulatorSet.initialize();
		}
		addObjectNoLock(confabulatorSet);
	}

	@Override
	protected InitializerDatabaseObject getNewObjectNoLock(String name) {
		return new ConfabulatorSet(getConfiguration());
	}

	public ConfabulatorSet getConfabulatorSet() {
		ConfabulatorSet r = null;
		InitializerDatabaseObject obj = getObjectByNameNoLock("ConfabulatorSet");
		if (obj!=null) {
			r = (ConfabulatorSet) obj;
		}
		return r;
	}
}

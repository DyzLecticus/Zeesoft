package nl.zeesoft.zsc.confab;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.InitializerDatabaseObject;
import nl.zeesoft.zodb.db.InitializerObject;
import nl.zeesoft.zsc.mod.ModZSC;

public class ConfabulatorTrainer extends InitializerObject {
	private ConfabulatorSet		confabulatorSet		= null;
	private String				name				= "";

	public ConfabulatorTrainer(Config config,ConfabulatorSet confabSet,String name) {
		super(config,ModZSC.NAME + "/TrainingSets/" + name);
		confabulatorSet = confabSet;
		this.name = name;
	}

	protected String getName() {
		return name;
	}
	
	@Override
	protected void initializeDatabaseObjectsNoLock() {
		// Not used
	}

	@Override
	protected InitializerDatabaseObject getNewObjectNoLock(String name) {
		TrainingSet ts = new TrainingSet();
		ts.setName(name);
		return ts;
	}

	@Override
	protected void loadedObjectNoLock(InitializerDatabaseObject object) {
		confabulatorSet.trainAndReplaceConfabulator((TrainingSet) object);
	}
}

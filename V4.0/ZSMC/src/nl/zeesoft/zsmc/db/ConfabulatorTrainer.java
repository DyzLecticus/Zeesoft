package nl.zeesoft.zsmc.db;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.InitializerDatabaseObject;
import nl.zeesoft.zodb.db.InitializerObject;
import nl.zeesoft.zsmc.mod.ModZSMC;

public class ConfabulatorTrainer extends InitializerObject {
	private ConfabulatorSet		confabulatorSet		= null;
	private String				name				= "";

	public ConfabulatorTrainer(Config config,ConfabulatorSet confabSet,String name) {
		super(config,ModZSMC.NAME + "/TrainingSets/" + name);
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
	protected void loadedObject(InitializerDatabaseObject object) {
		confabulatorSet.trainAndReplaceConfabulator((TrainingSet) object);
	}
}

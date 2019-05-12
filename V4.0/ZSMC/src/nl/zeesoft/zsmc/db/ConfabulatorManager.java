package nl.zeesoft.zsmc.db;

import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.InitializerDatabaseObject;
import nl.zeesoft.zodb.db.InitializerObject;
import nl.zeesoft.zodb.db.StateListener;
import nl.zeesoft.zsmc.mod.ModZSMC;

public class ConfabulatorManager extends InitializerObject implements StateListener {
	private ConfabulatorSet							confabulatorSet		= null;
	
	private SortedMap<String,ConfabulatorTrainer>	trainers			= new TreeMap<String,ConfabulatorTrainer>();

	public ConfabulatorManager(Config config) {
		super(config,ModZSMC.NAME + "/TrainingSets/");
		setTimeoutSeconds(60);
	}

	public void setConfabulatorSet(ConfabulatorSet confabulatorSet) {
		lockMe(this);
		this.confabulatorSet = confabulatorSet;
		unlockMe(this);
	}
	
	public ConfabulatorSet getConfabulatorSet() {
		ConfabulatorSet r = null;
		lockMe(this);
		r = confabulatorSet;
		unlockMe(this);
		return r;
	}
	
	public KnowledgeBaseConfabulator getConfabulator(String name) {
		KnowledgeBaseConfabulator r = null;
		lockMe(this);
		r = confabulatorSet.getConfabulator(name);
		unlockMe(this);
		return r;
	}

	public boolean retrainConfabulator(String name) {
		boolean r = false;
		lockMe(this);
		ConfabulatorTrainer trainer = trainers.get(name);
		KnowledgeBaseConfabulator conf = confabulatorSet.getConfabulator(name);
		if (trainer==null && conf!=null) {
			trainer = new ConfabulatorTrainer(getConfiguration(),confabulatorSet,name);
			trainer.addListener(this);
			trainers.put(name,trainer);
			trainer.initialize();
			r = true;
		}
		unlockMe(this);
		return r;
	}
	
	@Override
	public void stateChanged(Object source, boolean open) {
		if (open && source instanceof ConfabulatorTrainer) {
			lockMe(this);
			ConfabulatorTrainer trainer = (ConfabulatorTrainer) source;
			trainers.remove(trainer.getName());
			trainer.destroy();
			unlockMe(this);
		}
	}

	@Override
	protected void initializeDatabaseObjectsNoLock() {
		if (confabulatorSet.getConfabulators().size()==0) {
			confabulatorSet.initialize();
		}
		for (KnowledgeBaseConfabulator conf: confabulatorSet.getConfabulators()) {
			addObjectNoLock(conf.getTrainingSet());
			confabulatorSet.trainAndReplaceConfabulator(conf.getTrainingSet());
		}
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
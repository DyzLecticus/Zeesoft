package nl.zeesoft.zsmc.db;

import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zsmc.confab.Confabulator;
import nl.zeesoft.zsmc.kb.KnowledgeBase;

public class KnowledgeBaseConfabulator extends Locker {
	private Config							configuration		= null;
	private String							name				= "";
	private int								maxDistance			= 4;
	
	private Confabulator					confabulator		= null;
	private KnowledgeBase					knowledgeBase		= null;
	private TrainingSet						trainingSet			= null;
	
	public KnowledgeBaseConfabulator(Config config,String name,int maxDistance) {
		super(config.getMessenger());
		this.configuration = config;
		this.name = name;
		this.maxDistance = maxDistance;
	}
	
	public String getName() {
		String r = "";
		lockMe(this);
		r = name;
		unlockMe(this);
		return r;
	}
	
	public void setName(String name) {
		lockMe(this);
		this.name = name;
		unlockMe(this);
	}

	public int getMaxDistance() {
		int r = 0;
		lockMe(this);
		r = maxDistance;
		unlockMe(this);
		return r;
	}

	public void setMaxDistance(int maxDistance) {
		lockMe(this);
		this.maxDistance = maxDistance;
		unlockMe(this);
	}
	
	public void setTrainingSet(TrainingSet trainingSet) {
		lockMe(this);
		this.trainingSet = trainingSet;
		unlockMe(this);
	}
	
	public void initializeFromTrainingSet() {
		lockMe(this);
		if (trainingSet!=null) {
			name = trainingSet.getName();
			knowledgeBase = new KnowledgeBase(configuration.getMessenger(),maxDistance,false);
			for (TrainingSequence seq: trainingSet.getSequences()) {
				knowledgeBase.learnSequence(seq.sequence, seq.context);
			}
			knowledgeBase.calculateProbabilities();
			confabulator = new Confabulator(configuration.getMessenger(),configuration.getUnion(),knowledgeBase);
		}
		unlockMe(this);
	}
	
	public TrainingSet getTrainingSet() {
		TrainingSet r = null;
		lockMe(this);
		r = trainingSet;
		unlockMe(this);
		return r;
	}
	
	public KnowledgeBase getKnowledgeBase() {
		KnowledgeBase r = null;
		lockMe(this);
		r = knowledgeBase;
		unlockMe(this);
		return r;
	}
	
	public Confabulator getConfabulator() {
		Confabulator r = null;
		lockMe(this);
		r = confabulator;
		unlockMe(this);
		return r;
	}
}

package nl.zeesoft.zsc.confab;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.InitializerDatabaseObject;
import nl.zeesoft.zsc.mod.ModZSC;

public class ConfabulatorSet extends Locker implements InitializerDatabaseObject {
	public static final String	TEST_CONFAB_1	= "SelfTestConfabulator1";
	public static final String	TEST_CONFAB_2	= "SelfTestConfabulator2";
	
	private Config				configuration	= null;
	private List<Confabulator>	confabulators	= new ArrayList<Confabulator>();
	private List<TrainingSet>	trainingSets	= new ArrayList<TrainingSet>();
	
	public ConfabulatorSet(Config config) {
		super(config.getMessenger());
		this.configuration = config;
	}

	public void initialize() {
		initializeConfabulators();
		for (Confabulator conf: confabulators) {
			initializeTrainingSetForConfabulator(conf.getName());
		}
	}

	@Override
	public String getObjectName() {
		return "ConfabulatorSet";
	}
	
	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem confsElem = new JsElem("confabulators",true);
		json.rootElement.children.add(confsElem);
		for (Confabulator conf: confabulators) {
			JsElem confElem = new JsElem();
			confsElem.children.add(confElem);
			confElem.children.add(new JsElem("name",conf.getName(),true));
			confElem.children.add(new JsElem("maxDistance","" + conf.getMaxDistance()));
		}
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			confabulators.clear();
			JsElem confsElem = json.rootElement.getChildByName("confabulators");
			for (JsElem confElem: confsElem.children) {
				String name = confElem.getChildString("name","");
				int maxDistance = confElem.getChildInt("maxDistance",4);
				if (name.length()>0) {
					confabulators.add(new Confabulator(configuration,name,maxDistance));
				}
			}
		}
	}

	public void addConfabulator(String name) {
		addConfabulator(name,4);
	}

	public void addConfabulator(String name, int maxDistance) {
		if (maxDistance<=0) {
			maxDistance = 1;
		}
		lockMe(this);
		Confabulator conf = getConfabulatorNoLock(name);
		if (conf==null) {
			confabulators.add(new Confabulator(configuration,name,maxDistance));
		}
		unlockMe(this);
	}
	
	public List<Confabulator> getConfabulators() {
		List<Confabulator> r = null;
		lockMe(this);
		r = new ArrayList<Confabulator>(confabulators);
		unlockMe(this);
		return r;
	}
	
	public Confabulator getConfabulator(String name) {
		Confabulator r = null;
		lockMe(this);
		r = getConfabulatorNoLock(name);
		unlockMe(this);
		return r;
	}

	protected void addTrainingSet(TrainingSet ts) {
		TrainingSet tse = getTrainingSet(ts.getName());
		if (tse==null) {
			trainingSets.add(ts);
		}
	}
	
	protected List<TrainingSet> getTrainingSets() {
		return new ArrayList<TrainingSet>(trainingSets);
	}
	
	protected TrainingSet getTrainingSet(String name) {
		TrainingSet r = null;
		for (TrainingSet ts: trainingSets) {
			if (ts.getName().equals(name)) {
				r = ts;
				break;
			}
		}
		return r;
	}
	
	protected void trainConfabulator(TrainingSet ts) {
		trainConfabulator(null,ts);
	}
		
	protected void trainConfabulator(Confabulator conf,TrainingSet ts) {
		if (conf==null) {
			conf = getConfabulator(ts.getName());
		}
		if (conf!=null) {
			if (ts.getSequences().size()>0) {
				configuration.debug(this,"Training confabulator: " + conf.getName()+ ", sequences: " + ts.getSequences().size() + " ...");
				for (TrainingSequence seq: ts.getSequences()) {
					conf.learnSequence(seq.sequence,seq.context);
				}
				conf.calculateProbabilities();
				configuration.debug(this,"Trained confabulator: " + conf.getName()+ ", sequences: " + ts.getSequences().size());
			}
		}
	}
	
	protected void trainAndReplaceConfabulator(TrainingSet ts) {
		Confabulator conf = getConfabulator(ts.getName());
		if (conf!=null) {
			Confabulator repl = new Confabulator(configuration,conf.getName(),conf.getMaxDistance());
			trainConfabulator(repl,ts);
			
			lockMe(this);
			int index = confabulators.indexOf(conf);
			if (index>=0) {
				confabulators.remove(index);
				confabulators.add(index,repl);
			} else {
				confabulators.add(repl);
			}
			unlockMe(this);
		}
	}
	
	protected void initializeConfabulators() {
		if (configuration.getModule(ModZSC.NAME).selfTest) {
			addConfabulator(TEST_CONFAB_1);
			addConfabulator(TEST_CONFAB_2);
		}
	}
	
	protected void initializeTrainingSetForConfabulator(String name) {
		TrainingSet ts = new TrainingSet();
		ts.setName(name);
		if (name.equals(TEST_CONFAB_1)) {
			ts.addSequence("My name is Dyz Lecticus.","Name");
			ts.addSequence("My goal is to understand and help humans.","Goal");
			ts.addSequence("I am an artificially intelligent virtual agent.","SelfDescription");
			ts.addSequence("I can learn context sensitive symbol sequences.","SelfDescription");
			ts.addSequence("I can then use what I have learned to correct and determine context for similar sequences.","SelfDescription");
		} else if (name.equals(TEST_CONFAB_2)) {
			ts.addSequence("Limited data confabulator","Test");
		}
		addTrainingSet(ts);
	}

	protected Confabulator getConfabulatorNoLock(String name) {
		Confabulator r = null;
		for (Confabulator conf: confabulators) {
			if (conf.getName().equals(name)) {
				r = conf;
				break;
			}
		}
		return r;
	}

}

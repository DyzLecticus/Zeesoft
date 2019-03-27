package nl.zeesoft.zsmc.db;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.InitializerDatabaseObject;
import nl.zeesoft.zsmc.mod.ModZSMC;

public class ConfabulatorSet extends Locker implements InitializerDatabaseObject {
	public static final String				CONFABULATOR_SET	= "ConfabulatorSet";
	public static final String				TEST_CONFAB_1		= "SelfTestConfabulator1";
	public static final String				TEST_CONFAB_2		= "SelfTestConfabulator2";
	
	private Config							configuration		= null;
	private List<KnowledgeBaseConfabulator>	confabulators		= new ArrayList<KnowledgeBaseConfabulator>();
	
	public ConfabulatorSet(Config config) {
		super(config.getMessenger());
		this.configuration = config;
	}

	public void initialize() {
		initializeConfabulators();
		for (KnowledgeBaseConfabulator conf: confabulators) {
			initializeTrainingSetForConfabulator(conf.getName());
		}
	}

	@Override
	public String getObjectName() {
		return CONFABULATOR_SET;
	}
	
	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem confsElem = new JsElem("confabulators",true);
		json.rootElement.children.add(confsElem);
		for (KnowledgeBaseConfabulator conf: confabulators) {
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
					confabulators.add(new KnowledgeBaseConfabulator(configuration,name,maxDistance));
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
		KnowledgeBaseConfabulator conf = getConfabulatorNoLock(name);
		if (conf==null) {
			confabulators.add(new KnowledgeBaseConfabulator(configuration,name,maxDistance));
		}
		unlockMe(this);
	}
	
	public List<KnowledgeBaseConfabulator> getConfabulators() {
		List<KnowledgeBaseConfabulator> r = null;
		lockMe(this);
		r = new ArrayList<KnowledgeBaseConfabulator>(confabulators);
		unlockMe(this);
		return r;
	}
	
	public KnowledgeBaseConfabulator getConfabulator(String name) {
		KnowledgeBaseConfabulator r = null;
		lockMe(this);
		r = getConfabulatorNoLock(name);
		unlockMe(this);
		return r;
	}

	protected void trainAndReplaceConfabulator(TrainingSet ts) {
		KnowledgeBaseConfabulator conf = getConfabulator(ts.getName());
		if (conf!=null) {
			KnowledgeBaseConfabulator repl = new KnowledgeBaseConfabulator(configuration,conf.getName(),conf.getMaxDistance());
			repl.setTrainingSet(ts);
			repl.initializeFromTrainingSet();
			
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
		if (configuration.getModule(ModZSMC.NAME).selfTest) {
			addConfabulator(TEST_CONFAB_1);
			addConfabulator(TEST_CONFAB_2);
		}
	}
	
	protected void initializeTrainingSetForConfabulator(String name) {
		KnowledgeBaseConfabulator conf = getConfabulator(name);
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
		conf.setTrainingSet(ts);
		conf.initializeFromTrainingSet();
	}

	protected KnowledgeBaseConfabulator getConfabulatorNoLock(String name) {
		KnowledgeBaseConfabulator r = null;
		for (KnowledgeBaseConfabulator conf: confabulators) {
			if (conf.getName().equals(name)) {
				r = conf;
				break;
			}
		}
		return r;
	}

}

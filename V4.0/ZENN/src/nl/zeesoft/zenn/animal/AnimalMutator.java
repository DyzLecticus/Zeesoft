package nl.zeesoft.zenn.animal;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.genetic.Evolver;
import nl.zeesoft.zdk.genetic.EvolverUnit;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zenn.environment.Animal;
import nl.zeesoft.zenn.simulator.SimulatorAnimal;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseResponse;
import nl.zeesoft.zodb.db.init.Persistable;

public class AnimalMutator extends Evolver implements Persistable, JsClientListener {
	private Config				configuration		= null;
	private boolean				herbivore			= true;
	private AnimalEvolver		evolver				= null;
	
	private int					topScore			= 0;
	private SimulatorAnimal		topScoringAnimal	= null;
	
	private boolean				loaded				= false;
	private	long				id					= 0;
	
	public AnimalMutator(Config config,boolean herbivore,AnimalEvolver evolver) {
		super(
			config.getMessenger(),config.getUnion(),
			AnimalConstants.MAX_LAYERS,AnimalConstants.MAX_NEURONS,
			100,AnimalTestSet.getNewAnimalTestSet(herbivore),1);
		this.configuration = config;
		this.herbivore = herbivore;
		this.evolver = evolver;
		setSleepMs(100);
		setSleepMsFoundBest(100);
	}
	
	public AnimalMutator(Messenger msgr, WorkerUnion uni,boolean herbivore) {
		super(
			msgr,uni,
			AnimalConstants.MAX_LAYERS,AnimalConstants.MAX_NEURONS,
			100,AnimalTestSet.getNewAnimalTestSet(herbivore),1);
		this.herbivore = herbivore;
		setSleepMs(100);
		setSleepMsFoundBest(100);
	}

	public boolean checkBest(Animal ani,SimulatorAnimal simAni) {
		boolean r = false;
		EvolverUnit unit1 = null;
		EvolverUnit unit2 = null;
		lockMe(this);
		if (loaded && ani.score > topScore) {
			topScore = ani.score;
			topScoringAnimal = simAni.copy();
			unit1 = topScoringAnimal.unit.copy();
			unit2 = topScoringAnimal.unit.copy();
			r = true;
		}
		unlockMe(this);
		if (r) {
			setBestSoFar(unit1);
			if (evolver!=null) {
				evolver.setBestSoFarIfBetter(unit2);
			}
			save();
			if (!isWorking()) {
				start();
			}
		}
		return r;
	}
	
	public void resetTopScoringAnimal() {
		lockMe(this);
		topScore = 0;
		topScoringAnimal = null;
		unlockMe(this);
		if (isWorking()) {
			stop();
			whileStopping();
		}
	}

	public SimulatorAnimal getTopScoringAnimal() {
		SimulatorAnimal r = null;
		lockMe(this);
		if (topScoringAnimal!=null) {
			r = topScoringAnimal.copy();
		}
		unlockMe(this);
		return r;
	}

	@Override
	public ZStringBuilder getObjectName() {
		return new ZStringBuilder(ZStringBuilder.upperCaseFirst(getType()));
	}
	
	@Override
	public void handledRequest(JsClientResponse response) {
		DatabaseResponse res = configuration.handledDatabaseRequest(response);
		if (response.error.length()>0) {
			configuration.error(this,response.error.toString(),response.ex);
		}
		if (res!=null) {
			if (res.request.type.equals(DatabaseRequest.TYPE_GET)) {
				if (res.results.size()>0 && res.results.get(0).object!=null) {
					lockMe(this);
					id = res.results.get(0).id;
					unlockMe(this);
					fromJson(res.results.get(0).object);
				}
				lockMe(this);
				loaded = true;
				SimulatorAnimal tsa = topScoringAnimal;
				unlockMe(this);
				EvolverUnit bsf = getBestSoFar();
				if (bsf!=null && tsa!=null && !isWorking() && 
					tsa.unit.geneticNN.code.getCode().equals(bsf.geneticNN.code.getCode())) {
					start();
				}
			} else if (res.request.type.equals(DatabaseRequest.TYPE_ADD)) {
				load();
			} 
		}
	}
	
	@Override
	public JsFile toJson() {
		JsFile json = super.toJson();
		lockMe(this);
		json.rootElement.children.add(0,new JsElem("topScore","" + topScore));
		if (topScoringAnimal!=null) {
			JsElem topElem = new JsElem("topScoringAnimal",true);
			json.rootElement.children.add(topElem);
			topElem.children.add(topScoringAnimal.toJson().rootElement);
		}
		unlockMe(this);
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		super.fromJson(json);
		if (json.rootElement!=null) {
			lockMe(this);
			topScore = json.rootElement.getChildInt("topScore",topScore);
			JsElem topElem = json.rootElement.getChildByName("topScoringAnimal");
			if (topElem!=null && topElem.children.size()>0) {
				JsFile js = new JsFile();
				js.rootElement = topElem.children.get(0);
				topScoringAnimal = new SimulatorAnimal();
				topScoringAnimal.fromJson(js);
			}
			unlockMe(this);
		}
	}
	
	public void load() {
		if (configuration!=null) {
			configuration.getObject(new ZStringBuilder("ZENN/Mutators/" + getObjectName()),this,10);
		}
	}
	
	protected void save() {
		if (configuration!=null) {
			long objId = 0;
			lockMe(this);
			objId = id;
			unlockMe(this);
			if (objId>0) {
				configuration.setObject(objId,this,this);
			} else {
				configuration.addObject(this,new ZStringBuilder("ZENN/Mutators/" + getObjectName()),this);
			}
		}
	}
	
	@Override
	protected void selectedBest() {
		stop();
		whileStopping();
		save();
	}
	
	protected String getType() {
		String r = "herbivore";
		if (!herbivore) {
			r = "carnivore";
		}
		return r;
	}
}

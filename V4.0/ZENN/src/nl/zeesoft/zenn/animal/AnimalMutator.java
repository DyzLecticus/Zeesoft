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
import nl.zeesoft.zenn.environment.EnvironmentConfig;
import nl.zeesoft.zenn.simulator.Simulator;
import nl.zeesoft.zenn.simulator.SimulatorAnimal;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseResponse;
import nl.zeesoft.zodb.db.init.Persistable;

public class AnimalMutator extends Evolver implements Persistable, JsClientListener {
	private Config				configuration		= null;
	private boolean				herbivore			= true;
	private AnimalEvolver		evolver				= null;
	
	private int					minTopScore			= 0;
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
		setSleepMs(200);
		setSleepMsFoundBest(200);
	}
	
	public AnimalMutator(Messenger msgr, WorkerUnion uni,boolean herbivore) {
		super(
			msgr,uni,
			AnimalConstants.MAX_LAYERS,AnimalConstants.MAX_NEURONS,
			100,AnimalTestSet.getNewAnimalTestSet(herbivore),1);
		this.herbivore = herbivore;
		setSleepMs(200);
		setSleepMsFoundBest(200);
	}
	
	public void setEnvironmentConfig(EnvironmentConfig environment) {
		lockMe(this);
		minTopScore = environment.minTopScore;
		unlockMe(this);
	}
	
	public boolean checkBest(Animal ani,SimulatorAnimal simAni) {
		boolean r = false;
		EvolverUnit unit1 = null;
		EvolverUnit unit2 = null;
		boolean save = false;
		lockMe(this);
		if (loaded) {
			if (ani.score > topScore) {
				topScore = ani.score;
				topScoringAnimal = simAni.copy();
				unit1 = topScoringAnimal.unit.copy();
				unit2 = topScoringAnimal.unit.copy();
				r = true;
			} else if (topScore>minTopScore && topScoringAnimal!=null && simAni.unit.compareTo(topScoringAnimal.unit)>0) {
				if (topScore>(minTopScore * 1.5F)) {
					topScore = topScore - Math.round((topScore - 200) * 0.90F);
					if (topScore <= ani.score) {
						topScore = ani.score + 1;
					}
				} else {
					topScore--;
				}
				save = true;
			}
		}
		unlockMe(this);
		if (r) {
			if (evolver!=null) {
				evolver.setBestSoFarIfBetter(unit1);
			}
			EvolverUnit unit = getBestSoFar();
			if (unit==null || !unit.geneticNN.code.getCode().equals(unit2.geneticNN.code.getCode())) {
				setBestSoFar(unit2);
				if (!isWorking()) {
					start();
				}
			}
			save();
		} else if (save) {
			save();
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
		setBestSoFar(null);
		save();
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
	
	public int getTopScore() {
		int r = 0;
		lockMe(this);
		if (topScoringAnimal!=null) {
			r = topScore;
		}
		unlockMe(this);
		return r;
	}

	public ZStringBuilder getTopScoringAnimalSummary() {
		ZStringBuilder r = new ZStringBuilder();
		lockMe(this);
		if (topScoringAnimal!=null) {
			r = Simulator.formatUnitSummary(topScoringAnimal.unit);
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
			configuration.getObject(new ZStringBuilder("ZENN/Mutators/" + getObjectName()),this,30);
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
		if (evolver!=null) {
			EvolverUnit unit = getBestSoFar();
			if (unit!=null) {
				evolver.setBestSoFarIfBetter(unit);
			}
		}
	}
	
	protected String getType() {
		String r = "herbivore";
		if (!herbivore) {
			r = "carnivore";
		}
		return r;
	}
}

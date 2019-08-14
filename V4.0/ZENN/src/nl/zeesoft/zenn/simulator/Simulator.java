package nl.zeesoft.zenn.simulator;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.genetic.EvolverUnit;
import nl.zeesoft.zdk.neural.Prediction;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zenn.animal.CarnivoreEvolver;
import nl.zeesoft.zenn.animal.HerbivoreEvolver;
import nl.zeesoft.zenn.environment.Animal;
import nl.zeesoft.zenn.environment.EnvironmentConfig;
import nl.zeesoft.zenn.environment.EnvironmentState;
import nl.zeesoft.zodb.Config;

public class Simulator extends Locker {
	private WorkerUnion					union				= null;
	private EnvironmentInitializer		envInitializer		= null;
	private HerbivoreEvolver			herbEvolver			= null;
	private CarnivoreEvolver			carnEvolver			= null;
	private SimulatorWorker				worker				= null;
	private HistoryManager				histManager			= null;

	private EnvironmentConfig			environmentConfig	= null;
	private EnvironmentState			environmentState	= null;

	private List<SimulatorAnimalWorker>	animalWorkers		= new ArrayList<SimulatorAnimalWorker>();
	private boolean						working				= false;
	
	private long						energyUpdated		= 0;
	
	public Simulator(Config config,EnvironmentInitializer envInit,HerbivoreEvolver herbEvo,CarnivoreEvolver carnEvo) {
		super(config.getMessenger());
		union = config.getUnion();
		envInitializer = envInit;
		herbEvolver = herbEvo;
		carnEvolver = carnEvo;
		worker = new SimulatorWorker(config.getMessenger(),config.getUnion(),this);
		histManager = new HistoryManager(config);
	}
	
	public void setEnvironment(EnvironmentConfig environmentConfig,EnvironmentState environmentState) {
		lockMe(this);
		this.environmentConfig = environmentConfig;
		this.environmentState = environmentState;
		worker.setEnvironmentConfig(environmentConfig);
		histManager.setEnvironment(environmentConfig,environmentState);
		unlockMe(this);
	}
	
	public void start() {
		lockMe(this);
		if (environmentConfig!=null && environmentState!=null && !working) {
			if (environmentState.prepareForStart()) {
				if (envInitializer!=null) {
					envInitializer.updatedState();
				}
			}
			long now = System.currentTimeMillis();
			energyUpdated = now;
			
			worker.start();
			working = true;
			
			animalWorkers.clear();
			for (Animal ani: environmentState.getAnimals()) {
				animalWorkers.add(new SimulatorAnimalWorker(getMessenger(),union,this,ani,environmentConfig));
			}
			for (SimulatorAnimalWorker animalWorker: animalWorkers) {
				animalWorker.start();
			}
			
			if (getMessenger()!=null) {
				getMessenger().debug(this,"Started simulator");
			}
		}
		unlockMe(this);
	}
	
	public void stop() {
		boolean stop = false;
		lockMe(this);
		if (working) {
			for (SimulatorAnimalWorker animalWorker: animalWorkers) {
				animalWorker.stop();
			}
			animalWorkers.clear();
			working = false;
			stop = true;
		}
		unlockMe(this);
		if (stop) {
			worker.stop();
			if (getMessenger()!=null) {
				getMessenger().debug(this,"Stopped simulator");
			}
		}
	}
	
	public boolean isWorking() {
		boolean r = false;
		lockMe(this);
		r = working;
		unlockMe(this);
		return r;
	}
	
	public void destroy() {
		lockMe(this);
		if (!working) {
			worker.destroy();
			worker = null;
		}
		unlockMe(this);
	}
	
	protected SimulatorAnimalWorker getAnimalWorkerByAnimalName(String name) {
		SimulatorAnimalWorker r = null;
		for (SimulatorAnimalWorker animalWorker: animalWorkers) {
			if (animalWorker.getAnimalName().equals(name)) {
				r = animalWorker;
				break;
			}
		}
		return r;
	}
	
	protected void simulateNextState() {
		lockMe(this);
		List<Animal> animals = environmentState.getAnimals();
		unlockMe(this);
		if (environmentState!=null) {
			for (Animal ani: animals) {
				SimulatorAnimalWorker animalWorker = getAnimalWorkerByAnimalName(ani.name);
				if (animalWorker!=null && !animalWorker.hasSimulatorAnimal()) {
					EvolverUnit bestSoFar = null;
					if (ani.herbivore) {
						bestSoFar = herbEvolver.getBestSoFar();
					} else if (ani.herbivore) {
						bestSoFar = carnEvolver.getBestSoFar();
					}
					if (bestSoFar!=null) {
						SimulatorAnimal simAni = new SimulatorAnimal();
						simAni.name = ani.name;
						simAni.code = bestSoFar.code;
						simAni.neuralNet = bestSoFar.neuralNet;
						animalWorker.setSimulatorAnimal(simAni);
					}
				}
			}
			lockMe(this);
			long now = System.currentTimeMillis();
			if (now>(energyUpdated + 1000)) {
				environmentState.updatePlants();
				energyUpdated = now;
			}
			unlockMe(this);
			if (isWorking() && envInitializer!=null) {
				envInitializer.updatedState();
				histManager.updateHistory();
			}
		}
	}
	
	protected boolean animalIsAlive(Animal ani) {
		boolean r = true;
		if (ani.dateTimeDied + (environmentConfig.deathDurationSeconds * 1000) > System.currentTimeMillis()) {
			r = false;
		}
		return r;
	}
	
	protected void setPredictionInputForAnimal(Animal ani,Prediction p) {
		// System.out.println("Get prediction for animal: " + ani.name);
		// TODO: set input
	}
	
	protected void handlePredictionOutputForAnimal(Animal ani,Prediction p) {
		// System.out.println("Handle output for animal: " + ani.name);
		// TODO: handle output
	}
}

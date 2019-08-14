package nl.zeesoft.zenn.simulator;

import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zenn.animal.CarnivoreEvolver;
import nl.zeesoft.zenn.animal.HerbivoreEvolver;
import nl.zeesoft.zenn.environment.Environment;
import nl.zeesoft.zodb.Config;

public class Simulator extends Locker {
	private WorkerUnion				union				= null;
	private EnvironmentInitializer	envInitializer		= null;
	private HerbivoreEvolver		herbEvolver			= null;
	private CarnivoreEvolver		carnEvolver			= null;
	private SimulatorWorker			worker				= null;
	private HistoryManager			histManager			= null;

	private Environment				environment			= null;

	private boolean					working				= false;
	
	private long					energyUpdated		= 0;
	private long					environmentUpdated	= 0;
	
	public Simulator(Config config,EnvironmentInitializer envInit,HerbivoreEvolver herbEvo,CarnivoreEvolver carnEvo) {
		super(config.getMessenger());
		union = config.getUnion();
		envInitializer = envInit;
		herbEvolver = herbEvo;
		carnEvolver = carnEvo;
		worker = new SimulatorWorker(config.getMessenger(),config.getUnion(),this);
		histManager = new HistoryManager(config);
	}
	
	public void setEnvironment(Environment environment) {
		lockMe(this);
		boolean loaded = this.environment != environment && environment!=null;
		this.environment = environment;
		unlockMe(this);
		if (loaded) {
			loadedEnvironment();
		}
	}
	
	public void loadedEnvironment() {
		lockMe(this);
		if (environment!=null) {
			worker.setEnvironment(environment);
			histManager.setEnvironment(environment);
		}
		unlockMe(this);
	}
	
	public void start() {
		lockMe(this);
		if (environment!=null && !working) {
			if (environment.prepareForStart()) {
				if (envInitializer!=null) {
					envInitializer.updateObjectsNoLock();
				}
			}
			long now = System.currentTimeMillis();
			energyUpdated = now;
			environmentUpdated = now;
			worker.start();
			working = true;
			if (getMessenger()!=null) {
				getMessenger().debug(this,"Started simulator");
			}
		}
		unlockMe(this);
	}
	
	public void stop() {
		lockMe(this);
		if (working) {
			worker.stop();
			working = false;
			if (getMessenger()!=null) {
				getMessenger().debug(this,"Stopped simulator");
			}
		}
		unlockMe(this);
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
	
	protected void simulateNextState() {
		lockMe(this);
		if (environment!=null) {
			long now = System.currentTimeMillis();
			if (now>(energyUpdated + 1000)) {
				environment.updatePlants();
				energyUpdated = now;
			}
			if (envInitializer!=null) {
				if (now>(environmentUpdated + 10000)) {
					envInitializer.updateObjectsNoLock();
					environmentUpdated = now;
				}
			}
			histManager.updateHistory();
		}
		unlockMe(this);
	}
}

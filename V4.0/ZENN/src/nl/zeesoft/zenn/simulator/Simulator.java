package nl.zeesoft.zenn.simulator;

import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zenn.animal.CarnivoreEvolver;
import nl.zeesoft.zenn.animal.HerbivoreEvolver;
import nl.zeesoft.zenn.environment.EnvironmentConfig;
import nl.zeesoft.zenn.environment.EnvironmentState;
import nl.zeesoft.zodb.Config;

public class Simulator extends Locker {
	private WorkerUnion				union				= null;
	private EnvironmentInitializer	envInitializer		= null;
	private HerbivoreEvolver		herbEvolver			= null;
	private CarnivoreEvolver		carnEvolver			= null;
	private SimulatorWorker			worker				= null;
	private HistoryManager			histManager			= null;

	private EnvironmentConfig		environmentConfig	= null;
	private EnvironmentState		environmentState	= null;

	private boolean					working				= false;
	
	private long					energyUpdated		= 0;
	
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
		if (environmentState!=null) {
			long now = System.currentTimeMillis();
			if (now>(energyUpdated + 1000)) {
				environmentState.updatePlants();
				energyUpdated = now;
			}
			if (envInitializer!=null) {
				envInitializer.updatedState();
				histManager.updateHistory();
			}
		}
		unlockMe(this);
	}
}

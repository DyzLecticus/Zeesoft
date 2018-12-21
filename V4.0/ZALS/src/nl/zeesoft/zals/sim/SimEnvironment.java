package nl.zeesoft.zals.sim;

import java.util.Date;

import nl.zeesoft.zals.env.Environment;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.Config;

public class SimEnvironment extends Locker {
	private Config					configuration	= null;
	private Environment				environment		= null;
	private SimEnvironmentWorker	worker			= null;
	
	private Date					energyUpdated	= new Date();
	
	public SimEnvironment(Config config,Environment env) {
		super(config.getMessenger());
		configuration = config;
		environment = env;
		worker = new SimEnvironmentWorker(config,this);
	}
	
	public void start() {
		worker.start();
	}

	public void stop() {
		worker.stop();
	}

	protected void simulate() {
		lockMe(this);
		environment.addHistory();
		Date now = new Date();
		if (now.getTime()>(energyUpdated.getTime() + 1000)) {
			environment.updatePlants();
			energyUpdated = now;
		}
		unlockMe(this);
	}
	
	protected Environment getEnvironment() {
		return environment;
	}
}

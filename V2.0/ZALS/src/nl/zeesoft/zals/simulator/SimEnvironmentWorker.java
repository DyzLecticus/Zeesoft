package nl.zeesoft.zals.simulator;

import java.util.Date;

import nl.zeesoft.zodb.Worker;

public class SimEnvironmentWorker extends Worker {
	private SimEnvironment	environment		= null;
	private Date			refreshed		= new Date();
	private Date			updated			= new Date();

	protected SimEnvironmentWorker() {
		this.environment = new SimEnvironment();
	}

	protected void initialize() {
		environment.initialize();
		setSleep((1000 / environment.getEnvironment().getStatesPerSecond()));
	}
	
	@Override
	public void start() {
		super.start();
		//Messenger.getInstance().debug(this,"Started environment worker");
	}

	@Override
	public void stop() {
		super.stop();
		waitForStop(10,false);
		environment.stopSimulating();
		//Messenger.getInstance().debug(this,"Stopped environment worker");
	}

	@Override
	public void whileWorking() {
		environment.simulate();
		Date now = new Date();
		if (now.getTime()>(refreshed.getTime() + 10000)) {
			environment.refreshEnvironment();
			setSleep((1000 / environment.getEnvironment().getStatesPerSecond()));
			refreshed = now;
		}
		if (now.getTime()>(updated.getTime() + 30000)) {
			environment.updateObjects();
			updated = now;
		}
	}
}

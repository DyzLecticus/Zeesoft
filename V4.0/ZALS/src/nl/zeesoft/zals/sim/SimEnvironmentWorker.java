package nl.zeesoft.zals.sim;

import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zodb.Config;

public class SimEnvironmentWorker extends Worker {
	private SimEnvironment	simulator		= null;
	
	public SimEnvironmentWorker(Config config,SimEnvironment simEnv) {
		super(config.getMessenger(),config.getUnion());
		simulator = simEnv;
		setSleep((1000 / simEnv.getEnvironment().statesPerSecond));
	}
	
	@Override
	public void stop() {
		super.stop();
		waitForStop(10,false);
	}

	@Override
	public void whileWorking() {
		simulator.simulate();
	}
}

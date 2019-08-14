package nl.zeesoft.zenn.simulator;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zenn.environment.Environment;

public class SimulatorWorker extends Worker {
	private Simulator	simulator	= null;

	protected SimulatorWorker(Messenger msgr, WorkerUnion union,Simulator simulator) {
		super(msgr, union);
		this.simulator = simulator;
	}
	
	protected void setEnvironment(Environment environment) {
		setSleep(1000 / environment.statesPerSecond);
	}
	
	@Override
	public void stop() {
		super.stop();
		this.waitForStop(10,false);
	}
	
	@Override
	protected void whileWorking() {
		simulator.simulateNextState();
	}
	
	protected void destroy() {
		simulator = null;
	}
}

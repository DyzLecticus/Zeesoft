package nl.zeesoft.zacs.simulator;

import nl.zeesoft.zodb.Worker;

public class SimControllerCommandWorker extends Worker {
	boolean first = true;
	
	public SimControllerCommandWorker() {
		setSleep((SimController.getInstance().getControl().getUpdateControlSeconds() * 1000));
	}

	@Override
	public void stop() {
		super.stop();
		waitForStop(10,false);
	}
	
	@Override
	public void whileWorking() {
		// Skip first
		if (first) {
			first = false;
		} else {
			SimController.getInstance().getControllerCommand();
		}
	}
}

package nl.zeesoft.zacs.simulator;

import nl.zeesoft.zodb.Worker;

public class SimControllerReactivateWorker extends Worker {
	boolean skip = true;
	
	public SimControllerReactivateWorker() {
		setSleep((SimController.getInstance().getControl().getReactivateMinutes() * 60000));
	}

	@Override
	public void stop() {
		super.stop();
		skip = true; 
	}
	
	@Override
	public void whileWorking() {
		if (skip) {
			skip = false;
		} else {
			SimController.getInstance().reactivate();
		}
	}
}

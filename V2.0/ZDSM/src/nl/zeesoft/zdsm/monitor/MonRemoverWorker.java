package nl.zeesoft.zdsm.monitor;

import nl.zeesoft.zodb.Worker;

public class MonRemoverWorker extends Worker {
	private MonRemover 	remover	= new MonRemover();
	private int 		skip 	= 1;

	protected MonRemoverWorker() {
		setSleep(60000);
	}
	
	@Override
	public void start() {
		skip = 1;
		super.start();
	}

	@Override
	public void stop() {
		remover.stopWorking();
		super.stop();
		waitForStop(10,false);
	}

	@Override
	public void whileWorking() {
		if (skip>0) {
			skip--;
		} else {
			remover.work();
		}
	}
}

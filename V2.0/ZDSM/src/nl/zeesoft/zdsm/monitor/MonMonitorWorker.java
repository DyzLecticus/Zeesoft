package nl.zeesoft.zdsm.monitor;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;

public class MonMonitorWorker extends Worker {
	private MonMonitor 	monitor 	= new MonMonitor();
	private int 		skip 		= 10;
	
	protected MonMonitorWorker() {
		setSleep(1000);
	}

	@Override
	public void start() {
		skip = 10;
		super.start();
		Messenger.getInstance().debug(this,"Started monitor worker");
	}

	@Override
	public void stop() {
		Messenger.getInstance().debug(this,"Stopping monitor worker ...");
		monitor.stopWorking();
		super.stop();
		waitForStop(60,false);
		Messenger.getInstance().debug(this,"Stopped monitor worker");
	}
	
	@Override
	public void whileWorking() {
		if (skip>0) {
			skip--;
		} else {
			monitor.work();
		}
	}
}

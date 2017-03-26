package nl.zeesoft.zjmo.orchestra;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class ConductorConnectorWorker extends Worker {
	private ConductorConnector	connector	= null;
	private boolean				stopping	= false;

	public ConductorConnectorWorker(Messenger msgr, WorkerUnion union,ConductorConnector connector) {
		super(msgr,union);
		setSleep(1000);
		this.connector = connector;
	}

	@Override
	public void start() {
		setStopping(false);
		super.start();
	}

	public void setStopping(boolean stopping) {
		lockMe(this);
		this.stopping = stopping;
		unlockMe(this);
	}
	
	@Override
	public void stop() {
		super.stop();
		this.waitForStop(1,false);
	}
	
	@Override
	public void whileWorking() {
		boolean stop = false;
		lockMe(this);
		stop = stopping;
		unlockMe(this);
		if (!stop) {
			connector.connect();
		}
	}
}

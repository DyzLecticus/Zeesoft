package nl.zeesoft.zsmc.confab;

import java.util.Date;

import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zsmc.confab.confabs.ConfabulationObject;

public abstract class ModuleWorker extends Worker {
	protected ConfabulationObject	confab			= null;

	private boolean					done			= false;
	
	public ModuleWorker(ConfabulationObject confab) {
		super(confab.messenger,confab.union);
		this.confab = confab;
		setSleep(0);
		setStopOnException(true);
	}
	
	@Override
	protected void setCaughtException(Exception caughtException) {
		super.setCaughtException(caughtException);
		setDone(true);
	}
	
	@Override
	public void start() {
		setDone(false);
		super.start();
	}

	protected boolean confabulationIsTimeOut() {
		Date now = new Date();
		return now.getTime() > (confab.started.getTime() + confab.maxTime);
	}

	protected void setDone(boolean d) {
		lockMe(this);
		done = d;
		unlockMe(this);
	}

	public boolean isDone() {
		boolean r = false;
		lockMe(this);
		r = done;
		unlockMe(this);
		return r;
	}
}

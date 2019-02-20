package nl.zeesoft.zsmc.confab;

import java.util.Date;

import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zsmc.confab.confabs.ConfabulationObject;

public abstract class ModuleWorker extends Worker {
	protected ConfabulationObject		confab			= null;
	
	public ModuleWorker(ConfabulationObject confab) {
		super(confab.messenger,confab.union);
		this.confab = confab;
		setSleep(0);
	}

	protected boolean confabulationIsTimeOut() {
		Date now = new Date();
		return now.getTime() > (confab.started.getTime() + confab.maxTime);
	}
}

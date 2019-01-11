package nl.zeesoft.zsmc.confab;

import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zsmc.confab.confabs.ConfabulationObject;

public class ModuleNormalizeWorker extends Worker {
	private Module			module			= null;
	
	public ModuleNormalizeWorker(ConfabulationObject confab,Module module) {
		super(confab.messenger,confab.union);
		this.module = module;
		setSleep(0);
	}

	@Override
	public void whileWorking() {
		if (!module.isLocked()) {
			module.normalize();
		} else {
			stop();
		}
	}
	
}

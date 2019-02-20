package nl.zeesoft.zsmc.confab;

import nl.zeesoft.zsmc.confab.confabs.ConfabulationObject;

public class ModuleNormalizeWorker extends ModuleWorker {
	private Module					module			= null;
	
	public ModuleNormalizeWorker(ConfabulationObject confab,Module module) {
		super(confab);
		this.confab = confab;
		this.module = module;
		setSleep(0);
	}

	@Override
	public void whileWorking() {
		if (confabulationIsTimeOut()) {
			stop();
		} else if (module.isLocked()) {
			stop();
		} else {
			module.normalize(true);
		}
	}
	
}

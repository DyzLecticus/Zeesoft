package nl.zeesoft.zsmc.confab;

import nl.zeesoft.zsmc.confab.confabs.ConfabulationObject;

public class ModuleContextWorker extends ModuleWorker {
	private Module					contextModule	= null;
	
	public ModuleContextWorker(ConfabulationObject confab,Module contextModule) {
		super(confab);
		this.contextModule = contextModule;
		setSleep(0);
	}

	@Override
	public void whileWorking() {
		if (confabulationIsTimeOut()) {
			contextModule.normalizeCheckLock();
			setDone(true);
			stop();
		} else if (contextModule.isLocked()) {
			setDone(true);
			stop();
		} else {
			/*
			confab.getAndFireLinksInModule(moduleIndex, context);
			boolean checkLock = false;
			Module prev = null;
			if (moduleIndex>0) {
				int m = 0;
				for (Module mod: confab.modules) {
					if (m>=moduleIndex) {
						break;
					}
					if (!mod.isLocked()) {
						prev = mod;
					}
					m++;
				}
			}
			if (prev==null || prev.isLocked()) {
				checkLock = true;
			}
			*/
			contextModule.normalizeCheckLock();
		}
	}
}

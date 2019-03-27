package nl.zeesoft.zsmc.confab;

import nl.zeesoft.zsmc.kb.KbContext;

public class ModuleSequenceWorker extends ModuleWorker {
	private int						moduleIndex		= 0;
	private Module					module			= null;
	private Module					next			= null;
	private KbContext				context			= null;
	
	protected ModuleSequenceWorker(ConfabulationObject confab,int moduleIndex,KbContext context) {
		super(confab);
		this.moduleIndex = moduleIndex;
		this.context = context;
		module = confab.modules.get(moduleIndex);
		if (moduleIndex<(confab.modules.size() - 1)) {
			next = confab.modules.get(moduleIndex + 1);
		}
		setSleep(0);
	}

	@Override
	public void whileWorking() {
		if (confabulationIsTimeOut()) {
			module.normalize(true,next,confab,context,confab.threshold);
			setDone(true);
			stop();
		} else if (module.isLocked()) {
			setDone(true);
			stop();
		} else {
			confab.getAndFireLinksInModule(moduleIndex,context);
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
			module.normalize(checkLock,next,confab,context,confab.threshold);
		}
	}
}

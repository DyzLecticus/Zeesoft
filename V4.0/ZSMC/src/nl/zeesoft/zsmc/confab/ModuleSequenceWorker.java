package nl.zeesoft.zsmc.confab;

import nl.zeesoft.zsmc.confab.confabs.ConfabulationObject;
import nl.zeesoft.zsmc.confab.confabs.ExtensionConfabulation;
import nl.zeesoft.zsmc.kb.KbContext;

public class ModuleSequenceWorker extends ModuleWorker {
	private int						moduleIndex		= 0;
	private Module					module			= null;
	private Module					prev			= null;
	private Module					next			= null;
	private KbContext				context			= null;
	
	public ModuleSequenceWorker(ConfabulationObject confab,int moduleIndex,String contextSymbol) {
		super(confab);
		this.moduleIndex = moduleIndex;
		context = confab.kb.getContext(contextSymbol);
		module = confab.modules.get(moduleIndex);
		if (moduleIndex>0) {
			prev = confab.modules.get(moduleIndex - 1);
		}
		if (moduleIndex<(confab.modules.size() - 1)) {
			next = confab.modules.get(moduleIndex + 1);
		}
		setSleep(0);
	}

	@Override
	public void whileWorking() {
		if (confabulationIsTimeOut()) {
			//System.out.println("Timeout module: " + moduleIndex);
			setDone(true);
			stop();
		} else if (module.isLocked()) {
			//System.out.println("Module locked: " + moduleIndex);
			setDone(true);
			stop();
		} else {
			//System.out.println("Module confabulating: " + moduleIndex);
			confab.getAndFireLinksInModule(moduleIndex, context);
			boolean lock = false;
			if (prev==null || prev.isLocked()) {
				lock = true;
				//System.out.println("Lock: " + confab.modules.indexOf(module));
			}
			ModuleSymbol winner = module.normalize(lock);
			if (winner!=null) {
				if (next!=null && !next.isLocked() &&
					confab instanceof ExtensionConfabulation && ((ExtensionConfabulation) confab).strict
					) {
					//System.out.println("Module limit: " + moduleIndex);
					confab.limitLinksInModule(next,winner,context);
					//System.out.println("Module limited: " + moduleIndex);
				}
				module.setLocked(true);
				//System.out.println("Module locked: " + moduleIndex + ": " + module.isLocked());
			}
		}
	}
}

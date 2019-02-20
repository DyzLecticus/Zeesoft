package nl.zeesoft.zsmc.confab;

import nl.zeesoft.zsmc.confab.confabs.ConfabulationObject;
import nl.zeesoft.zsmc.kb.KbContext;

public class ModuleSequenceWorker extends ModuleWorker {
	private int						moduleIndex		= 0;
	private Module					module			= null;
	private KbContext				context			= null;
	
	public ModuleSequenceWorker(ConfabulationObject confab,int moduleIndex,String contextSymbol) {
		super(confab);
		this.moduleIndex = moduleIndex;
		context = confab.kb.getContext(contextSymbol);
		module = confab.modules.get(moduleIndex);
		setSleep(0);
	}

	@Override
	public void whileWorking() {
		if (confabulationIsTimeOut()) {
			//System.out.println("Timeout module: " + moduleIndex);
			stop();
		} else if (module.isLocked()) {
			//System.out.println("Module locked: " + moduleIndex);
			stop();
		} else {
			//System.out.println("Module confabulating: " + moduleIndex);
			confab.getAndFireLinksInModule(moduleIndex, context);
		}
	}
}

package nl.zeesoft.zsmc.confab;

import java.util.SortedMap;

import nl.zeesoft.zsmc.confab.confabs.ConfabulationObject;
import nl.zeesoft.zsmc.kb.KbContext;

public class ModuleContextWorker extends ModuleWorker {
	private int							symbolIndex		= 0;
	private Module						contextModule	= null;
	private SortedMap<String,KbContext>	contexts		= null;
	
	public ModuleContextWorker(ConfabulationObject confab,int symbolIndex,Module contextModule,SortedMap<String,KbContext> contexts) {
		super(confab);
		this.symbolIndex = symbolIndex;
		this.contextModule = contextModule;
		this.contexts = contexts;
		setSleep(0);
	}

	@Override
	public void whileWorking() {
		if (confabulationIsTimeOut()) {
			setDone(true);
			stop();
		} else if (contextModule.isLocked()) {
			setDone(true);
			stop();
		} else {
			confab.getAndFireLinksInContextModule(symbolIndex,contextModule,contexts);
			setDone(true);
			stop();
		}
	}
}

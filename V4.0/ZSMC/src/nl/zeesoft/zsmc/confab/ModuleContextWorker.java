package nl.zeesoft.zsmc.confab;

import java.util.SortedMap;

import nl.zeesoft.zsmc.kb.KbContext;

public class ModuleContextWorker extends ModuleWorker {
	private int							symbolIndex		= 0;
	private Module						contextModule	= null;
	private SortedMap<String,KbContext>	contexts		= null;
	
	protected ModuleContextWorker(ConfabulationObject confab,int symbolIndex,Module contextModule,SortedMap<String,KbContext> contexts) {
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
			if (confab.symbols.size()==1) {
				confab.getAndFireSymbolsInContextModule(symbolIndex,contextModule,contexts);
			} else {
				confab.getAndFireLinksInContextModule(symbolIndex,contextModule,contexts);
			}
			setDone(true);
			stop();
		}
	}
}

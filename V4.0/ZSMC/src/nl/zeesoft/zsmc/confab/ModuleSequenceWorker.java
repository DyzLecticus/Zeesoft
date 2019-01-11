package nl.zeesoft.zsmc.confab;

import java.util.List;

import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zsmc.confab.confabs.ConfabulationObject;
import nl.zeesoft.zsmc.kb.KbContext;
import nl.zeesoft.zsmc.kb.KbLink;

public class ModuleSequenceWorker extends Worker {
	private int						moduleIndex		= 0;
	private ConfabulationObject		confab			= null;
	private String					contextSymbol	= "";
	
	private Module					module			= null;
	private KbContext				context			= null;
	
	public ModuleSequenceWorker(ConfabulationObject confab,int moduleIndex,String contextSymbol) {
		super(confab.messenger,confab.union);
		this.confab = confab;
		this.moduleIndex = moduleIndex;
		this.contextSymbol = contextSymbol;
		context = confab.kb.getContext(contextSymbol);
		module = confab.modules.get(moduleIndex);
		setSleep(0);
	}

	@Override
	public void whileWorking() {
		if (!module.isLocked()) {
			int start = moduleIndex - confab.kb.getMaxDistance();
			int end = moduleIndex + confab.kb.getMaxDistance();
			if (start<0) {
				start = 0;
			}
			if (end>confab.modules.size()) {
				end = confab.modules.size();
			}
			List<ModuleSymbol> modSyms = module.getActiveSymbols();
			for (int i = start; i<end; i++) {
				if (i!=moduleIndex) {
					int distance = 0;
					if (i<moduleIndex) {
						distance = moduleIndex - i;
					} else {
						distance = i - moduleIndex;
					}
					List<ModuleSymbol> modSymsComp = confab.modules.get(i).getActiveSymbols();   
					for (ModuleSymbol modSym: modSyms) {
						for (ModuleSymbol modSymComp: modSymsComp) {
							List<KbLink> lnks = null;
							if (i<moduleIndex) {
								lnks = confab.kb.getLinks(modSymComp.symbol,distance,contextSymbol,modSym.symbol,confab.caseSensitive);
							} else {
								lnks = confab.kb.getLinks(modSym.symbol,distance,contextSymbol,modSymComp.symbol,confab.caseSensitive);
							}
							for (KbLink lnk: lnks) {
								double prob = ((context.linkBandwidth + (context.linkMaxProb - lnk.prob)) * modSymComp.probNormalized);
								if (i<moduleIndex) {
									module.exciteSymbol(lnk.symbolTo,prob);
								} else {
									module.exciteSymbol(lnk.symbolFrom,prob);
								}
							}
						}
					}
				}
			}
		} else {
			stop();
		}
	}
	
}

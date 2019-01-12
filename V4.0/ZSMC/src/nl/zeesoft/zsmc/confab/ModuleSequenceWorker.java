package nl.zeesoft.zsmc.confab;

import java.util.List;

import nl.zeesoft.zdk.ZIntegerGenerator;
import nl.zeesoft.zsmc.confab.confabs.ConfabulationObject;
import nl.zeesoft.zsmc.kb.KbContext;
import nl.zeesoft.zsmc.kb.KbLink;

public class ModuleSequenceWorker extends ModuleWorker {
	private ZIntegerGenerator		generator		= new ZIntegerGenerator(1,100);
	
	private int						moduleIndex		= 0;
	private String					contextSymbol	= "";
	
	private Module					module			= null;
	private KbContext				context			= null;
	
	public ModuleSequenceWorker(ConfabulationObject confab,int moduleIndex,String contextSymbol) {
		super(confab);
		this.moduleIndex = moduleIndex;
		this.contextSymbol = contextSymbol;
		context = confab.kb.getContext(contextSymbol);
		module = confab.modules.get(moduleIndex);
		setSleep(0);
	}

	@Override
	public void whileWorking() {
		if (confabulationIsTimeOut()) {
			stop();
		} else if (!module.isLocked()) {
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
					if (modSyms.size()>0) {
						for (ModuleSymbol modSym: modSyms) {
							getAndFireLinks(modSymsComp,(i<moduleIndex),distance,modSym.symbol);
						}
					} else {
						getAndFireLinks(modSymsComp,(i<moduleIndex),distance,"");
					}
				}
			}
		} else {
			stop();
		}
	}
	
	private void getAndFireLinks(List<ModuleSymbol> sourceSymbols,boolean from,int distance,String targetSymbol) {
		for (ModuleSymbol sourceSymbol: sourceSymbols) {
			List<KbLink> lnks = null;
			if (from) {
				lnks = confab.kb.getLinks(sourceSymbol.symbol,distance,contextSymbol,targetSymbol,confab.caseSensitive);
			} else {
				lnks = confab.kb.getLinks(targetSymbol,distance,contextSymbol,sourceSymbol.symbol,confab.caseSensitive);
			}
			for (KbLink lnk: lnks) {
				double prob = ((context.linkBandwidth + (context.linkMaxProb - lnk.prob)) * sourceSymbol.probNormalized);
				if (confab.noise>0D) {
					double noise = (confab.noise / 100D) * (double)generator.getNewInteger();
					prob += (prob * noise);
				}
				if (from) {
					module.exciteSymbol(lnk.symbolTo,prob);
				} else {
					module.exciteSymbol(lnk.symbolFrom,prob);
				}
			}
		}
	}
	
}

package nl.zeesoft.zsmc.confab.confabs;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.confab.Module;
import nl.zeesoft.zsmc.confab.ModuleSequenceWorker;
import nl.zeesoft.zsmc.confab.ModuleSymbol;
import nl.zeesoft.zsmc.confab.SymbolCorrector;
import nl.zeesoft.zsmc.kb.KbContext;
import nl.zeesoft.zsmc.kb.KnowledgeBase;

public class CorrectionConfabulation extends ConfabulationObject {
	public String				contextSymbol	= "";
	public String				alphabet		= "";
	
	public ZStringSymbolParser	corrected		= new ZStringSymbolParser();
	public List<Correction>		corrections		= new ArrayList<Correction>();
	
	private KbContext			context			= null;
	private SymbolCorrector		corrector		= new SymbolCorrector();
	
	@Override
	public void initialize(Messenger msgr, WorkerUnion uni, KnowledgeBase kb) {
		super.initialize(msgr,uni,kb);
		for (int m = 0; m < symbols.size(); m++) {
			Module mod = new Module(msgr);
			modules.add(mod);
		}
		for (int m = 0; m < modules.size(); m++) {
			workers.add(new ModuleSequenceWorker(this, m, contextSymbol));
		}
		context = kb.getContext(contextSymbol);
		int m = 0;
		for (String symbol: symbols) {
			Module mod = modules.get(m);
			if (context.knownSymbols.contains(symbol)) {
				mod.setActiveSymbol(symbol);
				mod.setLocked(true);
			} else {
				long stopAfterMs = (maxTime / 5);
				List<String> syms = corrector.getVariations(symbol,context.knownSymbols,started,stopAfterMs,alphabet);
				if (syms.size()==1) {
					mod.setActiveSymbol(syms.get(0));
					mod.setLocked(true);
				} else {
					for (String sym: syms) {
						mod.exciteSymbol(sym,context.linkBandwidth);
					}
				}
			}
			m++;
		}
		initializeModules(contextSymbol);
		logModuleStateNoLock("Initialized modules");
	}
	
	@Override
	public void finalize() {
		List<String> syms = new ArrayList<String>();
		int i = 0;
		for (Module mod: modules) {
			if (mod.isLocked()) {
				List<ModuleSymbol> modSyms = mod.getActiveSymbols();
				if (modSyms.size()>0) {
					syms.add(modSyms.get(0).symbol);
				}
			} else {
				syms.add(symbols.get(i));
			}
			i++;
		}
		corrected.fromSymbols(syms,true,true);
		addLogLine("Finalized; " + corrected);
		super.finalize();
	}
}


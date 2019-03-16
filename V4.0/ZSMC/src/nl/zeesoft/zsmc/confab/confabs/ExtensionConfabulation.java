package nl.zeesoft.zsmc.confab.confabs;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.confab.Module;
import nl.zeesoft.zsmc.confab.ModuleSequenceWorker;
import nl.zeesoft.zsmc.confab.ModuleSymbol;
import nl.zeesoft.zsmc.kb.KnowledgeBase;

public class ExtensionConfabulation extends ConfabulationObject {
	public String				contextSymbol	= "";
	public int					extend			= 1;
	public boolean				strict			= true;

	public ZStringSymbolParser	extension		= new ZStringSymbolParser();
	
	@Override
	public void initialize(Messenger msgr, WorkerUnion uni, KnowledgeBase kb) {
		super.initialize(msgr,uni,kb);
		for (int m = 0; m < (symbols.size() + extend); m++) {
			Module mod = new Module(msgr);
			modules.add(mod);
		}
		for (int m = 0; m < modules.size(); m++) {
			workers.add(new ModuleSequenceWorker(this, m, contextSymbol));
		}
		int m = 0;
		for (String symbol: symbols) {
			Module mod = modules.get(m);
			mod.setActiveSymbol(symbol);
			mod.setLocked(true);
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
			if (i>=symbols.size()) {
				if (mod.isLocked()) {
					List<ModuleSymbol> modSyms = mod.getActiveSymbols();
					if (modSyms.size()>0) {
						syms.add(modSyms.get(0).symbol);
					}
				}
			}
			i++;
		}
		extension.fromSymbols(syms,false,true);
		addLogLine("Finalized; " + extension);
		super.finalize();
	}
}

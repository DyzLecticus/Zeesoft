package nl.zeesoft.zsmc.confab.confabs;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.confab.Module;
import nl.zeesoft.zsmc.confab.ModuleContextWorker;
import nl.zeesoft.zsmc.confab.ModuleSymbol;
import nl.zeesoft.zsmc.kb.KnowledgeBase;

public class ContextConfabulation extends ConfabulationObject {
	public List<ContextResult>	results		= new ArrayList<ContextResult>();
	
	@Override
	public void initialize(Messenger msgr, WorkerUnion uni,KnowledgeBase kb) {
		super.initialize(msgr,uni,kb);
		modules.add(new Module(msgr));
		for (int s = 0; s < symbols.size(); s++) {
			workers.add(new ModuleContextWorker(this,s,modules.get(0),kb.getContexts()));
		}
	}
	
	@Override
	public void finalize() {
		Module mod = modules.get(0);
		mod.normalize();
		List<ModuleSymbol> modSyms = mod.getActiveSymbolsNormalized();
		for (ModuleSymbol modSym: modSyms) {
			ContextResult context = new ContextResult();
			context.contextSymbol = modSym.symbol;
			context.prob = modSym.prob;
			context.probNormalized = modSym.probNormalized;
			results.add(context);
		}
		addLogLine("Finalized; " + results.size() + " context(s) found.");
		super.finalize();
	}
}

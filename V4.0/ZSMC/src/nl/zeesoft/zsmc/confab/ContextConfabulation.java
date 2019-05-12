package nl.zeesoft.zsmc.confab;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.kb.KbContext;
import nl.zeesoft.zsmc.kb.KnowledgeBase;

public class ContextConfabulation extends ConfabulationObject {
	public List<ContextResult>	results		= new ArrayList<ContextResult>();
	
	@Override
	protected void initialize(Messenger msgr, WorkerUnion uni,KnowledgeBase kb) {
		super.initialize(msgr,uni,kb);
		modules.add(new Module(msgr));
		SortedMap<String,KbContext> contexts = kb.getContexts();
		for (int s = 0; s < symbols.size(); s++) {
			workers.add(new ModuleContextWorker(this,s,modules.get(0),contexts));
		}
	}
	
	@Override
	protected void finalize() {
		Module mod = modules.get(0);
		mod.normalize(threshold);
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
package nl.zeesoft.zsmc.confab.confabs;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.confab.Module;
import nl.zeesoft.zsmc.confab.ModuleSequenceWorker;
import nl.zeesoft.zsmc.kb.KnowledgeBase;

public class ExtensionConfabulation extends ConfabulationObject {
	public String				contextSymbol	= "";
	public int					extend			= 1;

	public ZStringSymbolParser	extension		= new ZStringSymbolParser();

	@Override
	public void initialize(Messenger msgr, WorkerUnion uni, KnowledgeBase kb) {
		super.initialize(msgr,uni,kb);
		for (int m = 0; m < (symbols.size() + extend); m++) {
			Module mod = new Module(msgr);
			modules.add(mod);
		}
		int i = 0;
		for (Module modFrom: modules) {
			for (int d = 1; d <= kb.getMaxDistance(); d++) {
				int idx = i + d;
				if (idx<modules.size()) {
					Module modTo = modules.get(idx);
					workers.add(new ModuleSequenceWorker(msgr, uni, kb, modFrom, modTo, d, contextSymbol, caseSensitive));
				}
			}
			i++;
		}
	}
}

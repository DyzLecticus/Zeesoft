package nl.zeesoft.zsmc.confab.confabs;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.confab.Module;
import nl.zeesoft.zsmc.confab.ModuleNormalizeWorker;
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
		for (int m = 0; m < modules.size(); m++) {
			workers.add(new ModuleSequenceWorker(this, m, contextSymbol));
			workers.add(new ModuleNormalizeWorker(this, modules.get(m)));
		}
	}
}

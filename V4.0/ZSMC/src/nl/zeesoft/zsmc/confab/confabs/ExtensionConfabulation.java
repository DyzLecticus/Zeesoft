package nl.zeesoft.zsmc.confab.confabs;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.confab.Module;

public class ExtensionConfabulation extends ConfabulationObject {
	public String				contextSymbol	= "";
	public int					extend			= 1;
	public boolean				parallel		= true;

	public ZStringSymbolParser	extension		= new ZStringSymbolParser();

	@Override
	public void initialize(Messenger msgr, WorkerUnion uni) {
		super.initialize(msgr,uni);
		for (int m = 0; m < (symbols.size() + extend); m++) {
			Module mod = new Module(msgr);
			modules.add(mod);
		}
	}
}

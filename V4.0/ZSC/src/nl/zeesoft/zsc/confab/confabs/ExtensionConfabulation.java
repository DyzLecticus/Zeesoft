package nl.zeesoft.zsc.confab.confabs;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsc.confab.Module;

public class ExtensionConfabulation extends ConfabulationObject {
	public String				contextSymbol	= "";
	public int					extend			= 1;
	public boolean				parallel		= true;

	public ZStringSymbolParser	extension		= new ZStringSymbolParser();

	@Override
	public void initialize() {
		super.initialize();
		for (int m = 0; m < (symbols.size() + extend); m++) {
			Module mod = new Module();
			modules.add(mod);
		}
	}
}

package nl.zeesoft.zspp;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zspp.mod.ModZSPP;

public class ZSPPConfig extends Config {
	@Override
	protected void addModules() {
		super.addModules();
		getZODB().selfTest = false;
		addModule(new ModZSPP(this));
	}
}

package nl.zeesoft.zmc;

import nl.zeesoft.zmc.mod.ModZMC;
import nl.zeesoft.zodb.Config;

public class ZMCConfig extends Config {
	@Override
	protected void addModules() {
		super.addModules();
		getZODB().selfTest = false;
		getZODB().maxLenObj = 999999;
		addModule(new ModZMC(this));
	}
}

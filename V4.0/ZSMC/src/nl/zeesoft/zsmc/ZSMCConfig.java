package nl.zeesoft.zsmc;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zsmc.mod.ModZSMC;

public class ZSMCConfig extends Config {
	@Override
	protected void addModules() {
		super.addModules();
		getZODB().selfTest = false;
		addModule(new ModZSMC(this));
	}
}

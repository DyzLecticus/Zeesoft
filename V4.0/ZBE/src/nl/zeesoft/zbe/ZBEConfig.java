package nl.zeesoft.zbe;

import nl.zeesoft.zbe.mod.ModZBE;
import nl.zeesoft.zodb.Config;

public class ZBEConfig extends Config {
	@Override
	protected void addModules() {
		super.addModules();
		getZODB().selfTest = false;
		addModule(new ModZBE(this));
	}
}

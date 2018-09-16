package nl.zeesoft.znlb;

import nl.zeesoft.znlb.mod.ModZNLB;
import nl.zeesoft.zodb.Config;

public class ZNLBConfig extends Config {
	@Override
	protected void addModules() {
		super.addModules();
		getZODB().selfTest = false;
		addModule(new ModZNLB(this));
	}
}

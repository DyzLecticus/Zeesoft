package nl.zeesoft.zevt;

import nl.zeesoft.zevt.mod.ModZEVT;
import nl.zeesoft.zodb.Config;

public class ZEVTConfig extends Config {
	@Override
	protected void addModules() {
		super.addModules();
		getZODB().selfTest = false;
		addModule(new ModZEVT(this));
	}
}

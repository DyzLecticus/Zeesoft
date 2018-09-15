package nl.zeesoft.zevt;

import nl.zeesoft.zevt.mod.ModZEVT;
import nl.zeesoft.znlb.ZNLBConfig;

public class ZEVTConfig extends ZNLBConfig {
	@Override
	protected void addModules() {
		super.addModules();
		addModule(new ModZEVT(this));
	}
}

package nl.zeesoft.zsdm;

import nl.zeesoft.zevt.ZEVTConfig;
import nl.zeesoft.zsdm.mod.ModZSDM;

public class ZSDMConfig extends ZEVTConfig {
	@Override
	protected void addModules() {
		super.addModules();
		addModule(new ModZSDM(this));
	}
}

package nl.zeesoft.zsdm;

import nl.zeesoft.zevt.ZEVTConfig;
import nl.zeesoft.zsc.mod.ModZSC;
import nl.zeesoft.zsdm.mod.ModZSDM;

public class ZSDMConfig extends ZEVTConfig {
	@Override
	protected void addModules() {
		super.addModules();
		addModule(new ModZSC(this));
		addModule(new ModZSDM(this));
	}
}

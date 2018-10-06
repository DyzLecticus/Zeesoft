package nl.zeesoft.zals;

import nl.zeesoft.zals.mod.ModZALS;
import nl.zeesoft.zsc.ZSCConfig;

public class ZALSConfig extends ZSCConfig {
	@Override
	protected void addModules() {
		super.addModules();
		addModule(new ModZALS(this));
	}
}

package nl.zeesoft.zsc;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zsc.mod.ModZSC;

public class ZSCConfig extends Config {
	@Override
	protected void addModules() {
		super.addModules();
		addModule(new ModZSC(this));
	}
}

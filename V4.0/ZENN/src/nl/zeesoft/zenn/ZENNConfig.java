package nl.zeesoft.zenn;

import nl.zeesoft.zenn.mod.ModZENN;
import nl.zeesoft.zodb.Config;

public class ZENNConfig extends Config {
	@Override
	protected void addModules() {
		super.addModules();
		getZODB().selfTest = false;
		addModule(new ModZENN(this));
	}
}

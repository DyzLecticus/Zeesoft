package nl.zeesoft.zsda;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zsda.mod.ModZSDA;

public class ZSDAConfig extends Config {
	@Override
	protected void addModules() {
		super.addModules();
		getZODB().selfTest = false;
		getZODB().maxLenObj = 99999999;
		addModule(new ModZSDA(this));
	}
}

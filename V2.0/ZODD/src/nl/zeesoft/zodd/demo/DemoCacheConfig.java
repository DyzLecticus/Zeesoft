package nl.zeesoft.zodd.demo;

import nl.zeesoft.zodb.database.cache.CcConfig;

public final class DemoCacheConfig extends CcConfig {
	@Override
	protected void initializeConfig() {
		super.initializeConfig();
		getClassMaxSizeMap().put(DemoModel.DEMO_PARENT_FULL_NAME,10000);
		getClassMaxSizeMap().put(DemoModel.DEMO_CHILD_FULL_NAME,10000);
	}
}

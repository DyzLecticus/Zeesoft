package nl.zeesoft.zodb.test;

import nl.zeesoft.zodb.database.cache.CcConfig;

public final class TestCacheConfig extends CcConfig {
	@Override
	protected void initializeConfig() {
		super.initializeConfig();
		getClassMaxSizeMap().put(TestModel.TEST_CLASS_1_FULL_NAME,500);
		getClassMaxSizeMap().put(TestModel.TEST_CLASS_2_FULL_NAME,3000);
	}
}

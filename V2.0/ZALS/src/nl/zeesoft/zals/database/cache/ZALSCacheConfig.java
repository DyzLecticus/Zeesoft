package nl.zeesoft.zals.database.cache;

import nl.zeesoft.zac.database.cache.ZACCacheConfig;
import nl.zeesoft.zals.database.model.ZALSModel;

public class ZALSCacheConfig extends ZACCacheConfig {
	@Override
	protected void initializeConfig() {
		super.initializeConfig();
		getClassMaxSizeMap().put(ZALSModel.ENVIRONMENT_CLASS_FULL_NAME,10);
	}
}

package nl.zeesoft.zac.database.cache;

import nl.zeesoft.zac.database.model.ZACModel;
import nl.zeesoft.zodb.database.cache.CcConfig;

public class ZACCacheConfig extends CcConfig {
	@Override
	protected void initializeConfig() {
		super.initializeConfig();
		getClassMaxSizeMap().put(ZACModel.MODULE_CLASS_FULL_NAME,999);
		getClassMaxSizeMap().put(ZACModel.SYMBOL_LINK_CONTEXT_CLASS_FULL_NAME,9999);
		getClassMaxSizeMap().put(ZACModel.SYMBOL_LINK_SEQUENCE_CLASS_FULL_NAME,9999);
	}
}

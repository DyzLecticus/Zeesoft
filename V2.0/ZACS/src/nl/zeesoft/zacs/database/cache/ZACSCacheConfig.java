package nl.zeesoft.zacs.database.cache;

import nl.zeesoft.zacs.database.model.ZACSModel;
import nl.zeesoft.zodb.database.cache.CcConfig;

public class ZACSCacheConfig extends CcConfig {
	@Override
	protected void initializeConfig() {
		super.initializeConfig();
		getClassMaxSizeMap().put(ZACSModel.COMMAND_CLASS_FULL_NAME,10);
		getClassMaxSizeMap().put(ZACSModel.CONTROL_CLASS_FULL_NAME,10);
		getClassMaxSizeMap().put(ZACSModel.STATE_HISTORY_CLASS_FULL_NAME,10000);
		getClassMaxSizeMap().put(ZACSModel.SYMBOL_CLASS_FULL_NAME,10000);
		getClassMaxSizeMap().put(ZACSModel.SYMBOL_LINK_CLASS_FULL_NAME,10000);
		getClassMaxSizeMap().put(ZACSModel.MODULE_CLASS_FULL_NAME,10);
		getClassMaxSizeMap().put(ZACSModel.MODULE_SYMBOL_CLASS_FULL_NAME,10000);
		getClassMaxSizeMap().put(ZACSModel.CONTEXT_LINK_CLASS_FULL_NAME,10000);
		getClassMaxSizeMap().put(ZACSModel.EXAMPLE_CLASS_FULL_NAME,1000);
		getClassMaxSizeMap().put(ZACSModel.ASSIGNMENT_CLASS_FULL_NAME,1000);
		getClassMaxSizeMap().put(ZACSModel.CRAWLER_CLASS_FULL_NAME,10);
	}
}

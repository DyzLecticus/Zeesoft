package nl.zeesoft.zids.database.cache;

import nl.zeesoft.zids.database.model.ZIDSModel;
import nl.zeesoft.zodb.database.cache.CcConfig;

public class ZIDSCacheConfig extends CcConfig {
	@Override
	protected void initializeConfig() {
		super.initializeConfig();
		getClassMaxSizeMap().put(ZIDSModel.VARIABLE_TYPE_CLASS_FULL_NAME,100);
		getClassMaxSizeMap().put(ZIDSModel.DIALOG_CLASS_FULL_NAME,1000);
		getClassMaxSizeMap().put(ZIDSModel.DIALOG_EXAMPLE_CLASS_FULL_NAME,10000);
		getClassMaxSizeMap().put(ZIDSModel.DIALOG_VARIABLE_CLASS_FULL_NAME,10000);
		getClassMaxSizeMap().put(ZIDSModel.DIALOG_VARIABLE_EXAMPLE_CLASS_FULL_NAME,10000);
		getClassMaxSizeMap().put(ZIDSModel.SESSION_CLASS_FULL_NAME,10000);
		getClassMaxSizeMap().put(ZIDSModel.SESSION_DIALOG_VARIABLE_CLASS_FULL_NAME,10000);
		getClassMaxSizeMap().put(ZIDSModel.SELF_CLASS_FULL_NAME,10);
		getClassMaxSizeMap().put(ZIDSModel.HUMAN_CLASS_FULL_NAME,10000);
	}
}

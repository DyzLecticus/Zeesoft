package nl.zeesoft.zdsm.database.cache;

import nl.zeesoft.zdsm.database.model.ZDSMModel;
import nl.zeesoft.zodb.database.cache.CcConfig;

public class ZDSMCacheConfig extends CcConfig {
	@Override
	protected void initializeConfig() {
		super.initializeConfig();
		getClassMaxSizeMap().put(ZDSMModel.DOMAIN_CLASS_FULL_NAME,100);
		getClassMaxSizeMap().put(ZDSMModel.SERVICE_CLASS_FULL_NAME,1000);
		getClassMaxSizeMap().put(ZDSMModel.RESPONSE_CLASS_FULL_NAME,10000);
		getClassMaxSizeMap().put(ZDSMModel.PROCESS_CLASS_FULL_NAME,1000);
		getClassMaxSizeMap().put(ZDSMModel.PROCESS_LOG_CLASS_FULL_NAME,10000);
	}
}

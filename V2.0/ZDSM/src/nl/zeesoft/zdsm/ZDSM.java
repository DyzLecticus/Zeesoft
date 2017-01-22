package nl.zeesoft.zdsm;

import nl.zeesoft.zdsm.database.cache.ZDSMCacheConfig;
import nl.zeesoft.zdsm.database.model.ZDSMDataGenerator;
import nl.zeesoft.zdsm.database.model.ZDSMModel;
import nl.zeesoft.zdsm.database.server.ZDSMServer;
import nl.zeesoft.zdsm.monitor.MonMonitorController;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.database.server.SvrConfig;

/**
 * This class is the entry point to the ZDSM application.
 */
public class ZDSM {
	public static void main(String[] args) {
		DbConfig.getInstance().setModelClassName(ZDSMModel.class.getName());
		DbConfig.getInstance().setCacheConfigClassName(ZDSMCacheConfig.class.getName());
		SvrConfig.getInstance().setServerClassName(ZDSMServer.class.getName());
		if (SvrConfig.getInstance().getPort()==4321) {
			SvrConfig.getInstance().setPort(5454);
		}

		if (!DbConfig.getInstance().fileExists()) {
			args = new String[2];
			args[1] = "zdsm.jar";
			ZDSMDataGenerator generator = new ZDSMDataGenerator();
			generator.confirmInstallDemoData();
			DbController.getInstance().addSubscriber(generator);
		} else {
			DbController.getInstance().addSubscriber(MonMonitorController.getInstance());
		}

		DbController.getInstance().handleMainMethodsArguments(args);
	}
}
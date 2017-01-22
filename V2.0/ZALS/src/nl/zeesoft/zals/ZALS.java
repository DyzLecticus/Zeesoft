package nl.zeesoft.zals;

import nl.zeesoft.zals.database.cache.ZALSCacheConfig;
import nl.zeesoft.zals.database.model.ZALSDataGenerator;
import nl.zeesoft.zals.database.model.ZALSModel;
import nl.zeesoft.zals.database.server.ZALSServer;
import nl.zeesoft.zals.simulator.SimController;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.database.server.SvrConfig;

/**
 * This class is the entry point to the ZALS application.
 */
public class ZALS {
	public static void main(String[] args) {
		DbConfig.getInstance().setModelClassName(ZALSModel.class.getName());
		DbConfig.getInstance().setCacheConfigClassName(ZALSCacheConfig.class.getName());
		SvrConfig.getInstance().setServerClassName(ZALSServer.class.getName());
		if (SvrConfig.getInstance().getPort()==4321) {
			SvrConfig.getInstance().setPort(7654);
		}

		if (!DbConfig.getInstance().fileExists()) {
			args = new String[2];
			args[1] = "zals.jar";
			ZALSDataGenerator generator = new ZALSDataGenerator();
			generator.confirmInstallDemoData();
			DbController.getInstance().addSubscriber(generator);
		} else {
			DbController.getInstance().addSubscriber(SimController.getInstance());
		}

		DbController.getInstance().handleMainMethodsArguments(args);
	}
}
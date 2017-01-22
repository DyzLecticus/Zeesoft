package nl.zeesoft.zacs;

import nl.zeesoft.zacs.crawler.CrwManager;
import nl.zeesoft.zacs.database.cache.ZACSCacheConfig;
import nl.zeesoft.zacs.database.model.ZACSDataGenerator;
import nl.zeesoft.zacs.database.model.ZACSModel;
import nl.zeesoft.zacs.database.server.ZACSServer;
import nl.zeesoft.zacs.simulator.SimController;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.database.server.SvrConfig;

/**
 * This class is the entry point to the ZACS application.
 */
public class ZACS {
	public static void main(String[] args) {
		DbConfig.getInstance().setModelClassName(ZACSModel.class.getName());
		DbConfig.getInstance().setCacheConfigClassName(ZACSCacheConfig.class.getName());
		SvrConfig.getInstance().setServerClassName(ZACSServer.class.getName());
		if (SvrConfig.getInstance().getPort()==4321) {
			SvrConfig.getInstance().setPort(5544);
		}

		if (!DbConfig.getInstance().fileExists()) {
			args = new String[2];
			args[1] = "zacs.jar";
			ZACSDataGenerator generator = new ZACSDataGenerator();
			generator.confirmInstallExamplesAndAssignments();
			generator.confirmInstallCrawlers();
			DbController.getInstance().addSubscriber(generator);
		} else {
			DbController.getInstance().addSubscriber(SimController.getInstance());
			DbController.getInstance().addSubscriber(CrwManager.getInstance());
		}

		DbController.getInstance().handleMainMethodsArguments(args);
	}
}
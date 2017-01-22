package nl.zeesoft.zodd;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.database.server.SvrConfig;
import nl.zeesoft.zodd.demo.DemoCacheConfig;
import nl.zeesoft.zodd.demo.DemoDataGenerator;
import nl.zeesoft.zodd.demo.DemoModel;
import nl.zeesoft.zodd.demo.DemoServer;

/**
 * This class is the entry point to the ZODB demo application.
 */
public class ZODD {
	public static void main(String[] args) {
		DbConfig.getInstance().setModelClassName(DemoModel.class.getName());
		DbConfig.getInstance().setCacheConfigClassName(DemoCacheConfig.class.getName());
		SvrConfig.getInstance().setServerClassName(DemoServer.class.getName());
		SvrConfig.getInstance().setPort(5432);

		if (!DbConfig.getInstance().fileExists()) {
			args = new String[2];
			args[1] = "zodd.jar";
		} else {
			DbController.getInstance().addSubscriber(new DemoDataGenerator());
		}

		DbController.getInstance().handleMainMethodsArguments(args);
	}
}
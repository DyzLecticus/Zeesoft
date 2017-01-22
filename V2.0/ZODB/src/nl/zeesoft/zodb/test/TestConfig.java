package nl.zeesoft.zodb.test;

import java.io.File;

import nl.zeesoft.zodb.ZODB;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.database.DbControllerCommandWorker;
import nl.zeesoft.zodb.database.server.SvrConfig;

public final class TestConfig {
	public static final String 	TEST_DIR 		= "test/";
	public static final int 	TEST_PORT 		= 6543;
	
	private static TestConfig 	config 			= null;

	private TestConfig() {
		// Singleton
	}

	public static TestConfig getInstance() {
		if (config==null) {
			config = new TestConfig();
			config.initialize();
			
			DbConfig.getInstance().setInstallDir(TEST_DIR);
			DbConfig.getInstance().setModelClassName(TestModel.class.getName());
			DbConfig.getInstance().setCacheConfigClassName(TestCacheConfig.class.getName());
			DbConfig.getInstance().setOpenServer(false); // Start server separately with methods provided in this class
			DbConfig.getInstance().setDebug(true);
			DbConfig.getInstance().setDebugRequestLogging(true);
			DbConfig.getInstance().setDebugPerformance(true);
			DbConfig.getInstance().getCacheConfig().setDebug(true);

			SvrConfig.getInstance().setPort(TEST_PORT);
			SvrConfig.getInstance().setServerClassName(TestServer.class.getName());
			SvrConfig.getInstance().setDebugHTTPHeaders(true);
			SvrConfig.getInstance().setGenerateHTTPResources(true);
		}
		return config;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}
	
	protected void initialize() {
		File td = new File(TEST_DIR);
		if (!td.exists()) {
			td.mkdir();
			if (!td.exists()) {
				System.err.println("Unable to initialize test directory: " + TEST_DIR);
			}
		}
	}

	public static void startDatabase() {
		System.out.println("[Start test database]");
		boolean install = false;
		if (!DbConfig.getInstance().fileExists()) {
			install = true;
		}
		ZODB.main(null);
		if (install) {
			sleep(1000);
			ZODB.main(null);
		}
	}

	public static void stopDatabase() {
		sleep(1000);
		System.out.println("[Stop test database]");
		String[] stop = new String[1];
		stop[0] = DbControllerCommandWorker.DB_COMMAND_STOP;
		ZODB.main(stop);
	}

	public static boolean openServer() {
		System.out.println("[Start test database server]");
		return DbController.getInstance().openServer();
	}

	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

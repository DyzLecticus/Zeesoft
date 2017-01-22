package nl.zeesoft.zids.server;

import nl.zeesoft.zacs.simulator.SimController;
import nl.zeesoft.zids.database.model.ZIDSDataGenerator;
import nl.zeesoft.zids.dialog.pattern.PtnManager;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbController;

public class SvrControllerDatabase {
	private static SvrControllerDatabase	db 					= null;
	private	PtnManager						patternManager		= null;
	private ZIDSDataGenerator				dataGenerator		= null;
	
	private SvrControllerDatabase() {
		// Singleton
	}
	
	public static SvrControllerDatabase getInstance() {
		if (db==null) {
			db = new SvrControllerDatabase();
		}
		return db;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}
	
	public boolean start(String installDir,String modelClassName, String cacheConfigClassName,boolean debug) {
		boolean started = false;
		DbConfig.getInstance().setInstallDir(installDir);
		if (DbConfig.getInstance().fileExists()) {
			DbConfig.getInstance().unserialize();
		}
		DbConfig.getInstance().setDebug(debug);
		if (DbController.getInstance().checkWorking()) {
			Messenger.getInstance().debug(this, "ZODB is already working in: " + DbConfig.getInstance().getDataDir());
		} else {
			initializeDbConfig(modelClassName,cacheConfigClassName);
			if (!DbConfig.getInstance().fileExists()) {
				install();
			}
			DbConfig.getInstance().unserialize();
			DbController.getInstance().addSubscriber(SvrControllerDialogs.getInstance());
			DbController.getInstance().addSubscriber(SimController.getInstance());
			DbController.getInstance().addSubscriber(SvrControllerSessions.getInstance());
			DbController.getInstance().start(false);

			started = true;
		}
		return started;
	}

	public void stop() {
		DbController.getInstance().stop(false);
	}

	private void initializeDbConfig(String modelClassName, String cacheConfigClassName) {
		DbConfig.getInstance().setModelClassName(modelClassName);
		DbConfig.getInstance().setCacheConfigClassName(cacheConfigClassName);
		DbConfig.getInstance().setOpenServer(false);
		DbConfig.getInstance().setShowGUI(false);
	}
	
	private void install() {
		if (dataGenerator!=null) {
			DbController.getInstance().addSubscriber(dataGenerator);
		}
		DbController.getInstance().install("","",true);
	}

	protected void initializePatternManager() {
		if (patternManager!=null) {
			Messenger.getInstance().debug(this,"Initializing pattern manager ...");
			patternManager.initializePatterns();
			patternManager.initializeDatabasePatterns();
			Messenger.getInstance().debug(this,"Initialized pattern manager");
		} else {
			Messenger.getInstance().debug(this,"Pattern manager not set");
		}
	}
	
	/**
	 * @param patternManager the patternManager to set
	 */
	protected void setPatternManager(PtnManager patternManager) {
		this.patternManager = patternManager;
	}

	/**
	 * @return the patternManager
	 */
	public PtnManager getPatternManager() {
		return patternManager;
	}

	/**
	 * @param dataGenerator the dataGenerator to set
	 */
	protected void setDataGenerator(ZIDSDataGenerator dataGenerator) {
		this.dataGenerator = dataGenerator;
	}
}

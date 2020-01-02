package nl.zeesoft.zsda.grid;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.init.InitializerObject;
import nl.zeesoft.zodb.db.init.Persistable;
import nl.zeesoft.zsda.mod.ModZSDA;

public class LoggerInitializer extends InitializerObject {
	private ZGridFactoryInitializer		factoryInitializer	= null;
	
	private PersistableLogger			logger				= null;

	public LoggerInitializer(Config config,ZGridFactoryInitializer factoryInitializer) {
		super(config,ModZSDA.NAME + "/Log/");
		this.factoryInitializer = factoryInitializer;
		setTimeoutSeconds(10);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		if (logger!=null) {
			logger.destroy();
		}
	}
	
	@Override
	protected void initializeDatabaseObjectsNoLock() {
		logger = new PersistableLogger(getConfiguration().getMessenger(),factoryInitializer.getGrid(),getConfiguration());
		addObjectNoLock(logger);
	}

	@Override
	protected Persistable getNewObjectNoLock(ZStringBuilder name) {
		return new PersistableLogger(getConfiguration().getMessenger(),factoryInitializer.getGrid(),getConfiguration());
	}
	
	@Override
	protected void loadedObject(Persistable object) {
		lockMe(this);
		logger = (PersistableLogger) object;
		unlockMe(this);
	}
	
	public PersistableLogger getLogger() {
		PersistableLogger r = null;
		lockMe(this);
		r = logger;
		unlockMe(this);
		return r;
	}
}

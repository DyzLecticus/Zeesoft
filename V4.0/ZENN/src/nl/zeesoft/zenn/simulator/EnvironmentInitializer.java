package nl.zeesoft.zenn.simulator;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zenn.environment.EnvironmentConfig;
import nl.zeesoft.zenn.environment.EnvironmentState;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.init.InitializerObject;
import nl.zeesoft.zodb.db.init.Persistable;

public class EnvironmentInitializer extends InitializerObject {
	private EnvironmentConfig		environmentConfig	= null;
	private EnvironmentState		environmentState	= null;

	public EnvironmentInitializer(Config config) {
		super(config,"ZENN/Environment/");
		setTimeoutSeconds(30);
	}
	
	public void updatedState() {
		lockMe(this);
		updateObjectInDatabaseNoLock(environmentState);
		unlockMe(this);
	}
	
	@Override
	protected void initializeDatabaseObjectsNoLock() {
		environmentConfig = new EnvironmentConfig();
		addObjectNoLock(environmentConfig);
		environmentState = new EnvironmentState();
		environmentState.initialize(environmentConfig);
		addObjectNoLock(environmentState);
	}

	@Override
	protected Persistable getNewObjectNoLock(ZStringBuilder name) {
		Persistable r = null;
		if (name.endsWith("Config")) {
			r = new EnvironmentConfig();
		} else if (name.endsWith("State")) {
			r = new EnvironmentState();
		}
		return r;
	}
	
	@Override
	protected void loadedObject(Persistable object) {
		lockMe(this);
		if (object.getObjectName().endsWith("Config")) {
			environmentConfig = (EnvironmentConfig) object;
		} else if (object.getObjectName().endsWith("State")) {
			environmentState = (EnvironmentState) object;
		}
		if (environmentState!=null && environmentConfig!=null) {
			environmentState.config = environmentConfig;
		}
		unlockMe(this);
	}
	
	public EnvironmentConfig getEnvironmentConfig() {
		EnvironmentConfig r = null;
		lockMe(this);
		r = environmentConfig;
		unlockMe(this);
		return r;
	}
	
	public EnvironmentState getEnvironmentState() {
		EnvironmentState r = null;
		lockMe(this);
		r = environmentState;
		unlockMe(this);
		return r;
	}
}

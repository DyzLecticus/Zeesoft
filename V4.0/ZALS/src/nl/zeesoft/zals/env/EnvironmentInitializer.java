package nl.zeesoft.zals.env;

import nl.zeesoft.zals.mod.ModZALS;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.InitializerDatabaseObject;
import nl.zeesoft.zodb.db.InitializerObject;

public class EnvironmentInitializer extends InitializerObject {
	private Environment		environment		= null;
	
	public EnvironmentInitializer(Config config) {
		super(config,ModZALS.NAME + "/Environment/");
	}

	public Environment getEnvironment() {
		if (environment==null) {
			lockMe(this);
			environment = (Environment) getObjectsNoLock().get(0);
			unlockMe(this);
		}
		return environment;
	}
	
	public void destroy() {
		super.destroy();
		lockMe(this);
		environment.destroy();
		environment = null;
		unlockMe(this);
	}

	@Override
	protected void initializeDatabaseObjectsNoLock() {
		environment = new Environment();
		environment.initialize();
		addObjectNoLock(environment);
	}

	@Override
	protected InitializerDatabaseObject getNewObjectNoLock(String name) {
		return new Environment();
	}
}

package nl.zeesoft.zenn.simulator;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zenn.environment.Environment;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.init.InitializerObject;
import nl.zeesoft.zodb.db.init.Persistable;

public class EnvironmentInitializer extends InitializerObject {
	private Environment		environment = null;

	public EnvironmentInitializer(Config config) {
		super(config,"ZENN/Environment/");
	}

	@Override
	protected void initializeDatabaseObjectsNoLock() {
		Environment env = new Environment();
		env.initialize();
		addObjectNoLock(env);
	}

	@Override
	protected Persistable getNewObjectNoLock(ZStringBuilder name) {
		return new Environment();
	}
	
	@Override
	protected void loadedObject(Persistable object) {
		lockMe(this);
		environment = (Environment) object;
		unlockMe(this);
	}
	
	public Environment getEnvironment() {
		Environment r = null;
		lockMe(this);
		r = environment;
		unlockMe(this);
		return r;
	}
}

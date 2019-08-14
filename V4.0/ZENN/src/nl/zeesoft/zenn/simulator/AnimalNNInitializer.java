package nl.zeesoft.zenn.simulator;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zenn.environment.Environment;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.init.InitializerObject;
import nl.zeesoft.zodb.db.init.Persistable;

public class AnimalNNInitializer extends InitializerObject {
	private Environment		environment		= null;
	
	public AnimalNNInitializer(Config config) {
		super(config,"ZENN/Environment/AnimalNeuralNets/");
	}

	public void setEnvironment(Environment environment) {
		lockMe(this);
		this.environment = environment;
		unlockMe(this);
	}
	
	@Override
	protected void initializeDatabaseObjectsNoLock() {
		// TODO Auto-generated method stub
		
		//addObjectNoLock(object);
	}

	@Override
	protected Persistable getNewObjectNoLock(ZStringBuilder name) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

package nl.zeesoft.zsda.grid;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.init.InitializerObject;
import nl.zeesoft.zodb.db.init.Persistable;

public class GeneratorInitializer extends InitializerObject {
	private ZGridFactoryInitializer		factoryInitializer	= null;
	
	private PersistableGenerator		generator			= null;

	public GeneratorInitializer(Config config,ZGridFactoryInitializer factoryInitializer) {
		super(config,"ZSDA/Generate/");
		this.factoryInitializer = factoryInitializer;
		setTimeoutSeconds(10);
	}
	
	public void updatedGenerator() {
		updateObject(generator.getObjectName());
	}
	
	@Override
	protected void initializeDatabaseObjectsNoLock() {
		generator = new PersistableGenerator(getConfiguration().getMessenger(),getConfiguration().getUnion(),factoryInitializer.getGrid());
		addObjectNoLock(generator);
	}

	@Override
	protected Persistable getNewObjectNoLock(ZStringBuilder name) {
		return new PersistableGenerator(getConfiguration().getMessenger(),getConfiguration().getUnion(),factoryInitializer.getGrid());
	}
	
	@Override
	protected void loadedObject(Persistable object) {
		lockMe(this);
		generator = (PersistableGenerator) object;
		unlockMe(this);
	}
	
	public PersistableGenerator getGenerator() {
		PersistableGenerator r = null;
		lockMe(this);
		r = generator;
		unlockMe(this);
		return r;
	}
}

package nl.zeesoft.zsda.grid;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.grid.ZGrid;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.init.InitializerObject;
import nl.zeesoft.zodb.db.init.Persistable;
import nl.zeesoft.zsda.ZSDAConfig;

public class ZGridFactoryInitializer extends InitializerObject {
	private PersistableZGridFactory		factory		= null;
	private ZGrid						grid		= null;

	public ZGridFactoryInitializer(Config config) {
		super(config,"ZSDA/Grid/");
		setTimeoutSeconds(10);
	}
	
	public void updatedFactory() {
		updateObject(factory.getObjectName());
	}
	
	@Override
	protected void initializeDatabaseObjectsNoLock() {
		factory = new PersistableZGridFactory(getConfiguration().getMessenger(),getConfiguration().getUnion());
		if (getConfiguration() instanceof ZSDAConfig) {
			((ZSDAConfig) getConfiguration()).initializeFactory(factory);
		} else {
			factory.initializeDefaultGrid();
		}
		grid = factory.buildNewGrid();
		addObjectNoLock(factory);
	}

	@Override
	protected Persistable getNewObjectNoLock(ZStringBuilder name) {
		return new PersistableZGridFactory(getConfiguration().getMessenger(),getConfiguration().getUnion());
	}
	
	@Override
	protected void loadedObject(Persistable object) {
		lockMe(this);
		factory = (PersistableZGridFactory) object;
		grid = factory.buildNewGrid();
		unlockMe(this);
	}
	
	public PersistableZGridFactory getFactory() {
		PersistableZGridFactory r = null;
		lockMe(this);
		r = factory;
		unlockMe(this);
		return r;
	}
	
	public ZGrid getGrid() {
		ZGrid r = null;
		lockMe(this);
		r = grid;
		unlockMe(this);
		return r;
	}
}

package nl.zeesoft.zsda.grid;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.grid.ZGrid;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.init.InitializerObject;
import nl.zeesoft.zodb.db.init.Persistable;

public class ZGridInitializer extends InitializerObject {
	private ZGridFactoryInitializer		factoryInitializer = null;
	
	public ZGridInitializer(Config config,ZGridFactoryInitializer factoryInitializer) {
		super(config,"ZSDA/State/");
		this.factoryInitializer = factoryInitializer;
		setTimeoutSeconds(60);
	}
	
	public PersistableProcessorState getProcessorState(String columnId) {
		PersistableProcessorState r = null;
		lockMe(this);
		r = (PersistableProcessorState) getObjectByNameNoLock(new ZStringBuilder(columnId));
		unlockMe(this);
		return r;
	}
	
	public void updateProcessorState(String columnId) {
		updateObject(new ZStringBuilder(columnId));
	}

	@Override
	protected void initializeDatabaseObjectsNoLock() {
		factoryInitializer.getGrid().randomizePoolerConnections();
		for (String columnId: factoryInitializer.getFactory().getColumnIds()) {
			PersistableProcessorState state = new PersistableProcessorState(getConfiguration().getMessenger());
			state.setColumnId(columnId);
			addObjectNoLock(state);
		}
	}
	
	@Override
	protected Persistable getNewObjectNoLock(ZStringBuilder name) {
		PersistableProcessorState state = new PersistableProcessorState(getConfiguration().getMessenger());
		state.setColumnId(name.toString());
		return state;
	}
	
	@Override
	protected void loadedObject(Persistable object) {
		ZGrid grid = factoryInitializer.getGrid();
		PersistableProcessorState state = (PersistableProcessorState) object;
		ZStringBuilder stateData = state.getStateData();
		if (stateData.length()>0 && !stateData.equals(new ZStringBuilder("null"))) {
			grid.setColumnStateData(state.getColumnId(),stateData);
		}
	}
}

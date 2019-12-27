package nl.zeesoft.zsda.grid;

import java.util.SortedMap;
import java.util.Map.Entry;

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
	
	public PersistableGridState getProcessorState(String columnId) {
		PersistableGridState r = null;
		lockMe(this);
		r = (PersistableGridState) getObjectByNameNoLock(new ZStringBuilder(columnId));
		unlockMe(this);
		return r;
	}
	
	public void updateProcessorState(String columnId) {
		updateObject(new ZStringBuilder(columnId));
	}
	
	public void updateState() {
		ZGrid grid = factoryInitializer.getGrid();
		if (grid!=null && !grid.isActive()) {
			SortedMap<String,ZStringBuilder> stateData = grid.getStateData();
			for (Entry<String,ZStringBuilder> entry: stateData.entrySet()) {
				PersistableGridState state = getProcessorState(entry.getKey());
				if (state!=null) {
					state.setStateData(entry.getValue());
					updateProcessorState(entry.getKey());
				}
			}
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		factoryInitializer = null;
	}

	@Override
	protected void initializeDatabaseObjectsNoLock() {
		factoryInitializer.getGrid().randomizePoolerConnections();
		PersistableGridState state = new PersistableGridState(getConfiguration().getMessenger());
		state.setKey("SELF");
		addObjectNoLock(state);
		for (String columnId: factoryInitializer.getFactory().getColumnIds()) {
			state = new PersistableGridState(getConfiguration().getMessenger());
			state.setKey(columnId);
			addObjectNoLock(state);
		}
	}
	
	@Override
	protected Persistable getNewObjectNoLock(ZStringBuilder name) {
		PersistableGridState state = new PersistableGridState(getConfiguration().getMessenger());
		state.setKey(name.toString());
		return state;
	}
	
	@Override
	protected void loadedObject(Persistable object) {
		ZGrid grid = factoryInitializer.getGrid();
		PersistableGridState state = (PersistableGridState) object;
		ZStringBuilder stateData = state.getStateData();
		if (stateData.length()>0 && !stateData.equals(new ZStringBuilder("null"))) {
			grid.setStateData(state.getKey(),stateData);
		}
	}
}

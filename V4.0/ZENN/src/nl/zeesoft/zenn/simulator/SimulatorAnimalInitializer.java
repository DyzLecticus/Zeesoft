package nl.zeesoft.zenn.simulator;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.init.InitializerObject;
import nl.zeesoft.zodb.db.init.Persistable;

public class SimulatorAnimalInitializer extends InitializerObject {
	public SimulatorAnimalInitializer(Config config) {
		super(config,"ZENN/Animal/");
		setTimeoutSeconds(10);
	}

	protected SimulatorAnimal getSimulatorAnimalByName(String name) {
		SimulatorAnimal r = null;
		for (Persistable object: getObjectsNoLock()) {
			SimulatorAnimal simAni = (SimulatorAnimal) object;
			if (simAni.name.equals(name)) {
				r = simAni;
				break;
			}
		}
		return r;
	}
	
	@Override
	protected void initializeDatabaseObjectsNoLock() {
		// Objects are added by simulator
	}

	@Override
	protected Persistable getNewObjectNoLock(ZStringBuilder name) {
		SimulatorAnimal ani = new SimulatorAnimal();
		ani.name = name.toString();
		return ani;
	}
}

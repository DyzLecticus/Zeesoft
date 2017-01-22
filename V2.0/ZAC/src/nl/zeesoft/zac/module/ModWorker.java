package nl.zeesoft.zac.module;

import nl.zeesoft.zac.database.model.Module;
import nl.zeesoft.zodb.Worker;

public abstract class ModWorker extends Worker {
	private Module							module					= null;

	protected ModWorker(Module module) {
		this.module = module;
	}
	
	/**
	 * @return the module
	 */
	protected Module getModule() {
		return module;
	}
}

package nl.zeesoft.zsmc.confab;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class ModuleNormalizeWorker extends Worker {
	private Module			module			= null;
	
	public ModuleNormalizeWorker(Messenger msgr, WorkerUnion union,Module module) {
		super(msgr, union);
		this.module = module;
		setSleep(0);
	}

	@Override
	public void whileWorking() {
		if (!module.isLocked()) {
			module.normalize();
		} else {
			stop();
		}
	}
	
}

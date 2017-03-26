package nl.zeesoft.zjmo.orchestra.controller;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class ControllerStateUpdateWorker extends Worker {
	private OrchestraController controller = null;

	public ControllerStateUpdateWorker(Messenger msgr, WorkerUnion union,OrchestraController controller) {
		super(msgr, union);
		setSleep(10000);
		this.controller = controller;
	}

	@Override
	public void whileWorking() {
		controller.forceUpdateOrchestraState();
	}
}

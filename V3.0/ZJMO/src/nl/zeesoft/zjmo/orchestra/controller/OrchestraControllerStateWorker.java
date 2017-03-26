package nl.zeesoft.zjmo.orchestra.controller;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class OrchestraControllerStateWorker extends Worker {
	private OrchestraController controller = null;

	public OrchestraControllerStateWorker(Messenger msgr, WorkerUnion union,OrchestraController controller) {
		super(msgr, union);
		setSleep(1000);
		this.controller = controller;
	}

	@Override
	public void whileWorking() {
		controller.updateOrchestraState();
	}
}

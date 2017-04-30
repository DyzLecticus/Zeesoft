package nl.zeesoft.zmmt.gui;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class CompositionUpdateWorker extends Worker {
	private Controller				controller		= null;

	public CompositionUpdateWorker(Messenger msgr, WorkerUnion union,Controller controller) {
		super(msgr, union);
		setSleep(10);
		this.controller = controller;
	}
	
	@Override
	public void whileWorking() {
		controller.handleCompositionUpdates();
	}
}

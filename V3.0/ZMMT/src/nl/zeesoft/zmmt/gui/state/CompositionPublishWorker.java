package nl.zeesoft.zmmt.gui.state;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class CompositionPublishWorker extends Worker {
	private CompositionStateManager		stateManager	= null;

	public CompositionPublishWorker(Messenger msgr, WorkerUnion union,CompositionStateManager stateManager) {
		super(msgr, union);
		setSleep(100);
		this.stateManager = stateManager;
	}
	
	@Override
	public void whileWorking() {
		stateManager.publishChanges();
	}
}

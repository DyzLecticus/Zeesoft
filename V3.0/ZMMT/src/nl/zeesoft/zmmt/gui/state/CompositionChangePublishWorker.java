package nl.zeesoft.zmmt.gui.state;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class CompositionChangePublishWorker extends Worker {
	private StateManager		stateManager	= null;

	public CompositionChangePublishWorker(Messenger msgr, WorkerUnion union,StateManager stateManager) {
		super(msgr, union);
		setSleep(100);
		this.stateManager = stateManager;
	}
	
	@Override
	public void whileWorking() {
		stateManager.publishChanges();
	}
}

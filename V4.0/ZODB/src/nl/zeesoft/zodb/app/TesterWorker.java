package nl.zeesoft.zodb.app;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class TesterWorker extends Worker {

	public TesterWorker(Messenger msgr, WorkerUnion union) {
		super(msgr, union);
		setSleep(10);
	}

	@Override
	public void whileWorking() {
		// TODO Auto-generated method stub
		
	}

}

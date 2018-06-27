package nl.zeesoft.zsd.interpret;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class ClassificationWorker extends Worker {

	public ClassificationWorker(Messenger msgr, WorkerUnion union) {
		super(msgr, union);
		setSleep(0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void whileWorking() {
		// TODO Auto-generated method stub
		
	}
	
}

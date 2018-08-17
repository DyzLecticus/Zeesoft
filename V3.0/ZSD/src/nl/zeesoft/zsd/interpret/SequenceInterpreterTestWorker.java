package nl.zeesoft.zsd.interpret;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class SequenceInterpreterTestWorker extends Worker {
	private SequenceInterpreterTester	tester	= null;
	
	public SequenceInterpreterTestWorker(Messenger msgr, WorkerUnion union,SequenceInterpreterTester tester) {
		super(msgr, union);
		this.tester = tester;
		setSleep(10);
		setStopOnException(true);
	}

	@Override
	public void whileWorking() {
		if (tester.test()) {
			stop();
		}
	}

	@Override
	protected void setCaughtException(Exception caughtException) {
		super.setCaughtException(caughtException);
		tester.handleTestException(caughtException);
	}
}

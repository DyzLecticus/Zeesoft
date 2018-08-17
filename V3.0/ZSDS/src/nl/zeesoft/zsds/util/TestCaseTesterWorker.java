package nl.zeesoft.zsds.util;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class TestCaseTesterWorker extends Worker {
	private TestCaseTester	tester	= null;
	
	public TestCaseTesterWorker(Messenger msgr, WorkerUnion uni,TestCaseTester tester,int sleep) {
		super(msgr,uni);
		this.tester = tester;
		setSleep(sleep);
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
		tester.stop();
	}
}

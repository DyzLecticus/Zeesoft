package nl.zeesoft.zsds.tester;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class TestCaseSetTesterWorker extends Worker {
	private TestCaseSetTester	tester	= null;
	
	public TestCaseSetTesterWorker(Messenger msgr, WorkerUnion uni,TestCaseSetTester tester) {
		super(msgr,uni);
		this.tester = tester;
		setSleep(1000);
		setStopOnException(true);
	}

	@Override
	public void whileWorking() {
		if (tester.getDialogs()) {
			stop();
		}
	}

	@Override
	protected void setCaughtException(Exception caughtException) {
		super.setCaughtException(caughtException);
		tester.stop();
	}
}

package nl.zeesoft.zsds.test;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsds.AppBaseConfiguration;
import nl.zeesoft.zsds.tester.AppTester;
import nl.zeesoft.zsds.tester.TestCaseSetTester;

public class TestAppTester extends TestObject {
	public TestAppTester(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestAppTester(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test runs the *AppTester* self test. It assumes the development server is up and running locally.");
	}
	
	@Override
	protected void test(String[] args) {
		ZDKFactory factory = new ZDKFactory();
		Messenger messenger = factory.getMessenger();
		WorkerUnion union = factory.getWorkerUnion(messenger);
		messenger.setPrintDebugMessages(true);
		messenger.start();

		AppBaseConfiguration base = new AppBaseConfiguration();
		base.setSelfTest(true);
		AppTester appTester = new AppTester(messenger,union,base);
		appTester.getConfiguration().setTestCaseDir("src/");
		appTester.initialize("http://localhost:8080/ZSDS/dialogRequestHandler.json",false);
		
		while (!appTester.isInitialized()) {
			sleep(100);
		}
		
		TestCaseSetTester tester = appTester.getTester(AppTester.ENVIRONMENT_NAME_SELF);
		if (tester.start()) {
			while (tester.isTesting()) {
				sleep(100);
			}
		} else {
			System.err.println("Failed to start tester");
		}
		
		messenger.stop();
		union.stopWorkers();
		messenger.whileWorking();

		JsFile summary = tester.getSummary();
		System.out.println("Summary:\n" + summary.toStringBuilderReadFormat());
	}
}

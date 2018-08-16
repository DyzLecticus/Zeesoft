package nl.zeesoft.zsds.test;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsds.util.AppTester;
import nl.zeesoft.zsds.util.TestCaseSetTester;

public class TestAppTester extends TestObject {
	public TestAppTester(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestAppTester(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		// TODO: Describe.
		/*
		System.out.println("This test shows how to use a *DialogSetToJson* instance to convert a list of dialogs into a JSON file.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the dialog set");
		System.out.println("DialogSet ds = new DialogSet();");
		System.out.println("// Initialize the dialog set");
		System.out.println("ds.initialize();");
		System.out.println("// Create the DialogToJson instance");
		System.out.println("DialogSetToJson convertor = new DialogSetToJson();");
		System.out.println("// Convert the english dialogs to JSON");
		System.out.println("JsFile json = convertor.getJsonForDialogs(ds,\"Optional language code\"));");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockEntityValueTranslator.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestDialogSetToJson.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogSetToJson.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows a sample of the converted JSON.  ");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		ZDKFactory factory = new ZDKFactory();
		Messenger messenger = factory.getMessenger();
		WorkerUnion union = factory.getWorkerUnion(messenger);
		messenger.setPrintDebugMessages(true);
		messenger.start();

		AppTester appTester = new AppTester(messenger,union,new BaseConfiguration());
		appTester.getConfiguration().setTestCaseDir("resources/");
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

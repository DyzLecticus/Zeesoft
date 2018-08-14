package nl.zeesoft.zsd.test;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.dialog.DialogHandlerConfiguration;
import nl.zeesoft.zsd.interpret.SequenceInterpreterTester;

public class TestSequenceInterpreterTester extends TestObject {
	public TestSequenceInterpreterTester(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSequenceInterpreterTester(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test show how to use a *SequenceInterpreterTester* to test sequence classification for a *DialogHandlerConfiguration*.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the DialogHandlerConfiguration");
		System.out.println("DialogHandlerConfiguration config = new DialogHandlerConfiguration();");
		System.out.println("// Start the initialization (and wait until it's done)");
		System.out.println("config.initialize();");
		System.out.println("// Create the tester");
		System.out.println("SequenceInterpreterTester tester = new SequenceInterpreterTester(config);");
		System.out.println("// Initialize the tester");
		System.out.println("tester.initialize();");
		System.out.println("// Start the tester (and wait until it's done)");
		System.out.println("tester.start();");
		System.out.println("// Get the test results summary");
		System.out.println("JsFile summary = tester.getSummary();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestSequenceInterpreterTester.class));
		System.out.println(" * " + getTester().getLinkForClass(SequenceInterpreterTester.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogHandlerConfiguration.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the test results summary.  ");
	}
	
	@Override
	protected void test(String[] args) {
		DialogHandlerConfiguration config = TestDialogHandlerConfiguration.getConfig(getTester());
		if (config==null) {
			System.out.println("This test has been skipped due to configuration initialization failure");
		} else {
			SequenceInterpreterTester tester = new SequenceInterpreterTester(config);
			testTester(tester,23);
		}
	}
	
	protected void testTester(SequenceInterpreterTester tester, int expectedErrors) {
		tester.initialize();
		if (tester.start()) {
			while (tester.isTesting()) {
				sleep(100);
				//System.out.println("Tested: " + tester.getDonePercentage() + "%");
			}
			JsFile summary = tester.getSummary();
			System.out.println(summary.toStringBuilderReadFormat());
			int errors = 0;
			errors = summary.rootElement.getChildByName("errors").children.size();
			assertEqual(errors,expectedErrors,"The number of errors does not match expectation");
		} else {
			System.err.println("ERROR: Failed to start tester");
		}
	}
}

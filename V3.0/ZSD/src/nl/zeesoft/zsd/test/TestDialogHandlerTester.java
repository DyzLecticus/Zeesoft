package nl.zeesoft.zsd.test;

import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.dialog.DialogHandlerConfiguration;
import nl.zeesoft.zsd.dialog.DialogHandlerTester;

public class TestDialogHandlerTester extends TestSequenceInterpreterTester {
	public TestDialogHandlerTester(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDialogHandlerTester(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		// TODO: Describe
		System.out.println("This test show how to use a *DialogHandlerTester* to test dialog handling for a *DialogHandlerConfiguration*.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the DialogHandlerConfiguration");
		System.out.println("DialogHandlerConfiguration config = new DialogHandlerConfiguration();");
		System.out.println("// Start the initialization (and wait until it's done)");
		System.out.println("config.initialize();");
		System.out.println("// Create the tester");
		System.out.println("DialogHandlerTester tester = new DialogHandlerTester(config);");
		System.out.println("// Initialize the tester");
		System.out.println("tester.initialize();");
		System.out.println("// Start the tester (and wait until it's done)");
		System.out.println("tester.start();");
		System.out.println("// Get the test results summary");
		System.out.println("JsFile summary = tester.getSummary();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestDialogHandlerTester.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogHandlerTester.class));
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
			DialogHandlerTester tester = new DialogHandlerTester(config);
			testTester(tester,14);
		}
	}
}

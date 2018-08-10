package nl.zeesoft.zsd.test;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.dialog.DialogHandlerConfiguration;
import nl.zeesoft.zsd.util.SequenceInterpreterTester;

public class TestSequenceInterpreterTester extends TestObject {
	public TestSequenceInterpreterTester(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSequenceInterpreterTester(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/*
		System.out.println("This test show how to use a *DialogHandler* to process an *DialogRequest*.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the DialogHandlerConfiguration");
		System.out.println("DialogHandlerConfiguration config = new DialogHandlerConfiguration();");
		System.out.println("// Add a listener");
		System.out.println("config.addListener(this);");
		System.out.println("// Start the initialization (and wait until it's done)");
		System.out.println("config.initialize();");
		System.out.println("// Create the dialog handler");
		System.out.println("DialogHandler handler = new DialogHandler(config);");
		System.out.println("// Create the handler request");
		System.out.println("DialogRequest request = new DialogRequest(\"The optional question that prompted the input\",\"The input sequence\");");
		System.out.println("// Use the handler to process the request");
		System.out.println("DialogResponse response = interpreter.handleDialogRequest(request);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestSequenceInterpreterTester.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogHandlerConfiguration.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogRequest.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogResponse.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogHandler.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows several dialog handler requests and the debug log of corresponding responses.  ");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		DialogHandlerConfiguration config = TestDialogHandlerConfiguration.getConfig(getTester());
		if (config==null) {
			System.out.println("This test has been skipped due to configuration initialization failure");
		} else {
			SequenceInterpreterTester tester = new SequenceInterpreterTester(config);
			tester.initialize();
			if (tester.start()) {
				while (tester.isTesting()) {
					sleep(100);
					//System.out.println("Tested: " + tester.getDonePercentage() + "%");
				}
				System.out.println(tester.getSummary().toStringBuilderReadFormat());
			} else {
				System.err.println("ERROR: Failed to start tester");
			}
		}
	}
}

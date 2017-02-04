package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.messenger.MessengerListener;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class TestMessenger extends TestObject {
	public TestMessenger(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestMessenger(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test reflects a default implementation of the *Messenger* combined with the *WorkerUnion*.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create a factory");
		System.out.println("ZDKFactory factory = new ZDKFactory();");
		System.out.println("// Get the messenger from the factory");
		System.out.println("Messenger messenger = factory.getMessenger();");
		System.out.println("// Add a debug message");
		System.out.println("messenger.debug(this,\"Example debug message\");");
		System.out.println("// Add a warning message");
		System.out.println("messenger.warn(this,\"Example warning message\");");
		System.out.println("// Enable debug message printing");
		System.out.println("messenger.setPrintDebugMessages(true);");
		System.out.println("// Start the messenger");
		System.out.println("messenger.start();");
		System.out.println("// Add an error message");
		System.out.println("messenger.error(this,\"Example error message\");");
		System.out.println("// Stop the messenger");
		System.out.println("messenger.stop();");
		System.out.println("// Ensure all application workers are stopped");
		System.out.println("WorkerUnion.getInstance().stopWorkers();");
		System.out.println("// Trigger the messenger to print the remaining messages");
		System.out.println("messenger.whileWorking();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("The *Messenger* can be used to log debug, warning and error messages and print them to the standard and/or error out.");
		System.out.println("It implements the *Worker* class to minimize wait time impact for threads that call the *Messenger*.");
		System.out.println("The Messenger is thread safe so it can be shared across the entire application.");
		System.out.println("Classes that implement the *MessengerListener* interface can subscribe to *Messenger* message printing events.");
		System.out.println("The *WorkerUnion* can be used to ensure all workers that have been started are stopped when stopping the application.");
		System.out.println("It will log an error if it fails to stop a worker.");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestMessenger.class));
		System.out.println(" * " + getTester().getLinkForClass(Messenger.class));
		System.out.println(" * " + getTester().getLinkForClass(MessengerListener.class));
		System.out.println(" * " + getTester().getLinkForClass(Worker.class));
		System.out.println(" * " + getTester().getLinkForClass(WorkerUnion.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the standard (and error) output of the test log messages.");
	}
	
	@Override
	protected void test(String[] args) {
		ZDKFactory factory = new ZDKFactory();
		Messenger messenger = factory.getMessenger();
		WorkerUnion workerUnion = factory.getWorkerUnion(messenger);
		TestMessengerListener listener = new TestMessengerListener();
		messenger.debug(listener,"Test log debug message before Messenger has started");
		sleep(150);
		messenger.setPrintDebugMessages(true);
		messenger.addListener(listener);
		messenger.start();
		sleep(150);
		messenger.error(listener,"Test log error message while Messenger is working");
		sleep(150);
		messenger.stop();
		sleep(150);
		workerUnion.stopWorkers();
		messenger.warn(listener,"Test log warning message after Messenger has stopped");
		messenger.whileWorking(); // Prints and flushes remaining messages
		assertEqual(messenger.isWorking(),false,"Failed to stop the messenger");
		assertEqual(messenger.isWarning(),true,"The messenger failed to register the warning message correctly");
		assertEqual(messenger.isError(),true,"The messenger failed to register the warning error correctly");
		assertEqual(listener.getMessages().size(),3,"Number of listened messages does not meet expectation");
	}
}

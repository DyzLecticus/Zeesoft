package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.messenger.MessengerListener;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class TestMessenger extends TestObject {
	public static void main(String[] args) {
		(new TestMessenger()).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test reflects a default implementation of the *Messenger* singleton combined with the *WorkerUnion* singleton.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Add a debug message");
		System.out.println("Messenger.getInstance().debug(this,\"Example debug message\");");
		System.out.println("// Add a warning message");
		System.out.println("Messenger.getInstance().warn(this,\"Example warning message\");");
		System.out.println("// Enable debug message printing");
		System.out.println("Messenger.getInstance().setPrintDebugMessages(true);");
		System.out.println("// Start the messenger");
		System.out.println("Messenger.getInstance().start();");
		System.out.println("// Add an error message");
		System.out.println("Messenger.getInstance().error(this,\"Example error message\");");
		System.out.println("// Stop the messenger");
		System.out.println("Messenger.getInstance().stop();");
		System.out.println("// Ensure all application workers are stopped");
		System.out.println("WorkerUnion.getInstance().stopWorkers();");
		System.out.println("// Trigger the messenger to print the remaining messages");
		System.out.println("Messenger.getInstance().whileWorking();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("The *Messenger* can be used to log debug, warning and error messages and print them to the standard and/or error out.");
		System.out.println("It is implemented as a thread safe singleton to allow easy application wide access.");
		System.out.println("It implements the *Worker* class to minimize wait time impact for threads that call the *Messenger*.");
		System.out.println("Classes that implement the *MessengerListener* interface can subscribe to *Messenger* message printing events.");
		System.out.println("The *WorkerUnion* can be used to ensure all workers that have been started are stopped when stopping the application.");
		System.out.println("It will log an error if it fails to stop a worker.");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + Tester.getInstance().getLinkForClass(TestMessenger.class));
		System.out.println(" * " + Tester.getInstance().getLinkForClass(Messenger.class));
		System.out.println(" * " + Tester.getInstance().getLinkForClass(MessengerListener.class));
		System.out.println(" * " + Tester.getInstance().getLinkForClass(Worker.class));
		System.out.println(" * " + Tester.getInstance().getLinkForClass(WorkerUnion.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the standard (and error) output of the test log messages.");
	}
	
	@Override
	protected void test(String[] args) {
		TestMessengerListener listener = new TestMessengerListener();
		
		Messenger.getInstance().debug(listener,"Test log debug message before Messenger has started");
		sleep(150);
		Messenger.getInstance().setPrintDebugMessages(true);
		Messenger.getInstance().addListener(listener);
		Messenger.getInstance().start();
		sleep(150);
		Messenger.getInstance().error(listener,"Test log error message while Messenger is working");
		sleep(150);
		Messenger.getInstance().stop();
		sleep(150);
		WorkerUnion.getInstance().stopWorkers();
		Messenger.getInstance().warn(listener,"Test log warning message after Messenger has stopped");
		Messenger.getInstance().whileWorking(); // Prints and flushes remaining messages
		
		assertEqual(Messenger.getInstance().isWorking(),false,"Failed to stop the messenger");
		assertEqual(Messenger.getInstance().isWarning(),true,"The messenger failed to register the warning message correctly");
		assertEqual(Messenger.getInstance().isError(),true,"The messenger failed to register the warning error correctly");
		assertEqual(listener.getMessages().size(),3,"Number of listened messages does not meet expectation");
	}
}

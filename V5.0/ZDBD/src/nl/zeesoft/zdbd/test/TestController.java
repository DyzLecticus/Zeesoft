package nl.zeesoft.zdbd.test;

import nl.zeesoft.zdbd.Controller;
import nl.zeesoft.zdbd.Settings;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerManager;
import nl.zeesoft.zdk.thread.Waiter;

public class TestController extends TestObject {
	public TestController(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestController(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		/* TODO: Describe
		System.out.println("This test shows how a *Str* instance can be used to split a comma separated string into a list of *Str* instances. ");
		System.out.println("The *Str* class is designed to add features of the Java String to a Java StringBuilder. ");
		System.out.println("It also contains methods for file writing and reading. ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the Str");
		System.out.println("Str str = new Str(\"qwer,asdf,zxcv\");");
		System.out.println("// Split the Str");
		System.out.println("List<Str> strs = str.split(\",\");");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockStr.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestController.class));
		System.out.println(" * " + getTester().getLinkForClass(Str.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the string input and lists the *Str* objects.  ");
		*/
	}

	@Override
	protected void test(String[] args) {
		Logger.setLoggerDebug(true);
		Controller controller = new Controller();
		Settings settings = new Settings();
		settings.soundBankDir = "../../V3.0/ZeeTracker/resources/";
		settings.workDir = "dist";
		CodeRunnerChain chain = controller.initialize(settings);
		Waiter.startAndWaitFor(chain,3000);
		
		sleep(1000);
		
		chain = controller.destroy();
		Waiter.startAndWaitFor(chain,1000);
		
		assertEqual(CodeRunnerManager.getActiverRunners().size(),0,"Number of active code runners does not match expectation");
	}
}

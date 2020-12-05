package nl.zeesoft.zdbd.test;

import java.util.List;

import nl.zeesoft.zdbd.ThemeControllerSettings;
import nl.zeesoft.zdbd.ThemeController;
import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerManager;
import nl.zeesoft.zdk.thread.Waiter;

public class TestThemeController extends TestObject {
	public TestThemeController(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestThemeController(new Tester())).runTest(args);
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
		
		ThemeControllerSettings settings = new ThemeControllerSettings();
		settings.soundBankDir = "../../V3.0/ZeeTracker/resources/";
		settings.workDir = "dist";
		
		ThemeController controller = new ThemeController();
		CodeRunnerChain chain = controller.initialize(settings);
		assertNotNull(chain,"Failed to initialize controller");
		if (chain!=null) {
			Waiter.startAndWaitFor(chain,10000);
			assertEqual(FileIO.getActionLog().size(),2,"Action log size does not match expectation (1)");
			
			System.out.println();
			chain = controller.saveTheme();
			Waiter.startAndWaitFor(chain,10000);
			assertEqual(FileIO.getActionLog().size(),21,"Action log size does not match expectation (2)");
			List<String> themes = controller.listThemes();
			assertEqual(themes.size(),1,"Number of themes does not match expectation");
			assertEqual(themes.get(0),"Demo","The listed theme name does not match expectation");
			
			System.out.println();
			chain = controller.destroy();
			Waiter.startAndWaitFor(chain,1000);
			assertEqual(FileIO.getActionLog().size(),22,"Action log size does not match expectation (3)");
			
			
			System.out.println();
			ThemeController controller2 = new ThemeController();
			chain = controller2.initialize(settings);
			Waiter.startAndWaitFor(chain,20000);
			settings = controller2.getSettings();
			assertEqual(settings.workingTheme,"Demo","Working theme does not match expectation");
			assertEqual(FileIO.getActionLog().size(),41,"Action log size does not match expectation (4)");

			System.out.println();
			chain = controller2.destroy();
			Waiter.startAndWaitFor(chain,1000);

			assertEqual(CodeRunnerManager.getActiverRunners().size(),0,"Number of active code runners does not match expectation");
			System.out.println();
			System.out.println(FileIO.getActionLogStr());
		}
	}
}

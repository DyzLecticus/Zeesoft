package nl.zeesoft.zdbd.test;

import nl.zeesoft.zdbd.ThemeController;
import nl.zeesoft.zdbd.ThemeControllerSettings;
import nl.zeesoft.zdbd.gui.MainWindow;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;

public class TestGUI extends TestObject {
	public TestGUI(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestGUI(new Tester())).runTest(args);
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
		
		ThemeController controller = new ThemeController();
		ThemeControllerSettings settings = new ThemeControllerSettings();
		MainWindow window = new MainWindow(controller, settings);
		window.initialize();
		assertEqual(window.getFrame().getTitle(),MainWindow.NAME,"Main window title does not match expectation");
		
		sleep(60000 * 60);
		System.exit(0);
	}
}

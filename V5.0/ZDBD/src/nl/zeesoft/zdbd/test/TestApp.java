package nl.zeesoft.zdbd.test;

import nl.zeesoft.zdbd.App;
import nl.zeesoft.zdbd.theme.ThemeControllerSettings;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;

public class TestApp extends TestObject {
	public TestApp(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestApp(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test is not included in the test set");
	}

	@Override
	protected void test(String[] args) {
		Logger.setLoggerDebug(true);
		
		ThemeControllerSettings settings = new ThemeControllerSettings();
		settings.soundBankDir = "../../V3.0/ZeeTracker/resources/";
		
		App app = new App();
		boolean started = app.start(settings);
		assertEqual(started,true,"Failed to start the app");
		if (started) {
			sleep(3600000);
			app.stop(null);
		}
	}
}

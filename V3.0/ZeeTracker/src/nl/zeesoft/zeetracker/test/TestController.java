package nl.zeesoft.zeetracker.test;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zeetracker.gui.Controller;
import nl.zeesoft.zeetracker.gui.Settings;

public class TestController extends TestObject {
	public TestController(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestController(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test is used to manually test the *Controller*.");
	}

	@Override
	protected void test(String[] args) {
		Settings settings = new Settings();
		Controller controller = new Controller(settings);
		controller.initialize();
		controller.start(true);
	}
}

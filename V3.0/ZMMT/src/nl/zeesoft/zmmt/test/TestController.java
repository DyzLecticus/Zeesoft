package nl.zeesoft.zmmt.test;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zmmt.gui.Controller;

public class TestController extends TestObject {
	public TestController(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestController(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test is used to test the controller.");
	}

	@Override
	protected void test(String[] args) {
		Controller controller = new Controller();
		controller.initialize();
		controller.start(true);
	}
}

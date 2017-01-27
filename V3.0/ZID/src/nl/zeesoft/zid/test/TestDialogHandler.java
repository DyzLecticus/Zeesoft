package nl.zeesoft.zid.test;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zid.dialog.DialogHandler;

public class TestDialogHandler extends TestObject {
	public static void main(String[] args) {
		(new TestDialogHandler()).test(args);
	}

	@Override
	protected void describe() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void test(String[] args) {
		DialogHandler handler = (DialogHandler) Tester.getInstance().getMockedObject(MockDialogHandler.class.getName());
		StringBuilder output = handler.processInput(new StringBuilder("hello"));
		assertEqual(output.toString(),"","Output does not match expectation");
		System.out.println(handler.getLog());
	}
}

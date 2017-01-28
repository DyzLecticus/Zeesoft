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
		assertEqual(output.toString(),"Hello. My name is Dyz Lecticus. What is your name?","Output does not match expectation");
		output = handler.processInput(new StringBuilder("my name is Andre van der Zee"));
		System.out.println(handler.getLog());
	}
}

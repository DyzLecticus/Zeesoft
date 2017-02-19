package nl.zeesoft.zid.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zid.dialog.DialogHandler;

public abstract class TestDialogHandlerObject extends TestObject {
	private List<TestDialogHandlerScriptLine> script = new ArrayList<TestDialogHandlerScriptLine>();
	
	public TestDialogHandlerObject(Tester tester) {
		super(tester);
	}

	protected TestDialogHandlerScriptLine addScriptLine(String input, String output) {
		TestDialogHandlerScriptLine line = new TestDialogHandlerScriptLine();
		line.setInput(input);
		line.setOutput(output);
		script.add(line);
		return line;
	}
	
	protected void testScript(DialogHandler handler) {
		Date started = new Date();
		int l = 0;
		for (TestDialogHandlerScriptLine line: script) {
			l++;
			System.out.println("<<< " + line.getInput());
			ZStringSymbolParser output = handler.handleInput(new ZStringSymbolParser(line.getInput()));
			System.out.println(">>> " + output);
			if (l<10) {
				assertEqual(output.toString(),line.getOutput(),"Line  " + l + ": " + line.getMessage());
			} else {
				assertEqual(output.toString(),line.getOutput(),"Line " + l + ": " + line.getMessage());
			}
		}
		long time = ((new Date()).getTime() - started.getTime()) / 10; 
		System.out.println("Average time spent thinking per response: " + time + " ms");
	}
}

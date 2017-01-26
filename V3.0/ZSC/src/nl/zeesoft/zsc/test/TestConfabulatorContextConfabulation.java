package nl.zeesoft.zsc.test;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsc.Generic;
import nl.zeesoft.zsc.confabulator.ConfabulationObject;
import nl.zeesoft.zsc.confabulator.Confabulator;
import nl.zeesoft.zsc.confabulator.confabulations.ContextConfabulation;

public class TestConfabulatorContextConfabulation extends TestObject {
	public static void main(String[] args) {
		(new TestConfabulatorContextConfabulation()).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a trained *Confabulator* instance to confabulate one or more context symbols for a certain input sequence.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create confabulator");
		System.out.println("Confabulator confabulator = new Confabulator();");
		System.out.println("// Train confabulator");
		System.out.println("confabulator.learnSequence(\"Example symbol sequence.\",\"Optional Context Symbols\");");
		System.out.println("// Create ContextConfabulation");
		System.out.println("ContextConfabulation confabulation = new ContextConfabulation(\"Example\");");
		System.out.println("// Confabulate");
		System.out.println("confabulator.confabulate(confabulation);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + Tester.getInstance().getLinkForClass(TestConfabulatorContextConfabulation.class));
		System.out.println(" * " + Tester.getInstance().getLinkForClass(MockConfabulator.class));
		System.out.println(" * " + Tester.getInstance().getLinkForClass(Confabulator.class));
		System.out.println(" * " + Tester.getInstance().getLinkForClass(ContextConfabulation.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the confabulation input sequence, log summary, and output.  ");
		System.out.println("Please note how the context confabulation favours the most significant context symbol over the stronger associated context symbols in the *MockConfabulator*.  ");
	}

	@Override
	protected void test(String[] args) {
		Confabulator confabulator = (Confabulator) Tester.getInstance().getMockedObject(MockConfabulator.class.getName());
		
		ContextConfabulation confab = new ContextConfabulation("What is your name?");
		confab.setLogModuleSymbolLevels(true);
		testConfabulation(confabulator,confab);
		
		assertEqual(confab.getOutput().toString(),"Name","Confabulation output does not match expectation");
	}
	
	public static void testConfabulation(Confabulator confabulator,ConfabulationObject confab) {
		System.out.print("Confabulation input sequence: " + confab.getSequence());
		if (confab.getContext().length()>0) {
			System.out.print(" (context: " + confab.getContext() + ")");
		}
		System.out.println();
		Date start = new Date();
		long ms = 0;
		confabulator.confabulate(confab);
		ms = (new Date()).getTime() - start.getTime();
		List<StringBuilder> lines = Generic.stringBuilderSplit(confab.getLog(),"\n");
		if (lines.size()>100) {
			int i = 0;
			for (StringBuilder line: lines) {
				if (i<=30 || i>(lines.size()-30)) {
					System.out.println(line);
				} else if (i==31) {
					System.out.println();
					System.out.println("[ ... ]");
					System.out.println();
				}
				i++;
			}
		} else {
			System.out.print(confab.getLog());
		}
		System.out.println("Confabulation output: " + confab.getOutput() + " (" + ms + " ms)");
	}
}

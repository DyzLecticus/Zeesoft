package nl.zeesoft.zsc.test;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsc.confabulator.Confabulator;
import nl.zeesoft.zsc.confabulator.confabulations.ExtensionConfabulation;

public class TestConfabulatorExtensionConfabulation extends TestObject {
	public TestConfabulatorExtensionConfabulation(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestConfabulatorExtensionConfabulation(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a trained *Confabulator* instance to confabulate a starter sequence or an extension for an input sequence, optionally restricted by one or more context symbols.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create confabulator");
		System.out.println("Confabulator confabulator = new Confabulator();");
		System.out.println("// Train confabulator");
		System.out.println("confabulator.learnSequence(\"Example symbol sequence.\",\"Optional Context Symbols\");");
		System.out.println("// Create ExtensionConfabulation");
		System.out.println("ExtensionConfabulation confabulation = new ExtensionConfabulation(\"Optional sequence symbols\",\"Optional Context\");");
		System.out.println("// Confabulate");
		System.out.println("confabulator.confabulate(confabulation);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestConfabulatorExtensionConfabulation.class));
		System.out.println(" * " + getTester().getLinkForClass(MockConfabulator.class));
		System.out.println(" * " + getTester().getLinkForClass(Confabulator.class));
		System.out.println(" * " + getTester().getLinkForClass(ExtensionConfabulation.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the confabulation input sequence, log summary, and output for several confabulations;  ");
		System.out.println(" * One confabulation without an input sequence.  ");
		System.out.println(" * One confabulation without context.  ");
		System.out.println(" * Two confabulations with context.  ");
		System.out.println();
		System.out.println("Please note how the confabulated extensions depend on the (lack of) context.");
	}

	@Override
	protected void test(String[] args) {
		Confabulator confabulator = (Confabulator) getTester().getMockedObject(MockConfabulator.class.getName());

		// Test sequence start without context
		ExtensionConfabulation confab = new ExtensionConfabulation();
		confab.setMaxOutputSymbols(5);
		confab.setLogModuleSymbolLevels(true);
		TestConfabulatorContextConfabulation.testConfabulation(confabulator,confab);
		assertEqual(confab.getOutput().toString(),"What is your name ?","Confabulation output does not match expectation");

		System.out.println();

		// Test sequence without context
		confab.setSequence(new ZStringSymbolParser("What is artificial cognition?"));
		confab.setMaxOutputSymbols(16);
		TestConfabulatorContextConfabulation.testConfabulation(confabulator,confab);
		assertEqual(confab.getOutput().toString(),"My name is Dyz Lecticus .","Confabulation output does not match expectation");

		System.out.println();

		// Test sequence with context Cognition
		confab.setContext(new ZStringSymbolParser("Cognition"));
		TestConfabulatorContextConfabulation.testConfabulation(confabulator,confab);
		assertEqual(confab.getOutput().toString(),"Cognition refers to mental processes within the brain .","Confabulation output does not match expectation");

		System.out.println();

		// Test sequence with context Name
		confab.setContext(new ZStringSymbolParser("Artificial"));
		TestConfabulatorContextConfabulation.testConfabulation(confabulator,confab);
		assertEqual(confab.getOutput().toString(),"I am an artificial cognition .","Confabulation output does not match expectation");
	}
}

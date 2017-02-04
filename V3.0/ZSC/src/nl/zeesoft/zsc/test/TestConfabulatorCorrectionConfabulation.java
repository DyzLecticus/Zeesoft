package nl.zeesoft.zsc.test;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsc.confabulator.Confabulator;
import nl.zeesoft.zsc.confabulator.confabulations.CorrectionConfabulation;

public class TestConfabulatorCorrectionConfabulation extends TestObject {
	public TestConfabulatorCorrectionConfabulation(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestConfabulatorCorrectionConfabulation(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a trained *Confabulator* instance to confabulate a correction for a certain input sequence, optionally restricted by one or more context symbols.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create confabulator");
		System.out.println("Confabulator confabulator = new Confabulator();");
		System.out.println("// Train confabulator");
		System.out.println("confabulator.learnSequence(\"Example symbol sequence.\",\"Optional Context Symbols\");");
		System.out.println("// Create CorrectionConfabulation");
		System.out.println("CorrectionConfabulation confabulation = new CorrectionConfabulation(\"Example symbol bla.\",\"Optional Context\");");
		System.out.println("// Confabulate");
		System.out.println("confabulator.confabulate(confabulation);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestConfabulatorCorrectionConfabulation.class));
		System.out.println(" * " + getTester().getLinkForClass(MockConfabulator.class));
		System.out.println(" * " + getTester().getLinkForClass(Confabulator.class));
		System.out.println(" * " + getTester().getLinkForClass(CorrectionConfabulation.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the confabulation input sequence, log summary, and output for several confabulations;  ");
		System.out.println(" * One confabulation without context.  ");
		System.out.println(" * One confabulation with context.  ");
		System.out.println();
		System.out.println("Please note how the modules are used in the first confabulation to search through all possible symbol combinations.");
	}

	@Override
	protected void test(String[] args) {
		Confabulator confabulator = (Confabulator) getTester().getMockedObject(MockConfabulator.class.getName());
		
		CorrectionConfabulation confab = new CorrectionConfabulation("What is your bla?");
		confab.setLogModuleSymbolLevels(true);

		// Test without context
		TestConfabulatorContextConfabulation.testConfabulation(confabulator,confab);
		assertEqual(confab.getOutput().toString(),"What is your name ?","Confabulation output does not match expectation");
		assertEqual(confab.getCorrectionKeys().size(),1,"Number of correction keys not match expectation");
		assertEqual(confab.getCorrectionValues().size(),1,"Number of correction values not match expectation");

		System.out.println();

		// Test with context
		confab.setContext(new ZStringSymbolParser("Goal"));
		TestConfabulatorContextConfabulation.testConfabulation(confabulator,confab);
		assertEqual(confab.getOutput().toString(),"What is your goal ?","Confabulation output does not match expectation");
		assertEqual(confab.getCorrectionKeys().size(),1,"Number of correction keys not match expectation");
		assertEqual(confab.getCorrectionValues().size(),1,"Number of correction values not match expectation");
	}
}

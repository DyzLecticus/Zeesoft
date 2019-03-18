package nl.zeesoft.zsmc.test;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zsmc.confab.Confabulator;
import nl.zeesoft.zsmc.confab.confabs.ConfabulationObject;
import nl.zeesoft.zsmc.confab.confabs.ContextConfabulation;
import nl.zeesoft.zsmc.confab.confabs.CorrectionConfabulation;
import nl.zeesoft.zsmc.confab.confabs.ExtensionConfabulation;
import nl.zeesoft.zsmc.kb.KnowledgeBase;

public class TestConfabulator extends TestObject {
	public TestConfabulator(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestConfabulator(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/*
		 * TODO: Describe
		System.out.println("This test shows how to train a *Confabulator* to and use it to correct sequences, determine context, confabulate extensions and synonyms.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the confabulator");
		System.out.println("Confabulator conf = new Confabulator(new Config(),\"MockConfabulator\",4);");
		System.out.println("// Train the confabulator");
		System.out.println("conf.learnSequence(\"A sequence to learn.\",\"OptionalContextSymbolToAssociate1 ContextSymbol2\");");
		System.out.println("conf.calculateProbabilities();");
		System.out.println("// Create a correction confabulation");
		System.out.println("CorrectionConfabulation confab1 = new CorrectionConfabulation();");
		System.out.println("confab1.input.append(\"A sequence to correct\");");
		System.out.println("// Confabulate the correction");
		System.out.println("conf.confabulate(confab1);");
		System.out.println("// Create a context confabulation");
		System.out.println("ContextConfabulation confab2 = new ContextConfabulation();");
		System.out.println("confab2.input.append(\"A sequence to determine context for\");");
		System.out.println("// Confabulate the context");
		System.out.println("conf.confabulate(confab2);");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockKnowledgeBase.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestKnowledgeBase.class));
		System.out.println(" * " + getTester().getLinkForClass(MockKnowledgeBase.class));
		System.out.println(" * " + getTester().getLinkForClass(Confabulator.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * Some details about the trained confabulator  ");
		System.out.println(" * The result of some confabulations  ");
		 */
	}
	
	@Override
	protected void test(String[] args) {
		KnowledgeBase kb = (KnowledgeBase) getTester().getMockedObject(MockKnowledgeBase.class.getName());
		Config conf = new Config();
		Confabulator confabulator = new Confabulator(conf.getMessenger(),conf.getUnion(),kb,"");
		
		ExtensionConfabulation exConfab = new ExtensionConfabulation();
		exConfab.appendLog = true;
		exConfab.input.append("brain structures");
		exConfab.extend = 7;
		confabulate(confabulator,exConfab);
		assertEqual(exConfab.extension.toString(),", modules, emerged that could be","Extension not match expectation");

		exConfab = new ExtensionConfabulation();
		exConfab.appendLog = true;
		exConfab.input.append("My goal");
		exConfab.extend = 7;
		confabulate(confabulator,exConfab);
		assertEqual(exConfab.extension.toString(),"is to understand and help people.","Extension not match expectation");

		CorrectionConfabulation corConfab = new CorrectionConfabulation();
		corConfab.appendLog = true;
		corConfab.input.append("My game is");
		confabulate(confabulator,corConfab);
		assertEqual(corConfab.corrected.toString(),"My name is","Extension not match expectation");

		ContextConfabulation conConfab = new ContextConfabulation();
		conConfab.appendLog = true;
		conConfab.input.append("My name is");
		confabulate(confabulator,conConfab);
		assertEqual(conConfab.results.size(),1,"Number of contexts not match expectation");
	}
	
	private void confabulate(Confabulator confabulator,ConfabulationObject confab) {
		System.out.println("Confabulating ...");
		confabulator.confabulate(confab);
		System.out.println(confab.log);
	}
}

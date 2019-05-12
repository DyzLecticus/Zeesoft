package nl.zeesoft.zsmc.test;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zsmc.confab.ConfabulationObject;
import nl.zeesoft.zsmc.confab.Confabulator;
import nl.zeesoft.zsmc.confab.ContextConfabulation;
import nl.zeesoft.zsmc.confab.CorrectionConfabulation;
import nl.zeesoft.zsmc.confab.ExtensionConfabulation;
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
		System.out.println("This test shows how to create a *Confabulator* and use it to correct sequences, determine context and confabulate extensions.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the knowledge base");
		System.out.println("KnowledgeBase kb = new KnowledgeBase(new Config(),4);");
		System.out.println("// Train the knowledge base");
		System.out.println("kb.learnSequence(\"A sequence to learn.\",\"OptionalContextSymbolToAssociate1 ContextSymbol2\");");
		System.out.println("kb.calculateProbabilities();");
		System.out.println("// Create the confabulator");
		System.out.println("Confabulator conf = new Confabulator(new Messenger(),new WorkerUnion(),kb);");
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
		System.out.println(" * " + getTester().getLinkForClass(ExtensionConfabulation.class));
		System.out.println(" * " + getTester().getLinkForClass(CorrectionConfabulation.class));
		System.out.println(" * " + getTester().getLinkForClass(ContextConfabulation.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the logs of some confabulations.  ");
	}
	
	@Override
	protected void test(String[] args) {
		KnowledgeBase kb = (KnowledgeBase) getTester().getMockedObject(MockKnowledgeBase.class.getName());
		Config conf = new Config();
		Confabulator confabulator = new Confabulator(conf.getMessenger(),conf.getUnion(),kb);
		
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

		exConfab = new ExtensionConfabulation();
		exConfab.appendLog = true;
		exConfab.input.append("Confabulation");
		exConfab.extend = 16;
		confabulate(confabulator,exConfab);
		assertEqual(exConfab.extension.toSymbolsPunctuated().size(),16,"Number of symbols not match expectation");

		CorrectionConfabulation corConfab = new CorrectionConfabulation();
		corConfab.appendLog = true;
		corConfab.input.append("My game is");
		confabulate(confabulator,corConfab);
		assertEqual(corConfab.corrected.toString(),"My name is","Correction not match expectation");

		corConfab = new CorrectionConfabulation();
		corConfab.appendLog = true;
		corConfab.input.append("bran");
		confabulate(confabulator,corConfab);
		assertEqual(corConfab.corrected.toString(),"brain","Correction not match expectation");

		ContextConfabulation conConfab = new ContextConfabulation();
		conConfab.appendLog = true;
		conConfab.input.append("My name is");
		confabulate(confabulator,conConfab);
		assertEqual(conConfab.results.size(),1,"Number of contexts not match expectation");

		conConfab = new ContextConfabulation();
		conConfab.appendLog = true;
		conConfab.input.append("brain");
		confabulate(confabulator,conConfab);
		assertEqual(conConfab.results.size(),1,"Number of contexts not match expectation");
	}
	
	private void confabulate(Confabulator confabulator,ConfabulationObject confab) {
		System.out.println("Confabulating ...");
		confabulator.confabulate(confab);
		System.out.println(confab.log);
	}
}
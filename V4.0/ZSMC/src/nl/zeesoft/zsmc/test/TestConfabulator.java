package nl.zeesoft.zsmc.test;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zsmc.confab.Confabulator;
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
		exConfab.input.append("brain");
		
		
		confabulator.confabulate(exConfab);
		/*
		System.out.println("Confabulator max. distance: " + kb.getMaxDistance());
		
		KbContext def = kb.getContext("");
		KbContext self = kb.getContext("Self");

		assertEqual(def.totalSymbols,181,"Total symbols for default context does not match expectation");
		assertEqual(def.totalLinks,1014,"Total links for default context does not match expectation");
		assertEqual(self.totalSymbols,37,"Total symbols for 'Self' context does not match expectation");
		assertEqual(self.totalLinks,152,"Total links for 'Self' context does not match expectation");
		
		System.out.println("Symbols/links for default context: " + def.totalSymbols + "/" + def.totalLinks);
		System.out.println("Symbols/links for 'Name' context: " + self.totalSymbols + "/" + self.totalLinks);
		System.out.println("Symbol/link bandwidth for default context: " + def.symbolBandwidth + "/" + def.linkBandwidth);
		System.out.println("Symbol to link bandwidth factor for default context: " + def.symbolToLinkBandwidthFactor);
		*/
	}
}

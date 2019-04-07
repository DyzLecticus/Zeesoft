package nl.zeesoft.zsmc.test;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsmc.kb.KbContext;
import nl.zeesoft.zsmc.kb.KnowledgeBase;

public class TestKnowledgeBase extends TestObject {
	public TestKnowledgeBase(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestKnowledgeBase(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to train a *KnowledgeBase* and use it to learn context sensitive symbol sequences.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the knowledge base");
		System.out.println("KnowledgeBase kb = new KnowledgeBase(new Messenger(),4);");
		System.out.println("// Train the knowledge base");
		System.out.println("kb.learnSequence(\"A sequence to learn.\",\"OptionalContextSymbolToAssociate1 ContextSymbol2\");");
		System.out.println("kb.calculateProbabilities();");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockKnowledgeBase.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestKnowledgeBase.class));
		System.out.println(" * " + getTester().getLinkForClass(MockKnowledgeBase.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows some details about the trained confabulator.  ");
	}
	
	@Override
	protected void test(String[] args) {
		KnowledgeBase kb = (KnowledgeBase) getTester().getMockedObject(MockKnowledgeBase.class.getName());
		System.out.println("Knowledge base max. distance: " + kb.getMaxDistance());
		
		KbContext def = kb.getContext("");
		KbContext self = kb.getContext("Self");

		assertEqual(def.totalSymbols,490,"Total symbols for default context does not match expectation");
		assertEqual(def.totalLinks,4004,"Total links for default context does not match expectation");
		assertEqual(self.totalSymbols,37,"Total symbols for 'Self' context does not match expectation");
		assertEqual(self.totalLinks,152,"Total links for 'Self' context does not match expectation");
		
		System.out.println("Symbols/links for default context: " + def.totalSymbols + "/" + def.totalLinks);
		System.out.println("Symbols/links for 'Name' context: " + self.totalSymbols + "/" + self.totalLinks);
		System.out.println("Symbol/link bandwidth for default context: " + def.symbolBandwidth + "/" + def.linkBandwidth);
		System.out.println("Symbol to link bandwidth factor for default context: " + def.symbolToLinkBandwidthFactor);
	}
}

package nl.zeesoft.zsmc.test;

import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsmc.confabulator.KnowledgeBase;
import nl.zeesoft.zsmc.confabulator.KnowledgeBases;
import nl.zeesoft.zsmc.confabulator.KnowledgeLink;

public class TestNLQnAKnowledgeBases extends TestObject {
	public TestNLQnAKnowledgeBases(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestNLQnAKnowledgeBases(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/*
		System.out.println("This test shows how to use the *SpellingChecker* to correct word spelling.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the SpellingChecker");
		System.out.println("SpellChecker checker = new SpellingChecker();");
		System.out.println("// Initialize the SpellingChecker");
		System.out.println("checker.initialize(new ZStringSymbolParser(\"Some text containing correctly spelled words.\"));");
		System.out.println("// Use SpellingChecker to correct a word");
		System.out.println("String correction = checker.correct(\"contaning\");");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("This encoding mechanism can be used to encode and decode passwords and other sensitive data.");
		System.out.println("The minimum key length is 64. Longer keys provide stronger encoding.");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestKnowledgeBases.class));
		System.out.println(" * " + getTester().getLinkForClass(SpellingChecker.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the number of generated variations and corrections for certain words.");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		KnowledgeBases kbs = (KnowledgeBases) getTester().getMockedObject(MockNLQnAKnowledgeBases.class.getName());
		assertEqual(kbs.getKnowledgeBases().size(),7,"The total number knowledge bases does not match expectation");
		
		int links = 0;
		int kbi = 0;
		for (KnowledgeBase kb: kbs.getKnowledgeBases()) {
			kbi++;
			int show = 0;
			for (Entry<String,List<KnowledgeLink>> entry: kb.getLinksBySource().entrySet()) {
				for (KnowledgeLink link: entry.getValue()) {
					if (show<3) {
						System.out.println("Knowledge base: " + kbi + ", s -> t: " + link.source + " -> " + link.target + ", count: " + link.count + ", prob: " + link.prob + ", sourceWeight: " + link.sourceWeight  + ", targetWeight: " + link.targetWeight);
					} else if (show==3) {
						System.out.println("... ");
					}
					show++;
					links++;
				}
			}
			System.out.println();
		}
		int show = 0;
		for (Entry<String,List<KnowledgeLink>> entry: kbs.getContext().getLinksBySource().entrySet()) {
			for (KnowledgeLink link: entry.getValue()) {
				if (show<3) {
					System.out.println("Context knowledge base: s -> t: " + link.source + " -> " + link.target + ", count: " + link.count + ", prob: " + link.prob + ", sourceWeight: " + link.sourceWeight  + ", targetWeight: " + link.targetWeight);
				} else if (show==3) {
					System.out.println("... ");
				}
				links++;
				show++;
			}
		}
		assertEqual(links,661315,"The total number of knowledge links not match expectation");
	}
}

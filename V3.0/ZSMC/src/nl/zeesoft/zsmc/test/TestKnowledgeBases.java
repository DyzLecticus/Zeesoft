package nl.zeesoft.zsmc.test;

import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsmc.confabulator.KnowledgeBase;
import nl.zeesoft.zsmc.confabulator.KnowledgeBases;
import nl.zeesoft.zsmc.confabulator.KnowledgeLink;

public class TestKnowledgeBases extends TestObject {
	public TestKnowledgeBases(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestKnowledgeBases(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how a *KnowledgeBases* instance can be used to learn text symbol knowledge links.");
		System.out.println("A knowledge base is a collection of symbol links (probabilities) over a certain module distance.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the SpellingChecker");
		System.out.println("KnowledgeBases kbs = new KnowledgeBases();");
		System.out.println("// Initialize the SpellingChecker");
		System.out.println("kbs.initialize(new ZStringSymbolParser(\"Some text containing correctly spelled words.\"));");
		System.out.println("// Use SpellingChecker to correct a word");
		System.out.println("String correction = checker.correct(\"contaning\");");
		System.out.println("~~~~");
		getTester().describeMock(MockKnowledgeBases.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestKnowledgeBases.class));
		System.out.println(" * " + getTester().getLinkForClass(MockKnowledgeBases.class));
		System.out.println(" * " + getTester().getLinkForClass(KnowledgeBases.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the number of generated variations and corrections for certain words.");
	}
	
	@Override
	protected void test(String[] args) {
		KnowledgeBases kbs = (KnowledgeBases) getTester().getMockedObject(MockKnowledgeBases.class.getName());
		assertEqual(kbs.getKnowledgeBases().size(),7,"The total number of forward knowledge bases not match expectation");
		int kbi = 0;
		int links = 0;
		List<KnowledgeBase> list = kbs.getKnowledgeBases();
		for (KnowledgeBase kb: list) {
			kbi++;
			int kblS = 0;
			int kblT = 0;
			for (Entry<String,List<KnowledgeLink>> entry: kb.getLinksBySource().entrySet()) {
				for (KnowledgeLink link: entry.getValue()) {
					System.out.println("Knowledge base: " + kbi + ", s -> t: " + link.source + " -> " + link.target + ", count: " + link.count + ", prob: " + link.prob);
					links++;
					kblS++;
				}
			}
			for (Entry<String,List<KnowledgeLink>> entry: kb.getLinksBySource().entrySet()) {
				kblT += entry.getValue().size();
			}
			System.out.println("Knowledge base: " + kbi + ", total links: " + kblS);
			assertEqual(kblT,kblS,"The number of knowledge base target knowledge links not match expectation");
		}
		assertEqual(links,407,"The total number of knowledge links not match expectation");
	}
}

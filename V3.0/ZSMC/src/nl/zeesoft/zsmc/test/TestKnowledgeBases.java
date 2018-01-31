package nl.zeesoft.zsmc.test;

import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsmc.confabulator.Confabulator;
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
		System.out.println("Knowledge bases are required to create a *Confabulator* instance.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the KnowledgeBases");
		System.out.println("KnowledgeBases kbs = new KnowledgeBases();");
		System.out.println("// Initialize the KnowledgeBases");
		System.out.println("kbs.initialize(new ZStringSymbolParser(\"Some text containing correctly spelled words.\"));");
		System.out.println("~~~~");
		getTester().describeMock(MockKnowledgeBases.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestKnowledgeBases.class));
		System.out.println(" * " + getTester().getLinkForClass(Confabulator.class));
		System.out.println(" * " + getTester().getLinkForClass(MockKnowledgeBases.class));
		System.out.println(" * " + getTester().getLinkForClass(KnowledgeBases.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the content of the knowledge bases.");
	}
	
	@Override
	protected void test(String[] args) {
		KnowledgeBases kbs = (KnowledgeBases) getTester().getMockedObject(MockKnowledgeBases.class.getName());
		assertEqual(kbs.getKnowledgeBases().size(),7,"The total number of knowledge bases not match expectation");
		int kbi = 0;
		int links = 0;
		List<KnowledgeBase> list = kbs.getKnowledgeBases();
		for (KnowledgeBase kb: list) {
			kbi++;
			int kblS = 0;
			int kblT = 0;
			int show = 0;
			for (Entry<String,List<KnowledgeLink>> entry: kb.getLinksBySource().entrySet()) {
				for (KnowledgeLink link: entry.getValue()) {
					if (show<3) {
						System.out.println("Knowledge base: " + kbi + ", s -> t: " + link.source + " -> " + link.target + ", count: " + link.count + ", prob: " + link.prob + ", sourceWeight: " + link.sourceWeight  + ", targetWeight: " + link.targetWeight);
					} else if (show==3) {
						System.out.println("... ");
					}
					links++;
					kblS++;
					show++;
				}
			}
			for (Entry<String,List<KnowledgeLink>> entry: kb.getLinksByTarget().entrySet()) {
				kblT += entry.getValue().size();
			}
			System.out.println("Knowledge base: " + kbi + ", total links: " + kblS);
			assertEqual(kblT,kblS,"The number of knowledge base target knowledge links not match expectation");
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
		assertEqual(links,407,"The total number of knowledge links not match expectation");
	}
}

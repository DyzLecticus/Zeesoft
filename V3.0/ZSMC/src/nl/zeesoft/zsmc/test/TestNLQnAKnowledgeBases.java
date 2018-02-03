package nl.zeesoft.zsmc.test;

import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsmc.confabulator.KnowledgeBases;

public class TestNLQnAKnowledgeBases extends TestKnowledgeBases {
	public TestNLQnAKnowledgeBases(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestNLQnAKnowledgeBases(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how a *KnowledgeBases* instance can be used to learn text symbol knowledge links.");
		System.out.println("Knowledge bases can be initialized using an input file name.");
		System.out.println("The formatting of the file determines how it will be parsed.");
		System.out.println("Question and Answer (and optional Context symbol) (tab separated values) formatting will assume the first line is a header so it will not be parsed.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the KnowledgeBases");
		System.out.println("KnowledgeBases kbs = new KnowledgeBases();");
		System.out.println("// Initialize the KnowledgeBases");
		System.out.println("String err = kbs.initialize(\"path/filename.txt\");");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockNLQnAKnowledgeBases.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestNLQnAKnowledgeBases.class));
		System.out.println(" * " + getTester().getLinkForClass(MockNLQnAKnowledgeBases.class));
		System.out.println(" * " + getTester().getLinkForClass(KnowledgeBases.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows a summary of the content of the knowledge bases.");
	}
	
	@Override
	protected void test(String[] args) {
		KnowledgeBases kbs = (KnowledgeBases) getTester().getMockedObject(MockNLQnAKnowledgeBases.class.getName());
		testKnowledgeBases(kbs,7,707285);
	}
}

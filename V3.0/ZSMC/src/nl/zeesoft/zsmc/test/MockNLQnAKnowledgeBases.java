package nl.zeesoft.zsmc.test;

import java.util.Date;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zsmc.confabulator.KnowledgeBases;

public class MockNLQnAKnowledgeBases extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockNLQnAKnowledgeBases*.");
	}

	@Override
	protected Object initialzeMock() {
		Date started = new Date();
		KnowledgeBases kbs = new KnowledgeBases();
		System.out.println("Initializing mock NL QnA knowledge bases ... ");
		kbs.initialize("resources/nl-qna.txt");
		System.out.println("Initializing mock NL QnA knowledge bases took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		return kbs;
	}
}

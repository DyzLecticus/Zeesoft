package nl.zeesoft.zsmc.test;

import java.util.Date;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zsmc.confabulator.KnowledgeBases;

public class MockKnowledgeBases extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockKnowledgeBases*.");
	}

	@Override
	protected Object initialzeMock() {
		Date started = new Date();
		KnowledgeBases kbs = new KnowledgeBases();
		kbs.setMinCount(0);
		kbs.initialize(new ZStringSymbolParser(TestSymbolCorrector.TEST_SEQUENCE));
		System.out.println("Initializing mock knowledge bases took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		return kbs;
	}
}

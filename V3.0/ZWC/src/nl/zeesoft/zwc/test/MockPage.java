package nl.zeesoft.zwc.test;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zwc.page.PageReader;

public class MockPage extends MockObject {
	public static final String TEST_URL = "http://www.w3.org/TR/html401/";
	
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockPage*. It is used to read and share the page at " + TEST_URL + " for tests.");
	}
	
	@Override
	protected Object initialzeMock() {
		ZDKFactory factory = new ZDKFactory();
		Messenger messenger = factory.getMessenger();
		messenger.start();
		PageReader reader = new PageReader(messenger);
		ZStringBuilder page = reader.getPageAtUrl(TEST_URL);
		messenger.stop();
		factory.getWorkerUnion(messenger).stopWorkers();
		messenger.whileWorking();
		return page;
	}
}

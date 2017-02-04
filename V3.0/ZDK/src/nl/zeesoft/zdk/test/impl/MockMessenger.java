package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.messenger.MessageObject;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.test.MockObject;

public class MockMessenger extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockMessenger*.");
	}

	@Override
	protected Object initialzeMock() {
		Messenger msgr = new Messenger(null) {
			@Override
			protected void printMessage(MessageObject msg, boolean error) {
				super.printMessage(msg,false);
			}
		};
		return msgr;
	}
}

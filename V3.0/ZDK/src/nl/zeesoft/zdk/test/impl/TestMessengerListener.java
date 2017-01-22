package nl.zeesoft.zdk.test.impl;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.MessageObject;
import nl.zeesoft.zdk.messenger.MessengerListener;

public class TestMessengerListener implements MessengerListener {
	private List<MessageObject> messages = new ArrayList<MessageObject>();

	@Override
	public void printedMessage(MessageObject msg) {
		messages.add(msg);
	}

	public List<MessageObject> getMessages() {
		return messages;
	}
}

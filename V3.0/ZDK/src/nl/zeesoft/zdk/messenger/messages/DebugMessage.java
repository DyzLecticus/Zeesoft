package nl.zeesoft.zdk.messenger.messages;

import nl.zeesoft.zdk.messenger.MessageObject;

/**
 * Debug message.
 */
public final class DebugMessage extends MessageObject {
	public DebugMessage(Object source,String message) {
		super(source,message);
	}

	private DebugMessage() {
		// Only used for copying
	}
	
	@Override
	public String getType() {
		return "DBG";
	}

	@Override
	public MessageObject getCopy() {
		DebugMessage msg = new DebugMessage();
		copyDataToMessageObject(msg);
		return msg;
	}
}

package nl.zeesoft.zdk.messenger.messages;

import nl.zeesoft.zdk.messenger.MessageObject;

/**
 * Warning message.
 */
public final class WarningMessage extends MessageObject {
	public WarningMessage(Object source,String message) {
		super(source,message);
	}

	private WarningMessage() {
		// Only used for copying
	}
	
	@Override
	public String getType() {
		return "WRN";
	}

	@Override
	public MessageObject getCopy() {
		WarningMessage msg = new WarningMessage();
		copyDataToMessageObject(msg);
		return msg;
	}
}

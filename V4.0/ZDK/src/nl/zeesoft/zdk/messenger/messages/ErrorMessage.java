package nl.zeesoft.zdk.messenger.messages;

import nl.zeesoft.zdk.messenger.MessageObject;

/**
 * Error message.
 */
public final class ErrorMessage extends MessageObject {
	public ErrorMessage(Object source,String message) {
		super(source,message);
	}

	private ErrorMessage() {
		// Only used for copying
	}
	
	@Override
	public String getType() {
		return "ERR";
	}

	@Override
	public MessageObject getCopy() {
		ErrorMessage msg = new ErrorMessage();
		copyDataToMessageObject(msg);
		return msg;
	}
}

package nl.zeesoft.zdk.messenger;

/**
 * Error message.
 */
public final class MessageError extends MessageObject {
	public MessageError(Object source,String message) {
		super(source,message);
	}

	private MessageError() {
		// Only used for copying
	}
	
	@Override
	public String getType() {
		return "ERR";
	}

	@Override
	public MessageObject getCopy() {
		MessageError msg = new MessageError();
		copyDataToMessageObject(msg);
		return msg;
	}
}

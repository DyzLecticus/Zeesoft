package nl.zeesoft.zdk.messenger;

/**
 * Warning message.
 */
public final class MessageWarning extends MessageObject {
	public MessageWarning(Object source,String message) {
		super(source,message);
	}

	private MessageWarning() {
		// Only used for copying
	}
	
	@Override
	public String getType() {
		return "WRN";
	}

	@Override
	public MessageObject getCopy() {
		MessageWarning msg = new MessageWarning();
		copyDataToMessageObject(msg);
		return msg;
	}
}

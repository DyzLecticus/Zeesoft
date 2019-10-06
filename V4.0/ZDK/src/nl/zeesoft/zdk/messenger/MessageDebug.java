package nl.zeesoft.zdk.messenger;

/**
 * Debug message.
 */
public final class MessageDebug extends MessageObject {
	public MessageDebug(Object source,String message) {
		super(source,message);
	}

	private MessageDebug() {
		// Only used for copying
	}
	
	@Override
	public String getType() {
		return "DBG";
	}

	@Override
	public MessageObject getCopy() {
		MessageDebug msg = new MessageDebug();
		copyDataToMessageObject(msg);
		return msg;
	}
}

package nl.zeesoft.zdk.messenger;

/**
 * Classes that implement the MessengerListener interface can subscribe to Messenger message printing events.
 */
public interface MessengerListener {
	public abstract void printedMessage(MessageObject msg);
}

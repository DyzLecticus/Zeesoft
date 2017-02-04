package nl.zeesoft.zdk.messenger;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.messages.DebugMessage;
import nl.zeesoft.zdk.messenger.messages.ErrorMessage;
import nl.zeesoft.zdk.messenger.messages.WarningMessage;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

/**
 * A Messenger can be used to log debug, warning and error messages and print them to the standard and/or error out.
 * It implements the Worker class to minimize wait time impact for threads that call the Messenger.
 * Classes that implement the MessengerListener interface can subscribe to Messenger message printing events.
 * The Messenger is thread safe so it can be shared across the entire application.
 */
public class Messenger extends Worker  {
	private List<MessageObject>			messages			= new ArrayList<MessageObject>();
	private List<MessengerListener>		listeners			= new ArrayList<MessengerListener>();

	private boolean						printDebugMessages	= false;
	
	private boolean						warning				= false;
	private boolean						error				= false;
	
	public Messenger(WorkerUnion union) {
		super(null,union);
		setSleep(100);
	}

	/**
	 * Adds an error message to the buffer.
	 * 
	 * @param source The source of the message
	 * @param message The message
	 */
	public void error(Object source, String message) {
		ErrorMessage msg = new ErrorMessage(source,message);
		addMessage(msg);
	}
	
	/**
	 * Adds a warning message to the buffer.
	 * 
	 * @param source The source of the message
	 * @param message The message
	 */
	public void warn(Object source, String message) {
		WarningMessage msg = new WarningMessage(source,message);
		addMessage(msg);
	}
	
	/**
	 * Adds a debug message to the buffer.
	 * 
	 * @param source The source of the message
	 * @param message The message
	 */
	public void debug(Object source, String message) {
		DebugMessage msg = new DebugMessage(source,message);
		addMessage(msg);
	}

	/**
	 * Prints all messages in the buffer.
	 * Notifies all MessageListener instances.
	 * Clears the message buffer.
	 */
	@Override
	public void whileWorking() {
		printMessages();
		printedMessages(messages);
		flushMessages();
	}
	
	/**
	 * Stops the Messenger.
	 * 
	 * Waits a maximum of one second for the Messenger to stop. 
	 */
	@Override
	public void stop() {
		super.stop();
		waitForStop(1,true);
	}
	
	/**
	 * Prints all messages in the buffer.
	 */
	public void printMessages() {
		lockMe(this);
		if (messages.size()>0) {
			for (MessageObject msg: messages) {
				if (msg instanceof ErrorMessage) {
					printMessage(msg,true);
				} else if (!(msg instanceof DebugMessage) || printDebugMessages) {
					printMessage(msg,false);
				}
			}
		}
		unlockMe(this);
	}
	
	/**
	 * Clears the message buffer.
	 */
	public void flushMessages() {
		lockMe(this);
		messages.clear();
		unlockMe(this);
	}

	/**
	 * Returns the messages in the buffer.
	 * 
	 * @return The messages in the buffer
	 */
	public List<MessageObject> getMessages() {
		List<MessageObject> r = new ArrayList<MessageObject>();
		lockMe(this);
		if (messages.size()>0) {
			for (MessageObject msg: messages) {
				r.add(msg.getCopy());
			}
		}
		unlockMe(this);
		return r;
	}

	/**
	 * Notifies attached listeners that a message has been printed.
	 * 
	 * @param msg The message that has been printed
	 */
	public void printedMessage(MessageObject msg) {
		lockMe(this);
		if (listeners.size()>0) {
			for (MessengerListener listener: listeners) {
				if (!(msg instanceof DebugMessage) || printDebugMessages) {
					listener.printedMessage(msg);
				}
			}
		}
		unlockMe(this);
	}
	
	/**
	 * Notifies the Messenger a certain set of messages has been printed.
	 * 
	 * @param messages The list of printed messages
	 */
	public void printedMessages(List<MessageObject> messages) {
		if (messages.size()>0) {
			for (MessageObject msg: messages) {
				printedMessage(msg);
			}
		}
	}

	/**
	 * Returns true if a warning message has been added to the Messenger.
	 * 
	 * @return true if a warning message has been added to the Messenger
	 */
	public boolean isWarning() {
		boolean r = false;
		lockMe(this);
		r = warning;
		unlockMe(this);
		return r;
	}
	
	/**
	 * Returns true if an error message has been added to the Messenger.
	 * 
	 * @return true if an error message has been added to the Messenger
	 */
	public boolean isError() {
		boolean r = false;
		lockMe(this);
		r = error;
		unlockMe(this);
		return r;
	}
	
	/**
	 * Attaches a MessengerListener to the Messenger.
	 * 
	 * @param listener The listener
	 */
	public void addListener(MessengerListener listener) {
		lockMe(this);
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
		unlockMe(this);
	}

	/**
	 * Enables or disables printing of debug messages.
	 * 
	 * @param printDebugMessages Indicates debug messages will be printed
	 */
	public void setPrintDebugMessages(boolean printDebugMessages) {
		lockMe(this);
		this.printDebugMessages = printDebugMessages;
		unlockMe(this);
	}

	protected void printMessage(MessageObject msg, boolean error) {
		System.out.println(msg);
		if (error) {
			System.err.println(msg);
		}
	}

	private void addMessage(MessageObject msg) {
		lockMe(msg.getSource());
		messages.add(msg);
		if (messages.size()>1000) {
			messages.remove(0);
		}
		if (msg instanceof ErrorMessage) {
			error = true;
		} else if (msg instanceof WarningMessage) {
			warning = true;
		}
		unlockMe(msg.getSource());
	}
}

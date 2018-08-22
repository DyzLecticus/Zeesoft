package nl.zeesoft.zdk.messenger;

import java.util.Date;

import nl.zeesoft.zdk.ZDate;

/**
 * Abstract message object.
 */
public abstract class MessageObject {
	private ZDate	date		= new ZDate();
	private Object	source		= null;
	private String	message		= "";
	
	public MessageObject(Object source,String message) {
		this.source = source;
		this.message = message;
	}

	protected MessageObject() {
		
	}

	/**
	 * Returns the date the message was created.
	 * 
	 * @return the date the message was created
	 */
	public ZDate getDate() {
		return date;
	}

	/**
	 * Returns the source of the message.
	 * 
	 * @return the source of the message
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * Returns the message.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns a complete message string.
	 * 
	 * @return the complete message string
	 */
	@Override
	public String toString() {
		StringBuilder msg = new StringBuilder();
		msg.append(getDate().getDateTimeString());
		msg.append(" ");
		msg.append(getType());
		msg.append(" ");
		msg.append(getSource().getClass().getName());
		msg.append(":");
		msg.append(" ");
		msg.append(getMessage());
		return msg.toString();
	}
	
	/**
	 * Return a short string representation of the message type. 
	 * 
	 * @return a short string representation of the message type
	 */
	public abstract String getType();

	/**
	 * Return a copy of the message object.
	 * 
	 * @return a copy of the message object
	 */
	public abstract MessageObject getCopy();

	/**
	 * Copies the data from this message to another.
	 * 
	 * @param copy The message object to copy data to
	 */
	protected void copyDataToMessageObject(MessageObject copy) {
		Date d = new Date();
		d.setTime(getDate().getDate().getTime());
		copy.setDate(d);
		copy.setSource(getSource());
		copy.setMessage(new String(getMessage()));
	}
	
	/**
	 * Sets the message creation date.
	 * 
	 * @param date The message creation date
	 */
	protected void setDate(Date date) {
		this.date.setDate(date);
	}

	/**
	 * Sets the message source.
	 * 
	 * @param source The message source
	 */
	protected void setSource(Object source) {
		this.source = source;
	}

	/**
	 * Sets the message.
	 * 
	 * @param message The message
	 */
	protected void setMessage(String message) {
		this.message = message;
	}
}

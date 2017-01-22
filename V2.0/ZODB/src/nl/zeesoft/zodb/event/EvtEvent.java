package nl.zeesoft.zodb.event;

import java.util.Date;

public class EvtEvent {
	public String type 		= "";
	public Object source 	= null;
	public Object value 	= "";
	public Date	datetime 	= new Date();
	
	public EvtEvent(String type, Object source, Object value) {
		this.type = type;
		this.source = source;
		this.value = value;
	}

	public EvtEvent(String type, Object source, String value) {
		this.type = type;
		this.source = source;
		this.value = value;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the source
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @return the datetime
	 */
	public Date getDatetime() {
		return datetime;
	}
}

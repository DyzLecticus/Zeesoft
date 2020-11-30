package nl.zeesoft.zdbd;

public class Event {
	protected Object	source		= null;
	protected String	name		= "";
	protected Object	param		= null;
	
	public Event(Object source, String name, Object param) {
		this.source = source;
		this.name = name;
		this.param = param;
	}
}

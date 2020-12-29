package nl.zeesoft.zdbd.theme;

public class Event {
	public Object	source		= null;
	public String	name		= "";
	public Object	param		= null;
	
	public Event(Object source, String name, Object param) {
		this.source = source;
		this.name = name;
		this.param = param;
	}
}

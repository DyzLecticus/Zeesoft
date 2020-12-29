package nl.zeesoft.zdbd.theme;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;

public class EventPublisher {
	private Lock							lock		= new Lock();
	private List<EventListener>				listeners	= new ArrayList<EventListener>();
	
	public void addListener(EventListener listener) {
		lock.lock(this);
		listeners.add(listener);
		lock.unlock(this);
	}
	
	public void removeListener(EventListener listener) {
		lock.lock(this);
		listeners.remove(listener);
		lock.unlock(this);
	}
	
	public void publishEvent(Object source, String name) {
		publishEvent(source,name,null);
	}
	
	public void publishEvent(Object source, String name, Object param) {
		lock.lock(this);
		List<EventListener> list = new ArrayList<EventListener>(listeners);
		lock.unlock(this);
		Event event = new Event(source, name, param);
		for (EventListener listener: list) {
			try {
				listener.handleEvent(event);
			} catch(Exception e) {
				Logger.err(this, new Str("Caught event listener exception"), e);
			}
		}
	}

	public RunCode getPublishEventRunCode(Object source, String name) {
		return getPublishEventRunCode(source, name, null);
	}
	
	public RunCode getPublishEventRunCode(Object source, String name, Object param) {
		RunCode r = new RunCode() {
			@Override
			protected boolean run() {
				Event event = (Event) params[0];
				publishEvent(event.source,event.name,event.param);
				return true;
			}
		};
		r.params[0] = new Event(source, name, param);
		return r;
	}
}

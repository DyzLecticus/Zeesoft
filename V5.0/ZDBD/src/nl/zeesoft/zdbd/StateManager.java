package nl.zeesoft.zdbd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.Lock;

public class StateManager {
	private Lock							lock		= new Lock();
	private HashMap<String,StateObject>		objects		= new HashMap<String,StateObject>();
	private List<StateListener>				listeners	= new ArrayList<StateListener>();
	
	public void addListener(StateListener listener) {
		lock.lock(this);
		listeners.add(listener);
		lock.unlock(this);
	}
	
	public void putObject(String name, Object object) {
		putObject(name, object, false);
	}
	
	public void putAndPublishObject(String name, Object object) {
		putObject(name, object, true);
	}
	
	public void changedStateObject(String name, HashMap<String,Object> params) {
		lock.lock(this);
		StateObject obj = objects.get(name);
		if (obj!=null) {
			obj.changed = System.currentTimeMillis();
		}
		List<StateListener> list = new ArrayList<StateListener>(listeners);
		lock.unlock(this);
		publishObjectChange(list,name,params);
	}
	
	public Object getObject(String name) {
		Object r = null;
		lock.lock(this);
		StateObject obj = objects.get(name);
		if (obj!=null) {
			r = obj.object;
		}
		lock.unlock(this);
		return r;
	}
	
	public long getObjectChanged(String name) {
		long r = 0;
		lock.lock(this);
		StateObject obj = objects.get(name);
		if (obj!=null) {
			r = obj.changed;
		}
		lock.unlock(this);
		return r;
	}
	
	public StateObject removeObject(String name) {
		lock.lock(this);
		StateObject r = objects.remove(name);
		lock.unlock(this);
		return r;
	}
	
	protected void putObject(String name, Object object, boolean publishChange) {
		lock.lock(this);
		StateObject obj = new StateObject();
		obj.name = name;
		obj.object = object;
		objects.put(name, obj);
		List<StateListener> list = new ArrayList<StateListener>(listeners);
		lock.unlock(this);
		if (publishChange) {
			publishObjectChange(list,name,null);
		}
	}
	
	protected void publishObjectChange(List<StateListener> list, String name, HashMap<String,Object> params) {
		if (params==null) {
			params = new HashMap<String,Object>();
		}
		for (StateListener listener: list) {
			try {
				listener.changedState(name, params);
			} catch(Exception e) {
				Logger.err(this, new Str("Caught state listener exception"), e);
			}
		}
	}
}

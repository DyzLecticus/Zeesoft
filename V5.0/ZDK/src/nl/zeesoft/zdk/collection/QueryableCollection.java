package nl.zeesoft.zdk.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.Lock;

public class QueryableCollection {
	public final static String				ID_CONCATENATOR			= "@";
	
	protected final Lock					lock					= new Lock();
	
	protected long							nextId					= 1;
	protected final SortedMap<Str,Object>	objects					= new TreeMap<Str,Object>();
	
	public Str put(Str id,Object object) {
		if (object!=null) {
			lock.lock(this);
			id = putNoLock(id, object);
			lock.unlock(this);
		}
		return id;
	}

	public Str put(Object object) {
		return put(null,object);
	}

	public List<Str> putAll(SortedMap<Str,Object> objects) {
		List<Str> ids = new ArrayList<Str>();
		for (Entry<Str,Object> entry: objects.entrySet()) {
			Str id = put(entry.getKey(),entry.getValue());
			ids.add(id);
		}
		return ids;
	}
	
	public List<Str> putAll(List<Object> objects) {
		List<Str> ids = new ArrayList<Str>();
		for (Object object: objects) {
			Str id = put(null,object);
			ids.add(id);
		}
		return ids;
	}
	
	public Object remove(Str id) {
		Object r = null;
		if (id!=null) {
			lock.lock(this);
			r = removeNoLock(id);
			lock.unlock(this);
		}
		return r;
	}
	
	public List<Object> removeAll(List<Str> ids) {
		List<Object> r = new ArrayList<Object>();
		for (Str id: ids) {
			r.add(remove(id));
		}
		return r;
	}
	
	public void clear() {
		lock.lock(this);
		clearNoLock(); 
		lock.unlock(this);
	}
	
	public int size() {
		lock.lock(this);
		int r = objects.size(); 
		lock.unlock(this);
		return r;
	}
	
	public List<Str> getObjectIds() {
		lock.lock(this);
		List<Str> r = new ArrayList<Str>(objects.keySet()); 
		lock.unlock(this);
		return r;
	}
	
	public SortedMap<Str,Object> getObjects() {
		return getObjects(null);
	}
	
	public SortedMap<Str,Object> getObjects(String className) {
		lock.lock(this);
		SortedMap<Str,Object> r = getObjectsNoLock(className);
		lock.unlock(this);
		return r;
	}
	
	public Object get(Str id) {
		Object r = null;
		if (id!=null) {
			lock.lock(this);
			r = objects.get(id); 
			lock.unlock(this);
		}
		return r;
	}
	
	public boolean containsId(Str id) {
		boolean r = false;
		if (id!=null) {
			lock.lock(this);
			r = objects.containsKey(id);
			lock.unlock(this);
		}
		return r;
	}
	
	public Query query(Query query) {
		query = query.copy();
		lock.lock(this);
		boolean selected = false;
		for (String className: query.getClassNames()) {
			query.results.putAll(getObjectsNoLock(className));
			selected = true;
		}
		if (!selected) {
			query.results.putAll(objects);
		}
		lock.unlock(this);
		return query.applyAllFilters();
	}
	
	protected Str putNoLock(Str id, Object object) {
		if (id==null) {
			id = new Str(object.getClass().getName());
			id.sb().append(ID_CONCATENATOR);
			id.sb().append(nextId);
			nextId++;
		}
		objects.put(id, object);
		return id;
	}
	
	protected Object removeNoLock(Str id) {
		return objects.remove(id);
	}
	
	protected void clearNoLock() {
		objects.clear();
	}
	
	public SortedMap<Str,Object> getObjectsNoLock(String className) {
		SortedMap<Str,Object> r = new TreeMap<Str,Object>(); 
		if (className==null || className.length()==0) {
			r.putAll(objects);
		} else {
			className += ID_CONCATENATOR;
			for (Entry<Str,Object> entry: objects.entrySet()) {
				if (entry.getKey().startsWith(className)) {
					r.put(entry.getKey(),entry.getValue());
				}
			}
		}
		return r;
	}
}

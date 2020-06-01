package nl.zeesoft.zdk.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.Lock;

public class QueryableCollection {
	public final static String				ID_CONCATENATOR			= "@";
	
	protected final Lock					lock					= new Lock();
	protected Logger						logger					= new Logger();
	
	protected long							nextId					= 1;
	protected final SortedMap<Str,Object>	objects					= new TreeMap<Str,Object>();
	
	public QueryableCollection() {
		
	}
	
	public QueryableCollection(Logger logger) {
		this.logger = logger;
	}
	
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
		SortedMap<Str,Object> r = getExternalObjectsNoLock(className);
		lock.unlock(this);
		return r;
	}
	
	public Object get(Str id) {
		Object r = null;
		if (id!=null) {
			lock.lock(this);
			r = getExternalObjectNoLock(id); 
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
			query.results.putAll(getExternalObjectsNoLock(className));
			selected = true;
		}
		if (!selected) {
			query.results.putAll(getExternalObjectsNoLock(null));
		}
		lock.unlock(this);
		return query.applyAllFilters();
	}
	
	protected static Str getIdForObject(Object object, long uid) {
		Str id = new Str(object.getClass().getName());
		id.sb().append(ID_CONCATENATOR);
		id.sb().append(uid);
		return id;
	}
	
	protected Str putNoLock(Str id, Object object) {
		if (object!=null) {
			if (id==null) {
				id = getIdForObject(object,nextId);
				nextId++;
			}
			objects.put(new Str(id), object);
		}
		return id;
	}
	
	protected Object removeNoLock(Str id) {
		return objects.remove(id);
	}
	
	protected void clearNoLock() {
		objects.clear();
		nextId = 0L;
	}
	
	protected Object getExternalObjectNoLock(Str id) {
		return getInternalObjectNoLock(id);
	}
	
	protected Object getInternalObjectNoLock(Str id) {
		return objects.get(id);
	}
	
	protected SortedMap<Str,Object> getExternalObjectsNoLock(String className) {
		SortedMap<Str,Object> r = new TreeMap<Str,Object>();
		if (className!=null && className.length()>0) {
			className += ID_CONCATENATOR;
		}
		for (Entry<Str,Object> entry: objects.entrySet()) {
			if (className==null || className.length()==0 || entry.getKey().startsWith(className)) {
				r.put(new Str(entry.getKey()),getExternalObjectNoLock(entry.getKey()));
			}
		}
		return r;
	}
}

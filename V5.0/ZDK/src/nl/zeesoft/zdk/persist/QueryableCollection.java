package nl.zeesoft.zdk.persist;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.Lock;

public class QueryableCollection {
	protected final Lock					lock					= new Lock();
	
	protected final SortedMap<Str,Object>	objects					= new TreeMap<Str,Object>();
	protected final SortedMap<Str,Str>		objectIds				= new TreeMap<Str,Str>();
	
	public Str put(Object object) {
		lock.lock(this);
		Str id = getObjectIdForObjectNoLock(object);
		objects.put(id, object);
		lock.unlock(this);
		return id;
	}
	
	public List<Str> putAll(List<Object> objects) {
		List<Str> ids = new ArrayList<Str>();
		for (Object object: objects) {
			Str id = put(object);
			ids.add(id);
		}
		return ids;
	}
	
	public void remove(Str id) {
		lock.lock(this);
		if (objects.containsKey(id)) {
			Object object = objects.remove(id);
			Str oid = getObjectId(object);
			objectIds.remove(oid);
		}
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
		lock.lock(this);
		Object r = objects.get(id); 
		lock.unlock(this);
		return r;
	}
	
	public void clear() {
		lock.lock(this);
		clearNoLock(); 
		lock.unlock(this);
	}
	
	public Query query(Query query) {
		query = query.copy();
		lock.lock(this);
		boolean selected = false;
		for (QueryFilter filter: query.filters) {
			if (filter.propertyName.equals(QueryFilter.CLASS_NAME) &&
				filter.value!=null &&
				filter.value instanceof String &&
				((String)filter.value).length()>0
				) {
				query.results.putAll(getObjectsNoLock((String)filter.value));
				selected = true;
			}
		}
		if (!selected) {
			query.results.putAll(objects);
		}
		lock.unlock(this);
		return query.applyAllFilters();
	}
	
	protected Str getObjectIdForObjectNoLock(Object object) {
		Str oid = getObjectId(object);
		Str r = objectIds.get(oid);
		if (r==null) {
			r = oid;
			objectIds.put(oid, r);
		}
		return r;
	}
	
	protected static Str getObjectId(Object object) {
		Str r = new Str(object.getClass().getName());
		r.sb().append("@");
		r.sb().append(object.hashCode());
		return r;
	}
	
	public SortedMap<Str,Object> getObjectsNoLock(String className) {
		SortedMap<Str,Object> r = new TreeMap<Str,Object>(); 
		if (className==null || className.length()==0) {
			r.putAll(objects);
		} else {
			for (Entry<Str,Object> entry: objects.entrySet()) {
				if (entry.getKey().startsWith(className + "@")) {
					r.put(entry.getKey(),entry.getValue());
				}
			}
		}
		return r;
	}
	
	protected void clearNoLock() {
		objects.clear();
		objectIds.clear();
	}
	
	protected static Field getFieldByName(Object object, String fieldName) {
		Field r = null;
		Class<?> cls = object.getClass();
		while(cls!=Object.class) {
			try {
				r = cls.getDeclaredField(fieldName);
				if (r!=null) {
					break;
				}
			} catch (NoSuchFieldException e) {
				// Ignore
			} catch (SecurityException e) {
				// Ignore
			}
			cls = cls.getSuperclass();
		}
		return r;
	}
	
	protected static Object getFieldValue(Object object, Field field) {
		Object r = null;
		try {
			field.setAccessible(true);
			r = field.get(object);
		} catch (IllegalArgumentException e) {
			// Ignore
		} catch (IllegalAccessException e) {
			// Ignore
		}
		return r;
	}
	
	protected static void setFieldValue(Object object, Field field, Object value) {
		try {
			field.setAccessible(true);
			field.set(object, value);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}

package nl.zeesoft.zdk.collection;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;

public class PersistableCollection extends PersistableCollectionBase {
	public static final String		CLASS_NAME			= "@CN|";
	public static final String		PROPERTY_NAME		= "@PN|";
	
	protected List<String>			classNames			= new ArrayList<String>();
	protected List<String>			propertyNames		= new ArrayList<String>();
	
	public PersistableCollection() {
		
	}
	
	public PersistableCollection(Logger logger) {
		super(logger);
	}
	
	public void addClassName(String className) {
		lock.lock(this);
		if (!classNames.contains(className)) {
			classNames.add(className);
		}
		lock.unlock(this);
	}
	
	public void addClassNames(List<String> classNames) {
		for (String className: classNames) {
			addClassName(className);
		}
	}
	
	@Override
	public Str putNoLock(Str id, Object object) {
		id = super.putNoLock(id, object);
		if (id!=null) {
			if (!classNames.contains(object.getClass().getName())) {
				classNames.add(object.getClass().getName());
				List<Field> fields = getPersistedFields(object);
				for (Field field : fields) {
					if (!propertyNames.contains(field.getName())) {
						propertyNames.add(field.getName());
					}
				}
			}
		}
		return id;
	}
	
	@Override
	protected void clearNoLock() {
		super.clearNoLock();
		classNames.clear(); 
		propertyNames.clear();
	}
	
	@Override
	protected Str toStrNoLock(SortedMap<Str,Object> objects) {
		Str r = super.toStrNoLock(objects);
		SortedMap<String,String> replacements = getReplacementsNoLock();
		for (Entry<String,String> entry: replacements.entrySet()) {
			r.replace(entry.getValue(),entry.getKey());
		}
		r.sb().insert(0,getReplacementsStrNoLock(replacements).sb());
		return r;
	}
	
	@Override
	protected List<Str> fromStrNoLock(Str str) {
		SortedMap<String,String> replacements = parseReplacementsFromStrNoLock(str);
		for (Entry<String,String> entry: replacements.entrySet()) {
			str.replace(entry.getKey(),entry.getValue());
		}
		return super.fromStrNoLock(str);
	}
	
	protected SortedMap<String,String> getReplacementsNoLock() {
		SortedMap<String,String> r = new TreeMap<String,String>();
		int i = 0;
		List<String> list = new ArrayList<String>(classNames);
		list.add(Object.class.getName());
		list.add(StringBuilder.class.getName());
		list.add(String.class.getName());
		list.add(Integer.class.getName());
		list.add(Long.class.getName());
		list.add(Float.class.getName());
		list.add(Double.class.getName());
		list.add(Boolean.class.getName());
		list.add(Byte.class.getName());
		list.add(Short.class.getName());
		list.add(BigDecimal.class.getName());
		list.add(List.class.getName());
		list.add(Str.class.getName());
		for (String className: list) {
			String find = className + ID_CONCATENATOR;
			String rep = CLASS_NAME + String.format("%03d", i) + ID_CONCATENATOR; 
			r.put(rep,find);
			i++;
		}
		i = 0;
		String eq = EQUALS.substring(0,EQUALS.length() - 1);
		for (String propertyName: propertyNames) {
			String find = PERSISTABLE_PROPERTY + propertyName + eq;
			String rep = PROPERTY_NAME + String.format("%03d", i) + eq; 
			r.put(rep,find);
			i++;
		}
		return r;
	}
	
	protected Str getReplacementsStrNoLock(SortedMap<String,String> replacements) {
		Str r = new Str();
		for (Entry<String,String> entry: replacements.entrySet()) {
			if (r.length()>0) {
				r.sb().append("\n");
			}
			r.sb().append(entry.getKey());
			r.sb().append(EQUALS);
			r.sb().append(entry.getValue());
		}
		r.sb().append("\n");
		return r;
	}
	
	protected SortedMap<String,String> parseReplacementsFromStrNoLock(Str str) {
		SortedMap<String,String> r = new TreeMap<String,String>();
		str = str.split(START_OBJECTS).get(0);
		List<Str> lines = str.split("\n");
		for (Str line: lines) {
			if (line.length()>EQUALS.length() &&
				(line.startsWith(CLASS_NAME) || line.startsWith(PROPERTY_NAME))
				) {
				List<Str> repFind = line.split(EQUALS);
				r.put(repFind.get(0).toString(), repFind.get(1).toString());
			}
		}
		return r;
	}
}

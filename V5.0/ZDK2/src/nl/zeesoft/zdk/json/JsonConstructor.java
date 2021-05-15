package nl.zeesoft.zdk.json;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ArrUtil;
import nl.zeesoft.zdk.Reflector;

public class JsonConstructor {
	public static String	CLASS_NAME	= "className";
	
	public static Json fromObject(Object object) {
		Json r = new Json();
		r.root.put(CLASS_NAME, object.getClass().getName());
		addKeyValues(r, Reflector.getFieldValues(object));
		return r;
	}

	public static Json fromKeyValues(SortedMap<String,Object> keyValues) {
		Json r = new Json();
		addKeyValues(r, keyValues);
		return r;
	}
	
	private static void addKeyValues(Json json, SortedMap<String,Object> keyValues) {
		for (Entry<String,Object> entry: keyValues.entrySet()) {
			if (ArrUtil.isOneDimensionalArray(entry.getValue())) {
				json.root.putArray(entry.getKey(), ArrUtil.unpackArray(entry.getValue()));
			} else if (entry.getValue() instanceof List) {
				addList(json, entry.getKey(), entry.getValue());
			} else if (isPrimitiveType(entry.getValue())) {
				json.root.put(entry.getKey(), entry.getValue());
			} else {
				JElem elem = json.root.put(entry.getKey(),null);
				if (entry.getValue()!=null) {
					Json object = fromObject(entry.getValue());
					elem.children = object.root.children;
				}
			}
		}
	}
	
	private static void addList(Json json, String key, Object value) {
		JElem array = json.root.putArray(key, null);
		@SuppressWarnings("unchecked")
		List<Object> objects = (List<Object>) value;
		for (Object object: objects) {
			array.children.add(getListItem(object));
		}
	}
	
	private static JElem getListItem(Object object) {
		Json child = null;
		if (isPrimitiveType(object)) {
			SortedMap<String,Object> keyValues = new TreeMap<String,Object>();
			keyValues.put(CLASS_NAME, object.getClass().getName());
			keyValues.put("value", object);
			child = fromKeyValues(keyValues);
		} else {
			child = fromObject(object);
		}
		return child.root;
	}
	
	private static boolean isPrimitiveType(Object value) {
		boolean r = false;
		if (value instanceof String ||
			value instanceof StringBuilder ||
			value instanceof Integer ||
			value instanceof Long ||
			value instanceof Float ||
			value instanceof Double ||
			value instanceof Boolean ||
			value instanceof Byte ||
			value instanceof Short
			) {
			r = true;
		}
		return r;
	}
}

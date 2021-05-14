package nl.zeesoft.zdk.json;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

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
			} else {
				json.root.put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	private static void addList(Json json, String key, Object value) {
		JElem array = json.root.putArray(key, null);
		@SuppressWarnings("unchecked")
		List<Object> objects = (List<Object>) value;
		for (Object object: objects) {
			Json child = fromObject(object);
			array.children.add(child.root);
		}
	}
}

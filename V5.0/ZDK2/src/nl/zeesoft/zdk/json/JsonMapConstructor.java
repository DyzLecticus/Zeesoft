package nl.zeesoft.zdk.json;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class JsonMapConstructor {
	public static String	KEY_VALUES	= "keyValues";
	public static String	KEY			= "key";
	public static String	VALUE		= "value";
	
	public static boolean isMap(Object object) {
		return isMap(object.getClass().getName());
	}
	
	public static boolean isMap(String className) {
		boolean r = false;
		if (className.equals(HashMap.class.getName()) ||
			className.equals(ConcurrentHashMap.class.getName()) ||
			className.equals(TreeMap.class.getName())
			) {
			r = true;
		}
		return r;
	}
	
	public static Json fromMap(Map<Object,Object> map, boolean useConvertors) {
		JsonConstructor constructor = new JsonConstructor(useConvertors);
		Json r = new Json();
		JElem keyValues = getKeyValues();
		r.root.children.add(keyValues);
		for (Entry<Object, Object> entry: map.entrySet()) {
			keyValues.children.add(getKeyValue(constructor, entry.getKey(), entry.getValue()));
		}
		return r;
	}
	
	private static JElem getKeyValues() {
		JElem r = new JElem();
		r.key = KEY_VALUES;
		r.isArray = true;
		return r;
	}
	
	private static JElem getKeyValue(JsonConstructor constructor, Object key, Object value) {
		JElem r = new JElem();
		r.children.add(getChild(constructor, KEY, key));
		r.children.add(getChild(constructor, VALUE, value));
		return r;
	}
	
	private static JElem getChild(JsonConstructor constructor, String key, Object value) {
		JElem r = constructor.getListItem(value);
		r.key = key;
		return r;
	}
}

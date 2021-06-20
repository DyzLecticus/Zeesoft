package nl.zeesoft.zdk.json;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectMapConstructor {	
	public static Map<Object,Object> fromJson(Json json) {
		Map<Object,Object> r = getNewMap(getClassName(json));
		JElem keyValues = json.root.get(JsonMapConstructor.KEY_VALUES);
		for (JElem kv: keyValues.children) {
			r.put(getObject(kv, "key"), getObject(kv, "value"));
		}
		return r;
	}
	
	public static Map<Object,Object> getNewMap(String className) {
		Map<Object,Object> r = null;
		if (className.equals(HashMap.class.getName())) {
			r = new HashMap<Object,Object>();
		} else if (className.equals(ConcurrentHashMap.class.getName())) {
			r = new ConcurrentHashMap<Object,Object>();
		} else if (className.equals(TreeMap.class.getName())) {
			r = new TreeMap<Object,Object>();
		}
		return r;
	}
	
	private static String getClassName(Json json) {
		return json.root.get(JsonConstructor.CLASS_NAME).value.toString();
	}
	
	private static Object getObject(JElem kv, String key) {
		Json kjs = new Json();
		kjs.root.children = kv.get(key).children;
		return ObjectConstructor.fromJson(kjs);
	}
}

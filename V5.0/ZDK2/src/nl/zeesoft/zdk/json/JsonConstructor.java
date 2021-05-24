package nl.zeesoft.zdk.json;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ArrUtil;
import nl.zeesoft.zdk.Reflector;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;

public class JsonConstructor {
	public static String	CLASS_NAME		= "className";
	
	public boolean			useConvertors	= false;
	
	public JsonConstructor(boolean useConvertors) {
		this.useConvertors = useConvertors;
	}
	
	public static Json fromObject(Object object) {
		return (new JsonConstructor(false)).fromObj(object);
	}
	
	public static Json fromObjectUseConvertors(Object object) {
		return (new JsonConstructor(true)).fromObj(object);
	}
	
	public Json fromObj(Object object) {
		Json r = new Json();
		r.root.put(CLASS_NAME, object.getClass().getName());
		if (useConvertors) {
			fromObjUseConvertors(object, r);
		} else {
			addKeyValues(r, Reflector.getFieldValues(object));
		}
		return r;
	}

	public Json fromKeyVals(SortedMap<String,Object> keyValues) {
		Json r = new Json();
		addKeyValues(r, keyValues);
		return r;
	}
	
	private void fromObjUseConvertors(Object object, Json r) {
		ObjectStringConvertor conv = ObjectStringConvertors.getConvertor(object.getClass());
		if (conv!=null) {
			r.root.put(conv.getClass().getSimpleName(), conv.toStringBuilder(object));
		} else {
			addKeyValues(r, Reflector.getFieldValues(object));
		}
	}

	private void addKeyValues(Json json, SortedMap<String,Object> keyValues) {
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
					elem.children = getObjectAsJson(entry.getValue()).root.children;
				}
			}
		}
	}
	
	private void addList(Json json, String key, Object value) {
		JElem array = json.root.putArray(key, null);
		@SuppressWarnings("unchecked")
		List<Object> objects = (List<Object>) value;
		for (Object object: objects) {
			array.children.add(getListItem(object));
		}
	}
	
	private JElem getListItem(Object object) {
		Json child = null;
		if (isPrimitiveType(object)) {
			child = getPrimitiveAsJson(object);
		} else {
			child = getObjectAsJson(object);
		}
		return child.root;
	}
	
	private Json getPrimitiveAsJson(Object object) {
		SortedMap<String,Object> keyValues = new TreeMap<String,Object>();
		keyValues.put(CLASS_NAME, object.getClass().getName());
		keyValues.put("value", object);
		return (new JsonConstructor(useConvertors)).fromKeyVals(keyValues);
	}
	
	private Json getObjectAsJson(Object object) {
		Json json = null;
		if (useConvertors) {
			json = fromObjectUseConvertors(object);
		} else {
			json = fromObject(object);
		}
		return json;
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

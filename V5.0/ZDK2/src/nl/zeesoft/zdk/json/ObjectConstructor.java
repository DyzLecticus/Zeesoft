package nl.zeesoft.zdk.json;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Instantiator;
import nl.zeesoft.zdk.Reflector;

public class ObjectConstructor {
	public static Object fromJson(Json json) {
		return fromJson(json.root);
	}

	protected static Object fromJson(JElem parent) {
		Object r = null;
		JElem cn = parent.get(JsonConstructor.CLASS_NAME);
		r = Instantiator.getNewClassInstance(cn.value.toString());
		if (r!=null) {
			fromFields(r, parent);
		}
		return r;
	}

	private static void fromFields(Object r, JElem parent) {
		List<Field> fields = Reflector.getFields(r);
		for (Field field: fields) {
			JElem child = parent.get(field.getName());
			if (child!=null) {
				if (!child.isArray) {
					Reflector.setFieldValue(r, field, child.value);
				} else {
					fromArray(r, field, child);
				}
			}
		}
	}

	private static void fromArray(Object r, Field field, JElem child) {
		List<Object> cObjects = new ArrayList<Object>();
		Object value = null;
		for (JElem arrayElem: child.children) {
			if (arrayElem.value == null) {
				cObjects.add(fromJson(arrayElem));
				value = cObjects;
			} else {
				cObjects.add(arrayElem.value);
			}
		}
		if (value==null) {
			value = Instantiator.getNewArrayInstance(field.getType().toString(), cObjects);
		}
		Reflector.setFieldValue(r, field, value);
	}
}

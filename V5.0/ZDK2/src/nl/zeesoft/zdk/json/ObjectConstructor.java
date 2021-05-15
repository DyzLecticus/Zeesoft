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
	
	public static Object convertValueToClass(Object value, Class<?> cls) {
		Object r = value;
		if (value!=null && cls!=null && value.getClass()!=cls) {
			if (cls == String.class) {
				r = value.toString();
			} else if (cls == Long.class && value.getClass() == Integer.class) {
				r = ((Integer)value).longValue();
			} else if (cls == Float.class && value.getClass() == Double.class) {
				r = new Float((Double)value);
			} else if (cls == Float.class && value.getClass() == Integer.class) {
				r = ((Integer)value).floatValue();
			} else if (cls == Double.class && value.getClass() == Integer.class) {
				r = ((Integer)value).doubleValue();
			} else if (cls == Byte.class && value.getClass() == Integer.class) {
				r = ((Integer)value).byteValue();
			} else if (cls == Short.class && value.getClass() == Integer.class) {
				r = ((Integer)value).shortValue();
			}
		}
		return r;
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
			addChild(r, field, parent.get(field.getName()));
		}
	}

	private static void addChild(Object r, Field field, JElem child) {
		if (child!=null) {
			if (!child.isArray) {
				Object value = child.value;
				if (child.children.size()>0) {
					value = fromJson(child);
				}
				Reflector.setFieldValue(r, field, convertValueForField(value, field));
			} else {
				fromArray(r, field, child);
			}
		}
	}
	
	private static void fromArray(Object r, Field field, JElem child) {
		List<Object> cObjects = new ArrayList<Object>();
		Object value = null;
		for (JElem arrayElem: child.children) {
			value = addArrayElement(field, cObjects, arrayElem);
		}
		if (value==null) {
			value = Instantiator.getNewArrayInstance(field.getType().toString(), cObjects);
		}
		if (value==null) {
			value = cObjects;
		}
		Reflector.setFieldValue(r, field, value);
	}
	
	private static Object addArrayElement(Field field, List<Object> objects, JElem arrayElem) {
		Object r = null;
		if (arrayElem.value == null) {
			objects.add(fromJson(arrayElem));
			r = objects;
		} else {
			objects.add(convertValueForField(arrayElem.value, field));
		}
		return r;
	}
	
	private static Object convertValueForField(Object value, Field field) {
		String type = Reflector.getTypeSafe(field.getType().toString());
		type = Reflector.getArrayTypeSafe(type);
		type = Reflector.getPrimitiveArrayTypeSafe(type);
		return convertValueToClass(value, Instantiator.getClassForName(type));
	}
}

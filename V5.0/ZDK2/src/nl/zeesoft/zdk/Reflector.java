package nl.zeesoft.zdk;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Reflector {
	public static List<Field> getFields(Object object) {
		return getFields(object, null);
	}
	
	public static List<Field> getFields(Object object, @SuppressWarnings("rawtypes") Class notAnnotation) {
		List<Field> r = new ArrayList<Field>();
		Class<?> cls = object.getClass();
		while(cls!=Object.class) {
			for (Field field : cls.getDeclaredFields()) {
				if (addToList(field, notAnnotation)) {
					r.add(field);
				}
			}
			cls = cls.getSuperclass();
		}
		return r;
	}
	
	public static Object getFieldValue(Object object, Field field) {
		Object r = null;
		try {
			r = field.get(object);
		} catch (IllegalArgumentException e) {
			// Ignore
		} catch (IllegalAccessException e) {
			// Ignore
		}
		return r;
	}
	
	public static SortedMap<String,Object> getFieldValues(Object object) {
		return getFieldValues(object, getFields(object));
	}

	public static SortedMap<String,Object> getFieldValues(Object object, @SuppressWarnings("rawtypes") Class notAnnotation) {
		return getFieldValues(object, getFields(object, notAnnotation));
	}

	public static SortedMap<String,Object> getFieldValues(Object object, List<Field> fields) {
		SortedMap<String,Object> r = new TreeMap<String,Object>();
		for (Field field: fields) {
			field.setAccessible(true);
			r.put(field.getName(), getFieldValue(object, field));
		}
		return r;
	}
	
	public static void setFieldValue(Object object, Field field, Object value) {
		setFieldValue(object, field, value, true);
	}
	
	public static void setFieldValue(Object object, Field field, Object value, boolean force) {
		try {
			if (!field.isAccessible() && force) {
				field.setAccessible(true);
			}
			field.set(object, value);
		} catch (IllegalArgumentException e) {
			Logger.error(field, "Caught illegal argument exception", e);
		} catch (IllegalAccessException e) {
			Logger.error(field, "Caught illegal access exception", e);
		}
	}
	
	public static String getTypeSafe(String type) {
		if (type.contains(" ")) {
			type = type.split(" ")[1];
		}
		return type;
	}
	
	public static String getArrayTypeSafe(String type) {
		if (type.startsWith("[L")) {
			type = type.substring(2, type.length() - 1);
		}
		return type;
	}
	
	public static String getPrimitiveArrayTypeSafe(String type) {
		Class<?> cls = getPrimitiveArrayType(type);
		if (cls!=null) {
			type = cls.getName();
		}
		return type;
	}
	
	protected static Class<?> getPrimitiveArrayType(String type) {
		Class<?> r = null;
		if (type.equals("[I")) {
			r = int.class;
		} else if (type.equals("[J")) {
			r = long.class;
		} else if (type.equals("[F")) {
			r = float.class;
		} else if (type.equals("[D")) {
			r = double.class;
		} else if (type.equals("[Z")) {
			r = boolean.class;
		} else if (type.equals("[B")) {
			r = byte.class;
		} else if (type.equals("[S")) {
			r = short.class;
		}
		return r;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static boolean addToList(Field field, Class notAnnotation) {
		boolean r = false;
		if (!Modifier.isStatic(field.getModifiers())
			&& (notAnnotation==null || (
				field.getAnnotation(notAnnotation)==null &&
				field.getType().getAnnotation(notAnnotation)==null
			)
			)) {
			r = true;
		}
		return r;
	}
}

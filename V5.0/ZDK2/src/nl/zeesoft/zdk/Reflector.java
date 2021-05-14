package nl.zeesoft.zdk;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Reflector {
	public static List<Field> getFields(Object object) {
		List<Field> r = new ArrayList<Field>();
		Class<?> cls = object.getClass();
		while(cls!=Object.class) {
			for (Field field : cls.getDeclaredFields()) {
				if (!Modifier.isStatic(field.getModifiers())) {
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
}

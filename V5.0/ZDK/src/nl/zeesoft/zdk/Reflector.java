package nl.zeesoft.zdk;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class Reflector {
	public static String getClassName(String className) {
		if (className.contains(" ")) {
			className = className.split(" ")[1];
		}
		if (className.startsWith("[L") && className.endsWith(";")) {
			className = className.substring(2).substring(0, className.length() - 3);
		}
		return className;
	}
	
	public static boolean isArrayType(String className) {
		if (className.contains(" ")) {
			className = className.split(" ")[1];
		}
		return className.startsWith("[");
	}
	
	public static Field getFieldByName(Object object, String fieldName) {
		Field r = null;
		Class<?> cls = object.getClass();
		while(cls!=Object.class) {
			try {
				r = cls.getDeclaredField(fieldName);
				if (r!=null) {
					break;
				}
			} catch (NoSuchFieldException e) {
				// Ignore
			} catch (SecurityException e) {
				// Ignore
			}
			cls = cls.getSuperclass();
		}
		return r;
	}
	
	public static Object getFieldValue(Object object, Field field) {
		Object r = null;
		try {
			field.setAccessible(true);
			r = field.get(object);
		} catch (IllegalArgumentException e) {
			// Ignore
		} catch (IllegalAccessException e) {
			// Ignore
		}
		return r;
	}
	
	public static void setFieldValue(Object object, Field field, Object value) {
		try {
			field.setAccessible(true);
			field.set(object, value);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean fieldIsPrimitiveArray(Field field) {
		boolean r = false;
		if (field.getType().isAssignableFrom(int[].class) ||
			field.getType().isAssignableFrom(long[].class) ||
			field.getType().isAssignableFrom(float[].class) ||
			field.getType().isAssignableFrom(double[].class) ||
			field.getType().isAssignableFrom(boolean[].class)
			) {
			r = true;
		}
		return r;
	}
	
	public static Class<?> getFieldPrimitiveArrayClass(Field field) {
		Class<?> r = null;
		if (field.getType().isAssignableFrom(int[].class)) {
			r = int.class;
		} else if (field.getType().isAssignableFrom(long[].class)) {
			r = boolean.class;
		} else if (field.getType().isAssignableFrom(float[].class)) {
			r = float.class;
		} else if (field.getType().isAssignableFrom(double[].class)) {
			r = double.class;
		} else if (field.getType().isAssignableFrom(boolean[].class)) {
			r = boolean.class;
		}
		return r;
	}
	
	public static Class<?> getFieldArrayClass(Field field) {
		Class<?> r = null;
		if (field.getType().isAssignableFrom(StringBuilder[].class)) {
			r = StringBuilder.class;
		} else if (field.getType().isAssignableFrom(String[].class)) {
			r = String.class;
		} else if (field.getType().isAssignableFrom(Integer[].class)) {
			r = Integer.class;
		} else if (field.getType().isAssignableFrom(Long[].class)) {
			r = Long.class;
		} else if (field.getType().isAssignableFrom(Float[].class)) {
			r = Float.class;
		} else if (field.getType().isAssignableFrom(Double[].class)) {
			r = Double.class;
		} else if (field.getType().isAssignableFrom(Boolean[].class)) {
			r = Boolean.class;
		} else if (field.getType().isAssignableFrom(BigDecimal[].class)) {
			r = BigDecimal.class;
		} else if (field.getType().isAssignableFrom(Str[].class)) {
			r = Str.class;
		}
		return r;
	}
}

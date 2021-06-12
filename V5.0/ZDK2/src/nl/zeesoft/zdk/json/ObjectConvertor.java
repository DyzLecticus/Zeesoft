package nl.zeesoft.zdk.json;

import nl.zeesoft.zdk.Util;

public class ObjectConvertor {
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

	public static Object convertValueToType(Object value, Object type) {
		Object r = null;
		if (value!=null) {
			if (type instanceof String) {
				r = value.toString();
			} else if (type instanceof StringBuilder && value instanceof StringBuilder) {
				r = value;
			} else if (type instanceof Integer && value instanceof Integer) {
				r = value;
			} else if (type instanceof Long) {
				r = Util.parseLong(value.toString());
			} else if (type instanceof Boolean && value instanceof Boolean) {
				r = value;
			} else if (type instanceof Byte) {
				r = Util.parseInt(value.toString()).byteValue();
			} else if (type instanceof Short) {
				r = Util.parseInt(value.toString()).shortValue();
			}
		}
		return r;
	}
}

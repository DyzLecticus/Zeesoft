package nl.zeesoft.zdk.str;


import java.util.List;

import nl.zeesoft.zdk.Instantiator;
import nl.zeesoft.zdk.Util;

public abstract class ObjectStringConvertor {
	public ObjectStringConvertor() {
		ObjectStringConvertors.addConvertor(this);
	}
	
	public abstract Class<?> getObjectClass();
	
	public abstract StringBuilder toStringBuilder(Object obj);
	
	public abstract Object fromStringBuilder(StringBuilder str);

	public static StringBuilder getDataTypeStringBuilderForObject(Object value, String separator) {
		StringBuilder r = new StringBuilder();
		if (value==null) {
			r.append(StrUtil.NULL);
		} else {
			r.append(value.getClass().getName());
			r.append(separator);
			r.append(getStringBuilderForObject(value));
		}
		return r;
	}

	public static StringBuilder getStringBuilderForObject(Object value) {
		StringBuilder r = new StringBuilder();
		if (value==null) {
			r.append(StrUtil.NULL);
		} else {
			ObjectStringConvertor child = ObjectStringConvertors.getConvertor(value.getClass());
			if (child!=null) {
				r.append(child.toStringBuilder(value));
			} else {
				r.append(value.toString());
			}
		}
		return r;
	}
	
	public static Object getObjectForDataTypeStringBuilder(StringBuilder value, String separator) {
		Object r = null;
		if (!StrUtil.equals(value,StrUtil.NULL)) {
			List<StringBuilder> elems = StrUtil.split(value, separator);
			Class<?> dataType = Instantiator.getClassForName(elems.get(0).toString());
			r = getObjectForStringBuilder(dataType, elems.get(1));
		}
		return r;
	}
	
	public static Object getObjectForStringBuilder(Class<?> dataType, StringBuilder value) {
		Object r = null;
		ObjectStringConvertor conv = ObjectStringConvertors.getConvertor(dataType);
		if (conv==null) {
			r = convertStringBuilderToPrimitive(value, dataType);
		} else {
			r = conv.fromStringBuilder(value);
		}
		return r;
	}
	
	public static Object convertStringBuilderToPrimitive(StringBuilder value, Class<?> cls) {
		Object r = value;
		if (value!=null && cls!=null && value.getClass()!=cls) {
			r = convertToPrimitive(value, cls);
		}
		return r;
	}
	
	private static Object convertToPrimitive(StringBuilder value, Class<?> cls) {
		if (cls == String.class) {
			return value.toString();
		} else if (cls == Integer.class) {
			return Util.parseInt(value.toString());
		} else if (cls == Long.class) {
			return Util.parseLong(value.toString());
		} else if (cls == Float.class) {
			return Util.parseFloat(value.toString());
		} else if (cls == Double.class) {
			return Util.parseDouble(value.toString());
		} else if (cls == Boolean.class) {
			return Boolean.parseBoolean(value.toString());
		} else if (cls == Byte.class) {
			return Util.parseInt(value.toString()).byteValue();
		} else if (cls == Short.class) {
			return Util.parseInt(value.toString()).shortValue();
		}
		return value;
	}
}

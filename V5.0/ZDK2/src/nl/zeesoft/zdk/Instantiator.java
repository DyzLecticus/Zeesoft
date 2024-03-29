package nl.zeesoft.zdk;

import java.lang.reflect.Array;
import java.util.List;

public class Instantiator {
	public static Class<?> getClassForName(String className) {
		Class<?> r = getPrimitiveClassForName(className);
		if (r==null) {
			try {
				r = Class.forName(className);
			} catch (ClassNotFoundException e) {
				// Ignore
			}
		}
		return r;
	}

	public static Class<?> getPrimitiveClassForName(String className) {
		Class<?> r = null;
		if (className.equals("int")) {
			r = Integer.class;
		} else if (className.equals("long")) {
			r = Long.class;
		} else if (className.equals("float")) {
			r = Float.class;
		} else if (className.equals("double")) {
			r = Double.class;
		} else if (className.equals("boolean")) {
			r = Boolean.class;
		} else if (className.equals("byte")) {
			r = Byte.class;
		} else if (className.equals("short")) {
			r = Short.class;
		}
		return r;
	}

	public static Object getNewClassInstance(Class<?> cls) {
		Object r = getNewPrimitiveClassInstance(cls);
		if (r==null) {
			try {
				r = cls.newInstance();
			} catch (InstantiationException e) {
				// Ignore
			} catch (IllegalAccessException e) {
				// Ignore
			}
		}
		return r;
	}
	
	public static Object getNewPrimitiveClassInstance(Class<?> cls) {
		Object r = null;
		if (cls == Integer.class) {
			r = 0;
		} else if (cls == Long.class) {
			r = 0L;
		} else if (cls == Float.class) {
			r = 0F;
		} else if (cls == Double.class) {
			r = 0D;
		} else if (cls == Boolean.class) {
			r = false;
		} else if (cls == Byte.class) {
			r = new Integer(0).byteValue();
		} else if (cls == Short.class) {
			r = new Integer(0).shortValue();
		}
		return r;
	}
	
	public static Object getNewClassInstance(String className) {
		Object r = null;
		Class<?> cls = getClassForName(className);
		if (cls!=null) {
			r = getNewClassInstance(cls);
		}
		return r;
	}
	
	public static Object getNewArrayInstance(String type, int length) {
		type = Reflector.getTypeSafe(type);
		Object r = getNewObjectArrayInstance(type, length);
		if (r==null) {
			r = getNewPrimitiveArrayInstance(type, length);
		}
		return r;
	}
	
	public static Object getNewArrayInstance(String type, List<Object> values) {
		Object r = getNewArrayInstance(type, values.size());
		if (r!=null) {
			for (int i = 0; i < values.size(); i++) {
				Array.set(r, i, values.get(i));
			}
		}
		return r;
	}
	
	private static Object getNewObjectArrayInstance(String type, int length) {
		Object r = null;
		if (type.startsWith("[L")) {
			Class<?> cls = getClassForName(Reflector.getArrayTypeSafe(type));
			if (cls!=null) {
				r = Array.newInstance(cls, length);
			}
		}
		return r;
	}
	
	private static Object getNewPrimitiveArrayInstance(String type, int length) {
		Object r = null;
		Class<?> cls = Reflector.getPrimitiveArrayType(type);
		if (cls!=null) {
			r = Array.newInstance(cls, length);
		}
		return r;
	}
}

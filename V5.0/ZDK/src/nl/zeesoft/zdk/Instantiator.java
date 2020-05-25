package nl.zeesoft.zdk;

import java.lang.reflect.Array;

/**
 * This Intstantiator provides static methods to dynamically instantiate classes. 
 */
public class Instantiator {
	public static String getClassName(String className) {
		if (className.contains(" ")) {
			className = className.split(" ")[1];
		}
		if (className.startsWith("[L") && className.endsWith(";")) {
			className = className.substring(2).substring(0, className.length() - 3);
		}
		return className;
	}
	
	public static boolean hasClassForName(String className) {
		boolean r = true;
		try {
			Class<?> cls = Class.forName(className);
			r = cls!=null;
		} catch (ClassNotFoundException e) {
			r = false;
		}
		return r;
	}
	
	public static Class<?> getClassForName(String className) {
		Class<?> r = null;
		if (hasClassForName(className)) {
			try {
				r = Class.forName(className);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return r;
	}
	
	public static boolean canInstantiateClass(String className) {
		boolean r = true;
		Class<?> cls = getClassForName(className);
		if (cls==null) {
			r = false;
		} else {
			try {
				Object o = cls.newInstance();
				r = o!=null;
			} catch (InstantiationException e) {
				r = false;
			} catch (IllegalAccessException e) {
				r = false;
			}
		}
		return r;
	}
	
	public static Object getNewClassInstanceForName(String className) {
		Object r = null;
		if (canInstantiateClass(className)) {
			Class<?> cls = getClassForName(className);
			try {
				r = cls.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return r;
	}
	
	public static Object getNewArrayInstanceForName(String className, int length) {
		Object r = null;
		if (canInstantiateClass(className)) {
			Class<?> cls = getClassForName(className);
			r = Array.newInstance(cls, length);
		}
		return r;
	}
}

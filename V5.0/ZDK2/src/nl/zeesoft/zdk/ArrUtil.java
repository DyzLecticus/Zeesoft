package nl.zeesoft.zdk;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArrUtil {
	public static boolean isOneDimensionalArray(Object value) {
		boolean r = isPrimitiveArray(value);
		if (!r) {
			r = isObjectArray(value);
		}
		return r;
	}

	public static boolean isObjectArray(Object value) {
		boolean r = false;
		if (value instanceof String[] ||
			value instanceof StringBuilder[] ||
			value instanceof Integer[] ||
			value instanceof Long[] ||
			value instanceof Float[] ||
			value instanceof Double[] ||
			value instanceof Boolean[] ||
			value instanceof Byte[] ||
			value instanceof Short[]
			) {
			r = true;
		}
		return r;
	}
	
	public static boolean isPrimitiveArray(Object value) {
		boolean r = false;
		if (value instanceof int[] ||
			value instanceof long[] ||
			value instanceof float[] ||
			value instanceof double[] ||
			value instanceof boolean[] ||
			value instanceof byte[] ||
			value instanceof short[]
			) {
			r = true;
		}
		return r;
	}
	
	public static List<Object> unpackArray(Object value) {
		List<Object> r = new ArrayList<Object>();
		int length = Array.getLength(value);
		for (int i = 0; i < length; i++) {
			r.add(Array.get(value, i));
		}
		return r;
	}
}

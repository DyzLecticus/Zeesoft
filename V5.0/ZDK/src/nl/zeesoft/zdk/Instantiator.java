package nl.zeesoft.zdk;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * This Intstantiator provides static methods to dynamically instantiate classes. 
 */
public class Instantiator {
	public static final String	NULL	= "null";
	
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

	public static List<Object> createTypedList(String className, List<Str> values) {
		List<Object> r = new ArrayList<Object>();
		try {
			for (Str val: values) {
				if (val!=null && !val.toString().equals(NULL)) {
					if (className.equals(StringBuilder.class.getName())) {
						r.add(val.sb());
					} else if (className.equals(String.class.getName())) {
						r.add(val.toString());
					} else if (className.equals(Integer.class.getName())) {
						r.add(Integer.parseInt(val.toString()));
					} else if (className.equals(Long.class.getName())) {
						r.add(Long.parseLong(val.toString()));
					} else if (className.equals(Float.class.getName())) {
						r.add(Float.parseFloat(val.toString()));
					} else if (className.equals(Double.class.getName())) {
						r.add(Double.parseDouble(val.toString()));
					} else if (className.equals(Boolean.class.getName())) {
						r.add(Boolean.parseBoolean(val.toString()));
					} else if (className.equals(Byte.class.getName())) {
						r.add(Byte.parseByte(val.toString()));
					} else if (className.equals(Short.class.getName())) {
						r.add(Short.parseShort(val.toString()));
					} else if (className.equals(BigDecimal.class.getName())) {
						r.add(new BigDecimal(val.toString()));
					} else {
						r.add(val);
					}
				} else {
					r.add(null);
				}
			}
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		return r;
	}

	public static Object createPrimitiveTypedArray(String className, List<Str> values) {
		Object r = null;
		try {
			if (className.equals(int.class.getName())) {
				int[] valObj = new int[values.size()];
				int i = 0;
				for (Str val: values) {
					if (val!=null && !val.toString().equals(NULL)) {
						valObj[i] = Integer.parseInt(val.toString());
					}
					i++;
				}
				r = valObj;
			} else if (className.equals(long.class.getName())) {
				long[] valObj = new long[values.size()];
				int i = 0;
				for (Str val: values) {
					if (val!=null && !val.toString().equals(NULL)) {
						valObj[i] = Long.parseLong(val.toString());
					}
					i++;
				}
				r = valObj;
			} else if (className.equals(float.class.getName())) {
				float[] valObj = new float[values.size()];
				int i = 0;
				for (Str val: values) {
					if (val!=null && !val.toString().equals(NULL)) {
						valObj[i] = Float.parseFloat(val.toString());
					}
					i++;
				}
				r = valObj;
			} else if (className.equals(double.class.getName())) {
				double[] valObj = new double[values.size()];
				int i = 0;
				for (Str val: values) {
					if (val!=null && !val.toString().equals(NULL)) {
						valObj[i] = Double.parseDouble(val.toString());
					}
					i++;
				}
				r = valObj;
			} else if (className.equals(boolean.class.getName())) {
				boolean[] valObj = new boolean[values.size()];
				int i = 0;
				for (Str val: values) {
					if (val!=null && !val.toString().equals(NULL)) {
						valObj[i] = Boolean.parseBoolean(val.toString());
					}
					i++;
				}
				r = valObj;
			} else if (className.equals(byte.class.getName())) {
				byte[] valObj = new byte[values.size()];
				int i = 0;
				for (Str val: values) {
					if (val!=null && !val.toString().equals(NULL)) {
						valObj[i] = Byte.parseByte(val.toString());
					}
					i++;
				}
				r = valObj;
			} else if (className.equals(short.class.getName())) {
				short[] valObj = new short[values.size()];
				int i = 0;
				for (Str val: values) {
					if (val!=null && !val.toString().equals(NULL)) {
						valObj[i] = Short.parseShort(val.toString());
					}
					i++;
				}
				r = valObj;
			}
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		return r;
	}
	
	public static Object createTypedArray(String className, List<Str> values) {
		Object[] r = (Object[]) getNewArrayInstanceForName(className, values.size());
		try {
			int i = 0;
			for (Str val: values) {
				if (val!=null && !val.toString().equals(NULL)) {
					if (className.equals(StringBuilder.class.getName())) {
						r[i] = val.sb();
					} else if (className.equals(String.class.getName())) {
						r[i] = val.toString();
					} else if (className.equals(Integer.class.getName())) {
						r[i] = Integer.parseInt(val.toString());
					} else if (className.equals(Long.class.getName())) {
						r[i] = Long.parseLong(val.toString());
					} else if (className.equals(Float.class.getName())) {
						r[i] = Float.parseFloat(val.toString());
					} else if (className.equals(Double.class.getName())) {
						r[i] = Double.parseDouble(val.toString());
					} else if (className.equals(Boolean.class.getName())) {
						r[i] = Boolean.parseBoolean(val.toString());
					} else if (className.equals(Byte.class.getName())) {
						r[i] = Byte.parseByte(val.toString());
					} else if (className.equals(Short.class.getName())) {
						r[i] = Short.parseShort(val.toString());
					} else if (className.equals(BigDecimal.class.getName())) {
						r[i] = new BigDecimal(val.toString());
					} else if (className.equals(Str.class.getName())) {
						r[i] = val;
					}
				}
				i++;
			}
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		return r;
	}
}

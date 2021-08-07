package nl.zeesoft.zdk;

import java.util.List;

public class Util {
	public static boolean equals(Object obj1, Object obj2) {
		boolean r = true;
		if ((obj1==null && obj2!=null) ||
			(obj1!=null && obj2==null) ||
			(obj1!=null && !obj1.equals(obj2)) 
			) {
			r = false;
		}
		return r;
	}

	public static float getHypotenuse(float len1, float len2) {
		return (float) Math.sqrt((len1 * len1) + (len2 * len2));
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static void removeNullValuesFromList(List list) {
		int r = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(r)==null) {
				list.remove(r);
			} else {
				r++;
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static void applySizeLimitToList(List list, int limit) {
		if (limit<=0) {
			list.clear();
		} else if (list.size() > limit) {
			int remove = list.size() - limit;
			for (int n = 0; n < remove; n++) {
				list.remove(limit);
			}
		}
	}
	
	public static InterruptedException sleep(int ms) {
		InterruptedException r = null;
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			r = e;
		}
		return r;
	}
	
	public static InterruptedException sleepNs(int ns) {
		InterruptedException r = null;
		try {
			Thread.sleep(0, ns);
		} catch (InterruptedException e) {
			r = e;
		}
		return r;
	}
	
	public static float getStandardDeviation(List<Float> values) {
		float r = 0.0F;
		if (values.size()>1) {
			float sum = 0.0F;
			int size = values.size();
			for (Float value: values) {
				sum += value;
			}
			float mean = sum / size;
			float dev = 0.0F;
			for (Float value: values) {
				dev += Math.pow(value - mean, 2);
			}
			r = (float) Math.sqrt(dev/(size - 1));
		}
		return r;
	}
	
	public static float getRootMeanSquaredError(List<Float> values) {
		float r = 0.0F;
		if (values.size()>0) {
		    float square = 0;
		    float mean = 0;
			for (Float value: values) {
				square += Math.pow(value, 2);
			}
		    mean = (square / (float)values.size());
		    r = (float)Math.sqrt(mean);
		}
		return r;
	}
	
	public static Integer parseInt(String str) {
		Integer r = null;
		try {
			r = Integer.parseInt(str);
		} catch(NumberFormatException ex) {
			// ignore
		}
		return r;
	}
	
	public static Long parseLong(String str) {
		Long r = null;
		try {
			r = Long.parseLong(str);
		} catch(NumberFormatException ex) {
			// ignore
		}
		return r;
	}
	
	public static Float parseFloat(String str) {
		Float r = null;
		try {
			r = Float.parseFloat(str);
		} catch(NumberFormatException ex) {
			// ignore
		}
		return r;
	}
	
	public static Double parseDouble(String str) {
		Double r = null;
		try {
			r = Double.parseDouble(str);
		} catch(NumberFormatException ex) {
			// ignore
		}
		return r;
	}
	
	public static float getFloatValue(Object value) {
		float r = 0.0F;
		if (value instanceof Float) {
			r = (Float) value;
		} else if (value instanceof Integer) {
			r = ((Integer) value).floatValue();
		} else if (value instanceof Long) {
			r = ((Long) value).floatValue();
		} else if (value instanceof Double) {
			r = ((Double) value).floatValue();
		}
		return r;
	}
}

package nl.zeesoft.zdk;

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
}

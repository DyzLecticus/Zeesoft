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
	
	public static void appendLine(StringBuilder sb, String line) {
		if (sb.length()>0) {
			sb.append("\n");
		}
		sb.append(line);
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
}

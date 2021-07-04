package nl.zeesoft.zdk.str;

public class StrUtilBase {
	public static boolean equals(StringBuilder str, String other) {
		return equals(str, new StringBuilder(other));
	}
	
	public static boolean equals(StringBuilder str, StringBuilder other) {
		return equals(str,other,false);
	}
	
	public static boolean equalsIgnoreCase(StringBuilder str, String other) {
		return equalsIgnoreCase(str, new StringBuilder(other));
	}
	
	public static boolean equalsIgnoreCase(StringBuilder str, StringBuilder other) {
		return equals(str,other,true);
	}
	
	public static boolean startsWith(StringBuilder str, String start) {
		return startsWith(str, new StringBuilder(start));
	}
	
	public static boolean startsWith(StringBuilder str, StringBuilder start) {
		CharSequence eq = start.subSequence(0, start.length());
		return (str.length() >= start.length() && str.subSequence(0, start.length()).equals(eq));
	}
	
	public static boolean endsWith(StringBuilder str, String end) {
		return endsWith(str, new StringBuilder(end));
	}
	
	public static  boolean endsWith(StringBuilder str, StringBuilder end) {
		CharSequence eq = end.subSequence(0, end.length());
		return (str.length() >= end.length() && str.subSequence(str.length() - end.length(), str.length()).equals(eq));
	}
	
	public static boolean startsWith(StringBuilder str, String[] strs) {
		boolean r = false;
		for (int i = 0; i < strs.length; i++) {
			if (startsWith(str, strs[i])) {
				r = true;
				break;
			}
		}
		return r;
	}
	
	public static boolean endsWith(StringBuilder str, String[] strs) {
		boolean r = false;
		for (int i = 0; i < strs.length; i++) {
			if (endsWith(str, strs[i])) {
				r = true;
				break;
			}
		}
		return r;
	}
	
	private static boolean equals(StringBuilder str, StringBuilder other, boolean ignoreCase) {
		boolean eq = false;
		if (str.length()==other.length()) {
			eq = true;
			for (int i = 0; i < str.length(); i++) {
				if ((!ignoreCase && !str.substring(i,i+1).equals(other.substring(i,i+1))) ||
					(ignoreCase && !str.substring(i,i+1).equalsIgnoreCase(other.substring(i,i+1)))
					) {
					eq = false;
					break;
				}
			}
		}
		return eq;
	}
}

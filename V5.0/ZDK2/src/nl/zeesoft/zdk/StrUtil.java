package nl.zeesoft.zdk;

import java.util.ArrayList;
import java.util.List;

public class StrUtil {
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

	public static void trim(StringBuilder str) {
		String[] characters = {" ", "\n" ,"\r", "\t"};
		trim(str, characters);
	}

	public static void trim(StringBuilder str, String character) {
		String[] characters = {character};
		trim(str, characters);
	}

	public static void trim(StringBuilder str, String[] characters) {
		while (startsWith(str, characters)) {
			str.delete(0,1);
		}
		while (endsWith(str, characters)) {
			str.delete(str.length()-1,str.length());
		}
	}
	
	public static StringBuilder substring(StringBuilder str, int start, int end) {
		StringBuilder r = new StringBuilder();
		for (int i = start; i < end; i++) {
			r.append(str.substring(i,i+1));
		}
		return r;
	}
	
	public static int indexOf(StringBuilder str, String search, int offset) {
		int r = -1;
		if (str.length()>=search.length()) {
			for (int i = offset; i < str.length(); i++) {
				if (i + search.length() <= str.length()) {
					String sub = str.substring(i, i + search.length());
					if (sub.equals(search)) {
						r = i;
						break;
					}
				}
			}
		}
		return r;
	}

	public static List<StringBuilder> split(StringBuilder str, String concatenator) {
		List<StringBuilder> r = new ArrayList<StringBuilder>(); 
		if (str.length()>0) {
			int offset = 0;
			int i = indexOf(str,concatenator,offset);
			while (i>=0) {
				r.add(substring(str,offset,i));
				offset = i+concatenator.length();
				i = indexOf(str,concatenator,offset);
			}
			if (offset<str.length()) {
				r.add(substring(str,offset,str.length()));
			} else {
				r.add(new StringBuilder());
			}
		}
		if (r.size()==0) {
			r.add(new StringBuilder());
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

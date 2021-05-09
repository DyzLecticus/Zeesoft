package nl.zeesoft.zdk;

import java.util.ArrayList;
import java.util.List;

public class StrUtil {
	public static void trim(StringBuilder str) {
		trim(str, " ");
	}
	
	public static void trim(StringBuilder str, String trim) {
		for (int i = 0; i < str.length(); i++) {
			if (str.substring(i,i+1).equals(trim)) {
				str.replace(i, i+1, "");
				i--;
			} else {
				break;
			}
		}
		for (int i = str.length() - 1; i >= 0; i--) {
			if (str.substring(i,i+1).equals(trim)) {
				str.replace(i, i+1, "");
			} else {
				break;
			}
		}
	}
	
	public static boolean startsWith(StringBuilder str, String start) {
		boolean r = true;
		for (int i = 0; i < start.length(); i++) {
			if (!str.substring(i,i+1).equals(start.substring(i, i+1))) {
				r = false;
				break;
			}
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
				r.add(new StringBuilder(str.substring(offset,i)));
				offset = i+concatenator.length();
				i = indexOf(str,concatenator,offset);
			}
			if (offset<str.length()) {
				r.add(new StringBuilder(str.substring(offset,str.length())));
			} else {
				r.add(new StringBuilder());
			}
		}
		if (r.size()==0) {
			r.add(new StringBuilder());
		}
		return r;
	}
}

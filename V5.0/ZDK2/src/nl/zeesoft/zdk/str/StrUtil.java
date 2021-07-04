package nl.zeesoft.zdk.str;

import java.util.ArrayList;
import java.util.List;

public class StrUtil extends StrUtilBase {
	public static StringBuilder		NULL	= new StringBuilder("null");
	public static StringBuilder		CRLF	= new StringBuilder("\r\n");
	
	public static void appendLine(StringBuilder sb, String line) {
		append(sb, new StringBuilder(line), "\n");
	}
	
	public static void appendLine(StringBuilder sb, StringBuilder line) {
		append(sb, new StringBuilder(line), "\n");
	}
	
	public static void append(StringBuilder sb, String line, String concatenator) {
		append(sb, new StringBuilder(line), concatenator);
	}
	
	public static void append(StringBuilder sb, StringBuilder line, String concatenator) {
		if (sb.length()>0) {
			sb.append(concatenator);
		}
		sb.append(line);
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
			r = splitStr(str, concatenator);
		}
		if (r.size()==0) {
			r.add(new StringBuilder());
		}
		return r;
	}

	private static List<StringBuilder> splitStr(StringBuilder str, String concatenator) {
		List<StringBuilder> r = new ArrayList<StringBuilder>(); 
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
		return r;
	}
}

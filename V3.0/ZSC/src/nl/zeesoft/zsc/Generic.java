package nl.zeesoft.zsc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Generic utilities;
 * - Random integer generation
 * - Time stamping
 * - StringBuilder manipulation
 */
public final class Generic {
	private Generic() {
		// Abstract
	}
	
	/**
	 * Generates a random integer
	 * 
	 * @param valFrom Value from
	 * @param valTill Value till
	 * @return A random integer
	 */
	public final static int generateRandom (int valFrom, int valTill) {
		Random rand = new Random();
		int num = -1;
		while (num < valFrom) {
			 num = (int) (rand.nextDouble() * (valTill + 1));
		}
		return num;
	}

	/**
	 * Formats a stack trace for usage in error messages
	 * 
	 * @param stack The StackTraceElement array
	 * @param startingClassName The 
	 * @return The formatted stack trace
	 */
	public final static String getCallStackString(StackTraceElement[] stack, String startingClassName) {
		String value = "";
		boolean start = false;
		if (startingClassName==null) {
			startingClassName = "";
		}
		if (startingClassName.length()==0) {
			start = true;
		}
		for (int i = 0; i < stack.length; i++) {
			StackTraceElement elem = stack[i];
			if ((start) && 
				((startingClassName.length()==0) || (!elem.getClassName().endsWith(startingClassName)))
				)  {
				value = value + elem.getClassName() + "." + elem.getMethodName() + ", line: " + elem.getLineNumber();
				value = value + "\n";
			}
			if (elem.getClassName().endsWith(startingClassName)) {
				start = true;
			}
		}
		return value;
	}

	/**
	 * Formats a date object to 'YYYY-MM-DD HH:MM:SS:MS0'
	 * 
	 * @param date The date to format
	 * @return The formatted date string
	 */
	public final static String getDateTimeString(Date date) {
		return getDateTimeString(date,true,true);
	}

	public final static String getDateTimeString(Date date,boolean ymd,boolean ms) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String dts = 
			getDateString(date,ymd) + " " + 
			getTimeString(date,ms);
		return dts;
	}

	public final static String getDateString(Date date, boolean ymd) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String ds = "";
		if (ymd) {
			ds = 
				minStrInt(cal.get(Calendar.YEAR),2) + "-" + 
				minStrInt(cal.get(Calendar.MONTH) + 1,2) + "-" + 
				minStrInt(cal.get(Calendar.DATE),2);
		} else {
			ds = 
				minStrInt(cal.get(Calendar.DATE),2) + "-" +
				minStrInt(cal.get(Calendar.MONTH) + 1,2) + "-" + 
				minStrInt(cal.get(Calendar.YEAR),2);
		}
		return ds;
	}

	public final static String getTimeString(Date date, boolean ms) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String ts = 
			minStrInt(cal.get(Calendar.HOUR_OF_DAY),2) + ":" + 
			minStrInt(cal.get(Calendar.MINUTE),2) + ":" + 
			minStrInt(cal.get(Calendar.SECOND),2);
			;
		if (ms) {
			ts = ts + ":" + minStrInt(cal.get(Calendar.MILLISECOND),3);
		}
		return ts;
	}

	public final static String getDurationString(long milliSeconds, boolean includeSeconds, boolean includeMilliSeconds) {
		long hours = (milliSeconds - (milliSeconds % 3600000)) / 3600000;
		long remaining = (milliSeconds - (hours * 3600000));
		long minutes = (remaining - (remaining % 60000)) / 60000;
		remaining = (remaining - (minutes * 60000));
		long seconds = (remaining - (remaining % 1000)) / 1000;
		remaining = (remaining - (seconds * 1000));
		String ds = "";
		if (includeMilliSeconds) {
			ds = hours + ":" + minutes + ":" + seconds + ":" + remaining;
		} else if (includeSeconds) {
			ds = hours + ":" + minutes + ":" + seconds;
		} else {
			ds = hours + ":" + minutes;
		}
		return ds;
	}

	public final static String minStrInt(int i, int l) {
		String s = "" + i;
		int sl = s.length();
		if (sl<l) {
			for (int z = 0; z < (l - sl); z++) {
				s = "0" + s;
			}
		}
		return s;
	}

	public final static boolean stringBuilderEndsWith(StringBuilder sb, String s) {
		return (sb!=null && sb.length()>=s.length() && sb.substring(sb.length() - s.length(),sb.length()).equals(s));
	}

	public final static boolean stringBuilderStartsWith(StringBuilder sb, String s) {
		return (sb!=null && sb.length()>=s.length() && sb.substring(0, s.length()).equals(s));
	}

	public final static boolean stringBuilderEquals(StringBuilder sb1, StringBuilder sb2) {
		boolean eq = true;
		if (sb1!=null && sb2!=null) {
			if (sb1.length()==sb2.length()) {
				for (int i = 0; i < sb1.length(); i++) {
					if (!sb1.substring(i,(i+1)).equals(sb2.substring(i,(i+1)))) {
						eq = false;
						break;
					}
				}
			} else {
				eq = false;
			}
		} else if (
			(sb1==null && sb2!=null) ||
			(sb1!=null && sb2==null)
			) {
			eq = false;
		}
		return eq;
	}

	public final static StringBuilder stringBuilderTrim(StringBuilder str) {
		while (Generic.stringBuilderStartsWith(str," ") || Generic.stringBuilderStartsWith(str,"\n") || Generic.stringBuilderStartsWith(str,"\r")) {
			str = str.delete(0,1);
		}
		while (Generic.stringBuilderEndsWith(str," ") || Generic.stringBuilderEndsWith(str,"\n") || Generic.stringBuilderStartsWith(str,"\r")) {
			str.delete(str.length()-1,str.length());
		}
		return str;
	}

	public final static StringBuilder stringBuilderReplaceStartEnd(StringBuilder str, String searchStart, String searchEnd, String replace) {
		if (str!=null) {
			int length = str.length();
			int stLength = searchStart.length();
			int seLength = searchEnd.length();
			if (length>=(stLength + seLength)) {
				boolean start = false;
				int startIdx = 0;
				int endIdx = 0;
				for (int i = 0; i < length; i++) {
					if (!start) {
						if ((i + searchStart.length()) > length) {
							break;
						}
						if (str.substring(i,(i + searchStart.length())).equals(searchStart)) {
							start = true;
							startIdx = i;
							i = i + (searchStart.length() - 1);
						}
					}
					if (start) {
						if ((i + searchEnd.length()) > length) {
							break;
						}
						if (str.substring(i,(i + searchEnd.length())).equals(searchEnd)) {
							start = false;
							endIdx = i + searchEnd.length();
							str.replace(startIdx,endIdx, replace);
							if (replace.length()>1) {
								i = startIdx + replace.length() - 1;
							}
							length = str.length();
						}
					}
				}
			}
		}
		return str;
	}
	
	public final static StringBuilder stringBuilderReplace(StringBuilder str, String search, String replace) {
		if (str!=null) {
			int length = str.length();
			int sLength = search.length();
			if (length>=sLength) {
				boolean found = true;
				for (int i = 0; i < length; i++) {
					found = true;
					for (int si = 0; si < sLength; si++) {
						if ((i + si) >= length) {
							found = false;
							break;
						}
						if (!str.substring(i + si,i + si + 1).equals(search.substring(si,si+1))) {
							found = false;
							break;
						}
					}
					if (found) {
						str.replace(i, i + sLength, replace);
						if (replace.length()>1) {
							i = i + replace.length() - 1;
						}
						length = str.length();
					}
				}
			}
		}
		return str;
	}

	public final static List<StringBuilder> stringBuilderSplit(StringBuilder str, String split) {
		List<StringBuilder> strs = new ArrayList<StringBuilder>();
		if (str!=null) {
			StringBuilder sAdd = new StringBuilder();
			int length = str.length();
			int sLength = split.length();
			boolean found = true;
			for (int i = 0; i < length; i++) {
				found = true;
				for (int si = 0; si < sLength; si++) {
					if ((i + si) >= length) {
						found = false;
						break;
					}
					if (!str.substring(i + si,i + si + 1).equals(split.substring(si,si + 1))) {
						found = false;
						break;
					}
				}
				if (found) {
					strs.add(sAdd);
					i = i + (sLength - 1);
					sAdd = new StringBuilder();
				} else {
					sAdd.append(str.substring(i,i + 1));
				}
			}
			if (sAdd.length()>0) {
				strs.add(sAdd);
			}
		}
		return strs;
	}

	public final static boolean stringBuilderContainsOneOfCharacters(StringBuilder str, String characters) {
		return stringBuilderContainsOneOfCharacters(str,characters,false);
	}

	public final static boolean stringBuilderContainsOneOfCharacters(StringBuilder str, String characters, boolean caseSensitive) {
		boolean contains = false;
		if (str!=null) {
			int length = str.length();
			int cLength = characters.length();
			for (int i = 0; i < length; i++) {
				for (int ci = 0; ci < cLength; ci++) {
					if ((!caseSensitive && str.substring(i,i + 1).toUpperCase().equals(characters.substring(ci,ci + 1))) || 
						(caseSensitive && str.substring(i,i + 1).equals(characters.substring(ci,ci + 1)))
						) {
						contains = true;
						break;
					}
				}
				if (contains) {
					break;
				}
			}
		}
		return contains;
	}
	
	public final static boolean stringBuilderContainsOneOfNonAllowedCharacters(StringBuilder str, String allowedcharacters) {
		return stringBuilderContainsOneOfNonAllowedCharacters(str,allowedcharacters,false);
	}

	public final static boolean stringBuilderContainsOneOfNonAllowedCharacters(StringBuilder str, String allowedcharacters,boolean caseSensitive) {
		boolean contains = false;
		if (str!=null) {
			int length = str.length();
			int cLength = allowedcharacters.length();
			for (int i = 0; i < length; i++) {
				boolean found = false;
				for (int ci = 0; ci < cLength; ci++) {
					if ((!caseSensitive && str.substring(i,i + 1).toUpperCase().equals(allowedcharacters.substring(ci,ci + 1))) || 
						(caseSensitive && str.substring(i,i + 1).equals(allowedcharacters.substring(ci,ci + 1)))
						) {
						found = true;
						break;
					}
				}
				if (!found) {
					contains = true;
					break;
				}
			}
		}
		return contains;
	}
}

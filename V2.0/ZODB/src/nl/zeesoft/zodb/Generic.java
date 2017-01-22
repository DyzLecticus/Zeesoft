package nl.zeesoft.zodb;

import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Generic utilities
 * 
 * Including;
 * - Key and seed based string encoding and decoding.
 * - Long numeric string compression and decompression.
 * - Call stack to debug string conversion.
 */
public abstract class Generic {
	private static final String[]	CHAR_COMPRESS			= {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","1","2","3","4","5","6","7","8","9","0","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","#",":","~"};

	public static final BigDecimal	ZERO_VALUE_BIG_DECIMAL	= new BigDecimal("0");

	public static final String		ALPHABETIC				= "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String		NUMERIC					= "0123456789";
	public static final String		ALPHANUMERIC			= ALPHABETIC + NUMERIC;
	public static final String		PASSWORD				= "!@#%=:;";
	
	public static final String		SEP_OBJ					= "@" + "OBJ" + "@";		
	public static final String		SEP_STR					= "@" + "STR" + "@";		
	
	public final static String dirName(String s) {
		if (s.length()>0) {
			if (s.contains("\\")) {
				s = s.replace("\\", "/");
			}
			if (!s.endsWith("/")) {
				s = s + "/";
			}
		}
		return s; 
	}
	
	/**
	 * Returns the CRC value for a certain text
	 * 
	 * WARNING: Might not reflect minor differences
	 * 
	 * @param text The text
	 * @return The CRC value
	 */
	public final static long calculateCRC(String text) {
		long crc = 0;

		int multiply = 1; 
		String[] lines = text.split("\n");
		crc = crc + lines.length;
		for (int ln = 0; ln < lines.length; ln++) {
			crc = crc + (lines[ln].length() * multiply);
			multiply++;
		}

		multiply = 1;
		char[] characters = text.toCharArray();
		for (int p = 0; p < characters.length; p++) {
			crc = crc + (Character.valueOf(characters[p]) * multiply);
			multiply = (multiply + multiply);
		}

	   	return crc;
	}
	
	/**
	 * Generates a random integer
	 * 
	 * @param valFrom Value from
	 * @param valTill Value till
	 * @return A random integer
	 */
	public final static int generateRandom (int valFrom, int valTill){
		Random rand = new Random();
		int num = -1;
		while (num < valFrom) {
			 num = (int) (rand.nextDouble() * (valTill + 1));
		}
		return num;
	}

	public final static String generateNewKey(int length) {
		StringBuilder ps = new StringBuilder();
		if (length < 64) {
			length = 64;
		}
		for (int i = 0; i < length; i++) {
			int val = Generic.generateRandom(0,99999999);
			ps.append(val);
			i = (ps.length() - 1);
			if (i < length) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// Ignore
				}
			} else if (i > length) {
				ps.replace(length - 1, i,"");
			}
		}
		return ps.toString();
	}

	public final static StringBuilder encodeAscii(StringBuilder str) {
		StringBuilder r = new StringBuilder();
		if (str.length()>0) {
			int[] ascVals = new int[str.length()];
			int avg = 0;
			for (int i = 0; i < str.length(); i++) {
				ascVals[i] = Character.valueOf(str.charAt(i));
				avg+=ascVals[i];
			}
			int key = (avg / str.length()) / 2;
			r.append(key);
			r.append(",");
			int pKey = key;
			for (int i = 0; i < ascVals.length; i++) {
				int iKey = key + (((i + 1) * pKey * 7) % 24);
				pKey = iKey;
				if (i>0) {
					r.append(",");
				}
				if (ascVals[i]==iKey) {
					r.append("0");
				} else if (ascVals[i]>iKey) {
					r.append(ascVals[i] - iKey);
				} else if (ascVals[i]<iKey) {
					r.append("-");
					r.append(iKey - ascVals[i]);
				}
			}
		}
		return r;
	}

	public final static StringBuilder decodeAscii(StringBuilder str) {
		StringBuilder r = new StringBuilder();
		if (str.length()>0) {
			List<StringBuilder> keyVals = Generic.stringBuilderSplit(str,",");
			if (keyVals.size()>=2) {
				int key = Integer.parseInt(keyVals.get(0).toString());
				int i = 0;
				int pKey = key;
				for (StringBuilder val: keyVals) {
					if (i>0) {
						int iKey = key + ((i * pKey * 7) % 24);
						pKey = iKey;
						int iVal = iKey;
						int v = Integer.parseInt(val.toString());
						if (v>0) {
							iVal = iVal + v;							
						} else if (v<0) {
							iVal = iVal - (v * -1);							
						}
						r.append((char) iVal);
					}
					i++;
				}
			}
		}
		return r;
	}
	
	/**
	 * Returns a key encoded text
	 * 
	 * @param text The text
	 * @param key The key
	 * @param seed The optional seed
	 * @return The encoded text
	 */
	public final static StringBuilder encodeKey(StringBuilder text, String key, long seed) {
		int sVar = ((int) (seed % 200));
		StringBuilder s = new StringBuilder();
		String str = "";
		int	val = 0;
		int idx = 0;
		int end = 0;
		char[] characters = new char[text.length()];
		text.getChars(0, text.length(), characters, 0);
		for (int p = 0; p < characters.length; p++) {
			idx = p % key.length();
			end = idx + 3;
			if (end > key.length()) {
				end = key.length();
			}
			val = (Character.valueOf(characters[p]) + ((Integer.parseInt(key.substring(idx,end))) / 2) + sVar);
			str = "" + val;
			while (str.length() < 3) {
				str = "0" + str;
			}
			s.append(str);
		}
		return compress(s,getCharCompressForKey(key,seed));
	}
	
	/**
	 * Returns a key decoded text
	 * 
	 * @param text The encoded text
	 * @param key The key
	 * @param seed The optional seed
	 * @return The decoded text
	 */
	public final static StringBuilder decodeKey(StringBuilder text, String key, long seed) {
		int sVar = ((int) (seed % 200));
		String str = "";
		int idx = 0;
		int end = 0;
		
		StringBuilder s = new StringBuilder();
		s.append(decompress(text,getCharCompressForKey(key,seed)));
		
		int len = s.length();
		char[] characters = new char[(len / 3)];
		for (int p = 0; p < (len / 3); p++) {
			idx = p % key.length();
			end = idx + 3;
			if (end > key.length()) {
				end = key.length();
			}
			str = s.substring((p * 3), ((p * 3) + 3));
			characters[p] = (char) (Integer.parseInt(str) - ((Integer.parseInt(key.substring(idx,end))) / 2) - sVar);
		}
		s = new StringBuilder();
		s.append(characters);
		return s;
	}
	
	private final static int getKeyOffset(String key) {
		int idx = 0;
		if (key.length()>=1024) {
			idx = (Integer.parseInt(key.substring(0,3)) + 1);
		} else if (key.length()>=512) {
			idx = (Integer.parseInt(key.substring(0,3)) + 1) / 2;
		} else if (key.length()>=256) {
			idx = ((Integer.parseInt(key.substring(0,2)) + 1) * 2);
		} else if (key.length()>=128) {
			idx = (Integer.parseInt(key.substring(0,2)) + 1);
		} else if (key.length()>=64) {
			idx = ((Integer.parseInt(key.substring(0,2)) + 1) / 2);
		}
		return (Integer.parseInt(key.substring(idx,idx + 6)));
	}

	private final static String[] getCharCompressForKey(String key, long seed) {
		int s = 0;
		if (seed > (Integer.MAX_VALUE - 999999)) {
			s = (Integer.MAX_VALUE - 999999);
		} else {
			s = (int) seed;
		}
		int start = (getKeyOffset(key) + s) % CHAR_COMPRESS.length;
		String[] charCompress = new String[CHAR_COMPRESS.length];
		for (int i = start; i < CHAR_COMPRESS.length + start; i ++) {
			int m = i % CHAR_COMPRESS.length;
			charCompress[(i - start)] = CHAR_COMPRESS[m];
		}
		return charCompress;
	}

	public final static StringBuilder compress(StringBuilder sb) {
		return compress(sb,CHAR_COMPRESS);
	}

	public final static StringBuilder decompress(StringBuilder sb) {
		return decompress(sb,CHAR_COMPRESS);
	}

	public final static StringBuilder decompress(StringBuilder sb, String[] charCompress) {
		StringBuilder r = new StringBuilder();
		if ((sb.length() % 2) != 1) {
			return r;
		}
		int added = Integer.parseInt(sb.substring(sb.length() - 1));
		sb = new StringBuilder(sb.substring(0,(sb.length() - 1)));
		SortedMap<String,Integer> decompressMap = new TreeMap<String,Integer>();
		for (int i = 0; i < charCompress.length; i++) {
			decompressMap.put(charCompress[i],i);
		}
		int len = sb.length() / 2;
		String s1 = "";
		String s2 = "";
		String st = "";
		for (int i = 0; i < len; i++) {
			s1 = sb.substring((i*2),((i*2)+1));
			s2 = sb.substring(((i*2)+1),((i*2)+2));
			if ((decompressMap.get(s1)==null) || (decompressMap.get(s1)==null)) {
				return r;
			}
			st = "" + (decompressMap.get(s1) + (decompressMap.get(s2) * charCompress.length));
			while (st.length()<3) {
				st = "0" + st;
			}
			if ((i==(len-1)) && (added>0)  && (added<3)) {
				st = st.substring(0,(3 - added));
			}
			r.append(st);
		}
		return r;
	}

	public final static StringBuilder compress(StringBuilder sb, String[] charCompress) {
		StringBuilder r = new StringBuilder();
		int n1 = 0;
		int n2 = 0;
		int added = 0;
		if ((sb.length() % 3) != 0) {
			n1 = sb.length() % 3;
			for (n2=0;n2<(3-n1);n2++) {
				sb.append("0");
				added++;
			}
		}
		if ((sb.length() % 3) != 0) {
			return r;
		}
		int len = sb.length() / 3;
		int num = 0;
		n1 = 0;
		n2 = 0;
		for (int i = 0; i < len; i++) {
			num = Integer.parseInt(sb.substring((i*3),((i*3)+3)));
			n1 = num % charCompress.length;
			n2 = (num - n1) / charCompress.length;
			r.append(charCompress[n1]);
			r.append(charCompress[n2]);
		}
		r.append(added);
		return r;
	}

	public final static String[] getValuesFromString(String str) {
		String[] l = null;
		if ((str!=null) && (str.length()>0)) {
			l = str.split(SEP_STR);
		}
		return l;
	}
	
	public final static List<String> getObjectsFromString(String str) {
		List<String> l = new ArrayList<String>();
		if ((str!=null) && (str.length()>0)) {
			String[] obj = str.split(SEP_OBJ);
			for (int i = 0; i < obj.length; i++) {
				l.add(obj[i]);
			}
		}
		return l;
	}

	public final static String getSerializableStringValue(String str) {
		str = str.replace(SEP_OBJ, ";");
		str = str.replace(SEP_STR, ",");
		str = str.replace("\n", "[]");
		str = str.replace(("<" + "!" + "[" + "CDATA"+ "["),"[CDATA_START]");
		str = str.replace(("]" + "]" + ">"),"[CDATA_END]");
		return str;
	}
	
	public final static Class<?> forName(String className) {
		return forName(className,false);
	}

	private final static Class<?> forName(String className, boolean silent) {
		Class<?> cls = null;
		try {
			cls = Class.forName(className);
		} catch (ClassNotFoundException e) {
			if (!silent) {
				e.printStackTrace();
			}
		}
		return cls;
	}
	
	public final static Object testInstanceForName(String className) {
		return instanceForName(className,true) ;
	}

	public final static Object instanceForName(String className) {
		return instanceForName(className,false) ;
	}

	private final static Object instanceForName(String className, boolean silent) {
		Object o = null;
		Class<?> cls = forName(className,silent);
		if (cls!=null) {
			try {
				o = cls.newInstance();
			} catch (InstantiationException e) {
				if (!silent) {
					e.printStackTrace();
				}
			} catch (IllegalAccessException e) {
				if (!silent) {
					e.printStackTrace();
				}
			}
		}
		return o;
	}
	
	public final static boolean instanceOf(Class<?> cls, String instanceOf) {
		boolean b = false;
		if (cls.getName().equals(instanceOf)) {
			b = true;
		} else if (cls.getSuperclass()!=null) {
			b = Generic.instanceOf(cls.getSuperclass(), instanceOf);
		}
		return b;
	}

	public final static boolean instanceOfImplements(Class<?> cls, String implement) {
		boolean b = false;
		for (Class<?> iface: cls.getInterfaces()) {
			if (iface.getName().equals(implement)) {
				b = true;
				break;
			}
		}
		if (!b && cls.getSuperclass()!=null) {
			b = Generic.instanceOfImplements(cls.getSuperclass(), implement);
		}
		return b;
	}
	
	public final static Object executeObjectClassMethod(Object o, String methodName, Class<?>[] params) {
		if (params==null) {
			params = new Class<?>[0];
		}
		Object paramsObj[] = {};
		Method doMethod = null;
		Object value = null;
		
		try {
			doMethod = o.getClass().getMethod(methodName,params);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		try {
			value = doMethod.invoke(o, paramsObj);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return value;
	}

	public final static boolean isNumeric(String str){
		for (char c:str.toCharArray()) {
	        if (!Character.isDigit(c)) {
	        	return false;
	        }
	    }
	    return true;
	}	

	public final static boolean isNumeric(StringBuilder str){
		char[] chrs = new char[str.length()];
		str.getChars(0, 0, chrs, 0);
		for (char c: chrs) {
	        if (!Character.isDigit(c)) {
	        	return false;
	        }
	    }
	    return true;
	}	

	public final static boolean stringContainsOneOfCharacters(String str, String characters) {
		boolean contains = false;
		if ((str!=null) && (str.length()>0)) {
			str = str.toUpperCase();
			for (int i = 0; i<characters.length(); i++) {
				if (str.contains(characters.substring(i, (i+1)))) {
					contains = true;
					break;
				}
			}
		}
		return contains;
	}

	public final static String replaceNonAllowedCharactersInString(String str, String allowedCharacters, String replaceCharacter) {
		StringBuilder sb = new StringBuilder();
		if ((str!=null) && (str.length()>0)) {
			for (int i = 0; i<str.length(); i++) {
				String s = str.substring(i,(i+1));
				if (allowedCharacters.contains(s.toUpperCase())) {
					sb.append(s);
				} else {
					sb.append(replaceCharacter);
				}
			}
		}
		return sb.toString();
	}

	public final static String removeNonAllowedCharactersFromString(String str, String allowedCharacters) {
		return replaceNonAllowedCharactersInString(str,allowedCharacters,"");
	}
	
	public final static String getCallStackString(StackTraceElement[] stack, String startingClassName) {
		String value = "";
		boolean start = false;
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

	public final static boolean idListEquals(List<Long> idList1, List<Long> idList2) {
		boolean eq = true;
		if (idList1!=null && idList2!=null) {
			if (idList1.size()==idList2.size()) {
				int i = 0;
				for (long id1: idList1) {
					long id2 = idList2.get(i);
					if (id1!=id2) {
						eq = false;
						break;
					}
					i++;
				}
			} else {
				eq = false;
			}
		} else if (
			(idList1==null && idList2!=null) ||
			(idList1!=null && idList2==null)
			) {
			eq = false;
		}
		return eq;
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

	public final static List<StringBuilder> getValuesFromStringBuilder(StringBuilder str) {
		List<StringBuilder> l = null;
		if ((str!=null) && (str.length()>0)) {
			l = stringBuilderSplit(str,SEP_STR);
		} else {
			l = new ArrayList<StringBuilder>();
		}
		return l;
	}

	public final static boolean canStartBrowser() {
		boolean canStart = false;
		try {
			Desktop desktop = Desktop.getDesktop();
			canStart = desktop.isSupported(Desktop.Action.BROWSE);
		} catch(HeadlessException e) {
			// Ignore
		} catch(UnsupportedOperationException  e) {
			// Ignore
		}
		return canStart;
	}

	public final static boolean startBrowserAtUrl(String url) {
		boolean started = false;
		if (canStartBrowser()) {
			Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.BROWSE)) {
				try {
					URL ur = new URL(url);
					desktop.browse(ur.toURI());
					started = true;
				} catch (URISyntaxException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return started;
	}

	public final static List<String> getDirNumberedFileNames(String dirName) {
		List<String> files = new ArrayList<String>();
		File dir = new File(dirName);
		String[] indexFiles = dir.list();
		if (indexFiles!=null) {
			for (int i = 0; i < indexFiles.length; i++) {
				if (Generic.isNumeric(indexFiles[i])) {
					files.add(indexFiles[i]);
				}
			}
		}
		return files;
	}
	
	/**
	 * The following base64 encoding and decoding methods were copied from https://en.wikipedia.org/wiki/Base64
	 */
	
    private static final String CODES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    public static final String base64Decode(StringBuilder input) {
    	return new String(base64Decode(input));
    }
    
    public static final byte[] base64Decode(String input) {
        if (input.length() % 4 != 0)    {
            throw new IllegalArgumentException("Invalid base64 input");
        }
        byte decoded[] = new byte[((input.length() * 3) / 4) - (input.indexOf('=') > 0 ? (input.length() - input.indexOf('=')) : 0)];
        char[] inChars = input.toCharArray();
        int j = 0;
        int b[] = new int[4];
        for (int i = 0; i < inChars.length; i += 4)     {
            // This could be made faster (but more complicated) by precomputing these index locations.
            b[0] = CODES.indexOf(inChars[i]);
            b[1] = CODES.indexOf(inChars[i + 1]);
            b[2] = CODES.indexOf(inChars[i + 2]);
            b[3] = CODES.indexOf(inChars[i + 3]);
            decoded[j++] = (byte) ((b[0] << 2) | (b[1] >> 4));
            if (b[2] < 64)      {
                decoded[j++] = (byte) ((b[1] << 4) | (b[2] >> 2));
                if (b[3] < 64)  {
                    decoded[j++] = (byte) ((b[2] << 6) | b[3]);
                }
            }
        }

        return decoded;
    }

    public static final StringBuilder base64Encode(StringBuilder in) {
    	return base64Encode(String.valueOf(in).getBytes());
    }

    public static final StringBuilder base64Encode(byte[] in) {
        StringBuilder out = new StringBuilder((in.length * 4) / 3);
        int b;
        for (int i = 0; i < in.length; i += 3)  {
            b = (in[i] & 0xFC) >> 2;
            out.append(CODES.charAt(b));
            b = (in[i] & 0x03) << 4;
            if (i + 1 < in.length)      {
                b |= (in[i + 1] & 0xF0) >> 4;
                out.append(CODES.charAt(b));
                b = (in[i + 1] & 0x0F) << 2;
                if (i + 2 < in.length)  {
                    b |= (in[i + 2] & 0xC0) >> 6;
                    out.append(CODES.charAt(b));
                    b = in[i + 2] & 0x3F;
                    out.append(CODES.charAt(b));
                } else  {
                    out.append(CODES.charAt(b));
                    out.append('=');
                }
            } else      {
                out.append(CODES.charAt(b));
                out.append("==");
            }
        }

        return out;
    }	
}

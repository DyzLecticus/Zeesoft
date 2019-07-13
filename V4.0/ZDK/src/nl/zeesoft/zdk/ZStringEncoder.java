package nl.zeesoft.zdk;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * The Zeesoft StringEncoder extends the Zeesoft StringBuilder with key based encoding and decoding and more.
 */
public class ZStringEncoder extends ZStringBuilder {
	private static final String[]	CHAR_COMPRESS	= {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","1","2","3","4","5","6","7","8","9","0","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","#",":","~"};
	private static final char		CHAR_MINUS		= "-".charAt(0);
	
	private ZIntegerGenerator		generator		= null;

	public ZStringEncoder() {
		generator = new ZIntegerGenerator(0,99999999);
	}
	
	public ZStringEncoder(ZIntegerGenerator generator) {
		this.generator = generator;
	}

	public ZStringEncoder(String s,ZIntegerGenerator generator) {
		super(s);
		this.generator = generator;
	}

	public ZStringEncoder(StringBuilder sb,ZIntegerGenerator generator) {
		super(sb);
		this.generator = generator;
	}

	public ZStringEncoder(ZStringBuilder zsb,ZIntegerGenerator generator) {
		super(zsb);
		this.generator = generator;
	}

	public ZStringEncoder(String s) {
		super(s);
		generator = new ZIntegerGenerator(0,99999999);
	}

	public ZStringEncoder(StringBuilder sb) {
		super(sb);
		generator = new ZIntegerGenerator(0,99999999);
	}

	public ZStringEncoder(ZStringBuilder zsb) {
		super(zsb);
		generator = new ZIntegerGenerator(0,99999999);
	}
	
	@Override
	public ZStringEncoder getCopy() {
		return new ZStringEncoder(getStringBuilder(),null);
	}
	
	/**
	 * Generates a key for key based encoding and decoding.
	 * 
	 * @param length The length of the key (longer is safer)
	 * @return The generated key
	 */
	public StringBuilder generateNewKey(int length) {
		StringBuilder k = new StringBuilder();
		if (length < 64) {
			length = 64;
		}
		int pv = -1;
		for (int i = 0; i < length; i++) {
			int v = pv;
			while (v == pv) {
				v = generator.getNewInteger();
			}
			k.append(v);
			if (k.length()>=length) {
				break;
			}
		}
		if (k.length()>length) {
			k.delete(length,k.length());
		}
		return k;
	}

	/**
	 * Returns the StringBuilder value encoded using the key and the optional seed.
	 * 
	 * @param key The key
	 * @param seed The optional seed
	 * @return The encoded StringBuilder value
	 */
	public StringBuilder encodeKey(StringBuilder key, long seed) {
		if (getStringBuilder()!=null && isNumber(key,false)) {
			int sVar = ((int) (seed % 200));
			StringBuilder s = new StringBuilder();
			String str = "";
			int	val = 0;
			int idx = 0;
			int end = 0;
			char[] characters = new char[getStringBuilder().length()];
			getStringBuilder().getChars(0, getStringBuilder().length(), characters, 0);
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
			setStringBuilder(s);
			compress(getCharCompressForKey(key,seed));
		}
		return getStringBuilder();
	}
	
	/**
	 * Returns the StringBuilder value decoded using the key and the optional seed.
	 * 
	 * @param key The key
	 * @param seed The optional seed
	 * @return The decoded text
	 */
	public StringBuilder decodeKey(StringBuilder key, long seed) {
		if (getStringBuilder()!=null && isNumber(key,false)) {
			int sVar = ((int) (seed % 200));
			String str = "";
			int idx = 0;
			int end = 0;
			decompress(getCharCompressForKey(key,seed));
			StringBuilder sb = getStringBuilder();
			int len = sb.length();
			char[] characters = new char[(len / 3)];
			for (int p = 0; p < (len / 3); p++) {
				idx = p % key.length();
				end = idx + 3;
				if (end > key.length()) {
					end = key.length();
				}
				str = sb.substring((p * 3), ((p * 3) + 3));
				if (isNumberNotNegative(str)) {
					characters[p] = (char) (Integer.parseInt(str) - ((Integer.parseInt(key.substring(idx,end))) / 2) - sVar);
				} else {
					characters[p] = CHAR_MINUS;
				}
			}
			sb = new StringBuilder();
			sb.append(characters);
			setStringBuilder(sb);
		}
		return getStringBuilder();
	}

	public StringBuilder compress() {
		return compress(CHAR_COMPRESS);
	}

	public StringBuilder decompress() {
		return decompress(CHAR_COMPRESS);
	}

	public StringBuilder decompress(String[] charCompress) {
		if (getStringBuilder()!=null && getStringBuilder().length()>0) {
			StringBuilder sb = getStringBuilder();
			StringBuilder r = new StringBuilder();
			if ((sb.length() % 2) != 1) {
				return r;
			}
			String tail = sb.substring(sb.length() - 1);
			int added = 0;
			if (isNumberNotNegative(tail)) {
				added = Integer.parseInt(tail);
			}
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
			setStringBuilder(r);
		}
		return getStringBuilder();
	}

	public StringBuilder compress(String[] charCompress) {
		if (getStringBuilder()!=null) {
			StringBuilder sb = getStringBuilder();
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
			String sub = "";
			for (int i = 0; i < len; i++) {
				sub = sb.substring((i*3),((i*3)+3));
				if (isNumber(sub)) {
					num = Integer.parseInt(sub);
				} else {
					num = 0;
				}
				n1 = num % charCompress.length;
				n2 = (num - n1) / charCompress.length;
				r.append(charCompress[n1]);
				r.append(charCompress[n2]);
			}
			r.append(added);
			setStringBuilder(r);
		}
		return getStringBuilder();
	}

	/**
	 * Returns the StringBuilder value encoded to ASCII.
	 * 
	 * @return The ASCII StringBuilder value
	 */
	public StringBuilder encodeAscii() {
		if (getStringBuilder()!=null) {
			StringBuilder sb = getStringBuilder();
			StringBuilder r = new StringBuilder();
			if (sb.length()>0) {
				int[] ascVals = new int[sb.length()];
				int avg = 0;
				for (int i = 0; i < sb.length(); i++) {
					ascVals[i] = Character.valueOf(sb.charAt(i));
					avg+=ascVals[i];
				}
				int key = (avg / sb.length()) / 2;
				r.append(key);
				int pKey = key;
				for (int i = 0; i < ascVals.length; i++) {
					int iKey = key + (((i + 1) * pKey * 7) % 24);
					pKey = iKey;
					r.append(",");
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
			setStringBuilder(r);
		}
		return getStringBuilder();
	}

	/**
	 * Returns the StringBuilder value decoded from ASCII.
	 * 
	 * @return The ASCII decoded StringBuilder value 
	 */
	public StringBuilder decodeAscii() {
		if (getStringBuilder()!=null) {
			StringBuilder r = new StringBuilder();
			if (getStringBuilder().length()>0) {
				List<ZStringBuilder> keyVals = split(",");
				if (keyVals.size()>=2 && isNumber(keyVals.get(0).toString())) {
					int key = Integer.parseInt(keyVals.get(0).toString());
					int i = 0;
					int pKey = key;
					for (ZStringBuilder val: keyVals) {
						if (i>0 && isNumber(val.toString())) {
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
			setStringBuilder(r);
		}
		return getStringBuilder();
	}

	/**
	 * Returns true if a string is a number.
	 * 
	 * @param str The string
	 * @return True if the string is a number
	 */
	public static final boolean isNumber(String str) {
		return isNumber(str,true);
	}
	
	/**
	 * Returns true if a string is a positive number or zero.
	 * 
	 * @param str The string
	 * @return True if the string is a positive number
	 */
	public static final boolean isNumberNotNegative(String str) {
		return isNumber(str,false);
	}
	
	/**
	 * Returns true if a string is a number.
	 * 
	 * @param str The string
	 * @param allowNegative Indicates negative integer values are considered valid
	 * @return True if the string is a number
	 */
	public static final boolean isNumber(String str, boolean allowNegative) {
		boolean r = str.length()>0;
		int i = 0;
		for (char c:str.toCharArray()) {
	        if (!(Character.isDigit(c) || (i==0 && allowNegative && c==CHAR_MINUS && str.length()>1))) {
	        	r = false;
	        	break;
	        }
	        i++;
	    }
	    return r;
	}
	
	/**
	 * Returns true if a string builder is a number.
	 * 
	 * @param str The string builder
	 * @param allowNegative Indicates negative integer values are considered valid
	 * @return True if the string builder is a number
	 */
	public static final boolean isNumber(StringBuilder str, boolean allowNegative) {
		boolean r = str.length()>0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
	        if (!(Character.isDigit(c) || (i==0 && allowNegative && c==CHAR_MINUS && str.length()>1))) {
	        	r = false;
	        	break;
	        }
		}
	    return r;
	}
	
	private int getKeyOffset(StringBuilder key) {
		int r = 0;
		if (isNumber(key,false)) {
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
			r = Integer.parseInt(key.substring(idx,idx + 6));
		}
		return r;
	}

	private String[] getCharCompressForKey(StringBuilder key, long seed) {
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
}

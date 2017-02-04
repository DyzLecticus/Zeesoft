package nl.zeesoft.zdk;

import java.util.List;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * The Zeesoft StringEncoder extends the Zeesoft StringBuilder with key based encoding and decoding and more.
 */
public class ZStringEncoder extends ZStringBuilder {
	private static final String[]	CHAR_COMPRESS	= {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","1","2","3","4","5","6","7","8","9","0","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","#",":","~"};
	
	public ZStringEncoder() {
		super();
	}

	public ZStringEncoder(String s) {
		super(s);
	}

	public ZStringEncoder(StringBuilder sb) {
		super(sb);
	}

	public ZStringEncoder(ZStringBuilder zsb) {
		super(zsb);
	}
	
	/**
	 * Generates a random integer.
	 * 
	 * @param valFrom Value from
	 * @param valTill Value till
	 * @return A random integer
	 */
	public int generateRandom (int valFrom, int valTill) {
		Random rand = new Random();
		int num = -1;
		while (num < valFrom) {
			 num = (int) (rand.nextDouble() * (valTill + 1));
		}
		return num;
	}
	
	/**
	 * Generates a key for key based encoding and decoding.
	 * 
	 * @param length The length of the key (longer is safer)
	 * @return The generated key
	 */
	public String generateNewKey(int length) {
		StringBuilder ps = new StringBuilder();
		if (length < 64) {
			length = 64;
		}
		for (int i = 0; i < length; i++) {
			int val = generateRandom(0,99999999);
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

	/**
	 * Returns the StringBuilder value encoded using the key and the optional seed.
	 * 
	 * @param key The key
	 * @param seed The optional seed
	 * @return The encoded StringBuilder value
	 */
	public StringBuilder encodeKey(String key, long seed) {
		if (getStringBuilder()!=null) {
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
	public StringBuilder decodeKey(String key, long seed) {
		if (getStringBuilder()!=null) {
			int sVar = ((int) (seed % 200));
			String str = "";
			int idx = 0;
			int end = 0;
			
			StringBuilder s = new StringBuilder();
			s.append(decompress(getCharCompressForKey(key,seed)));
			
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
			setStringBuilder(s);
		}
		return getStringBuilder();
	}
	
	private int getKeyOffset(String key) {
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

	private String[] getCharCompressForKey(String key, long seed) {
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

	public StringBuilder compress() {
		return compress(CHAR_COMPRESS);
	}

	public StringBuilder decompress() {
		return decompress(CHAR_COMPRESS);
	}

	public StringBuilder decompress(String[] charCompress) {
		if (getStringBuilder()!=null) {
			StringBuilder sb = getStringBuilder();
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
			for (int i = 0; i < len; i++) {
				num = Integer.parseInt(sb.substring((i*3),((i*3)+3)));
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
	 * Returns the StringBuilder value encoded to ASCII
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
			setStringBuilder(r);
		}
		return getStringBuilder();
	}

	/**
	 * Returns the StringBuilder value decoded from ASCII
	 * 
	 * @return The ASCII decoded StringBuilder value 
	 */
	public StringBuilder decodeAscii() {
		if (getStringBuilder()!=null) {
			StringBuilder r = new StringBuilder();
			if (getStringBuilder().length()>0) {
				List<StringBuilder> keyVals = split(",");
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
		}
		return getStringBuilder();
	}

}

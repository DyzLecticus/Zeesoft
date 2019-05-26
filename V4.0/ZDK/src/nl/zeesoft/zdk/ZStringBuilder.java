package nl.zeesoft.zdk;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * The Zeesoft StringBuilder provides advanced StringBuilder manipulation and methods.
 */
public class ZStringBuilder implements Comparable<ZStringBuilder> {
	private StringBuilder	sb			= null;
	private	String			encoding	= "UTF8"; 
	
	public ZStringBuilder() {
		sb = new StringBuilder();
	}

	public ZStringBuilder(String s) {
		sb = new StringBuilder(s);
	}

	public ZStringBuilder(StringBuilder sb) {
		this.sb = new StringBuilder(sb);
	}

	public ZStringBuilder(ZStringBuilder zsb) {
		sb = new StringBuilder(zsb.getStringBuilder());
	}
	
	@Override
	public String toString() {
		String r = null;
		if (sb!=null) {
			r = sb.toString();
		} else {
			r = "";
		}
		return r;
	}
	
	public void append(String x) {
		if (sb!=null) {
			sb.append(x);
		}
	}

	public void append(StringBuilder x) {
		if (sb!=null) {
			sb.append(x);
		}
	}

	public void append(ZStringBuilder x) {
		if (sb!=null) {
			sb.append(x.getStringBuilder());
		}
	}
	
	public void insert(int pos,String x) {
		if (sb!=null) {
			sb.insert(pos,x);
		}
	}

	public void insert(int pos,StringBuilder x) {
		if (sb!=null) {
			sb.insert(pos,x);
		}
	}

	public void insert(int pos,ZStringBuilder x) {
		if (sb!=null) {
			sb.insert(pos,x.getStringBuilder());
		}
	}

	public void replace(int start, int end, String replace) {
		if (sb!=null) {
			sb.replace(start, end, replace);
		}
	}

	public ZStringBuilder substring(int start) {
		return substring(start,sb.length());
	}

	public ZStringBuilder substring(int start, int end) {
		ZStringBuilder r = null;
		if (sb!=null) {
			r = new ZStringBuilder();
			for (int i = start; i<end; i++) {
				r.append(sb.substring(i,i+1));
			}
		}
		return r;
	}
	
	public ZStringBuilder getCopy() {
		return new ZStringBuilder(sb);
	}
	
	public StringBuilder getStringBuilder() {
		return sb;
	}

	public void setStringBuilder(StringBuilder sb) {
		this.sb = sb;
	}

	public int length() {
		int r = 0;
		if (sb!=null) {
			r = sb.length();
		}
		return r;
	}
	
	public ZStringBuilder substr(int s,int e) {
		ZStringBuilder r = new ZStringBuilder();
		if (sb!=null) {
			for (int i = s; i < e; i++) {
				r.append(sb.substring(i,i+1));
			}
		}
		return r;
	}

	public boolean endsWith(String s) {
		return (sb!=null && sb.length()>=s.length() && sb.substring(sb.length() - s.length(),sb.length()).equals(s));
	}

	public boolean endsWith(ZStringBuilder zsb) {
		return (sb!=null && sb.length()>=zsb.length() && substr(sb.length() - zsb.length(),sb.length()).equals(zsb));
	}

	public boolean startsWith(String s) {
		return (sb!=null && sb.length()>=s.length() && sb.substring(0, s.length()).equals(s));
	}
	
	public boolean startsWith(ZStringBuilder zsb) {
		return (sb!=null && sb.length()>=zsb.length() && substr(0, zsb.length()).equals(zsb));
	}

	public boolean equals(ZStringBuilder zsb) {
		return equals(zsb.getStringBuilder(),false);
	}
	
	public boolean equalsIgnoreCase(ZStringBuilder zsb) {
		return equals(zsb.getStringBuilder(),true);
	}

	public boolean equals(StringBuilder sbc) {
		return equals(sbc,false);
	}
	
	public boolean equalsIgnoreCase(StringBuilder sbc) {
		return equals(sbc,true);
	}

	public boolean equals(StringBuilder sbc,boolean ignoreCase) {
		boolean eq = true;
		if (sb!=null && sbc!=null) {
			if (sb.length()==sbc.length()) {
				for (int i = 0; i < sb.length(); i++) {
					if ((ignoreCase && !sb.substring(i,i+1).equalsIgnoreCase(sbc.substring(i,i+1))) ||
						!sb.substring(i,i+1).equals(sbc.substring(i,i+1))
						) {
						eq = false;
						break;
					}
				}
			} else {
				eq = false;
			}
		} else if (
			(sb==null && sbc!=null) ||
			(sb!=null && sbc==null)
			) {
			eq = false;
		}
		return eq;
	}

	public StringBuilder trim() {
		if (sb!=null) {
			while (startsWith(" ") || startsWith("\n") || startsWith("\r")) {
				sb = sb.delete(0,1);
			}
			while (endsWith(" ") || endsWith("\n") || startsWith("\r")) {
				sb.delete(sb.length()-1,sb.length());
			}
		}
		return sb;
	}

	public StringBuilder toCase(boolean lower) {
		if (sb!=null) {
			int length = sb.length();
			StringBuilder nsb = new StringBuilder();
			for (int i = 0; i < length; i++) {
				if (lower) {
					nsb.append(sb.substring(i,(i + 1)).toLowerCase());
				} else {
					nsb.append(sb.substring(i,(i + 1)).toUpperCase());
				}
			}
			sb = nsb;
		}
		return sb;
	}
	
	public StringBuilder upperCaseFirst() {
		if (sb!=null) {
			int length = sb.length();
			if (length>0) {
				StringBuilder nsb = new StringBuilder();
				nsb.append(sb.substring(0,1).toUpperCase());
				if (length>1) {
					nsb.append(sb.substring(1));
				}
				sb = nsb;
			}
		}
		return sb;
	}

	public StringBuilder replaceStartEnd(String searchStart, String searchEnd, String replace) {
		if (sb!=null) {
			int length = sb.length();
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
						if (sb.substring(i,(i + searchStart.length())).equals(searchStart)) {
							start = true;
							startIdx = i;
							i = i + (searchStart.length() - 1);
						}
					}
					if (start) {
						if ((i + searchEnd.length()) > length) {
							break;
						}
						if (sb.substring(i,(i + searchEnd.length())).equals(searchEnd)) {
							start = false;
							endIdx = i + searchEnd.length();
							sb.replace(startIdx,endIdx, replace);
							if (replace.length()>1) {
								i = startIdx + replace.length() - 1;
							}
							length = sb.length();
						}
					}
				}
			}
		}
		return sb;
	}
	
	public boolean contains(String search) {
		return contains(new ZStringBuilder(search));
	}
	
	public boolean contains(ZStringBuilder search) {
		boolean contains = false;
		if (sb!=null) {
			int length = sb.length();
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
						if (!substr(i + si,i + si + 1).equals(search.substr(si,si+1))) {
							found = false;
							break;
						}
					}
					if (found) {
						contains = true;
						break;
					}
				}
			}
		}
		return contains;
	}
	
	public StringBuilder replace(String search, String replace) {
		return replace(search,replace,false,false);
	}
	
	public StringBuilder replace(String search, String replace, boolean prefixAlphabetic,boolean suffixAlphabetic) {
		if (sb!=null) {
			int length = sb.length();
			int sLength = search.length();
			if (length>=sLength) {
				boolean found = true;
				for (int i = 0; i < length; i++) {
					found = true;
					if ((i==0 && prefixAlphabetic) ||
						(i>0 && prefixAlphabetic && !Character.isAlphabetic(sb.substring(i - 1,i).toCharArray()[0]))
						) {
						found = false;
					} else {
						for (int si = 0; si < sLength; si++) {
							if ((i + si) >= length) {
								found = false;
								break;
							}
							if (!sb.substring(i + si,i + si + 1).equals(search.substring(si,si+1))) {
								found = false;
								break;
							}
						}
					}
					if ((found && i==(length - 1) && suffixAlphabetic) ||
						(found && i<(length - 1) && suffixAlphabetic && !Character.isAlphabetic(sb.substring(i + 1,i + 2).toCharArray()[0]))
						) {
						found = false;
					}
					if (found) {
						sb.replace(i, i + sLength, replace);
						if (replace.length()>1) {
							i = i + replace.length() - 1;
						}
						length = sb.length();
					}
				}
			}
		}
		return sb;
	}

	public List<ZStringBuilder> split(String split) {
		List<ZStringBuilder> strs = new ArrayList<ZStringBuilder>();
		if (sb!=null) {
			ZStringBuilder sAdd = new ZStringBuilder();
			int length = sb.length();
			int sLength = split.length();
			boolean found = true;
			for (int i = 0; i < length; i++) {
				found = true;
				for (int si = 0; si < sLength; si++) {
					if ((i + si) >= length) {
						found = false;
						break;
					}
					if (!sb.substring(i + si,i + si + 1).equals(split.substring(si,si + 1))) {
						found = false;
						break;
					}
				}
				if (found) {
					strs.add(sAdd);
					i = i + (sLength - 1);
					sAdd = new ZStringBuilder();
				} else {
					sAdd.append(sb.substring(i,i + 1));
				}
			}
			if (sAdd.length()>0) {
				strs.add(sAdd);
			}
		}
		return strs;
	}

	public boolean containsOneOfCharacters(String characters) {
		return containsOneOfCharacters(characters,false);
	}

	public boolean containsOneOfCharacters(String characters, boolean caseSensitive) {
		boolean contains = false;
		if (sb!=null) {
			int length = sb.length();
			int cLength = characters.length();
			for (int i = 0; i < length; i++) {
				for (int ci = 0; ci < cLength; ci++) {
					if ((!caseSensitive && sb.substring(i,i + 1).equalsIgnoreCase(characters.substring(ci,ci + 1))) || 
						(caseSensitive && sb.substring(i,i + 1).equals(characters.substring(ci,ci + 1)))
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
	
	public boolean containsOneOfNonAllowedCharacters(String allowedcharacters) {
		return containsOneOfNonAllowedCharacters(allowedcharacters,false);
	}

	public boolean containsOneOfNonAllowedCharacters(String allowedcharacters,boolean caseSensitive) {
		boolean contains = false;
		if (sb!=null) {
			int length = sb.length();
			int cLength = allowedcharacters.length();
			for (int i = 0; i < length; i++) {
				boolean found = false;
				for (int ci = 0; ci < cLength; ci++) {
					if ((!caseSensitive && sb.substring(i,i + 1).equalsIgnoreCase(allowedcharacters.substring(ci,ci + 1))) || 
						(caseSensitive && sb.substring(i,i + 1).equals(allowedcharacters.substring(ci,ci + 1)))
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
	
	public ZStringBuilder fromFile(String fileName) {
		ZStringBuilder error = new ZStringBuilder();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			error.append("File not found: ");
			error.append(fileName);
		}
		if (fis!=null) {
			error = fromInputStream(fis);
		}
		return error;
	}

	public ZStringBuilder fromInputStream(InputStream is) {
		ZStringBuilder error = new ZStringBuilder();
		sb = new StringBuilder();
		InputStreamReader isr = null;
		try {
			isr = null;
			if (getEncoding().length()>0) {
				isr = new InputStreamReader(is,getEncoding());
			} else {
				isr = new InputStreamReader(is);
			}
			Reader in = new BufferedReader(isr);
			int ch;
			while ((ch = in.read()) > -1) {
				sb.append((char)ch);
			}
			in.close();
		} catch (IOException e) {
			error.append("" + e);
		} finally {
			if (is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					// Ignore
				}
			}
			if (isr!=null) {
				try {
					isr.close();
				} catch (IOException e) {
					// Ignore
				}
			}
		}
		return error;
	}

	public ZStringBuilder toFile(String fileName) {
		ZStringBuilder error = new ZStringBuilder();
		if (sb!=null) {
			char[] chars = new char[sb.length()];
			sb.getChars(0, sb.length(), chars, 0);
	
			FileOutputStream fos = null;
			Writer wtr = null;
			try {
				fos = new FileOutputStream(fileName);
				if (getEncoding().length()>0) {
					wtr = new OutputStreamWriter(fos,getEncoding());
				} else {
					wtr = new OutputStreamWriter(fos);
				}
				wtr.write(chars);
				wtr.close();
			} catch (IOException e) {
				error.append("" + e);
			} finally {
				if (fos!=null) {
					try {
						fos.close();
					} catch (IOException e) {
						// Ignore
					}
				}
				if (wtr!=null) {
					try {
						wtr.close();
					} catch (IOException e) {
						// Ignore
					}
				}
			}
		} else {
			error.append("Inner string builder value is null; nothing to write");
		}
		return error;
	}

	/**
	 * Returns the encoding for file I/O operations
	 * 
	 * @return The encoding 
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * Sets the encoding for file I/O operations
	 * 
	 * @param encoding The string representation of the file encoding
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public static String upperCaseFirst(String str) {
		String r = "";
		if (str!=null && str.length()>0) {
			r = str.substring(0,1).toUpperCase();
			if (str.length()>0) {
				r += str.substring(1);
			}
		}
		return r;
	}

	public char[] toCharArray() {
		char[] r = null;
		if (sb!=null) {
			r = new char[sb.length()];
			sb.getChars(0,r.length,r,0);
		}
		return r;
	}
	
	@Override
	public int compareTo(ZStringBuilder other) {
        int len1 = sb.length();
        int len2 = other.getStringBuilder().length();
        int lim = Math.min(len1, len2);
        char v1[] = toCharArray();
        char v2[] = other.toCharArray();
        int k = 0;
        while (k < lim) {
            char c1 = v1[k];
            char c2 = v2[k];
            if (c1 != c2) {
                return c1 - c2;
            }
            k++;
        }
        return len1 - len2;
	}
}

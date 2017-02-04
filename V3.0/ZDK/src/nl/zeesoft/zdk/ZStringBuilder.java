package nl.zeesoft.zdk;

import java.util.ArrayList;
import java.util.List;

/**
 * The Zeesoft StringBuilder provides advanced StringBuilder manipulation and methods.
 */
public class ZStringBuilder {
	private StringBuilder sb = null;

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

	public String substring(int start) {
		String r = null;
		if (sb!=null) {
			r = substring(start,sb.length());
		}
		return r;
	}

	public String substring(int start, int end) {
		String r = null;
		if (sb!=null) {
			r = sb.substring(start,end);
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

	public boolean endsWith(String s) {
		return (sb!=null && sb.length()>=s.length() && sb.substring(sb.length() - s.length(),sb.length()).equals(s));
	}

	public boolean startsWith(String s) {
		return (sb!=null && sb.length()>=s.length() && sb.substring(0, s.length()).equals(s));
	}

	public boolean equals(ZStringBuilder zsb) {
		return equals(zsb.getStringBuilder());
	}

	public boolean equals(StringBuilder sbc) {
		boolean eq = true;
		if (sb!=null && sbc!=null) {
			if (sb.length()==sbc.length()) {
				for (int i = 0; i < sb.length(); i++) {
					if (!sb.substring(i,(i+1)).equals(sbc.substring(i,(i+1)))) {
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
	
	public StringBuilder replace(String search, String replace) {
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
						if (!sb.substring(i + si,i + si + 1).equals(search.substring(si,si+1))) {
							found = false;
							break;
						}
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
					if ((!caseSensitive && sb.substring(i,i + 1).toUpperCase().equals(characters.substring(ci,ci + 1))) || 
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
					if ((!caseSensitive && sb.substring(i,i + 1).toUpperCase().equals(allowedcharacters.substring(ci,ci + 1))) || 
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
}

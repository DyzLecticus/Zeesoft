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

public class Str implements Comparable<Str>{
	private static final String		ENCODING	= "UTF8";
	
	private StringBuilder			sb			= null;
	
	public Str() {
		this.sb = new StringBuilder();
	}
	
	public Str(StringBuilder sb) {
		this.sb = new StringBuilder(sb);
	}
	
	public Str(String s) {
		this.sb = new StringBuilder(s);
	}
	
	public Str(Str str) {
		this.sb = new StringBuilder(str.sb());
	}
	
	@Override
	public String toString() {
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		return sb.hashCode();
	}
	
	public StringBuilder sb() {
		return sb;
	}
	
	public int length() {
		return sb.length();
	}

	@Override
	public boolean equals(Object object) {
		boolean r = false;
		if (object instanceof Str) {
			r = equals(((Str)object).sb(), false);
		}
		return r;
	}
	
	public boolean equalsIgnoreCase(Str other) {
		return equals(other.sb(), true);
	}

	@Override
	public int compareTo(Str other) {
		return compareTo(other.sb());
	}
	
	public boolean startsWith(Str str) {
		CharSequence eq = str.sb().subSequence(0, str.length());
		return (sb.length() >= str.length() && sb.subSequence(0, str.length()).equals(eq));
	}
	
	public boolean endsWith(Str str) {
		CharSequence eq = str.sb().subSequence(0, str.length());
		return (sb.length() >= str.length() && sb.subSequence(sb.length() - str.length(), sb.length()).equals(eq));
	}
	
	public boolean startsWith(String str) {
		return startsWith(new Str(str));
	}
	
	public boolean endsWith(String str) {
		return endsWith(new Str(str));
	}
	
	public boolean startsWith(String[] strs) {
		boolean r = false;
		for (int i = 0; i < strs.length; i++) {
			if (startsWith(strs[i])) {
				r = true;
				break;
			}
		}
		return r;
	}
	
	public boolean endsWith(String[] strs) {
		boolean r = false;
		for (int i = 0; i < strs.length; i++) {
			if (endsWith(strs[i])) {
				r = true;
				break;
			}
		}
		return r;
	}

	public Str trim() {
		String[] characters = {" ", "\n" ,"\r", "\t"};
		return trim(characters);
	}

	public Str trim(String character) {
		String[] characters = {character};
		return trim(characters);
	}

	public Str trim(String[] characters) {
		while (startsWith(characters)) {
			sb.delete(0,1);
		}
		while (endsWith(characters)) {
			sb.delete(sb.length()-1,sb.length());
		}
		return this;
	}
	
	public Str substring(int start, int end) {
		Str r = new Str();
		if (start<0) {
			start = 0;
		}
		if (end <= start) {
			end = start + 1;
		}
		if (end > sb.length()) {
			end = sb.length();
		}
		if (end>start) {
			for (int i = start; i < end; i++) {
				r.sb.append(sb.substring(i,i+1));
			}
		}
		return r;
	}

	public int indexOf(String search) {
		return indexOf(search,0);
	}
	
	public int indexOf(String search, int offset) {
		return indexOf(new Str(search),offset);
	}

	public int indexOf(Str search) {
		return indexOf(search,0);
	}

	public int indexOf(Str search, int offset) {
		int r = -1;
		if (sb.length()>=search.length()) {
			for (int i = offset; i < sb.length(); i++) {
				if (i + search.length() <= sb.length()) {
					Str sub = substring(i, i + search.length());
					if (sub.equals(search)) {
						r = i;
						break;
					}
				}
			}
		}
		return r;
	}
	
	public boolean contains(String search) {
		return indexOf(new Str(search)) >= 0;
	}
	
	public boolean contains(Str search) {
		return indexOf(search) >= 0;
	}
	
	public static boolean contains(List<Str> strs, String search) {
		return contains(strs,new Str(search));
	}
	
	public static boolean contains(List<Str> strs, Str search) {
		boolean r = false;
		for (Str str: strs) {
			if (str.contains(search)) {
				r = true;
				break;
			}
		}
		return r;
	}
	
	public boolean isAlphabetic(boolean space, boolean underscore) {
		return isType(true,false,space,underscore);
	}
	
	public boolean isNumeric(boolean space, boolean underscore) {
		return isType(false,true,space,underscore);
	}
	
	public boolean isAlphaNumeric(boolean space, boolean underscore) {
		return isType(true,true,space,underscore);
	}
	
	public static boolean isAlphabetic(String str, boolean space, boolean underscore) {
		Str t = new Str(str);
		return t.isType(true,false,space,underscore);
	}
	
	public static boolean isNumeric(String str, boolean space, boolean underscore) {
		Str t = new Str(str);
		return t.isType(false,true,space,underscore);
	}
	
	public static boolean isAlphaNumeric(String str, boolean space, boolean underscore) {
		Str t = new Str(str);
		return t.isType(true,true,space,underscore);
	}
	
	public Str replace(String search, String replace) {
		return replace(new Str(search),new Str(replace));
	}

	public Str replace(Str search, Str replace) {
		if (sb.length()>=search.length()) {
			String rep = replace.toString();
			int offset = 0;
			int i = indexOf(search,offset);
			while (i>=0) {
				sb.replace(i, i+search.length(), rep);
				offset = i+replace.length();
				i = indexOf(search,offset);
			}
		}
		return this;
	}

	public Str toLowerCase() {
		return toCase(true);
	}

	public Str toUpperCase() {
		return toCase(false);
	}
	
	public char[] toCharArray() {
		char[] r = null;
		if (sb!=null) {
			r = new char[sb.length()];
			sb.getChars(0,r.length,r,0);
		}
		return r;
	}

	public List<Str> split(String concatenator) {
		return split(new Str(concatenator));
	}

	public List<Str> split(Str concatenator) {
		List<Str> r = new ArrayList<Str>(); 
		if (sb.length()>0) {
			int offset = 0;
			int i = indexOf(concatenator,offset);
			while (i>=0) {
				r.add(substring(offset,i));
				offset = i+concatenator.length();
				i = indexOf(concatenator,offset);
			}
			if (offset<sb.length()) {
				r.add(substring(offset,sb.length()));
			} else {
				r.add(new Str());
			}
		}
		if (r.size()==0) {
			r.add(new Str());
		}
		return r;
	}

	public Str merge(List<Str> strs, String concatenator) {
		StringBuilder nsb = new StringBuilder();
		for(Str str: strs) {
			if (nsb.length()>0) {
				nsb.append(concatenator);
			}
			nsb.append(str.sb());
		}
		sb = nsb;
		return this;
	}
	
	public static Str mergeList(List<Str> strs, String concatenator) {
		Str r = new Str();
		return r.merge(strs,concatenator);
	}

	public Str mergeStrings(List<String> strs, String concatenator) {
		StringBuilder nsb = new StringBuilder();
		for(String str: strs) {
			if (nsb.length()>0) {
				nsb.append(concatenator);
			}
			nsb.append(str);
		}
		sb = nsb;
		return this;
	}
	
	public static Str mergeStringList(List<String> strs, String concatenator) {
		Str r = new Str();
		return r.mergeStrings(strs,concatenator);
	}
	
	public Str fromFile(String path) {
		return fromFile(path, null);
	}

	public Str fromFile(String path, String encoding) {
		Str error = new Str();
		if (FileIO.mockIO) {
			Str data = FileIO.readFile(path);
			if (data==null) {
				error.sb.append("File not found: ");
				error.sb.append(path);
			} else {
				sb = data.sb;
			}
		} else {
			FileInputStream fis = null;
			if (encoding==null) {
				encoding = ENCODING;
			}
			try {
				fis = new FileInputStream(path);
			} catch (FileNotFoundException e) {
				error.sb().append("File not found: ");
				error.sb().append(path);
			}
			if (fis!=null) {
				error = fromInputStream(fis, encoding);
			}
		}
		return error;
	}

	public Str fromInputStream(InputStream is, String encoding) {
		Str error = new Str();
		if (encoding==null) {
			encoding = ENCODING;
		}
		sb = new StringBuilder();
		InputStreamReader isr = null;
		try {
			isr = null;
			if (encoding.length()>0) {
				isr = new InputStreamReader(is, encoding);
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
			error.sb().append("" + e);
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

	public Str toFile(String path) {
		return toFile(path, null);
	}
	
	public Str toFile(String path, String encoding) {
		Str error = new Str();
		if (FileIO.mockIO) {
			FileIO.writeFile(this, path);
		} else {
			if (encoding==null) {
				encoding = ENCODING;
			}
			char[] chars = toCharArray();
			FileOutputStream fos = null;
			Writer wtr = null;
			try {
				fos = new FileOutputStream(path);
				if (encoding.length()>0) {
					wtr = new OutputStreamWriter(fos,encoding);
				} else {
					wtr = new OutputStreamWriter(fos);
				}
				wtr.write(chars);
				wtr.close();
			} catch (IOException e) {
				error.sb().append("" + e);
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
		}
		return error;
	}
	
	private boolean equals(StringBuilder other,boolean ignoreCase) {
		boolean eq = true;
		if (other!=null) {
			if (sb.length()==other.length()) {
				if (!ignoreCase) {
					eq = compareTo(other)==0;
				} else {
					for (int i = 0; i < sb.length(); i++) {
						if (!sb.substring(i,i+1).equalsIgnoreCase(other.substring(i,i+1))) {
							eq = false;
							break;
						}
					}
				}
			} else {
				eq = false;
			}
		} else {
			eq = false;
		}
		return eq;
	}
	
	private int compareTo(StringBuilder other) {
		int r = 0;
		int len1 = sb.length();
		int len2 = other.length();
		int lim = Math.min(len1, len2);
		int k = 0;
		while (k < lim) {
			char c1 = sb.charAt(k);
			char c2 = other.charAt(k);
			if (c1 != c2) {
				return c1 - c2;
			}
			k++;
		}
		r = len1 - len2;
		return r;
	}

	private Str toCase(boolean lower) {
		int length = sb.length();
		StringBuilder nsb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			String c = sb.substring(i,i+1);
			if (lower) {
				nsb.append(c.toLowerCase());
			} else {
				nsb.append(c.toUpperCase());
			}
		}
		sb = nsb;
		return this;
	}
	
	private boolean isType(boolean alphabetic, boolean numeric, boolean space, boolean underscore) {
		boolean r = true;
		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			if (!(c >= 'a' && c <= 'z') &&
				!(c >= 'A' && c <= 'Z') &&
				!(c >= '0' && c <= '9') &&
				!(c == ' ') &&
				!(c == '_')
				) {
				r = false;
				break;
			} else if (
				(!alphabetic && ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) ||
				(!numeric && (c >= '0' && c <= '9')) ||
				(!space && c == ' ') ||
				(!underscore && c == '_')
				) {
				r = false;
				break;
			}
		}
		return r;		
	}
}

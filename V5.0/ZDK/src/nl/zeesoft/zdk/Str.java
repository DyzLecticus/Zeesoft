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
	private	String			encoding	= "UTF8";
	private StringBuilder	sb			= null;
	
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
	
	public StringBuilder sb() {
		return sb;
	}
	
	public int length() {
		return sb.length();
	}
	
	public boolean equals(Str other) {
		return equals(other.sb(), false);
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

	public Str trim() {
		if (sb!=null) {
			while (startsWith(" ") || startsWith("\n") || startsWith("\r") || startsWith("\t")) {
				sb = sb.delete(0,1);
			}
			while (endsWith(" ") || endsWith("\n") || endsWith("\r") || endsWith("\t")) {
				sb.delete(sb.length()-1,sb.length());
			}
		}
		return this;
	}
	
	public Str replace(String search, String replace) {
		if (sb.length()>=search.length()) {
			StringBuilder nsb = new StringBuilder();
			for (int i = 0; i < sb.length(); i++) {
				if (i + search.length() < sb.length()) {
					String sub = sb.substring(i, i + search.length());
					if (sub.equals(search)) {
						nsb.append(replace);
						i += (search.length() - 1);
					} else {
						nsb.append(sb.substring(i,i+1));
					}
				} else {
					nsb.append(sb.substring(i));
					break;
				}
			}
			sb = nsb;
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
		List<Str> strs = new ArrayList<Str>();
		Str add = new Str();
		for (int i = 0; i < sb.length(); i++) {
			String c = sb.substring(i,i+1);
			if (c.equals(concatenator)) {
				strs.add(add);
				add = new Str();
			} else {
				add.sb().append(c);
			}
		}
		strs.add(add);
		return strs;
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

	public Str fromFile(String fileName) {
		Str error = new Str();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			error.sb().append("File not found: ");
			error.sb().append(fileName);
		}
		if (fis!=null) {
			error = fromInputStream(fis);
		}
		return error;
	}

	public Str fromInputStream(InputStream is) {
		Str error = new Str();
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

	public Str toFile(String fileName) {
		Str error = new Str();
		char[] chars = toCharArray();
		FileOutputStream fos = null;
		Writer wtr = null;
		try {
			fos = new FileOutputStream(fileName);
			if (getEncoding()!=null && getEncoding().length()>0) {
				wtr = new OutputStreamWriter(fos,getEncoding());
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
}

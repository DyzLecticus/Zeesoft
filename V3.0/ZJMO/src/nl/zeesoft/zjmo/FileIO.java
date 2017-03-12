package nl.zeesoft.zjmo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * This class is used to represent XML files
 */
public class FileIO {
	private	String	encoding = "UTF8"; 
	
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
	 * @param encoding
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	/**
	 * Reads the file, code page sensitive.
	 * The assumed code page is UTF-8.
	 * 
	 * @param fileName The name of file
	 * @return The content of the file
	 */
	public StringBuilder readFile(String fileName) {
		StringBuilder buffer = new StringBuilder();
		
		FileInputStream fis = null;
		InputStreamReader isr = null;
		try {
			fis = new FileInputStream(fileName);
			isr = null;
			if (encoding.length()>0) {
				isr = new InputStreamReader(fis,getEncoding());
			} else {
				isr = new InputStreamReader(fis);
			}
			Reader in = new BufferedReader(isr);
			int ch;
			while ((ch = in.read()) > -1) {
				buffer.append((char)ch);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis!=null) {
				try {
					fis.close();
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
		return buffer; 
	}
	
	/**
	 * Writes the file, code page sensitive.
	 * The assumed code page is UTF-8.
	 * 
	 * @param fileName The name of the file
	 * @param text The text to write to the file
	 * @return The error message if an error occurs
	 */
	public String writeFile(String fileName, StringBuilder text) {
		char[] chars = new char[text.length()];
		text.getChars(0, text.length(), chars, 0);

		String error = "";
		FileOutputStream fos = null;
		Writer wtr = null;
		try {
			fos = new FileOutputStream(fileName);
			wtr = new OutputStreamWriter(fos,getEncoding());
			wtr.write(chars);
			wtr.close();
		} catch (IOException e) {
			error = "" + e;
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
	 * Empties the specified directory.
	 * 
	 * @param dirName The full or relative path to the directory
	 * @return True if the directory has been emptied
	 */
	public boolean emptyDirectory(String dirName) {
		File dir = new File(dirName);
		return emptyDirectory(dir);
	}

	/**
	 * Empties the specified directory.
	 * 
	 * @param dir A file object that represents the directory to be emptied
	 * @return True if the directory has been emptied
	 */
	public boolean emptyDirectory(File dir) {
		boolean delete = true;
		for (File f: dir.listFiles()) {
			if (f.isFile()) {
				delete = f.delete();
				if (!delete) {
					break;
				}
			}
		}
		if (delete) {
			for (File f: dir.listFiles()) {
				if (f.isDirectory()) {
					delete = emptyDirectory(f);
					if (!delete) {
						break;
					} else {
						f.delete();
					}
				}
			}
		}
		return delete;
	}
}

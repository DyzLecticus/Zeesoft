package nl.zeesoft.zodb.file;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import javax.imageio.ImageIO;

/**
 * This class is used to represent XML files
 */
public class FileIO {

	private	String 					encoding		 = "UTF8"; 
		
	/**
	 * Returns true if a certain file exists
	 * 
	 * @param fileName The name of the file
	 * @return True if the file exists.
	 */
	public static boolean fileExists(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			return(true);
		} else {
			return(false);
		}
	}

	/**
	 * Deletes a certain file
	 * 
	 * @param fileName The name of the file
	 * @return True if the file was deleted
	 * @throws IOException
	 */
	public static boolean deleteFile(String fileName) throws IOException {
		File file = new File(fileName);
		boolean deleted = false;
		if (file.exists()) {
			if (file.isDirectory()) {
				deleted = deleteDirectory(fileName);
			} else {
				try {	
					file.delete();
					deleted = true;
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		return (deleted);
	}

	/**
	 * Deletes a certain directory
	 * 
	 * @param dirName The name of the directory
	 * @return True if the directory was deleted
	 * @throws IOException
	 */
	public static boolean deleteDirectory(String dirName) throws IOException {
		File file = new File(dirName);
		boolean deleted = false;
		if (file.exists() && file.isDirectory()) {
			for (File dirFile: file.listFiles()) {
				deleteFile(dirFile.getAbsolutePath());
			}
			try {
				file.delete();
				deleted = true;
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		return (deleted);
	}
	
	public static byte[] getImageAsByteArray(String fileName) {
		byte[] r = null;
		File file = new File(fileName);
		if (file.exists()) {
			try {
				BufferedImage img = ImageIO.read(file);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(img,"png",baos);
				baos.flush();
				r = baos.toByteArray();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return r;
	}
	
	/**
	 * Copies a file
	 * 
	 * @param from The file name to copy
	 * @param to The file name to copy to
	 * @throws IOException
	 */
	public static Exception copyFile(String from, String to) {
		Exception ex = null;
		File inputFile = new File(from);
		File outputFile = new File(to);

		InputStream input = null;
		OutputStream output = null;
		
		try {
			input = new FileInputStream(inputFile);
			output = new FileOutputStream(outputFile);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = input.read(buffer)) > 0) { 
				output.write(buffer, 0, length);
			}
			input.close();
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ex = e;
		} catch (IOException e) {
			e.printStackTrace();
			ex = e;
		} finally {
			if (input!=null) {
				try {
					input.close();
				} catch (IOException e) {
					// Ignore
				}
			}
			if (output!=null) {
				try {
					output.close();
				} catch (IOException e) {
					// Ignore
				}
			}
		}
		return ex;
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
	 * @param encoding
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	/**
	 * Reads the file, code page sensitive.
	 * The assumed code page is UTF-8.
	 * 
	 * @param  fileName The name of file
	 * @return		  The content of the file
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
	 * @return true if the file was written successfully
	 */
	public boolean writeFile(String fileName, String text) {
		return writeFileReturnError(fileName,text).length()==0;
	}
	
	/**
	 * Writes the file, code page sensitive.
	 * The assumed code page is UTF-8.
	 * 
	 * @param fileName The name of the file
	 * @param text The text to write to the file
	 */
	public String writeFileReturnError(String fileName, String text) {
		String error = "";
		FileOutputStream fos = null;
		Writer wtr = null;
		try {
			fos = new FileOutputStream(fileName);
			wtr = new OutputStreamWriter(fos,getEncoding());
			wtr.write(text);
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
	 * Writes the file, code page sensitive.
	 * The assumed code page is UTF-8.
	 * 
	 * @param fileName The name of the file
	 * @param text The text to write to the file
	 * @return true if the file was written successfully
	 */
	public boolean writeFile(String fileName, StringBuilder text) {
		return writeFileReturnError(fileName,text).length()==0;
	}
	
	/**
	 * Writes the file, code page sensitive.
	 * The assumed code page is UTF-8.
	 * 
	 * @param fileName The name of the file
	 * @param text The text to write to the file
	 */
	public String writeFileReturnError(String fileName, StringBuilder text) {
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
	 * Writes the file, code page sensitive.
	 * The assumed code page is UTF-8.
	 * 
	 * Creates a before image if the file already exists.
	 * The before image can be automatically restored before reading the file
	 * by using restoreFileSafe
	 * 
	 * @param fileName The name of the file
	 * @param text The text to write to the file
	 */
	public boolean writeFileSafe(String fileName, String text) {
		boolean succes = false;
		try {
			if (fileExists(fileName)) {
				copyFile(fileName,getBackupFile(fileName));
				deleteFile(fileName);
			}
			if (writeFile(fileName,text)) {
				deleteFile(getBackupFile(fileName));
				succes = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return succes;
	}

	/**
	 * See writeFileSafe
	 * 
	 * @param fileName The name of the file
	 */
	public boolean restoreFileSafe(String fileName) {
		boolean restored = false;
		if (fileExists(getBackupFile(fileName))) {
			try {
				copyFile(getBackupFile(fileName), fileName);
				deleteFile(getBackupFile(fileName));
				restored = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return restored;
	}

	private static String getBackupFile(String fileName) {
		return (fileName + ".bi");
	}
}

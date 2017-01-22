package nl.zeesoft.zodb.file;

import java.io.BufferedReader;
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

/**
 * This class is used to represent XML files
 */
public class FileObject {

    private	String 					encoding    	 = "UTF8"; 
    	
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
        File    file    = new File(fileName);
        boolean deleted = false;
        if (file.exists()) {
            try {
                file.delete();
                deleted = true;
            } catch (SecurityException e) {
            	e.printStackTrace();
            }
        }
        return (deleted);
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
	 * @return          The content of the file
	 */
    public StringBuffer readFile(String fileName) {
		StringBuffer buffer = new StringBuffer();
		
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
	 */
    public boolean writeFile(String fileName, String text) {
    	boolean succes = false;
    	FileOutputStream fos = null;
    	Writer wtr = null;
	    try {
			fos = new FileOutputStream(fileName);
			wtr = new OutputStreamWriter(fos,getEncoding());
			wtr.write(text);
			wtr.close();
			succes = true;
	    } catch (IOException e) {
	    	e.printStackTrace();
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
	    return succes;
    }

	/**
	 * Writes the file, code page sensitive.
	 * The assumed code page is UTF-8.
	 * 
	 * @param fileName The name of the file
	 * @param text The text to write to the file
	 */
    public boolean writeFile(String fileName, StringBuffer text) {
    	char[] chars = new char[text.length()];
    	text.getChars(0, text.length(), chars, 0);

    	boolean succes = false;
    	FileOutputStream fos = null;
    	Writer wtr = null;
	    try {
			fos = new FileOutputStream(fileName);
			wtr = new OutputStreamWriter(fos,getEncoding());
			wtr.write(chars);
			wtr.close();
			succes = true;
	    } catch (IOException e) {
	    	e.printStackTrace();
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
	    return succes;
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
	    	writeFile(fileName,text);
			deleteFile(getBackupFile(fileName));
			succes = true;
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
    	    	System.exit(1);
    		}
    	}
    	return restored;
    }

    private static String getBackupFile(String fileName) {
    	return (fileName + ".bi");
    }
}

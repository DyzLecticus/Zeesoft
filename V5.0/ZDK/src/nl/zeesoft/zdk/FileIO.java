package nl.zeesoft.zdk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.thread.Lock;

public class FileIO {
	public static final String					WRITE		= "WRITE:";
	public static final String					READ		= "READ:";
	public static final String					DELETE		= "DELETE:";
	
	public static boolean						mockIO		= false;
	
	private static final Lock					lock		= new Lock();
	private static final SortedMap<String,Str>	fileData	= new TreeMap<String,Str>();
	private static final List<Str>				actionLog	= new ArrayList<Str>();
	
	public static void writeFile(Str data, String path) {
		if (mockIO) {
			path = cleanPath(path);
			FileIO self = new FileIO();
			lock.lock(self);
			fileData.put(path, data);
			Str action = new Str(WRITE);
			action.sb().append(path);
			action.sb().append(":");
			action.sb().append(data.length());
			actionLog.add(action);
			lock.unlock(self);
		}
	}
	
	public static Str readFile(String path) {
		Str r = null; 
		if (mockIO) {
			path = cleanPath(path);
			FileIO self = new FileIO();
			lock.lock(self);
			r = fileData.get(path);
			if (r!=null) {
				Str action = new Str(READ);
				action.sb().append(path);
				action.sb().append(":");
				action.sb().append(r.length());
				actionLog.add(action);
			}
			lock.unlock(self);
		}
		return r;
	}
	
	public static Str deleteFile(String path) {
		Str r = null; 
		if (mockIO) {
			path = cleanPath(path);
			FileIO self = new FileIO();
			lock.lock(self);
			r = fileData.remove(path);
			if (r!=null) {
				Str action = new Str(DELETE);
				action.sb().append(path);
				action.sb().append(":");
				action.sb().append(r.length());
				actionLog.add(action);
			}
			lock.unlock(self);
		} else {
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
		}
		return r;
	}

	public static Str checkDirectory(String path) {
		Str error = new Str();
		if (!mockIO) {
			File dir = new File(path);
			if (!dir.exists()) {
				error.sb().append("Directory does not exist: " + path);
			} else if(!dir.isDirectory()) {
				error.sb().append("Path is not a directory: " + path);
			}
		}
		return error;
	}
	
	public static List<File> listFiles(String path) {
		List<File> r = new ArrayList<File>(); 
		if (mockIO) {
			FileIO self = new FileIO();
			lock.lock(self);
			path = cleanPath(addSlash(path));
			int pathLength = path.split("/").length;
			if (path.length()>0) {
				pathLength += 1;
			}
			for (String filePath: fileData.keySet()) {
				if (filePath.split("/").length == pathLength &&
					(path.length()==0 || filePath.startsWith(path))
					) {
					File file = new File(filePath);
					r.add(file);
				}
			}
			lock.unlock(self);
		} else {
			File dir = new File(path);
			if (dir.exists() && dir.isDirectory()) {
				File[] files = dir.listFiles();
				for (int i = 0; i < files.length; i++) {
					r.add(files[i]);
				}
			}
		}
		return r;
	}
	
	public static List<Str> getActionLog() {
		List<Str> r = new ArrayList<Str>(); 
		if (mockIO) {
			FileIO self = new FileIO();
			lock.lock(self);
			for (Str action: actionLog) {
				r.add(new Str(action));
			}
			lock.unlock(self);
		}
		return r;
	}
	
	public static void clear() {
		FileIO self = new FileIO();
		if (mockIO) {
			lock.lock(self);
			fileData.clear();
			actionLog.clear();
			lock.unlock(self);
		}
	}
	
	public static String cleanPath(String path) {
		return removePeriodSlash(correctBackSlashes(path));
	}
	
	public static String correctBackSlashes(String path) {
		return path.replace("\\","/");
	}
	
	public static String removePeriodSlash(String path) {
		String r = path;
		if (r.startsWith("./")) {
			if (r.length()>2) {
				r = r.substring(2);
			} else {
				r = "";
			}
		}
		return r;
	}
	
	public static String addSlash(String path) {
		String r = path;
		if (r.length()>0 && !r.endsWith("/")) {
			r += "/";
		}
		return r;
	}
}

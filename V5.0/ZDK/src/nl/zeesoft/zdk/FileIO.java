package nl.zeesoft.zdk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.thread.Lock;

public class FileIO {
	public static final String					MKDIRS		= "MKDIRS:";
	public static final String					RENAME		= "RENAME:";
	public static final String					WRITE		= "WRITE:";
	public static final String					READ		= "READ:";
	public static final String					DELETE		= "DELETE:";
	
	public static boolean						mockIO		= false;
	
	private static final Lock					lock		= new Lock();
	private static final SortedMap<String,Str>	fileData	= new TreeMap<String,Str>();
	private static final List<String>			paths		= new ArrayList<String>();
	private static final List<Str>				actionLog	= new ArrayList<Str>();

	public static Str mkDirs(String path) {
		path = addSlash(cleanPath(path));
		Str err = checkFileExists(path,false);
		if (err.length()>0) {
			if (mockIO) {
				FileIO self = new FileIO();
				lock.lock(self);
				if (!paths.contains(path)) {
					paths.add(path);
					addActionLogNoLock(MKDIRS,path);
				}
				lock.unlock(self);
			} else {
				File dir = new File(path);
				if (!dir.mkdirs()) {
					err.sb().append("Failed to create directory: ");
					err.sb().append(path);
				}
			}
		} else {
			err = new Str("Directory already exists: ");
			err.sb().append(path);
		}
		return err;
	}

	public static Str renameDir(String path, String newPath) {
		path = addSlash(cleanPath(path));
		newPath = addSlash(cleanPath(newPath));
		Str err = checkFileExists(path,false);
		if (err.length()==0) {
			err = checkFileExists(newPath,false);
			if (err.length()>0) {
				if (mockIO) {
					FileIO self = new FileIO();
					lock.lock(self);
					if (!paths.contains(newPath)) {
						paths.remove(path);
						paths.add(newPath);
						List<String[]> renames = new ArrayList<String[]>();
						for (Entry<String,Str> entry: fileData.entrySet()) {
							String dir = getDirName(entry.getKey());
							if (dir.equals(path)) {
								dir = newPath + entry.getKey().substring(path.length());
								String[] rename = {entry.getKey(), dir.toString()};
								renames.add(rename);
							}
						}
						for (String[] rename: renames) {
							Str data = fileData.remove(rename[0]);
							fileData.put(rename[1], data);
						}
						addActionLogNoLock(RENAME,path,newPath);
					} else {
						err = new Str("Directory already exists: ");
						err.sb().append(newPath);
					}
					lock.unlock(self);
				} else {
					File dir = new File(path);
					File newDir = new File(newPath);
					if (!dir.renameTo(newDir)) {
						err.sb().append("Failed to rename directory: ");
						err.sb().append(path);
						err.sb().append(" to: ");
						err.sb().append(newPath);
					}
				}
			} else {
				err = new Str("Directory already exists: ");
				err.sb().append(newPath);
			}
		}
		return err;
	}

	public static Str deleteDir(String path, boolean removeFiles) {
		path = addSlash(cleanPath(path));
		Str err = checkFileExists(path,false);
		if (err.length()==0) {
			if (removeFiles) {
				List<File> files = listFiles(path);
				for (File file: files) {
					err = deleteFile(path + file.getName());
					if (err.length()>0) {
						break;
					}
				}
			}
			if (err.length()==0) {
				if (mockIO) {
					FileIO self = new FileIO();
					lock.lock(self);
					if (paths.contains(path)) {
						paths.remove(path);
						addActionLogNoLock(DELETE,path);
					}
					lock.unlock(self);
				} else {
					File dir = new File(path);
					if (!dir.delete()) {
						err.sb().append("Failed to delete directory: ");
						err.sb().append(path);
					}
				}
			}
		}
		return err;
	}
	
	public static void writeFile(Str data, String path) {
		if (mockIO) {
			path = cleanPath(path);
			FileIO self = new FileIO();
			lock.lock(self);
			fileData.put(path, data);
			addActionLogNoLock(WRITE,path,data.length());
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
				addActionLogNoLock(READ,path,r.length());
			}
			lock.unlock(self);
		}
		return r;
	}
	
	public static Str deleteFile(String path) {
		Str error = new Str(); 
		if (mockIO) {
			path = cleanPath(path);
			FileIO self = new FileIO();
			lock.lock(self);
			if (fileData.containsKey(path)) {
				Str data = fileData.remove(path);
				addActionLogNoLock(DELETE,path,data.length());
			}
			lock.unlock(self);
		} else {
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			} else {
				error.sb().append("File not found: ");
				error.sb().append(path);
			}
		}
		return error;
	}
	
	public static Str checkFile(String path) {
		return checkFileExists(path,true);
	}

	public static Str checkDirectory(String path) {
		return checkFileExists(path,false);
	}
	
	public static List<File> listFiles(String path) {
		return list(path,true);
	}
	
	public static List<File> listDirectories(String path) {
		return list(path,false);
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
	
	public static Str getActionLogStr() {
		return Str.mergeList(getActionLog(),"\n");
	}
	
	public static void clear() {
		if (mockIO) {
			FileIO self = new FileIO();
			lock.lock(self);
			fileData.clear();
			paths.clear();
			actionLog.clear();
			lock.unlock(self);
		}
	}
	
	public static String getDirName(String filePath) {
		return getDirName(filePath,true);
	}
	
	public static String getParentDirName(String dirPath) {
		return getDirName(dirPath,false);
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

	private static Str checkFileExists(String path, boolean isFile) {
		Str error = new Str();
		path = cleanPath(path);
		if (mockIO) {
			FileIO self = new FileIO();
			lock.lock(self);
			if (isFile && !fileData.containsKey(path)) {
				error.sb().append("File not found: ");
				error.sb().append(path);
			} else if (!isFile && !paths.contains(addSlash(path))) {
				error.sb().append("Directory not found: ");
				error.sb().append(path);
			}
			lock.unlock(self);
		} else {
			File file = new File(path);
			if (!file.exists()) {
				if (isFile) {
					error.sb().append("File not found: ");
				} else {
					error.sb().append("Directory not found: ");
				}
				error.sb().append(path);
			} else if (isFile && !file.isFile()) {
				error.sb().append("Path is not a file: ");
				error.sb().append(path);
			} else if (!isFile && !file.isDirectory()) {
				error.sb().append("Path is not a directory: ");
				error.sb().append(path);
			}
		}
		return error;
	}
	
	private static List<File> list(String path, boolean listFiles) {
		List<File> r = new ArrayList<File>(); 
		if (mockIO) {
			FileIO self = new FileIO();
			lock.lock(self);
			path = cleanPath(addSlash(path));
			if (listFiles) {
				for (String filePath: fileData.keySet()) {
					if (getDirName(filePath).equals(path)) {
						r.add(new File(filePath));
					}
				}
			} else {
				for (String dirPath: paths) {
					if (getParentDirName(dirPath).equals(path)) {
						r.add(new File(dirPath));
					}
				}
			}
			lock.unlock(self);
		} else {
			File dir = new File(path);
			if (dir.exists() && dir.isDirectory()) {
				File[] files = dir.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (listFiles && files[i].isFile()) {
						r.add(files[i]);
					} else if (!listFiles && !files[i].isFile()) {
						r.add(files[i]);
					}
				}
			}
		}
		return r;
	}
	
	private static String getDirName(String path, boolean isFile) {
		path = cleanPath(path);
		if (!isFile && path.endsWith("/")) {
			path = path.substring(0,path.length() - 1);
		}
		if (!path.endsWith("/")) {
			if (path.contains("/")) {
				String[] split = path.split("/");
				if (split.length>1) {
					path = path.substring(0,path.length() - split[split.length - 1].length());
				}
			} else {
				path = "";
			}
		}
		return path;
	}
	
	private static void addActionLogNoLock(String action, String path) {
		addActionLogNoLock(action, path, -1);
	}
	
	private static void addActionLogNoLock(String action, String path, int param) {
		if (param>=0) {
			addActionLogNoLock(action, path, "" + param);
		} else {
			addActionLogNoLock(action, path, "");
		}
	}
	
	private static void addActionLogNoLock(String action, String path, String param) {
		Str act = new Str(action);
		act.sb().append(path);
		if (param.length()>0) {
			act.sb().append(":");
			act.sb().append(param);
		}
		actionLog.add(act);
	}
}

package nl.zeesoft.zdk.collection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.RunCode;
import nl.zeesoft.zdk.thread.Waiter;

public class PartitionableCollection extends PersistableCollection {
	public static final String	FILE_TYPE		= ".txt";
	protected int				partitionSize	= 1000;
	protected int				timeoutMs		= 10000;
	protected List<Str>			objStrs			= new ArrayList<Str>();
	
	public PartitionableCollection() {
		
	}
	
	public PartitionableCollection(Logger logger) {
		this.logger = logger;
	}
	
	public void setPartitionSize(int partitionSize) {
		lock.lock(this);
		this.partitionSize = partitionSize;
		lock.unlock(this);
	}
	
	public void setTimeoutMs(int timeoutMs) {
		lock.lock(this);
		this.timeoutMs = timeoutMs;
		lock.unlock(this);
	}
	
	@Override
	public Str toPath(String path) {
		Str error = checkDir(path);
		if (error.length()==0) {
			List<CodeRunner> runners = new ArrayList<CodeRunner>();
			int partitionNum = 0;
			SortedMap<Str,Object> partition = new TreeMap<Str,Object>();
			List<String> fileNames = new ArrayList<String>();
			lock.lock(this);
			for (Entry<Str,Object> entry: objects.entrySet()) {
				partition.put(entry.getKey(),entry.getValue());
				if (partition.size()==partitionSize) {
					String fileName	= partitionNum + FILE_TYPE;
					fileNames.add(fileName);
					String partitionPath = path + fileName;
					RunCode code = new RunCode(this,partition,partitionPath) {
						@Override
						protected boolean run() {
							PartitionableCollection collection = (PartitionableCollection) params[0];
							@SuppressWarnings("unchecked")
							SortedMap<Str,Object> partition = (SortedMap<Str,Object>) params[1];
							String path = (String) params[2];
							collection.savePartition(partition, path);
							return true;
						}
						
					};
					CodeRunner runner = new CodeRunner(code);
					runners.add(runner);
					partitionNum++;
					partition = new TreeMap<Str,Object>();
				}
			}
			lock.unlock(this);
			List<File> files = getFiles(path);
			for (File file: files) {
				if (!fileNames.contains(file.getName())) {
					file.delete();
				}
			}
			Waiter.startAndWaitTillDone(runners, timeoutMs);
		}
		return error;
	}

	@Override
	public Str fromPath(String path) {
		Str error = checkDir(path);
		if (error.length()==0) {
			clear();
			List<CodeRunner> runners = new ArrayList<CodeRunner>();
			List<File> files = getFiles(path);
			for (File file: files) {
				String partitionPath = path + file.getName();
				RunCode code = new RunCode(this,partitionPath) {
					@Override
					protected boolean run() {
						PartitionableCollection collection = (PartitionableCollection) params[0];
						String path = (String) params[1];
						collection.loadPartition(path);
						return true;
					}
					
				};
				CodeRunner runner = new CodeRunner(code);
				runners.add(runner);
			}
			Waiter.startAndWaitTillDone(runners, timeoutMs);
			lock.lock(this);
			expandObjectsNoLock(objStrs);
			objStrs.clear();
			lock.unlock(this);
		}
		return error;
	}
	
	public static List<File> getPartitionFiles(String path) {
		List<File> r = new ArrayList<File>();
		File dir = new File(path);
		File[] files = dir.listFiles();
		for (File file: files) {
			if (file.getName().endsWith(FILE_TYPE)) {
				String[] split = file.getName().split("\\.");
				if (split.length==2) {
					try {
						int partitionNum = Integer.parseInt(split[0]);
						if (partitionNum>=0) {
							r.add(file);
						}
					} catch(NumberFormatException ex) {
						// Ignore
					}
				}
			}
		}
		return r;
	}
	
	protected Str savePartition(SortedMap<Str,Object> objects, String path) {
		lock.lock(this);
		Str data = toStrNoLock(objects);
		lock.unlock(this);
		Str error = data.toFile(path);
		if (error.length()>0) {
			logger.error(this,error);
		}
		return error;
	}
	
	protected Str loadPartition(String path) {
		Str data = new Str();
		Str error = data.fromFile(path);
		if (error.length()==0) {
			lock.lock(this);
			fromStrNoLock(data);
			objStrs.addAll(fromStrNoLock(data));
			lock.unlock(this);
		} else {
			logger.error(this,error);
		}
		return error;
	}
	
	protected List<File> getFiles(String path) {
		return getPartitionFiles(path);
	}
	
	protected Str checkDir(String path) {
		Str error = new Str();
		File dir = new File(path);
		if (!dir.exists()) {
			error.sb().append("Directory does not exist: " + path);
		} else if(!dir.isDirectory()) {
			error.sb().append("Path is not a directory: " + path);
		}
		return error;
	}
}

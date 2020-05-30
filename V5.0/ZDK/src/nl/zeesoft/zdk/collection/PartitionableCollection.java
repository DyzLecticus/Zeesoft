package nl.zeesoft.zdk.collection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.RunCode;
import nl.zeesoft.zdk.thread.Waiter;

public class PartitionableCollection extends PersistableCollection {
	protected int		partitionSize		= 1000;
	protected int		timeoutMs			= 10000;
	protected List<Str>	objStrs				= new ArrayList<Str>();
	
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
			lock.lock(this);
			SortedMap<Str,Object> partition = new TreeMap<Str,Object>();
			for (Entry<Str,Object> entry: objects.entrySet()) {
				partition.put(entry.getKey(),entry.getValue());
				if (partition.size()==partitionSize) {
					String partitionPath = path + partitionNum + ".txt";
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
			List<File> files = getPartitionFiles(path);
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
	
	protected Str savePartition(SortedMap<Str,Object> objects, String path) {
		lock.lock(this);
		Str data = toStrNoLock(objects);
		lock.unlock(this);
		Str error = data.toFile(path);
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
		}
		return error;
	}
	
	/*
	@Override
	protected Str toPathNoLock(String path) {
		Str error = checkDir(path);
		if (error.length()==0) {
			List<CodeRunner> runners = new ArrayList<CodeRunner>();
			int partitionNum = 0;
			SortedMap<Str,Object> partition = new TreeMap<Str,Object>();
			for (Entry<Str,Object> entry: objects.entrySet()) {
				partition.put(entry.getKey(),entry.getValue());
				if (partition.size()==partitionSize) {
					String partitionPath = path + partitionNum + ".txt";
					RunCode code = new RunCode(this,partition,partitionPath) {
						@Override
						protected boolean run() {
							PartitionedCollection collection = (PartitionedCollection) params[0];
							@SuppressWarnings("unchecked")
							SortedMap<Str,Object> partition = (SortedMap<Str,Object>) params[1];
							String path = (String) params[2];
							collection.savePartitionNoLock(partition, path);
							return true;
						}
						
					};
					CodeRunner runner = new CodeRunner(code);
					runners.add(runner);
					partitionNum++;
					partition = new TreeMap<Str,Object>();
				}
			}
			Waiter.startWait(runners, timeoutMs);
		}
		return error;
	}

	@Override
	protected Str fromPathNoLock(String path) {
		Str error = checkDir(path);
		if (error.length()==0) {
			List<CodeRunner> runners = new ArrayList<CodeRunner>();
			List<File> files = getPartitionFiles(path);
			for (File file: files) {
				String partitionPath = path + file.getName();
				RunCode code = new RunCode(this,partitionPath) {
					@Override
					protected boolean run() {
						PartitionedCollection collection = (PartitionedCollection) params[0];
						String path = (String) params[1];
						collection.loadPartitionNoLock(path);
						return true;
					}
					
				};
				CodeRunner runner = new CodeRunner(code);
				runners.add(runner);
			}
			Waiter.startWait(runners, timeoutMs);
			expandObjectsNoLock(objStrs);
			objStrs.clear();
		}
		return error;
	}
	
	protected Str savePartitionNoLock(SortedMap<Str,Object> objects, String path) {
		Str data = toStrNoLock(objects);
		Str error = data.toFile(path);
		return error;
	}
	
	protected Str loadPartitionNoLock(String path) {
		Str data = new Str();
		Str error = data.fromFile(path);
		if (error.length()==0) {
			fromStrNoLock(data);
			objStrs.addAll(fromStrNoLock(data));
		}
		return error;
	}
	*/
	
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
	
	protected List<File> getPartitionFiles(String path) {
		List<File> r = new ArrayList<File>();
		File dir = new File(path);
		File[] files = dir.listFiles();
		for (File file: files) {
			if (file.getName().endsWith(".txt")) {
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
}

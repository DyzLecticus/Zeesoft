package nl.zeesoft.zdk.collection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
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
		super(logger);
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
			CodeRunnerChain runnerChain = getCodeRunnerChainForSave(path);
			Waiter.startAndWaitTillDone(runnerChain, timeoutMs);
		}
		return error;
	}

	@Override
	public Str fromPath(String path) {
		Str error = checkDir(path);
		if (error.length()==0) {
			clear();
			CodeRunnerChain runnerChain = getCodeRunnerChainForLoad(path);
			Waiter.startAndWaitTillDone(runnerChain, timeoutMs);
		}
		return error;
	}
	
	public CodeRunnerChain getCodeRunnerChainForSave(String path) {
		CodeRunnerChain r = new CodeRunnerChain();
		r.setLogger(logger);
		r.addAll(getRunCodesForSave(path));
		r.add(new RunCode() {
			@Override
			protected boolean run() {
				savedPartitions();
				return true;
			}
		});
		return r;
	}
	
	protected CodeRunnerChain getCodeRunnerChainForLoad(String path) {
		CodeRunnerChain r = new CodeRunnerChain();
		r.setLogger(logger);
		r.addAll(getRunCodesForLoad(path));
		r.add(new RunCode() {
			@Override
			protected boolean run() {
				loadedAllPartitions();
				loadedPartitions();
				return true;
			}
		});
		return r;
	}
	
	protected void savedPartitions() {
		// Override to implement
	}
	
	protected void loadedPartitions() {
		// Override to implement
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

	protected List<SortedMap<Str,Object>> getPartitions() {
		List<SortedMap<Str,Object>> r = new ArrayList<SortedMap<Str,Object>>();
		SortedMap<Str,Object> partition = new TreeMap<Str,Object>();
		lock.lock(this);
		for (Entry<Str,Object> entry: objects.entrySet()) {
			partition.put(entry.getKey(),entry.getValue());
			if (partition.size()==partitionSize) {
				r.add(partition);
				partition = new TreeMap<Str,Object>();
			}
		}
		if (partition.size()>0) {
			r.add(partition);
		}
		lock.unlock(this);
		return r;
	}
	
	protected List<RunCode> getRunCodesForSave(String path) {
		List<RunCode> r = new ArrayList<RunCode>();
		List<String> fileNames = new ArrayList<String>();
		int partitionNum = 0;
		List<SortedMap<Str,Object>> partitions = getPartitions();
		for (SortedMap<Str,Object> partition: partitions) {
			String fileName	= partitionNum + FILE_TYPE;
			fileNames.add(fileName);
			String partitionPath = path + fileName;
			RunCode code = new RunCode(partition,partitionPath) {
				@Override
				protected boolean run() {
					@SuppressWarnings("unchecked")
					SortedMap<Str,Object> partition = (SortedMap<Str,Object>) params[0];
					String path = (String) params[1];
					savePartition(partition, path);
					return true;
				}
			};
			r.add(code);
			partitionNum++;
		}
		List<File> files = getFiles(path);
		for (File file: files) {
			if (!fileNames.contains(file.getName())) {
				RunCode code = new RunCode(file) {
					@Override
					protected boolean run() {
						File file = (File) params[0];
						file.delete();
						return true;
					}
				};
				r.add(code);
			}
		}
		return r;
	}
	
	protected List<RunCode> getRunCodesForLoad(String path) {
		List<RunCode> r = new ArrayList<RunCode>();
		List<File> files = getFiles(path);
		for (File file: files) {
			String partitionPath = path + file.getName();
			RunCode code = new RunCode(partitionPath) {
				@Override
				protected boolean run() {
					String path = (String) params[0];
					loadPartition(path);
					return true;
				}
			};
			r.add(code);
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
			objStrs.addAll(fromStrNoLock(data));
			lock.unlock(this);
		} else {
			logger.error(this,error);
		}
		return error;
	}
	
	protected void loadedAllPartitions() {
		lock.lock(this);
		expandObjectsNoLock(objStrs);
		objStrs.clear();
		lock.unlock(this);
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

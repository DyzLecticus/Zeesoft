package nl.zeesoft.zodb.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public abstract class FileWriterWorkerObject extends Worker {
	private	Index				index			= null;
	private int 				todo			= 0;
	
	protected FileWriterWorkerObject(Messenger msgr, WorkerUnion union,Index index) {
		super(msgr, union);
		this.index = index;
		setSleep(100);
	}
	
	@Override
	public void whileWorking() {
		if (!todoGreaterThanZero()) {
			lockMe(this);
			int get = getMax() - todo;
			unlockMe(this);
			SortedMap<Integer,List<IndexElement>> files = getChangedFiles(get);
			if (files.size()>0) {
				writeChangedFiles(files);
				if (files.size()==get) {
					setSleep(1);
				} else {
					setSleep(10);
				}
			} else {
				setSleep(100);
			}
		}
	}
	
	@Override
	public void stop() {
		super.stop();
		waitForStop(10,false);
		SortedMap<Integer,List<IndexElement>> remaining = writeChangedFiles(getChangedFiles(0));
		boolean leftOvers = false;
		while(remaining.size()>0) {
			leftOvers = true;
			getMessenger().debug(this,"Remaining files: " + remaining.size());
			whileTodoGreaterThanZero();
			remaining = writeChangedFiles(remaining);
		}
		whileTodoGreaterThanZero();
		if (leftOvers) {
			getMessenger().debug(this,"Done");
		}
	}
	
	protected void destroy() {
		if (isWorking()) {
			stop();
		}
		index = null;
	}

	protected void writtenFile() {
		lockMe(this);
		todo--;
		unlockMe(this);
	}
	
	protected abstract int getMax();
	
	protected abstract FileWriteWorkerObject getNewFileWriteWorker(Messenger msgr, WorkerUnion union,FileWriterWorkerObject writer,String fileName, List<IndexElement> elements, StringBuilder key);
	
	protected abstract SortedMap<Integer,List<IndexElement>> getChangedFiles(int max);

	protected abstract String getDirectory();

	protected Index getIndex() {
		return index;
	}
	
	private SortedMap<Integer,List<IndexElement>> writeChangedFiles(SortedMap<Integer,List<IndexElement>> files) {
		SortedMap<Integer,List<IndexElement>> r = new TreeMap<Integer,List<IndexElement>>(files);
		List<FileWriteWorkerObject> workers = new ArrayList<FileWriteWorkerObject>();
		int i = 0;
		for (Entry<Integer,List<IndexElement>> entry: files.entrySet()) {
			r.remove(entry.getKey());
			String fileName = getDirectory() + entry.getKey() + ".txt";
			if (entry.getValue().size()==0) {
				File file = new File(fileName);
				if (file.exists() && !file.delete()) {
					getMessenger().error(this,"Failed to delete file: " + fileName);
				}
			} else {
				workers.add(getNewFileWriteWorker(getMessenger(),getUnion(),this,fileName,entry.getValue(),index.getKey()));
			}
			i++;
			if (i>=getMax()) {
				break;
			}
		}
		if (workers.size()>0) {
			lockMe(this);
			todo += workers.size();
			unlockMe(this);
			for (FileWriteWorkerObject worker: workers) {
				worker.start();
			}
		}
		return r;
	}
	
	private boolean todoGreaterThanZero() {
		return testTodoGreaterThan(0);
	}
	
	private void whileTodoGreaterThanZero() {
		whileTodoGreaterThan(0);
	}

	private boolean testTodoGreaterThan(int num) {
		boolean r = false;
		lockMe(this);
		r = todo > num;
		unlockMe(this);
		return r;
	}

	private void whileTodoGreaterThan(int num) {
		int sleep = 10;
		while(testTodoGreaterThan(num)) {
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				if (getMessenger()!=null) {
					getMessenger().error(this,"Waiting for object writing to finish was interrupted");
				} else {
					System.err.println("Waiting for object writing to finish was interrupted");
				}
			}
			if (sleep<100) {
				sleep += 10;
			}
		}
	}
}

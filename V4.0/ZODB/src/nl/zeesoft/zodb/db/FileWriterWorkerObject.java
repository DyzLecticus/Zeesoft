package nl.zeesoft.zodb.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public abstract class FileWriterWorkerObject extends Worker {
	private	Index				index			= null;
	private Set<Integer>		writing			= new HashSet<Integer>();
	
	protected FileWriterWorkerObject(Messenger msgr, WorkerUnion union,Index index) {
		super(msgr, union);
		this.index = index;
		setSleep(100);
	}
	
	@Override
	public void stop() {
		super.stop();
		waitForStop(10,false);
		whileTodoGreaterThanZero(); // Empty buffer to ensure all remaining changes are captured
		SortedMap<Integer,List<IndexElement>> remaining = writeChangedFiles(getChangedFiles(0));
		boolean leftOvers = false;
		while(remaining.size()>0) {
			leftOvers = true;
			getMessenger().debug(this,"Remaining files: " + remaining.size());
			whileTodoGreaterThanPartMax();
			remaining = writeChangedFiles(remaining);
		}
		whileTodoGreaterThanZero();
		if (leftOvers) {
			getMessenger().debug(this,"Done");
		}
	}
	
	@Override
	protected void whileWorking() {
		if (!todoGreaterThanPartMax()) {
			lockMe(this);
			int get = getMax() - writing.size();
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
	
	protected void destroy() {
		if (isWorking()) {
			stop();
		}
		lockMe(this);
		index = null;
		unlockMe(this);
	}

	protected void writtenFile(int num) {
		lockMe(this);
		writing.remove(num);
		unlockMe(this);
	}
	
	protected abstract int getMax();
	
	protected abstract FileWriteWorkerObject getNewFileWriteWorker(Messenger msgr,WorkerUnion union,FileWriterWorkerObject writer,int fileNum,String directory,List<IndexElement> elements,StringBuilder key);
	
	protected abstract SortedMap<Integer,List<IndexElement>> getChangedFiles(int max);

	protected abstract String getDirectory();

	protected Index getIndex() {
		return index;
	}
	
	protected Set<Integer> getWriting() {
		return writing;
	}
	
	private SortedMap<Integer,List<IndexElement>> writeChangedFiles(SortedMap<Integer,List<IndexElement>> files) {
		SortedMap<Integer,List<IndexElement>> r = new TreeMap<Integer,List<IndexElement>>(files);
		List<FileWriteWorkerObject> workers = new ArrayList<FileWriteWorkerObject>();
		int i = 0;
		for (Entry<Integer,List<IndexElement>> entry: files.entrySet()) {
			r.remove(entry.getKey());
			FileWriteWorkerObject worker = getNewFileWriteWorker(getMessenger(),getUnion(),this,entry.getKey(),getDirectory(),entry.getValue(),index.getKey());
			workers.add(worker);
			worker.start();
			lockMe(this);
			writing.add(entry.getKey());
			unlockMe(this);
			i++;
			if (i>=getMax()) {
				break;
			}
		}
		return r;
	}
	
	private boolean todoGreaterThanPartMax() {
		return testTodoGreaterThan((getMax() / 4) * 3);
	}
	
	private void whileTodoGreaterThanZero() {
		whileTodoGreaterThan(0);
	}
	
	private void whileTodoGreaterThanPartMax() {
		whileTodoGreaterThan((getMax() / 4) * 3);
	}

	private boolean testTodoGreaterThan(int num) {
		boolean r = false;
		lockMe(this);
		r = writing.size() > num;
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

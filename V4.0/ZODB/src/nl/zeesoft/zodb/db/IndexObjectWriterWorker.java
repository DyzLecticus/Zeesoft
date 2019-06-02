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

public class IndexObjectWriterWorker extends Worker {
	private static final int	MAX			= 100;
	
	private	Index				index		= null;
	
	private boolean				writing		= false;
	private int 				todo		= 0;
	private int					done		= 0;
	
	protected IndexObjectWriterWorker(Messenger msgr, WorkerUnion union,Index index) {
		super(msgr, union);
		this.index = index;
		setSleep(100);
	}
	
	@Override
	public void stop() {
		super.stop();
		waitForStop(10,false);
		SortedMap<Integer,List<IndexElement>> remaining = writeChangedFiles(index.getChangedDataFiles(0));
		boolean leftOvers = false;
		while(remaining.size()>0) {
			leftOvers = true;
			getMessenger().debug(this,"Remaining object files: " + remaining.size());
			whileWriting();
			remaining = writeChangedFiles(remaining);
		}
		whileWriting();
		if (leftOvers) {
			getMessenger().debug(this,"Done");
		}
	}
	
	@Override
	public void whileWorking() {
		if (!isWriting()) {
			SortedMap<Integer,List<IndexElement>> files = index.getChangedDataFiles(MAX);
			if (files.size()>0) {
				writeChangedFiles(files);
				if (files.size()==MAX) {
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
		index = null;
	}
	
	protected void writtenObject() {
		lockMe(this);
		done++;
		if (done==todo) {
			writing = false;
			done = 0;
			todo = 0;
		}
		unlockMe(this);
	}

	private SortedMap<Integer,List<IndexElement>> writeChangedFiles(SortedMap<Integer,List<IndexElement>> files) {
		SortedMap<Integer,List<IndexElement>> r = new TreeMap<Integer,List<IndexElement>>(files);
		List<IndexObjectWriteWorker> workers = new ArrayList<IndexObjectWriteWorker>();
		int i = 0;
		for (Entry<Integer,List<IndexElement>> entry: files.entrySet()) {
			r.remove(entry.getKey());
			String fileName = index.getObjectDirectory() + entry.getKey() + ".txt";
			if (entry.getValue().size()==0) {
				File file = new File(fileName);
				if (file.exists() && !file.delete()) {
					getMessenger().error(this,"Failed to delete file: " + fileName);
				}
			} else {
				workers.add(new IndexObjectWriteWorker(getMessenger(),getUnion(),this,fileName,entry.getValue(),index.getKey()));
			}
			i++;
			if (i>=MAX) {
				break;
			}
		}
		if (workers.size()>0) {
			lockMe(this);
			writing = true;
			todo += workers.size();
			unlockMe(this);
			for (IndexObjectWriteWorker worker: workers) {
				worker.start();
			}
		}
		return r;
	}
	
	private boolean isWriting() {
		boolean r = false;
		lockMe(this);
		r = writing;
		unlockMe(this);
		return r;
	}
	
	private void whileWriting() {
		int sleep = 10;
		while(isWriting()) {
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

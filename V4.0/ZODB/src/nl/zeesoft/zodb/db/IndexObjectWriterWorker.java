package nl.zeesoft.zodb.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class IndexObjectWriterWorker extends Worker {
	private static final int	MAX			= 1000;
	
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
		List<IndexElement> elements = index.getChangedElements(0);
		if (elements.size()>0) {
			getMessenger().debug(this,"Remaining objects: " + elements.size());
			List<IndexElement> remainder = writeChangedElements(elements);
			while(remainder.size()>0) {
				getMessenger().debug(this,"Remaining objects: " + elements.size());
				whileWriting();
				remainder = writeChangedElements(elements);
			}
		}
		whileWriting();
		if (elements.size()>0) {
			getMessenger().debug(this,"Done");
		}
	}
	
	@Override
	public void whileWorking() {
		if (!isWriting()) {
			List<IndexElement> elements = index.getChangedElements(MAX);
			if (elements.size()>0) {
				writeChangedElements(elements);
				setSleep(1);
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
	
	private List<IndexElement> writeChangedElements(List<IndexElement> elements) {
		List<IndexElement> r = new ArrayList<IndexElement>(elements);
		List<IndexObjectWriteWorker> workers = new ArrayList<IndexObjectWriteWorker>();
		int i = 0;
		for (IndexElement elem: elements) {
			r.remove(elem);
			String fileName = index.getObjectDirectory() + elem.id + ".txt";
			if (elem.removed) {
				File file = new File(fileName);
				if (!file.delete()) {
					getMessenger().error(this,"Failed to delete file: " + fileName);
				}
			} else if (elem.obj!=null) {
				workers.add(new IndexObjectWriteWorker(getMessenger(),getUnion(),this,fileName,elem.obj,index.getKey()));
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

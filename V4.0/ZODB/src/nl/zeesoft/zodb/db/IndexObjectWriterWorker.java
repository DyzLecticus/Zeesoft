package nl.zeesoft.zodb.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class IndexObjectWriterWorker extends Worker {
	private WorkerUnion		union		= null;
	private	Index			index		= null;
	
	private boolean			writing		= false;
	private int 			todo		= 0;
	private int				done		= 0;
	
	protected IndexObjectWriterWorker(Messenger msgr, WorkerUnion union,Index index) {
		super(msgr, union);
		this.union = union;
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
			writeChangedElements(elements);
			getMessenger().debug(this,"Done");
		}
	}
	
	@Override
	public void whileWorking() {
		boolean w = false;
		lockMe(this);
		w = writing;
		unlockMe(this);
		if (!w) {
			List<IndexElement> elements = index.getChangedElements(1000);
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
	
	private void writeChangedElements(List<IndexElement> elements) {
		List<IndexObjectWriteWorker> workers = new ArrayList<IndexObjectWriteWorker>();
		for (IndexElement elem: elements) {
			String fileName = index.getObjectDirectory() + elem.id + ".txt";
			if (elem.removed) {
				File file = new File(fileName);
				if (!file.delete()) {
					getMessenger().error(this,"Failed to delete file: " + fileName);
				}
			} else if (elem.obj!=null) {
				workers.add(new IndexObjectWriteWorker(getMessenger(),union,this,fileName,elem.obj,index.getKey()));
			}
		}
		if (workers.size()>0) {
			lockMe(this);
			writing = true;
			done = 0;
			todo = workers.size();
			unlockMe(this);
			for (IndexObjectWriteWorker worker: workers) {
				worker.start();
			}
		}
	}
}

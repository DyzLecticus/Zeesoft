package nl.zeesoft.zodb.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class IndexFileReader extends Locker {
	private Index						index	= null;
	private List<IndexFileReadWorker>	workers	= new ArrayList<IndexFileReadWorker>();
	private int							done	= 0;
	
	protected IndexFileReader(Messenger msgr, WorkerUnion uni,Index idx) {
		super(msgr);
		index = idx;
		for (int i = 0; i < 10; i++) {
			workers.add(new IndexFileReadWorker(msgr,uni,index,this));
		}
	}

	protected void start() {
		lockMe(this);
		List<String> fileNames = getFileNames();
		if (fileNames.size()==0) {
			index.setOpen(true);
		} else {
			int size = fileNames.size();
			for (int i = 0; i < size; i++) {
				int index = i % workers.size();
				IndexFileReadWorker worker = workers.get(index);
				worker.getFileNames().add(fileNames.get(i));
			}
			for (IndexFileReadWorker worker: workers) {
				if (worker.getFileNames().size()>0) {
					worker.start();
				} else {
					done++;
				}
			}
		}
		unlockMe(this);
	}
	
	protected void workerIsDone() {
		lockMe(this);
		done++;
		if (done>=workers.size()) {
			index.setOpen(true);
		}
		unlockMe(this);
	}
	
	private List<String> getFileNames() {
		List<String> r = new ArrayList<String>();
		File dir = new File(index.getDirectory());
		if (dir.exists()) {
			for (File file: dir.listFiles()) {
				r.add(file.getName());
			}
		}
		return r;
	}
}

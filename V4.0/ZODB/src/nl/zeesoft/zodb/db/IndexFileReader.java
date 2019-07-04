package nl.zeesoft.zodb.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.LockedCode;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class IndexFileReader extends Locker {
	private Index						index	= null;
	private List<IndexFileReadWorker>	workers	= new ArrayList<IndexFileReadWorker>();
	private int							done	= 0;
	private StringBuilder				newKey	= null;
	
	protected IndexFileReader(Messenger msgr, WorkerUnion uni,Index idx,StringBuilder nKey) {
		super(msgr);
		index = idx;
		newKey = nKey;
		for (int i = 0; i < 10; i++) {
			workers.add(new IndexFileReadWorker(msgr,uni,index,this));
		}
	}

	protected void start() {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				List<String> fileNames = getFileNames();
				if (fileNames.size()==0) {
					if (newKey!=null && newKey.length()>0) {
						index.setKey(newKey);
					}
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
				return null;
			}
		};
		doLocked(this,code);
	}
	
	protected void workerIsDone() {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				done++;
				if (done>=workers.size()) {
					if (newKey!=null && newKey.length()>0) {
						index.readAll();
						index.setKey(newKey);
						index.writeAll();
					}
					index.setOpen(true);
					index = null;
					workers.clear();
				}
				return null;
			}
		};
		doLocked(this,code);
	}
	
	private List<String> getFileNames() {
		List<String> r = new ArrayList<String>();
		File dir = new File(index.getFileDirectory());
		if (dir.exists()) {
			for (File file: dir.listFiles()) {
				if (file.getName().endsWith(".txt")) {
					r.add(file.getName());
				}
			}
		}
		return r;
	}
}

package nl.zeesoft.zodb.db;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class IndexObjectReaderWorker extends Worker {
	private	Index						index		= null;
	
	private List<Integer>				queue		= new ArrayList<Integer>();
	private List<Integer>				reading		= new ArrayList<Integer>();
	
	protected IndexObjectReaderWorker(Messenger msgr, WorkerUnion union,Index index) {
		super(msgr, union);
		this.index = index;
		setSleep(100);
	}

	protected void addFileNums(List<Integer> fileNumList) {
		if (fileNumList.size()>0) {
			boolean added = false;
			lockMe(this);
			for (Integer fileNum: fileNumList) {
				if (!queue.contains(fileNum) && !reading.contains(fileNum)) {
					queue.add(fileNum);
				}
			}
			unlockMe(this);
			if (added) {
				setSleep(0);
			}
		}
	}

	@Override
	public void stop() {
		super.stop();
		waitForStop(10,false);
		lockMe(this);
		reading.clear();
		queue.clear();
		unlockMe(this);
	}
	
	@Override
	public void whileWorking() {
		List<Integer> list = null;
		lockMe(this);
		list = new ArrayList<Integer>();
		if (queue.size()>0) {
			int add = (1000 - reading.size());
			if (add>0) {
				for (int i = 0; i < add; i++) {
					if (queue.size()==0) {
						break;
					}
					int fileNum = queue.get(0);
					reading.add(fileNum);
					list.add(fileNum);
					queue.remove(0);
				}
			}
		}
		unlockMe(this);
		if (list.size()>0) {
			readFileNumList(list);
			setSleep(1);
		} else {
			setSleep(100);
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
	
	protected void readObjects(int fileNum,SortedMap<Long,JsFile> idObjMap) {
		if (isWorking()) {
			lockMe(this);
			if (index!=null) {
				index.readObjects(idObjMap);
			}
			reading.remove(fileNum);
			unlockMe(this);
		}
	}
	
	private void readFileNumList(List<Integer> fileNumList) {
		List<IndexObjectReadWorker> workers = new ArrayList<IndexObjectReadWorker>();
		for (Integer fileNum: fileNumList) {
			String fileName = index.getObjectDirectory() + fileNum + ".txt";
			workers.add(new IndexObjectReadWorker(getMessenger(),getUnion(),this,fileNum,fileName,index.getKey()));
		}
		if (workers.size()>0) {
			for (IndexObjectReadWorker worker: workers) {
				worker.start();
			}
		}
	}
}

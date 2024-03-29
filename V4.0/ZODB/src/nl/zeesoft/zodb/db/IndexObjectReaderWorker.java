package nl.zeesoft.zodb.db;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.LockedCode;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class IndexObjectReaderWorker extends Worker {
	private static final int			MAX			= 1000;
	
	private	Index						index		= null;
	
	private List<Integer>				queue		= new ArrayList<Integer>();
	private List<Integer>				reading		= new ArrayList<Integer>();
	
	protected IndexObjectReaderWorker(Messenger msgr, WorkerUnion union,Index index) {
		super(msgr, union);
		this.index = index;
		setSleep(10);
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

	protected void addFileNums(List<Integer> fileNumList) {
		if (fileNumList.size()>0) {
			boolean added = false;
			lockMe(this);
			int num = MAX - reading.size();
			for (int i = 0; i < num; i++) {
				if (fileNumList.size()>0) {
					int fileNum = fileNumList.remove(0);
					reading.add(fileNum);
					readFileNum(fileNum);
				}
			}
			for (Integer fileNum: fileNumList) {
				if (!queue.contains(fileNum) && !reading.contains(fileNum)) {
					queue.add(fileNum);
					added = true;
				}
			}
			unlockMe(this);
			if (added) {
				setSleep(0);
			}
		}
	}
	
	@Override
	protected void whileWorking() {
		List<Integer> list = new ArrayList<Integer>();
		lockMe(this);
		if (queue.size()>0) {
			int add = (MAX - reading.size());
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
			setSleep(10);
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
			LockedCode code = new LockedCode() {
				@Override
				public Object doLocked() {
					if (index!=null) {
						index.readObjects(idObjMap);
					}
					reading.remove(fileNum);
					return null;
				}
			};
			doLocked(this,code);
		}
	}
	
	private void readFileNumList(List<Integer> fileNumList) {
		for (Integer fileNum: fileNumList) {
			readFileNum(fileNum);
		}
	}
	
	private void readFileNum(int fileNum) {
		String fileName = index.getObjectDirectory() + fileNum + ".txt";
		IndexObjectReadWorker worker = new IndexObjectReadWorker(getMessenger(),getUnion(),this,fileNum,fileName,index.getKey());
		worker.start();
	}
}

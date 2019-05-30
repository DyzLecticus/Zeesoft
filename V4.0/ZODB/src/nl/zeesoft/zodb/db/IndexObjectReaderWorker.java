package nl.zeesoft.zodb.db;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class IndexObjectReaderWorker extends Worker {
	private	Index						index		= null;
	
	private List<Long>					queue		= new ArrayList<Long>();
	private List<Long>					reading		= new ArrayList<Long>();
	
	protected IndexObjectReaderWorker(Messenger msgr, WorkerUnion union,Index index) {
		super(msgr, union);
		this.index = index;
		setSleep(100);
	}
	
	protected void addId(long id) {
		lockMe(this);
		boolean added = addIdNoLock(id);
		unlockMe(this);
		if (added) {
			setSleep(0);
		}
	}

	protected void addIdList(List<Long> idList) {
		if (idList.size()>0) {
			boolean added = false;
			lockMe(this);
			for (Long id: idList) {
				addIdNoLock(id);
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
		List<Long> list = null;
		lockMe(this);
		list = new ArrayList<Long>();
		if (queue.size()>0) {
			int add = (1000 - reading.size());
			if (add>0) {
				for (int i = 0; i < add; i++) {
					if (queue.size()==0) {
						break;
					}
					Long id = queue.get(0);
					reading.add(id);
					list.add(id);
					queue.remove(0);
				}
			}
		}
		unlockMe(this);
		if (list.size()>0) {
			readIdList(list);
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
	
	protected void readObject(long id,JsFile obj) {
		if (isWorking()) {
			lockMe(this);
			if (index!=null) {
				index.readObject(id,obj);
			}
			reading.remove(id);
			unlockMe(this);
		}
	}
	
	private void readIdList(List<Long> idList) {
		List<IndexObjectReadWorker> workers = new ArrayList<IndexObjectReadWorker>();
		for (Long id: idList) {
			String fileName = index.getObjectDirectory() + id + ".txt";
			workers.add(new IndexObjectReadWorker(getMessenger(),getUnion(),this,id,fileName,index.getKey()));
		}
		if (workers.size()>0) {
			for (IndexObjectReadWorker worker: workers) {
				worker.start();
			}
		}
	}
	
	private boolean addIdNoLock(long id) {
		boolean r = false;
		if (!queue.contains(id) && !reading.contains(id)) {
			queue.add(id);
			r = true;
		}
		return r;
	}
}

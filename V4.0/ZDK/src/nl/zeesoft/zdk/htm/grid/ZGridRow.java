package nl.zeesoft.zdk.htm.grid;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class ZGridRow extends Worker implements ZGridRequestNext {
	protected int 					index			= 0;
	protected List<ZGridColumn>		columns			= new ArrayList<ZGridColumn>();
	protected ZGridRequestNext		nextProcessor	= null;
	
	protected Queue<ZGridRequest>	queue			= new LinkedList<ZGridRequest>();
	protected ZGridRequest			request			= null;
	protected int					done			= 0;

	protected ZGridRow(Messenger msgr, WorkerUnion union) {
		super(msgr, union);
		setSleep(1);
	}
	
	protected void addColumn() {
		ZGridColumn r = new ZGridColumn(getMessenger(),getUnion());
		r.row = this;
		r.index = columns.size();
		columns.add(r);
	}
	
	protected void addRequest(ZGridRequest request) {
		lockMe(this);
		queue.add(request);
		unlockMe(this);
	}
	
	@Override
	public void start() {
		for (ZGridColumn col: columns) {
			col.start();
		}
		super.start();
	}
	
	@Override
	public void stop() {
		for (ZGridColumn col: columns) {
			col.stop();
		}
		super.stop();
	}
	
	protected void destroy() {
		lockMe(this);
		for (ZGridColumn col: columns) {
			col.destroy();
		}
		columns.clear();
		unlockMe(this);
	}

	@Override
	protected void whileWorking() {
		boolean r = false;
		lockMe(this);
		if (request==null) {
			request = queue.poll();
			if (request!=null) {
				r = true;
			}
		}
		unlockMe(this);
		if (r) {
			lockMe(this);
			done = 0;
			for (ZGridColumn col: columns) {
				col.setRequest(request);
			}
			unlockMe(this);
		}
	}
	
	protected void processedColumn() {
		lockMe(this);
		done++;
		if (done>=columns.size()) {
			nextProcessor.processedRequest(request);
			request = null;
		}
		unlockMe(this);
	}

	@Override
	public void processedRequest(ZGridRequest request) {
		addRequest(request);
	}
}

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
	
	private Queue<ZGridResult>		queue			= new LinkedList<ZGridResult>();
	private ZGridResult				result			= null;
	private int						done			= 0;
	
	protected ZGridRow(Messenger msgr, WorkerUnion union) {
		super(msgr, union);
		setSleep(0);
		setSleepNs(ZGrid.SLEEP_NS);
	}
	
	protected void addColumn() {
		ZGridColumn r = new ZGridColumn(getMessenger(),getUnion());
		r.row = this;
		r.index = columns.size();
		columns.add(r);
	}
	
	@Override
	public void start() {
		for (ZGridColumn col: columns) {
			if (col.isActive()) {
				col.start();
			}
		}
		super.start();
	}
	
	@Override
	public void stop() {
		for (ZGridColumn col: columns) {
			if (col.isActive()) {
				col.stop();
			}
		}
		super.stop();
		waitForStop(10,false);
	}

	@Override
	public void processedRequest(ZGridResult result) {
		addResult(result);
	}
	
	protected void destroy() {
		lockMe(this);
		for (ZGridColumn col: columns) {
			col.destroy();
		}
		columns.clear();
		unlockMe(this);
	}
	
	protected void addResult(ZGridResult result) {
		lockMe(this);
		queue.add(result);
		unlockMe(this);
	}

	@Override
	protected void whileWorking() {
		boolean r = false;
		int s = ZGrid.SLEEP_NS;
		lockMe(this);
		if (result==null) {
			result = queue.poll();
			if (result!=null) {
				r = true;
			}
		}
		if (result!=null) {
			s = 0;
		}
		unlockMe(this);
		setSleepNs(s);
		if (r) {
			lockMe(this);
			done = 0;
			for (ZGridColumn col: columns) {
				if (col.isActive()) {
					col.setRequest(result);
				} else {
					done++;
				}
			}
			unlockMe(this);
		}
	}
	
	protected void processedColumn() {
		lockMe(this);
		done++;
		if (done>=columns.size()) {
			nextProcessor.processedRequest(result);
			result = null;
		}
		unlockMe(this);
	}
}

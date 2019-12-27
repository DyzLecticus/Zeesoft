package nl.zeesoft.zdk.htm.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class ZGridResults extends Locker {
	private ZGrid							grid		= null;
	
	private long							uid			= 0;
	private SortedMap<Long,ZGridResult>		results		= new TreeMap<Long,ZGridResult>();
	private List<ZGridResultsListener>		listeners	= new ArrayList<ZGridResultsListener>();
	
	protected ZGridResults(Messenger msgr,ZGrid grid) {
		super(msgr);
		this.grid = grid;
	}
	
	public void addListener(ZGridResultsListener listener) {
		lockMe(this);
		listeners.add(listener);
		unlockMe(this);
	}
	
	protected long getUid() {
		long r = 0;
		lockMe(this);
		r = uid;
		unlockMe(this);
		return r;
	}
	
	protected void setUid(long uid) {
		lockMe(this);
		this.uid = uid;
		unlockMe(this);
	}

	protected long assignRequestId(ZGridRequest request) {
		lockMe(this);
		uid++;
		request.id = uid;
		long r = request.id;
		unlockMe(this);
		return r;
	}
	
	protected void addResult(ZGridResult result) {
		lockMe(this);
		if (result.getRequest().id==0) {
			uid++;
			result.getRequest().id = uid;
		}
		if (result.getRequest().id!=0 && !results.containsKey(result.getRequest().id)) {
			results.put(result.getRequest().id,result);
		}
		unlockMe(this);
	}
	
	protected ZGridResult getResult(long id) {
		ZGridResult r = null;
		lockMe(this);
		r = results.get(id);
		unlockMe(this);
		return r;
	}
	
	protected ZGridResult removeResult(long id) {
		ZGridResult r = null;
		lockMe(this);
		r = results.remove(id);
		unlockMe(this);
		return r;
	}

	protected List<ZGridResult> flush() {
		List<ZGridResult> r = new ArrayList<ZGridResult>();
		List<ZGridResultsListener> list = null;
		lockMe(this);
		for (Long key: results.keySet()) {
			r.add(results.get(key));
		}
		results.clear();
		if (r.size()>0) {
			list = new ArrayList<ZGridResultsListener>(listeners);
		}
		unlockMe(this);
		if (r.size()>0 && list.size()>0) {
			for (ZGridResult result: r) {
				for (ZGridResultsListener listener: list) {
					try {
						listener.processedRequest(grid,result);
					} catch(Exception e) {
						if (getMessenger()!=null) {
							getMessenger().error(this,"Grid listener exception",e);
						} else {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return r;
	}
	
	protected void destroy() {
		lockMe(this);
		grid = null;
		results.clear();
		listeners.clear();
		unlockMe(this);
	}
}

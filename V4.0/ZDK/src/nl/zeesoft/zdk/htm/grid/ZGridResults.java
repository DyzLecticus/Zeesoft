package nl.zeesoft.zdk.htm.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class ZGridResults extends Locker {
	private long							uid			= 0;
	private SortedMap<Long,ZGridRequest>	results		= new TreeMap<Long,ZGridRequest>();
	private List<ZGridResultsListener>		listeners	= new ArrayList<ZGridResultsListener>();
	
	protected ZGridResults(Messenger msgr) {
		super(msgr);
	}
	
	public void addListener(ZGridResultsListener listener) {
		lockMe(this);
		listeners.add(listener);
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
	
	protected void addResult(ZGridRequest result) {
		lockMe(this);
		if (result.id==0) {
			uid++;
			result.id = uid;
		}
		if (result.id!=0 && !results.containsKey(result.id)) {
			results.put(result.id,result);
		}
		unlockMe(this);
	}
	
	protected ZGridRequest getResult(long id) {
		ZGridRequest r = null;
		lockMe(this);
		r = results.get(id);
		unlockMe(this);
		return r;
	}
	
	protected ZGridRequest removeResult(long id) {
		ZGridRequest r = null;
		lockMe(this);
		r = results.remove(id);
		unlockMe(this);
		return r;
	}

	protected List<ZGridRequest> flush() {
		List<ZGridRequest> r = new ArrayList<ZGridRequest>();
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
			for (ZGridRequest request: r) {
				for (ZGridResultsListener listener: list) {
					try {
						listener.processedRequest(request);
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
		results.clear();
		listeners.clear();
		unlockMe(this);
	}
}

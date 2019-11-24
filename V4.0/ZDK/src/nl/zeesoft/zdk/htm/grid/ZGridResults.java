package nl.zeesoft.zdk.htm.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class ZGridResults extends Locker {
	private long							uid			= 0;
	private SortedMap<Long,ZGridRequest>		results		= new TreeMap<Long,ZGridRequest>();
	
	protected ZGridResults(Messenger msgr) {
		super(msgr);
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
		lockMe(this);
		for (Long key: results.keySet()) {
			r.add(results.get(key));
		}
		results.clear();
		unlockMe(this);
		return r;
	}
}

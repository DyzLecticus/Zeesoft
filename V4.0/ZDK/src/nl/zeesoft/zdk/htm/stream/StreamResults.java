package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class StreamResults extends Locker {
	private long							uid			= 0;
	private SortedMap<Long,StreamResult>	results		= new TreeMap<Long,StreamResult>();
	
	public StreamResults(Messenger msgr) {
		super(msgr);
	}
	
	protected StreamResult getNewResult(SDR inputSDR) {
		lockMe(this);
		uid++;
		StreamResult r = new StreamResult();
		r.inputSDR = inputSDR;
		r.id = uid;
		unlockMe(this);
		return r;
	}
	
	public void addResult(StreamResult result) {
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
	
	public StreamResult getResult(long id) {
		StreamResult r = null;
		lockMe(this);
		r = results.get(id);
		unlockMe(this);
		return r;
	}
	
	public StreamResult removeResult(long id) {
		StreamResult r = null;
		lockMe(this);
		r = results.remove(id);
		unlockMe(this);
		return r;
	}

	public List<StreamResult> flush() {
		List<StreamResult> r = new ArrayList<StreamResult>();
		lockMe(this);
		for (Long key: results.keySet()) {
			r.add(results.get(key));
		}
		results.clear();
		unlockMe(this);
		return r;
	}
}

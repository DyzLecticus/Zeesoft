package nl.zeesoft.zid.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class SessionManager extends Locker {
	private	long						uid			= 0;
	private SortedMap<Long,Session>		sessions	= new TreeMap<Long,Session>();
	
	public SessionManager(Messenger msgr) {
		super(msgr);
	}

	public List<Long> getSessionIds() {
		lockMe(this);
		List<Long> r = new ArrayList<Long>(sessions.keySet());
		unlockMe(this);
		return r;
	}

	public Session getSession(long id) {
		lockMe(this);
		Session r = sessions.get(id);
		unlockMe(this);
		return r;
	}
	
	public Session openSession() {
		lockMe(this);
		uid++;
		Session r = new Session(uid);
		sessions.put(r.getId(),r);
		unlockMe(this);
		return r;
	}
	
	public Session closeSession(long id) {
		lockMe(this);
		Session r = sessions.remove(id);
		if (r!=null) {
			closeSessionNoLock(r);
		}
		unlockMe(this);
		return r;
	}

	public List<Long> closeInactiveSessions(long inactiveMs) {
		lockMe(this);
		ZDate now = new ZDate();
		List<Long> removeKeys = new ArrayList<Long>();
		for (Entry<Long,Session> entry: sessions.entrySet()) {
			if (entry.getValue().getLastActivity().getDate().getTime() < (now.getDate().getTime() - inactiveMs)) {
				removeKeys.add(entry.getKey());
			}
		}
		for (Long id: removeKeys) {
			Session r = sessions.remove(id);
			closeSessionNoLock(r);
		}
		unlockMe(this);
		return removeKeys;
	}
	
	private void closeSessionNoLock(Session s) {
		s.setEnd(new ZDate());
	}
}

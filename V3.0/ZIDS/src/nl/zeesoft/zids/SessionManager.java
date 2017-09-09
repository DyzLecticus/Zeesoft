package nl.zeesoft.zids;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zid.dialog.DialogHandler;
import nl.zeesoft.zids.session.Session;
import nl.zeesoft.zspr.pattern.PatternManager;

public class SessionManager extends Locker {
	private List<Dialog>				dialogs			= null;
	private PatternManager				patternManager	= null;
	
	private long						uid				= 0;
	private SortedMap<Long,Session>		sessions		= new TreeMap<Long,Session>();
	
	public SessionManager(Messenger msgr, List<Dialog> dialogs, PatternManager patternManager) {
		super(msgr);
		this.dialogs = dialogs;
		this.patternManager = patternManager;
	}
	
	public Session getSession(long id) {
		Session r = null;
		lockMe(this);
		r = sessions.get(id);
		unlockMe(this);
		return r;
	}

	public Session addSession() {
		Session r = null;
		lockMe(this);
		r = getNewSession();
		sessions.put(r.getId(),r);
		unlockMe(this);
		return r;
	}

	public Session removeSession(long id) {
		Session r = null;
		lockMe(this);
		r = sessions.remove(id);
		unlockMe(this);
		return r;
	}

	protected Session getNewSession() {
		uid++;
		Session session = new Session(getMessenger(),getNewDialogHandler(),uid);
		return session;
	}

	protected DialogHandler getNewDialogHandler() {
		DialogHandler dh = new DialogHandler(getMessenger(),dialogs,patternManager);
		dh.initialize();
		return dh;
	}
}

package nl.zeesoft.zids.session;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zid.dialog.DialogHandler;

public class Session extends Locker {
	private DialogHandler 	handler 	= null;
	private long			id			= 0;
	private ZDate			started		= null;
	private ZDate			finished	= null;
	private ZStringBuilder	log			= null;
	
	public Session(Messenger msgr, DialogHandler handler,long id) {
		super(msgr);
		this.handler = handler;
		this.id = id;
		started = new ZDate();
	}

	public DialogHandler getHandler() {
		return handler;
	}

	public long getId() {
		return id;
	}

	public ZDate getStarted() {
		return started;
	}

	public void setStarted(ZDate started) {
		this.started = started;
	}

	public ZDate getFinished() {
		return finished;
	}

	public void setFinished(ZDate finished) {
		this.finished = finished;
	}

	public ZStringBuilder getLog() {
		return log;
	}

	public void setLog(ZStringBuilder log) {
		this.log = log;
	}
}

package nl.zeesoft.zodb.db;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zodb.db.idx.IndexConfig;

public class DatabaseConfig {
	private Messenger		messenger		= null;
	private WorkerUnion		union			= null;
	
	public boolean			debug			= false;
	public String			dataDir			= "";
	public StringBuilder	key				= new StringBuilder("1234567890123456789012345678901234567890123456789012345678901234");
	public StringBuilder	newKey			= null;
	public int				indexBlockSize	= 1000;
	public int				dataBlockSize	= 10;
	
	public IndexConfig		indexConfig		= null;
	
	protected DatabaseConfig(Messenger msgr, WorkerUnion uni) {
		messenger = msgr;
		union = uni;
		indexConfig = new IndexConfig(msgr);
	}
	
	protected Messenger getMessenger() {
		return messenger;
	}
	
	protected WorkerUnion getUnion() {
		return union;
	}
	
	protected void debug(Object source,String message) {
		if (debug) {
			messenger.debug(source,message);
		}
	}
	
	protected void warn(Object source,String message) {
		messenger.warn(source,message);
	}

	protected void error(Object source,String message) {
		messenger.error(source,message);
	}

	protected void error(Object source,String message,Exception e) {
		messenger.error(source,message,e);
	}
	
}

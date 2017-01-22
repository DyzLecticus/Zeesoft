package nl.zeesoft.zodb.database;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqObject;

/**
 * This class is the access point for database request execution. 
 * 
 * Create a request, pass it to the addRequest method and listen to the request done event.
 */
public final class DbRequestQueue extends Locker {
	private static DbRequestQueue					queue						= null;
	
	private List<ReqObject>							requestQueue				= new ArrayList<ReqObject>();
	private boolean									readOnly					= false;									
	
	private DbRequestQueue() {
		// Singleton
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	public static DbRequestQueue getInstance() {
		if (queue==null) {
			queue = new DbRequestQueue();
		}
		return queue;
	}

	public ReqObject getRequest(int index,Object source) {
		ReqObject req = null;
		lockMe(source);
		if (requestQueue.size()>index) {
			req = requestQueue.get(index);
		}
		unlockMe(source);
		return req;
	}

	public void addRequest(ReqObject req,Object source) {
		if (req.check(source)) {
			if ((!(req instanceof ReqGet)) && isReadOnly(source)) {
				Messenger.getInstance().debug(this,"Read only mode, refused request");
				req.addError(ReqObject.ERROR_CODE_DATABASE_IS_READ_ONLY,"Database is currently in read only mode");
				req.publishRequestDoneEvent(source);
			} else {
				ReqObject copy = ReqObject.copy(req);
				copy.prepare();
				lockMe(source);
				requestQueue.add(copy);
				if (requestQueue.size()>1) {
					DbRequestQueueWorker.getInstance().setSleep(0);
				} else {
					DbRequestQueueWorker.getInstance().setSleep(1);
				}
				unlockMe(source);
			}
		} else {
			req.publishRequestDoneEvent(source);
		}
	}

	public void removeRequest(ReqObject req,Object source) {
		lockMe(source);
		if (requestQueue.contains(req)) {
			requestQueue.remove(req);
		}
		if (requestQueue.size()==0) {
			DbRequestQueueWorker.getInstance().setSleep(1);
		}
		unlockMe(source);
	}

	public void clearQueue(Object source) {
		lockMe(source);
		List<ReqObject> queue = new ArrayList<ReqObject>(requestQueue);
		unlockMe(source);
		if (queue.size()>0) {
			Messenger.getInstance().debug(this,"Database stopped, refused requests: " + queue.size());
			for (ReqObject req: queue) {
				req.addError(ReqObject.ERROR_CODE_DATABASE_STOPPED,"Database has stopped");
				req.publishRequestDoneEvent(source);
			}
		}
		lockMe(source);
		for (ReqObject req: queue) {
			requestQueue.remove(req);
		}
		unlockMe(source);
		DbRequestQueueWorker.getInstance().setSleep(1);
	}
	
	/**
	 * @return the readOnly
	 */
	public boolean isReadOnly(Object source) {
		boolean r = false;
		lockMe(source);
		r = readOnly;
		unlockMe(source);
		return r;
	}

	/**
	 * @param readOnly the readOnly to set
	 */
	public void setReadOnly(boolean readOnly,Object source) {
		lockMe(source);
		this.readOnly = readOnly;
		unlockMe(source);
	}
}	

package nl.zeesoft.zodb.client;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.event.EvtEvent;

public class ClRequestQueue {
	private long					uid					= 0;
	private ClSession				session				= null;
	private List<ClRequest>			requestQueue		= new ArrayList<ClRequest>();
	private Object 					queueIsLockedBy		= null;
	private boolean					waitForResponse 	= false;

	public ClRequestQueue(ClSession sess) {
		session = sess;
	}

	public ClRequest getNewRequest(Object source) {
		lockQueue(source);
		ClRequest r = new ClRequest();
		r.setId(getNewRequestIdNoLock(source));
		unlockQueue(source);
		return r;
	}

	public int getQueueSize(Object source) {
		int size = 0;
		lockQueue(source);
		size = requestQueue.size();
		unlockQueue(source);
		return size;
	}

	public void addRequest(ClRequest r, Object source) {
		boolean added = false;
		lockQueue(source);
		int size = requestQueue.size();
		if (size>=ClConfig.getInstance().getMaxRequestQueueSize()) {
			size--;
			requestQueue.remove(size);
		}
		if (size<ClConfig.getInstance().getMaxRequestQueueSize()) {
			if (r.getId()==0) {
				r.setId(getNewRequestIdNoLock(source));
			}
			requestQueue.add(r);
			added = true;
		}
		unlockQueue(source);
		if (added) {
			ClSessionManager.getInstance().publishEvent(new EvtEvent(ClSessionManager.ADDED_REQUEST,this,r));
		}
	}
	
	public void sendRequestAndProcessResponse(Object source) {
		ClRequest r = null;
		boolean sent = false;
		lockQueue(source);
		if (!waitForResponse) {
			if (requestQueue.size()>0) {
				r = requestQueue.get(0);
				sent = sendRequestNoLock(r);
			}
		}
		unlockQueue(source);
		if (sent) {
			ClSessionManager.getInstance().publishEvent(new EvtEvent(ClSessionManager.SENT_REQUEST,this,r));
		}
	}
	
	public void processRequestResponse(ClRequest response, Object source) {
		ClRequest original = null;
		lockQueue(source);
		if ((response!=null) && (response.getId()>0) && (response.getId()==requestQueue.get(0).getId())) {
			original = requestQueue.get(0);
			requestQueue.remove(0);
			waitForResponse = false;
		}
		unlockQueue(source);
		if (original!=null) {
			response.setSendTime(original.getSendTime());
			try {
				ClSessionManager.getInstance().publishEvent(new EvtEvent(ClSessionManager.RECEIVED_RESPONSE,this,response));
				original.publishEvent(new EvtEvent(ClRequest.RECEIVED_REQUEST_RESPONSE,this,response));
			} catch (Exception e) {
				ClSessionManager.getInstance().publishEvent(new EvtEvent(ClSessionManager.REQUEST_RESPONSE_PROCESS_ERROR,this,e));
			}
		}
	}
	
	/****************** PRIVATE METHODS *******************/
	
	private synchronized void lockQueue(Object source) {
		int attempt = 0;
		while (queueIsLocked()) {
		    try {
                wait();
            } catch (InterruptedException e) { 
            	
            }
            attempt ++;
			if (attempt>=1000) {
				Messenger.getInstance().error(this,"Lock failed after " + attempt + " attempts. Source:" + source);
				attempt = 0;
			}
		}
		queueIsLockedBy = source;
	}

	private synchronized void unlockQueue(Object source) {
		if (queueIsLockedBy==source) {
			queueIsLockedBy=null;
			notifyAll();
		}
	}
	
	private synchronized boolean queueIsLocked() {
		return (queueIsLockedBy!=null);
	}

	private boolean sendRequestNoLock(ClRequest r) {
		boolean sent = false;
		if ((r!=null) && (r.getId()>0)) {
			waitForResponse = true;
			session.sendRequest(r);
			sent = true;
		}
		return sent;
	}

	private long getNewRequestIdNoLock(Object source) {
		uid++;
		return uid;
	}

}

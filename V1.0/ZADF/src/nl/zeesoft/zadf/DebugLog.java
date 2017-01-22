package nl.zeesoft.zadf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.client.ClRequest;
import nl.zeesoft.zodb.client.ClSessionManager;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventPublisher;
import nl.zeesoft.zodb.event.EvtEventSubscriber;

public final class DebugLog extends EvtEventPublisher implements EvtEventSubscriber {
	public static final String				ADDED_LOG_ITEM		= "ADDED_LOG_ITEM";
	
	private static DebugLog					log					= null;

	private Object							logIsLockedBy		= null;
	
	private long							uid					= 0;
	private	List<Long>						debugLogItemIdList 	= new ArrayList<Long>();
	private SortedMap<Long,DebugLogItem>	debugLogItemMap		= new TreeMap<Long,DebugLogItem>();

	private DebugLog() {
		// Singleton
	}

	public static DebugLog getInstance() {
		if (log==null) {
			log = new DebugLog();
		}
		return log;
	}
	
	@Override
	public void handleEvent(EvtEvent e) {
		if (DbConfig.getInstance().isDebug()) {
			if (
				(e.getType().equals(Messenger.MSG_DEBUG)) ||
				(e.getType().equals(Messenger.MSG_WARNING)) ||
				(e.getType().equals(Messenger.MSG_ERROR))
				) {
				addLogItem(e.getType() + ": " + e.getValue().toString(),e,e.getSource());
			} else if ( 
				(e.getType().equals(ClSessionManager.SENT_REQUEST)) || 
				(e.getType().equals(ClSessionManager.RECEIVED_RESPONSE))
				) {
				ClRequest r = (ClRequest) e.getValue();
				String desc = "";
				if (e.getType().equals(ClSessionManager.SENT_REQUEST)) {
					desc = "REQUEST: " + r.getId() + ": Sent"; 
				} else if (e.getType().equals(ClSessionManager.RECEIVED_RESPONSE)) {
					desc = "REQUEST: " + r.getId() + ": Response received";
					if (r.getSendTime()!=null) {
						desc = desc + " in " + (new Date().getTime() - r.getSendTime().getTime()) + " ms";
					}
				}
				addLogItem(desc,e.getValue(),e.getSource());
			}
		}
	}

	/**
	 * @return the debugLogItemIdList
	 */
	public List<Long> getDebugLogItemIdList(Object source) {
		List<Long> r = null;
		lockLog(source);
		r = new ArrayList<Long>(debugLogItemIdList);
		unlockLog(source);
		return r;
	}

	/**
	 * @return the debugLogItemMap
	 */
	public SortedMap<Long, DebugLogItem> getDebugLogItemMap(Object source) {
		SortedMap<Long, DebugLogItem> r = null;
		lockLog(source);
		r = new TreeMap<Long,DebugLogItem>(debugLogItemMap);
		unlockLog(source);
		return r;
	}
	
	private void addLogItem(String desc,Object obj,Object source) {
		desc = desc.replace("\n", " ");
		lockLog(source);
		
		uid++;
		Long id = new Long(uid);
		debugLogItemIdList.add(id);
		
		DebugLogItem item = new DebugLogItem();
		item.setId(id);
		item.setDesc(desc);
		item.setObj(obj);
		if (obj instanceof EvtEvent) {
			EvtEvent e = (EvtEvent) obj;
			if (e.getType().equals(Messenger.MSG_ERROR)) {
				item.setError(true);
			}
		} else if (obj instanceof ClRequest) {
			ClRequest r = (ClRequest) obj;
			if (r.getError()) {
				item.setError(true);
			}
		}
		debugLogItemMap.put(id, item);
		unlockLog(source);
		
		publishEvent(new EvtEvent(ADDED_LOG_ITEM, this, item));
	}

	private synchronized void lockLog(Object source) {
		int attempt = 0;
		while (indexIsLocked()) {
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
		logIsLockedBy = source;
	}

	private synchronized void unlockLog(Object source) {
		if (logIsLockedBy==source) {
			logIsLockedBy=null;
			notifyAll();
		}
	}
	
	private synchronized boolean indexIsLocked() {
		return (logIsLockedBy!=null);
	}
}

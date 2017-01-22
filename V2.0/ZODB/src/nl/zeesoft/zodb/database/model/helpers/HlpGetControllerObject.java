package nl.zeesoft.zodb.database.model.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.event.EvtEvent;

public abstract class HlpGetControllerObject extends HlpControllerObject {
	private String							className		= ""; 
	private SortedMap<Long,HlpObject>		objectsById		= new TreeMap<Long,HlpObject>();

	private ReqGet							getRequest		= null;
	
	public HlpGetControllerObject(String className) {
		this.className = className;
	}
	
	@Override
	public void initialize() {
		if (className.length()>0) {
			getHlpObjects();
		}
	}

	public void reinitialize() {
		lockMe(this);
		objectsById.clear();
		unlockMe(this);
		initialize();
	}

	public HlpObject getObjectById(long id) {
		HlpObject r = null;
		lockMe(this);
		r = objectsById.get(id);
		unlockMe(this);
		return r;
	}

	public List<HlpObject> getObjectsAsList() {
		List<HlpObject> r = new ArrayList<HlpObject>();
		lockMe(this);
		for (HlpObject obj: objectsById.values()) {
			r.add(obj);
		}
		unlockMe(this);
		return r;
	}

	@Override
	public void handleEvent(EvtEvent e) {
		resetTimeOut();
		if (getRequest!=null && e.getValue()==getRequest && getRequest.getClassName().equals(getClassName())) {
			if (getRequest.getObjects().size()>0) {
				for (ReqDataObject obj: getRequest.getObjects()) {
					HlpObject object = getNewObject();
					object.fromDataObject(obj.getDataObject());
					addObject(object);
				}
			}
			setDone(true);
		}
	}

	public void addObject(HlpObject object) {
		boolean added = false;
		lockMe(this);
		if (!objectsById.containsKey(object.getId())) {
			objectsById.put(object.getId(),object);
			added = true;
		}
		unlockMe(this);
		if (added) {
			addedObject(object);
		}
	}

	public void removeObject(HlpObject object) {
		boolean removed = false;
		lockMe(this);
		if (objectsById.containsKey(object.getId())) {
			object = objectsById.remove(object.getId());
			removed = true;
		}
		unlockMe(this);
		if (removed) {
			removedObject(object);
		}
	}
	
	protected void addedObject(HlpObject object) {
		
	}

	protected void removedObject(HlpObject object) {
		
	}
	
	protected abstract HlpObject getNewObject();
	
	protected ReqGet getNewGetRequest() {
		ReqGet get = new ReqGet(className);
		get.getProperties().add(ReqGet.ALL_PROPERTIES);
		get.addSubscriber(this);
		return get;
	}
	
	private void getHlpObjects() {
		setDone(false);
		getRequest = getNewGetRequest();
		DbRequestQueue.getInstance().addRequest(getRequest,this);
		waitTillDone();
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
}

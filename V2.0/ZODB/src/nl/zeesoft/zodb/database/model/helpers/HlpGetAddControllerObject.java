package nl.zeesoft.zodb.database.model.helpers;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.event.EvtEvent;

public abstract class HlpGetAddControllerObject extends HlpGetControllerObject {
	private ReqAdd			addRequest		= null;
	private HlpObject		addObject		= null;
	private List<HlpObject>	addObjects		= new ArrayList<HlpObject>();
	private int				addObjectIndex	= 0;
	
	public HlpGetAddControllerObject(String className) {
		super(className);
	}

	@Override
	public void handleEvent(EvtEvent e) {
		resetTimeOut();
		super.handleEvent(e);
		if (addRequest!=null && e.getValue()==addRequest && addRequest.getClassName().equals(getClassName())) {
			if (addRequest.getImpactedIds().size()>0) {
				addObject.setId(addRequest.getImpactedIds().get(0));
				addObject(addObject);
				
				addRequest = getNextAddRequest();
				if (addRequest!=null) {
					DbRequestQueue.getInstance().addRequest(addRequest,this);
				} else {
					setDone(true);
				}
			} else {
				if (addRequest.hasError()) {
					Messenger.getInstance().error(this,"Add request error: " + addRequest.getErrors().get(0).getMessage());
				} else {
					Messenger.getInstance().error(this,"Failed to add object");
				}
				setDone(true);
			}
		}
	}
	
	@Override
	public void addObject(HlpObject object) {
		if (object.getId()>0) {
			super.addObject(object);
		} else {
			List<HlpObject> addObjects = new ArrayList<HlpObject>();
			addObjects.add(object);
			addHlpObjects(addObjects);
		}
	}

	public void addHlpObjects(List<HlpObject> addObjects) {
		if (addObjects.size()>0) {
			setDone(false);
			this.addObjects = addObjects;
			addObjectIndex = 0;
			addRequest = getNextAddRequest();
			DbRequestQueue.getInstance().addRequest(addRequest,this);
			waitTillDone();
		}
	}
	
	private ReqAdd getNextAddRequest() {
		addObject = null;
		if (addObjectIndex<addObjects.size()) {
			addObject = addObjects.get(addObjectIndex);
			addObjectIndex++;
		}
		return getNewAddRequest(addObject);
	}
	
	private ReqAdd getNewAddRequest(HlpObject addObject) {
		ReqAdd add = null;
		if (addObject!=null) {
			add = new ReqAdd(getClassName());
			add.getObjects().add(new ReqDataObject(addObject.toDataObject()));
			add.addSubscriber(this);
		}
		return add;
	}
}
 
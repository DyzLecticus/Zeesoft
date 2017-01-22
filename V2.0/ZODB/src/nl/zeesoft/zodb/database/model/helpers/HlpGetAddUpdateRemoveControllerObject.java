package nl.zeesoft.zodb.database.model.helpers;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.request.ReqObject;
import nl.zeesoft.zodb.database.request.ReqRemove;
import nl.zeesoft.zodb.database.request.ReqUpdate;
import nl.zeesoft.zodb.event.EvtEvent;

public abstract class HlpGetAddUpdateRemoveControllerObject extends HlpGetAddControllerObject {
	private ReqObject		request				= null;
	private List<ReqObject>	requests			= new ArrayList<ReqObject>();
	private int				requestIndex		= 0;
	private List<String>	updateProperties	= new ArrayList<String>();
	private boolean			update				= true;
	
	public HlpGetAddUpdateRemoveControllerObject(String className,List<String> updateProperties) {
		super(className);
		if (updateProperties!=null) {
			this.updateProperties = updateProperties;
		}
	}

	@Override
	public void handleEvent(EvtEvent e) {
		super.handleEvent(e);
		if (request!=null && e.getValue()==request && request.getClassName().equals(getClassName())) {
			if (!request.hasError()) {
				request = getNextRequest();
				if (request!=null) {
					DbRequestQueue.getInstance().addRequest(request,this);
				} else {
					setDone(true);
				}
			} else {
				String type = "Update";
				if (!update) {
					type = "Remove";
				}
				Messenger.getInstance().error(this,type + " request error: " + request.getErrors().get(0).getMessage());
				setDone(true);
			}
		}
	}
	
	public void updateHlpObject(HlpObject object) {
		if (object.getId()>0) {
			List<HlpObject> updateObjects = new ArrayList<HlpObject>();
			updateObjects.add(object);
			updateHlpObjects(updateObjects);
		}
	}

	public void updateHlpObjects(List<HlpObject> updateObjects) {
		if (updateObjects.size()>0) {
			setDone(false);
			requestIndex = 0;
			requests.clear();
			update = true;
			for (HlpObject object: updateObjects) {
				if (object.getId()>0) {
					ReqUpdate req = object.getNewUpdateRequest(this);
					if (updateProperties.size()>0) {
						for (String pName: req.getUpdateObject().getProperties()) {
							if (!updateProperties.contains(pName)) {
								req.getUpdateObject().removePropertyValue(pName);
							}
						}
					}
					requests.add(req);
				}
			}
			request = getNextRequest();
			DbRequestQueue.getInstance().addRequest(request,this);
			waitTillDone();
		}
	}

	public void removeHlpObjects(List<HlpObject> removeObjects) {
		if (removeObjects.size()>0) {
			setDone(false);
			requestIndex = 0;
			requests.clear();
			update = false;
			for (HlpObject object: removeObjects) {
				if (object.getId()>0) {
					ReqRemove req = new ReqRemove(getClassName(),object.getId());
					req.addSubscriber(this);
					requests.add(req);
				}
			}
			request = getNextRequest();
			DbRequestQueue.getInstance().addRequest(request,this);
			waitTillDone();
			for (HlpObject object: removeObjects) {
				removeObject(object);
			}
		}
	}
	
	private ReqObject getNextRequest() {
		ReqObject r = null;
		if (requestIndex<requests.size()) {
			r = requests.get(requestIndex);
			requestIndex++;
		}
		return r;
	}
}
 
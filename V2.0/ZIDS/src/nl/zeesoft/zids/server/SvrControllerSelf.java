package nl.zeesoft.zids.server;

import nl.zeesoft.zids.database.model.Self;
import nl.zeesoft.zids.database.model.ZIDSModel;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.model.helpers.HlpGetControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqUpdate;
import nl.zeesoft.zodb.event.EvtEvent;

public class SvrControllerSelf extends HlpGetControllerObject {
	private static SvrControllerSelf	controller			= null;
	
	private ReqAdd						addSelfRequest 		= null;
	private ReqUpdate					updateSelfRequest 	= null;
	private Self						self				= null;	

	private SvrControllerSelf() {
		super(ZIDSModel.SELF_CLASS_FULL_NAME);
	}
	
	public static SvrControllerSelf getInstance() {
		if (controller==null) {
			controller = new SvrControllerSelf();
		}
		return controller;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}
	
	@Override
	protected HlpObject getNewObject() {
		return new Self();
	}

	@Override
	public void handleEvent(EvtEvent e) {
		super.handleEvent(e);
		resetTimeOut();
		if (addSelfRequest!=null && e.getValue()==addSelfRequest) {
			if (addSelfRequest.getImpactedIds().size()>0) {
				reinitialize();
			}
			setDone(true);
		}
	}
		
	public Self getSelf() {
		if (self==null) {
			if (this.getObjectsAsList().size()==0) {
				initialize();
			}
			self = (Self) this.getObjectById(1);
		}
		if (self==null) {
			addSelf();
			self = (Self) this.getObjectById(1);
		} else if (!self.getName().equals(getSelfFullName())) {
			self.setName(getSelfFullName());
			resetSelf(self);
		}
		return self;
	}
	
	private void addSelf() {
		setDone(false);
		addSelfRequest = new ReqAdd(ZIDSModel.SELF_CLASS_FULL_NAME);
		addSelfRequest.addSubscriber(this);
		Self self = new Self();
		self.setId(1);
		self.setName(getSelfFullName());
		addSelfRequest.getObjects().add(new ReqDataObject(self.toDataObject()));
		DbRequestQueue.getInstance().addRequest(addSelfRequest,this);
		waitTillDone();
	}

	private void resetSelf(Self self) {
		setDone(false);
		updateSelfRequest = new ReqUpdate(ZIDSModel.SELF_CLASS_FULL_NAME,1);
		updateSelfRequest.addSubscriber(this);
		updateSelfRequest.getObjects().add(new ReqDataObject(self.toDataObject()));
		DbRequestQueue.getInstance().addRequest(updateSelfRequest,this);
		waitTillDone();
	}

	private String getSelfFullName() {
		ZIDSModel model = (ZIDSModel)DbConfig.getInstance().getModel();
		return model.getSelfFirstName() + " " + model.getSelfLastName();
	}
}


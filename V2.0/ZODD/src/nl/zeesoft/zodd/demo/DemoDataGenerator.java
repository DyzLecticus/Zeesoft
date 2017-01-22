package nl.zeesoft.zodd.demo;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.gui.GuiController;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventExceptionSubscriber;

public class DemoDataGenerator extends Locker implements EvtEventExceptionSubscriber {
	private static final int 	GENERATE_PARENTS				= 50000;
	private static final int 	GENERATE_CHILDREN_PER_PARENT	= 4;

	private ReqGet				getParentRequest				= new ReqGet(DemoModel.DEMO_PARENT_FULL_NAME);

	private List<ReqAdd>		addParentRequests				= new ArrayList<ReqAdd>();
	private int					addParentRequestIndex			= 0;
	private ReqAdd 				addParentRequest 				= null;
	
	//private ReqAdd 				lastRequest						= null;
	private int					waitingForRequests				= 0;
	
	private boolean 			done	 						= false;

	@Override
	public void handleEventException(EvtEvent evt, Exception ex) {
		setDone(true);
	}
	
	@Override
	public void handleEvent(EvtEvent evt) {
		boolean wait = false;
		if (evt.getType().equals(DbController.DB_STARTED) && evt.getValue().toString().equals("true")) {
			Messenger.getInstance().debug(this,"Checking if demo data already generated ... ");
			getParentRequest.setLimit(1);
			getParentRequest.addSubscriber(this);
			DbRequestQueue.getInstance().addRequest(getParentRequest,this);
			wait = true;
		} else if (getParentRequest!=null && evt.getValue()==getParentRequest) {
			if (getParentRequest.hasError()) {
				Messenger.getInstance().error(this,"Error while executing get request: " + getParentRequest.getErrors().get(0).getMessage());
				setDone(true);
			} else if (getParentRequest.getObjects().size()>0) {
				Messenger.getInstance().debug(this,"Demo data already generated: " + getParentRequest.getCount());
				setDone(true);
			} else {
				boolean generate = GuiController.getInstance().showConfirmMsg("Do you want to generate the initial data?","Generate initial data?",true);
				if (generate) {
					Messenger.getInstance().debug(this,"Generating demo data ...");
					GuiController.getInstance().setProgressFrameTitle("Generating demo data ...");
					GuiController.getInstance().setProgressFrameTodo((GENERATE_PARENTS + (GENERATE_PARENTS * GENERATE_CHILDREN_PER_PARENT)));
					ReqAdd addRequest = null;
					for (int i = 0; i<GENERATE_PARENTS; i++) {
						if (i%1000==0) {
							addRequest = new ReqAdd(DemoModel.DEMO_PARENT_FULL_NAME);
							addRequest.addSubscriber(this);
							addParentRequests.add(addRequest);
						}
						addRequest.getObjects().add(new ReqDataObject(createParentObject((i + 1))));
					}
					if (addParentRequests.size()>0) {
						lockMe(this);
						waitingForRequests++;
						unlockMe(this);
						Messenger.getInstance().debug(this,"Add parent requests: " + addParentRequests.size());
						addParentRequest = addParentRequests.get(addParentRequestIndex);
						DbRequestQueue.getInstance().addRequest(addParentRequest,this);
					} else {
						setDone(true);
					}
				} else {
					setDone(true);
				}
			}
		} else if (addParentRequest!=null && evt.getValue()==addParentRequest) {
			if (!addParentRequest.hasError()) {
				GuiController.getInstance().incrementProgressFrameDone(addParentRequest.getImpactedIds().size());
				
				ReqAdd addRequest = new ReqAdd(DemoModel.DEMO_CHILD_FULL_NAME);
				addRequest.addSubscriber(this);
				for (long parentId: addParentRequest.getImpactedIds()) {
					for (int i = 0; i<GENERATE_CHILDREN_PER_PARENT; i++) {
						addRequest.getObjects().add(new ReqDataObject(createChildObject(parentId,(i + 1))));
					}
				}
				
				addParentRequestIndex++;
				Messenger.getInstance().debug(this,"Remaining parent requests: " + (addParentRequests.size() - addParentRequestIndex));
				if (addParentRequestIndex<addParentRequests.size()) {
					addParentRequest = addParentRequests.get(addParentRequestIndex);
					DbRequestQueue.getInstance().addRequest(addParentRequest,this);
				} else {
					lockMe(this);
					waitingForRequests--;
					unlockMe(this);
				}
				
				lockMe(this);
				waitingForRequests++;
				unlockMe(this);
				DbRequestQueue.getInstance().addRequest(addRequest,this);
			} else {
				Messenger.getInstance().error(this,"Error while executing add request: " + addParentRequest.getErrors().get(0).getMessage());
				setDone(true);
			}
		} else if (evt.getValue()!=null && evt.getValue() instanceof ReqAdd) {
			ReqAdd addRequest = (ReqAdd) evt.getValue();
			if (!addRequest.hasError()) {
				GuiController.getInstance().incrementProgressFrameDone(addRequest.getImpactedIds().size());
				lockMe(this);
				waitingForRequests--;
				if (waitingForRequests==0) {
					done = true;
				}
				unlockMe(this);
			} else {
				Messenger.getInstance().error(this,"Error while executing add request: " + addRequest.getErrors().get(0).getMessage());
				setDone(true);
			}
		}
			
		if (wait) { 
			while(!isDone()) {
				try {
					GuiController.getInstance().refreshProgressFrame();
					Thread.sleep(500);
				} catch (InterruptedException e) {
					Messenger.getInstance().error(this,"Generating demo data was interrupted");
				}
				
			}
			Messenger.getInstance().debug(this,"Generated demo data");
		}
	}
	
	private DbDataObject createParentObject(int num) {
		DbDataObject parentObj = new DbDataObject();
		StringBuilder demoStringShort = new StringBuilder("String value ");
		demoStringShort.append(num);
		StringBuilder demoStringLong = new StringBuilder();
		for (int b = 0; b < 200; b++) {
			demoStringLong.append("bla ");
			if (b > 0 && b % 15 == 0) {
				demoStringLong.append("\n");
			}
		}
		StringBuilder demoNumber = new StringBuilder("" + num);
		parentObj.setPropertyValue("demoStringLong",demoStringLong);
		parentObj.setPropertyValue("demoNumber",demoNumber);
		parentObj.setPropertyValue("demoStringShort",demoStringShort);
		return parentObj;
	}

	private DbDataObject createChildObject(long parentId,int num) {
		DbDataObject childObj = new DbDataObject();
		StringBuilder demoStringShort = new StringBuilder("String value ");
		demoStringShort.append(num);
		childObj.setLinkValue("demoLink",parentId);
		childObj.setPropertyValue("demoStringShort",demoStringShort);
		return childObj;
	}
	
	private void setDone(boolean done) {
		lockMe(this);
		this.done = done;
		unlockMe(this);
	}

	private boolean isDone() {
		boolean r = false;
		lockMe(this);
		r = done;
		unlockMe(this);
		return r;
	}
}

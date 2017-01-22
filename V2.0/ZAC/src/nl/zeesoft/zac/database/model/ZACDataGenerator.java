package nl.zeesoft.zac.database.model;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.gui.GuiController;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqObject;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventExceptionSubscriber;

public class ZACDataGenerator extends Locker implements EvtEventExceptionSubscriber {
	private ReqAdd 				addModuleRequest					= new ReqAdd(ZACModel.MODULE_CLASS_FULL_NAME);
	private ReqAdd 				addRequest							= null;
	private List<ReqAdd>		addRequests							= null;
	private int					addRequestIndex						= 0;

	private boolean 			done	 							= false;
	
	private boolean				installDemoData						= false;

	public void confirmInstallDemoData() {
		installDemoData = GuiController.getInstance().showConfirmMsg("Do you want to install the demo data?","Install default demo data?", installDemoData);
	}
	
	@Override
	public void handleEventException(EvtEvent evt, Exception ex) {
		setDone(true);
	}
	
	@Override
	public final void handleEvent(EvtEvent evt) {
		if (evt.getType().equals(DbController.DB_INITIALIZED_MODEL) && evt.getValue().toString().equals("true")) {
			generateInitialData();
		} else if (evt.getValue()==addModuleRequest) {
			if (!addModuleRequest.hasError()) {
				addRequests = getTrainingSequenceRequests(addModuleRequest.getImpactedIds().get(0));
				addRequests.addAll(getTestSequenceRequests(addModuleRequest.getImpactedIds().get(0)));
				addRequestIndex = 0;
				if (addRequests.size()>0) {
					addRequest = addRequests.get(addRequestIndex);
					DbRequestQueue.getInstance().addRequest(addRequest,this);
				} else {
					setDone(true);
				}
			} else {
				Messenger.getInstance().error(this,"Error executing add request: " + addModuleRequest.getErrors().get(0).getMessage());
				setDone(true);
			}
		} else if (addRequest!=null && evt.getValue()==addRequest) {
			if (!addRequest.hasError()) {
				if (addRequests.size()>(addRequestIndex + 1)) {
					addRequestIndex++;
					addRequest = addRequests.get(addRequestIndex);
					DbRequestQueue.getInstance().addRequest(addRequest,this);
				} else {
					setDone(true);
				}
			} else {
				Messenger.getInstance().error(this,"Error executing add request: " + addRequest.getErrors().get(0).getMessage());
				setDone(true);
			}
		} else if (evt.getValue()!=null && evt.getValue() instanceof ReqObject) {
			handleRequestEvent((ReqObject) evt.getValue(),evt);
		}
	}
	
	protected void handleRequestEvent(ReqObject request,EvtEvent evt) {
		// Override to implement
	}
	
	protected void generateInitialData() {
		if (installDemoData()) {
			Messenger.getInstance().debug(this,"Generating demo data ...");
			setDone(false);
			addModuleRequest.addSubscriber(this);
			addModuleRequest.getObjects().add(new ReqDataObject(createModuleObject("Demo").toDataObject()));
			DbRequestQueue.getInstance().addRequest(addModuleRequest,this);
			waitTillDone("Generating initial data was interrupted");
			Messenger.getInstance().debug(this,"Generated demo data");
		}
	}
	
	protected boolean installDemoData() {
		return installDemoData;
	}

	protected Module createModuleObject(String name) {
		Module obj = new Module();
		obj.setName(name);
		return obj;
	}

	protected List<ReqAdd> getTrainingSequenceRequests(long moduleId) {
		List<ReqAdd> addRequests = new ArrayList<ReqAdd>();
		addTrainingSequenceRequest(addRequests,moduleId,
			"What is your name? My name is Dyz Lecticus.",
			"I","Self","My","Name","Dyz","Lecticus","",""
			);
		addTrainingSequenceRequest(addRequests,moduleId,
			"What are you? I am an artificial cognition.",
			"I","Self","Am","Artificial","Cognition","","",""
			);
		addTrainingSequenceRequest(addRequests,moduleId,
			"Who created you? I was created by Andre van der Zee.",
			"I","Self","Creator","","","","",""
			);
		return addRequests;
	}
	
	protected List<ReqAdd> getTestSequenceRequests(long moduleId) {
		List<ReqAdd> addRequests = new ArrayList<ReqAdd>();
		addTestSequenceRequest(addRequests,moduleId,SymbolSequenceTest.CONFABULATE_CONTEXT,
			"What is your name?",
			"","","","","","","",""
			);
		addTestSequenceRequest(addRequests,moduleId,SymbolSequenceTest.CONFABULATE_CORRECT,
			"What is your bla?",
			"","","","","","","",""
			);
		addTestSequenceRequest(addRequests,moduleId,SymbolSequenceTest.CONFABULATE_EXTEND,
			"What is your name?",
			"","","","","","","",""
			);
		addTestSequenceRequest(addRequests,moduleId,SymbolSequenceTest.CONFABULATE_EXTEND,
			"What are you?",
			"","","","","","","",""
			);
		addTestSequenceRequest(addRequests,moduleId,SymbolSequenceTest.CONFABULATE_EXTEND,
			"What is your name?",
			"Name","","","","","","",""
			);
		addTestSequenceRequest(addRequests,moduleId,SymbolSequenceTest.CONFABULATE_EXTEND,
			"What is your name?",
			"Artificial","Cognition","","","","","",""
			);
		return addRequests;
	}

	protected void addTrainingSequenceRequest(List<ReqAdd> addRequests, long moduleId, String sequence, String contextSymbol1, String contextSymbol2, String contextSymbol3, String contextSymbol4, String contextSymbol5, String contextSymbol6, String contextSymbol7, String contextSymbol8) {
		ReqAdd addRequest = new ReqAdd(ZACModel.SYMBOL_SEQUENCE_TRAINING_CLASS_FULL_NAME);
		addRequest.addSubscriber(this);
		addRequest.getObjects().add(new ReqDataObject(createTrainingSequenceObject(moduleId,sequence,contextSymbol1,contextSymbol2,contextSymbol3,contextSymbol4,contextSymbol5,contextSymbol6,contextSymbol7,contextSymbol8).toDataObject()));
		addRequests.add(addRequest);
	}
	
	protected SymbolSequenceTraining createTrainingSequenceObject(long moduleId, String sequence, String contextSymbol1, String contextSymbol2, String contextSymbol3, String contextSymbol4, String contextSymbol5, String contextSymbol6, String contextSymbol7, String contextSymbol8) {
		SymbolSequenceTraining obj = new SymbolSequenceTraining();
		obj.setModuleId(moduleId);
		obj.setSequence(new StringBuilder(sequence));
		obj.setContextSymbol1(contextSymbol1);
		obj.setContextSymbol2(contextSymbol2);
		obj.setContextSymbol3(contextSymbol3);
		obj.setContextSymbol4(contextSymbol4);
		obj.setContextSymbol5(contextSymbol5);
		obj.setContextSymbol6(contextSymbol6);
		obj.setContextSymbol7(contextSymbol7);
		obj.setContextSymbol8(contextSymbol8);
		return obj;
	}

	protected void addTestSequenceRequest(List<ReqAdd> addRequests, long moduleId, String type, String sequence, String contextSymbol1, String contextSymbol2, String contextSymbol3, String contextSymbol4, String contextSymbol5, String contextSymbol6, String contextSymbol7, String contextSymbol8) {
		ReqAdd addRequest = new ReqAdd(ZACModel.SYMBOL_SEQUENCE_TEST_CLASS_FULL_NAME);
		addRequest.addSubscriber(this);
		addRequest.getObjects().add(new ReqDataObject(createTestSequenceObject(moduleId,type,sequence,contextSymbol1,contextSymbol2,contextSymbol3,contextSymbol4,contextSymbol5,contextSymbol6,contextSymbol7,contextSymbol8).toDataObject()));
		addRequests.add(addRequest);
	}
	
	protected SymbolSequenceTest createTestSequenceObject(long moduleId, String type, String sequence, String contextSymbol1, String contextSymbol2, String contextSymbol3, String contextSymbol4, String contextSymbol5, String contextSymbol6, String contextSymbol7, String contextSymbol8) {
		SymbolSequenceTest obj = new SymbolSequenceTest();
		obj.setModuleId(moduleId);
		obj.setType(type);
		obj.setSequence(new StringBuilder(sequence));
		obj.setContextSymbol1(contextSymbol1);
		obj.setContextSymbol2(contextSymbol2);
		obj.setContextSymbol3(contextSymbol3);
		obj.setContextSymbol4(contextSymbol4);
		obj.setContextSymbol5(contextSymbol5);
		obj.setContextSymbol6(contextSymbol6);
		obj.setContextSymbol7(contextSymbol7);
		obj.setContextSymbol8(contextSymbol8);
		return obj;
	}

	protected void setDone(boolean done) {
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
	
	protected void waitTillDone(String interruptedMessage) {
		while(!isDone()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Messenger.getInstance().error(this,interruptedMessage);
			}
		}
	}
}

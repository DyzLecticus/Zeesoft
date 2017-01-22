package nl.zeesoft.zids.server;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zacs.database.model.Example;
import nl.zeesoft.zids.database.model.Dialog;
import nl.zeesoft.zids.database.model.DialogExample;
import nl.zeesoft.zids.database.model.DialogVariable;
import nl.zeesoft.zids.database.model.DialogVariableExample;
import nl.zeesoft.zids.database.model.VariableType;
import nl.zeesoft.zids.database.model.ZIDSModel;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.model.helpers.HlpGetControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqObject;
import nl.zeesoft.zodb.database.request.ReqRemove;
import nl.zeesoft.zodb.event.EvtEvent;

public class SvrControllerDialogs extends HlpGetControllerObject {
	private static SvrControllerDialogs		controller							= null;
	
	private ReqGet 							getVariableTypeRequest				= new ReqGet(ZIDSModel.VARIABLE_TYPE_CLASS_FULL_NAME);
	private ReqGet 							getDialogExampleRequest				= new ReqGet(ZIDSModel.DIALOG_EXAMPLE_CLASS_FULL_NAME);
	private ReqGet 							getDialogVariableRequest			= new ReqGet(ZIDSModel.DIALOG_VARIABLE_CLASS_FULL_NAME);
	private ReqGet 							getDialogVariableExampleRequest		= new ReqGet(ZIDSModel.DIALOG_VARIABLE_EXAMPLE_CLASS_FULL_NAME);
	
	private SortedMap<Long,VariableType>	variableTypes						= new TreeMap<Long,VariableType>();
	private SortedMap<Long,DialogVariable>	dialogVariables						= new TreeMap<Long,DialogVariable>(); 
	
	private ReqRemove 						removeExampleRequest				= new ReqRemove(ZIDSModel.EXAMPLE_CLASS_FULL_NAME);
	private ReqAdd 							addExampleRequest					= new ReqAdd(ZIDSModel.EXAMPLE_CLASS_FULL_NAME);
	private SortedMap<String,Example>		addExamples							= new TreeMap<String,Example>(); 

	private SvrControllerDialogs() {
		super(ZIDSModel.DIALOG_CLASS_FULL_NAME);
	}
	
	public static SvrControllerDialogs getInstance() {
		if (controller==null) {
			controller = new SvrControllerDialogs();
		}
		return controller;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	public List<Dialog> getDialogsAsList() {
		List<Dialog> r = new ArrayList<Dialog>();
		for (HlpObject obj: getObjectsAsList()) {
			r.add((Dialog) obj);
		}
		return r;
	}

	public Dialog getDialogById(long id) {
		Dialog r = null;
		HlpObject obj = getObjectById(id);
		if (obj!=null) {
			r = (Dialog) obj;
		}
		return r;
	}

	public void regenerateExamples() {
		Messenger.getInstance().debug(this,"Generating examples ...");
		setDone(false);
		removeExampleRequest = new ReqRemove(ZIDSModel.EXAMPLE_CLASS_FULL_NAME);
		removeExampleRequest.addSubscriber(this);
		DbRequestQueue.getInstance().addRequest(removeExampleRequest,this);
		waitTillDone();
		Messenger.getInstance().debug(this,"Generated examples: " + addExamples.size());
	}

	@Override
	public void initialize() {
		SvrControllerDatabase.getInstance().initializePatternManager();
		Messenger.getInstance().debug(this,"Initializing dialogs ...");
		super.initialize();
		Messenger.getInstance().debug(this,"Loaded dialogs: " + getObjectsAsList().size());
		setDone(false);
		
		getVariableTypeRequest.addSubscriber(this);
		getVariableTypeRequest.getProperties().add(ReqGet.ALL_PROPERTIES);
		DbRequestQueue.getInstance().addRequest(getVariableTypeRequest,this);

		waitTillDone();
		Messenger.getInstance().debug(this,"Initialized dialogs");
	}

	@Override
	public void reinitialize() {
		variableTypes.clear();
		dialogVariables.clear();
		getDialogExampleRequest = new ReqGet(ZIDSModel.DIALOG_EXAMPLE_CLASS_FULL_NAME);
		getDialogVariableRequest = new ReqGet(ZIDSModel.DIALOG_VARIABLE_CLASS_FULL_NAME);
		getDialogVariableExampleRequest = new ReqGet(ZIDSModel.DIALOG_VARIABLE_EXAMPLE_CLASS_FULL_NAME);
		super.reinitialize();
	}

	@Override
	public void handleEvent(EvtEvent e) {
		super.handleEvent(e);
		if (e.getType().equals(DbController.DB_STARTED) && e.getValue().toString().equals("true")) {
			initialize();
			regenerateExamples();
		} else if (e.getType().equals(DbController.DB_UPDATED_MODEL)) {
			reinitialize();
			regenerateExamples();
		} else if (e.getValue()!=null && e.getValue() instanceof ReqObject) {
			ReqObject request = (ReqObject) e.getValue();
			if (request.hasError()) {
				Messenger.getInstance().error(this,"Error executing request: " + request.getErrors().get(0).getMessage());
				setDone(true);
			} else if (e.getValue()==getVariableTypeRequest) {
				for (ReqDataObject object: getVariableTypeRequest.getObjects()) {
					VariableType vt = new VariableType();
					vt.fromDataObject(object.getDataObject());
					variableTypes.put(vt.getId(),vt);
				}
				Messenger.getInstance().debug(this,"Loaded variable types: " + variableTypes.size());
				getDialogExampleRequest.getProperties().add(ReqGet.ALL_PROPERTIES);
				getDialogExampleRequest.addSubscriber(this);
				DbRequestQueue.getInstance().addRequest(getDialogExampleRequest,this);
			} else if (e.getValue()==getDialogExampleRequest) {
				for (ReqDataObject object: getDialogExampleRequest.getObjects()) {
					DialogExample de = new DialogExample();
					de.fromDataObject(object.getDataObject());
					Dialog d = getDialogById(de.getDialogId());
					if (d!=null) {
						d.getExamples().add(de);
					}
				}
				Messenger.getInstance().debug(this,"Loaded dialog examples: " + getDialogExampleRequest.getObjects().size());
				getDialogVariableRequest.getProperties().add(ReqGet.ALL_PROPERTIES);
				getDialogVariableRequest.addSubscriber(this);
				DbRequestQueue.getInstance().addRequest(getDialogVariableRequest,this);
			} else if (e.getValue()==getDialogVariableRequest) {
				for (ReqDataObject object: getDialogVariableRequest.getObjects()) {
					DialogVariable dv = new DialogVariable();
					dv.fromDataObject(object.getDataObject());
					dv.setType(variableTypes.get(dv.getTypeId()));
					dialogVariables.put(dv.getId(),dv);
					Dialog d = getDialogById(dv.getDialogId());
					if (d!=null) {
						d.getVariables().add(dv);
					}
				}
				Messenger.getInstance().debug(this,"Loaded dialog variables: " + getDialogVariableRequest.getObjects().size());
				getDialogVariableExampleRequest.getProperties().add(ReqGet.ALL_PROPERTIES);
				getDialogVariableExampleRequest.addSubscriber(this);
				DbRequestQueue.getInstance().addRequest(getDialogVariableExampleRequest,this);
			} else if (e.getValue()==getDialogVariableExampleRequest) {
				for (ReqDataObject object: getDialogVariableExampleRequest.getObjects()) {
					DialogVariableExample dve = new DialogVariableExample();
					dve.fromDataObject(object.getDataObject());
					DialogVariable dv = dialogVariables.get(dve.getVariableId());
					if (dv!=null) {
						dv.getExamples().add(dve);
					}
				}
				Messenger.getInstance().debug(this,"Loaded dialog variable examples: " + getDialogVariableExampleRequest.getObjects().size());
				setDone(true);
			} else if (e.getValue()==removeExampleRequest) {
				addExamples.clear();
				for (Dialog d: getDialogsAsList()) {
					for (DialogExample de: d.getExamples()) {
						addExampleToAddExamples(new StringBuilder(d.getContextSymbol()),de.getInput(),de.getOutput());
					}
					for (DialogVariable dv: d.getVariables()) {
						StringBuilder context = new StringBuilder();
						context.append(dv.getContextSymbol());
						if (d.getContextSymbol().length()>0) {
							if (context.length()>0) {
								context.append(" ");
							}
							context.append(d.getContextSymbol());
						}
						addExampleToAddExamples(context,new StringBuilder(dv.getPromptInput()),dv.getPrompt1());
						addExampleToAddExamples(context,new StringBuilder(dv.getPromptInput()),dv.getPrompt2());
						addExampleToAddExamples(context,new StringBuilder(dv.getPromptInput()),dv.getPrompt3());
						for (DialogVariableExample dve: dv.getExamples()) {
							addExampleToAddExamples(context,dve.getInput(),dve.getOutput());
						}
					}
				}
				if (addExamples.size()>0) {
					addExampleRequest = new ReqAdd(ZIDSModel.EXAMPLE_CLASS_FULL_NAME);
					for (Example ex: addExamples.values()) {
						addExampleRequest.getObjects().add(new ReqDataObject(ex.toDataObject()));
					}
					addExampleRequest.addSubscriber(this);
					DbRequestQueue.getInstance().addRequest(addExampleRequest,this);
				} else {
					setDone(true);
				}
			} else if (e.getValue()==addExampleRequest) {
				setDone(true);
			}
		}
	}

	@Override
	protected HlpObject getNewObject() {
		return new Dialog();
	}
	
	private void addExampleToAddExamples(StringBuilder context, StringBuilder input, StringBuilder output) {
		if (context.length()<1024 && input.length()<1024 && output.length()<4096) {
			String io = input.toString() + output.toString();
			if (!addExamples.containsKey(io)) {
				Example ex = new Example();
				ex.setContext(context);
				ex.setInput(input);
				ex.setOutput(output);
				addExamples.put(io,ex);
			}
		}
	}
}

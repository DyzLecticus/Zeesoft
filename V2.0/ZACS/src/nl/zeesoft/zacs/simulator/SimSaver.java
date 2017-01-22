package nl.zeesoft.zacs.simulator;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zacs.database.model.ContextLink;
import nl.zeesoft.zacs.database.model.Module;
import nl.zeesoft.zacs.database.model.ModuleSymbol;
import nl.zeesoft.zacs.database.model.Symbol;
import nl.zeesoft.zacs.database.model.SymbolLink;
import nl.zeesoft.zacs.database.model.ZACSModel;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.gui.GuiController;
import nl.zeesoft.zodb.database.model.helpers.HlpControllerObject;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqObject;
import nl.zeesoft.zodb.database.request.ReqRemove;
import nl.zeesoft.zodb.database.request.ReqUpdate;
import nl.zeesoft.zodb.event.EvtEvent;

public class SimSaver extends HlpControllerObject {
	private List<ReqObject>					executingRequests			= new ArrayList<ReqObject>();
	
	private List<ReqRemove>					removeRequests				= new ArrayList<ReqRemove>();
	private List<ReqAdd>					addRequests					= new ArrayList<ReqAdd>();
	private List<ReqUpdate>					updateRequests				= new ArrayList<ReqUpdate>();

	private int								removeRequestIndex			= 0;
	private int								addRequestIndex				= 0;
	private int								updateRequestIndex			= 0;
	
	private List<Long>						removedSymbolLinkIdList		= new ArrayList<Long>();
	private List<Long>						addedSymbolLinkIdList		= new ArrayList<Long>();

	private List<Long>						removedContextLinkIdList	= new ArrayList<Long>();
	private List<Long>						addedContextLinkIdList		= new ArrayList<Long>();

	private int								doneCounter					= 0;
	private int								todoCounter					= 0;
	
	@Override
	protected void initialize() {
		doneCounter	= 0;
		todoCounter	= 0;
	}

	@Override
	public void handleEventException(EvtEvent evt, Exception ex) {
		setDone(true);
	}
	
	@Override
	public void handleEvent(EvtEvent e) {
		resetTimeOut();
		lockMe(this);
		List<ReqObject> addQueueRequests = new ArrayList<ReqObject>();
		ReqObject executedRequest = null;
		for (ReqObject execReq: executingRequests) {
			if (execReq==e.getValue()) {
				executedRequest = execReq;
				break;
			}
		}
		if (executedRequest!=null) {
			executingRequests.remove(executedRequest);
			doneCounter++;
			if (doneCounter<=todoCounter) {
				GuiController.getInstance().incrementProgressFrameDone();
			}
		}
		while (executingRequests.size()<10) {
			if (executedRequest instanceof ReqRemove) {
				//removeRequestIndex++;
			} else if (executedRequest instanceof ReqAdd) {
				//addRequestIndex++;
				ReqAdd addRequest = (ReqAdd) executedRequest;
				for (long id: addRequest.getImpactedIds()) {
					if (addRequest.getClassName().equals(ZACSModel.SYMBOL_LINK_CLASS_FULL_NAME)) {
						addedSymbolLinkIdList.add(id);
					} else if (addRequest.getClassName().equals(ZACSModel.CONTEXT_LINK_CLASS_FULL_NAME)) {
						addedContextLinkIdList.add(id);
					}
				}
			} else if (executedRequest instanceof ReqUpdate) {
				//updateRequestIndex++;
			}
			ReqObject newRequest = null;
			if (removeRequestIndex<removeRequests.size()) {
				newRequest = removeRequests.remove(removeRequestIndex);
				if (newRequest.check(this)) {
					addQueueRequests.add(newRequest);
					executingRequests.add(newRequest);
				}
			} else if (addRequestIndex<addRequests.size()) {
				newRequest = addRequests.remove(addRequestIndex);
				if (newRequest.check(this)) {
					addQueueRequests.add(newRequest);
					executingRequests.add(newRequest);
				}
			} else if (updateRequestIndex<updateRequests.size()) {
				newRequest = updateRequests.remove(updateRequestIndex);
				if (newRequest.check(this)) {
					addQueueRequests.add(newRequest);
					executingRequests.add(newRequest);
				}
			} else {
				break;
			}
			if (newRequest!=null) {
				executedRequest = newRequest;
			}
		}
		boolean done = false;
		//Messenger.getInstance().debug(this,"Executing requests: " + executingRequests.size() + ", remaining: " + (todoCounter - doneCounter));
		if (executingRequests.size()==0) {
			done = true;
		}
		unlockMe(this);
		for (ReqObject addQueueRequest: addQueueRequests) {
			DbRequestQueue.getInstance().addRequest(addQueueRequest,this);
		}
		if (done) {
			addRequests.clear();
			updateRequests.clear();
			removeRequests.clear();
			removedSymbolLinkIdList.clear();
			removedContextLinkIdList.clear();
			setDone(true);
		}
	}

	protected void saveState(boolean stoppingDatabase,boolean saveModuleSymbolState) {
		Messenger.getInstance().debug(this,"Saving simulation state ...");
		if (stoppingDatabase) {
			GuiController.getInstance().setProgressFrameTitle("Saving simulation state ...");
		}

		addedSymbolLinkIdList.clear();
		addedContextLinkIdList.clear();
		
		addRequests.clear();
		updateRequests.clear();
		removeRequests.clear();
		executingRequests.clear();
		int totalSymbolLinks = 0;
		int addSymbolLinks = 0;
		int removeSymbolLinks = 0;
		int totalContextLinks = 0;
		int addContextLinks = 0;
		int removeContextLinks = 0;
		
		if (removedSymbolLinkIdList.size()>0) {
			for (long id: removedSymbolLinkIdList) {
				totalSymbolLinks++;
				removeSymbolLinks++;
				ReqRemove removeRequest = new ReqRemove(ZACSModel.SYMBOL_LINK_CLASS_FULL_NAME,id);
				removeRequest.addSubscriber(this);
				removeRequests.add(removeRequest);
			}
		}
		resetTimeOut();
		
		if (removedContextLinkIdList.size()>0) {
			for (long id: removedContextLinkIdList) {
				totalContextLinks++;
				removeContextLinks++;
				ReqRemove removeRequest = new ReqRemove(ZACSModel.CONTEXT_LINK_CLASS_FULL_NAME,id);
				removeRequest.addSubscriber(this);
				removeRequests.add(removeRequest);
			}
		}
		resetTimeOut();
		
		int totalSymbols = 0;
		for (Symbol symbol : SimController.getInstance().getSymbols().getSymbolsAsList()) {
			if (saveModuleSymbolState) {
				totalSymbols++;
				updateRequests.add(symbol.getNewUpdateRequest(this));
			}
			
			ReqAdd addRequest = new ReqAdd(ZACSModel.SYMBOL_LINK_CLASS_FULL_NAME);
			addRequest.addSubscriber(this);
			for (SymbolLink link: symbol.getSymbolLinks()) {
				totalSymbolLinks++;
				if (link.getId()==0) {
					addSymbolLinks++;
					if (addRequest.getObjects().size()>=1000) {
						addRequest = new ReqAdd(ZACSModel.SYMBOL_LINK_CLASS_FULL_NAME);
						addRequest.addSubscriber(this);
						addRequests.add(addRequest);
					}
					addRequest.getObjects().add(new ReqDataObject(link.toDataObject()));
				} else {
					updateRequests.add(link.getNewUpdateRequest(this));
				}
			}
			if (addRequest.getObjects().size()>0) {
				addRequests.add(addRequest);
			}
			
			addRequest = new ReqAdd(ZACSModel.CONTEXT_LINK_CLASS_FULL_NAME);
			addRequest.addSubscriber(this);
			for (ContextLink link: symbol.getContextLinks()) {
				totalContextLinks++;
				if (link.getId()==0) {
					addContextLinks++;
					if (addRequest.getObjects().size()>=1000) {
						addRequest = new ReqAdd(ZACSModel.CONTEXT_LINK_CLASS_FULL_NAME);
						addRequest.addSubscriber(this);
						addRequests.add(addRequest);
					}
					addRequest.getObjects().add(new ReqDataObject(link.toDataObject()));
				} else {
					updateRequests.add(link.getNewUpdateRequest(this));
				}
			}
			if (addRequest.getObjects().size()>0) {
				addRequests.add(addRequest);
			}
		}
		resetTimeOut();
		if (saveModuleSymbolState) {
			for (Module module : SimController.getInstance().getModules().getModulesAsList()) {
				updateRequests.add(module.getNewUpdateRequest(this));
				for (ModuleSymbol modSym: module.getSymbols()) {
					updateRequests.add(modSym.getNewUpdateRequest(this));
				}
			}
		}
		resetTimeOut();

		if (saveModuleSymbolState) {
			Messenger.getInstance().debug(this,"Total symbols: " + totalSymbols);
		}
		Messenger.getInstance().debug(this,"Total symbol links: " + totalSymbolLinks + " (add: " + addSymbolLinks + ", remove: " + removeSymbolLinks + ")");
		Messenger.getInstance().debug(this,"Total context links: " + totalContextLinks + "  (add: " + addContextLinks + ", remove: " + removeContextLinks + ")");
		if (saveModuleSymbolState) {
			Messenger.getInstance().debug(this,"Total modules: " + ZACSModel.getNumberOfModules() + " (module symbols: " + (totalSymbols * ZACSModel.getNumberOfModules()) + ")");
		}
		
		todoCounter = removeRequests.size() + updateRequests.size() + addRequests.size();
		if (todoCounter>0) {
			doneCounter = 0;
			if (stoppingDatabase) {
				GuiController.getInstance().setProgressFrameTodo(todoCounter);
			}
			setDone(false);
			removeRequestIndex = 0;
			addRequestIndex = 0;
			updateRequestIndex = 0;
			if (removeRequests.size()>0) {
				executingRequests.add(removeRequests.get(0));
			} else if (addRequests.size()>0) {
				executingRequests.add(addRequests.get(0));
			} else if (updateRequests.size()>0) {
				executingRequests.add(updateRequests.get(0));
			}
			DbRequestQueue.getInstance().addRequest(executingRequests.get(0),this);
			waitTillDone();
		}

		if (stoppingDatabase) {
			GuiController.getInstance().setProgressFrameTitle("Saved simulation state");
		}
		Messenger.getInstance().debug(this,"Saved simulation state");
	}

	protected List<Long> getAddedSymbolLinkIdList() {
		return addedSymbolLinkIdList;
	}
	
	protected List<Long> getAddedContextLinkIdList() {
		return addedContextLinkIdList;
	}
	
	protected void removedSymbolLink(long id) {
		lockMe(this);
		removedSymbolLinkIdList.add(id);
		unlockMe(this);
	}

	protected void removedContextLink(long id) {
		lockMe(this);
		removedContextLinkIdList.add(id);
		unlockMe(this);
	}
}

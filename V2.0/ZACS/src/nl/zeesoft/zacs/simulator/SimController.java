package nl.zeesoft.zacs.simulator;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zacs.database.model.Command;
import nl.zeesoft.zacs.database.model.Control;
import nl.zeesoft.zacs.database.model.StateHistory;
import nl.zeesoft.zacs.database.model.Symbol;
import nl.zeesoft.zacs.database.model.ZACSModel;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.gui.GuiController;
import nl.zeesoft.zodb.database.model.helpers.HlpControllerObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqUpdate;
import nl.zeesoft.zodb.event.EvtEvent;

public class SimController extends HlpControllerObject {
	public final static boolean				DEBUG_PERFORMANCE	= false;
	
	private static SimController			controller			= null;

	private SimControllerLocker				locker				= new SimControllerLocker();
	private SimControllerCommandWorker		commander			= null;
	private SimControllerReactivateWorker	reactivator			= null;
	
	private Control 						control				= null;

	private SimControllerCommands			commands			= null; 
	private SimControllerSymbols			symbols				= null; 
	private SimControllerModules			modules				= null;
	private SimControllerExamples			examples			= null;
	private SimControllerAssignments		assignments			= null;
	
	private SimSaver						saver				= new SimSaver();
	
	private ReqGet							getRequest			= null;
	private ReqUpdate						updateRequest		= null;
	private boolean							updateCommandOnly	= false;
	
	private SimAssignmentHandler 			assignmentHandler	= null;
		
	private SimController() {
		setTimeOutSeconds(120);
	}

	public static SimController getInstance() {
		if (controller==null) {
			controller = new SimController();
		}
		return controller;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getType().equals(DbController.DB_STARTED) && e.getValue().toString().equals("true")) {
			initialize();
			if (getControl()!=null) { 
				commander.start();
				SimStateHistoryWorker.getInstance().start();
				if (getControl().isAutoActivate() && !isActive()) {
					start();
				}
			}
		} else if (e.getType().equals(DbController.DB_UPDATING_MODEL)) {
			if (getControl()!=null) { 
				commander.stop();
				if (isActive()) {
					stop(false);
				}
				SimStateHistoryWorker.getInstance().stop();
			}
		} else if (e.getType().equals(DbController.DB_UPDATED_MODEL)) {
			if (getControl()!=null) { 
				commander.start();
				SimStateHistoryWorker.getInstance().start();
				if (getControl().isAutoActivate() && !isActive()) {
					start();
				}
			}
		} else if (e.getType().equals(DbController.DB_STOPPING)) {
			if (getControl()!=null) { 
				if (commander!=null) {
					commander.stop();
				}
				if (getControl()!=null && isActive()) {
					stop(true);
				}
				SimStateHistoryWorker.getInstance().stop();
			}
		} else if (getRequest!=null && e.getValue()==getRequest) {
			if (getRequest.getClassName().equals(ZACSModel.CONTROL_CLASS_FULL_NAME)) {
				if (getRequest.getObjects().size()>0) {
					lockMe(this);
					Control c = new Control();
					c.fromDataObject(getRequest.getObjects().get(0).getDataObject());
					if (c.getCommandId()>0 && commands!=null) {
						Command command = (Command) commands.getObjectById(c.getCommandId());
						if (command!=null) {
							c.setCommand(command);
						} else {
							c.setCommand(null);
						}
					}
					if (updateCommandOnly) {
						control.setCommand(c.getCommand());
						control.setCommandId(c.getCommandId());
						updateCommandOnly = false;
					} else {
						control = c;
					}
					unlockMe(this);
				} else {
					Messenger.getInstance().error(this,"No control object found");
				}
			}
			setDone(true);
		} else if (updateRequest!=null && e.getValue()==updateRequest) {
			if (updateRequest.getClassName().equals(ZACSModel.CONTROL_CLASS_FULL_NAME)) {
				// Ignore
			}
		}
	}
	
	public SimAssignmentHandler getAssignmentHandler() {
		Control c = getControl();
		if (assignmentHandler==null) {
			assignmentHandler = new SimAssignmentHandler(false);
			assignmentHandler.initializeParameters(c);
		}
		return assignmentHandler;
	}
	
	public Symbol getSymbolByCode(String code) {
		Symbol r = null;
		if (symbols!=null) {
			r = symbols.getSymbolByCode(code);
		}
		return r;
	}

	public Symbol getSymbolLikeCode(String code) {
		Symbol r = null;
		if (symbols!=null) {
			r = symbols.getSymbolLikeCode(code,true);
		}
		return r;
	}
	
	protected void removedSymbolLink(long id) {
		if (id>0) {
			saver.removedSymbolLink(id);
		}
	}

	protected void removedContextLink(long id) {
		if (id>0) {
			saver.removedContextLink(id);
		}
	}
	
	protected SimControllerCommands getCommands() {
		return commands;
	}

	protected SimControllerSymbols getSymbols() {
		return symbols;
	}

	protected SimControllerModules getModules() {
		return modules;
	}

	protected SimControllerExamples getExamples() {
		return examples;
	}
	
	protected SimControllerAssignments getAssignments() {
		return assignments;
	}
	
	protected Control getControl() {
		lockMe(this);
		Control r = control;
		unlockMe(this);
		return r;
	}

	// Called by state history worker 
	protected StateHistory getNewStateHistory() {
		StateHistory statHist = new StateHistory();
		statHist.setControlId(getControl().getId());
		statHist.setDateTime((new Date()).getTime());
		statHist.setStatus(getControl().getStatus());
		statHist.setStatTotalSymbols(symbols.getSize());
		statHist.setStatTotalSymLinks(symbols.getTotalSymbolLinks());
		statHist.setStatAvgSymLinkCount(symbols.getAverageSymbolLinkCount());
		statHist.setStatTotalSymbolLevel(modules.getTotalSymbolLevel());
		statHist.setStatTotalConLinks(symbols.getTotalContextLinks());
		statHist.setStatAvgConLinkCount(symbols.getAverageContextLinkCount());
		return statHist;
	}
	
	// Called by controller worker
	protected void getControllerCommand() {
		locker.lockSim(this);
		updateCommandOnly = true;
		getController();
		Control c = getControl();
		locker.unlockSim(this);
		if (c!=null && c.getCommand()!=null) {
			if (c.getCommand().getCode().equals(Command.ACTIVATE)) {
				if (!isActive()) {
					start();
				} else {
					resetControllerCommand();
				}
			} else if (c.getCommand().getCode().equals(Command.DEACTIVATE)) {
				if (isActive()) {
					stop(false);
				} else {
					resetControllerCommand();
				}
			} else if (c.getCommand().getCode().equals(Command.REACTIVATE)) {
				if (!reactivate()) {
					resetControllerCommand();
				}
			}
		}
	}

	// Called by controller reactivate worker
	protected boolean reactivate() {
		boolean reactivated = false;
		if (isActive()) {
			stop(false);
			reactivated = true;
		}
		if (!isActive()) {
			start();
		} else {
			reactivated = false;
		}
		return reactivated;
	}

	@Override
	protected void initialize() {
		Messenger.getInstance().debug(this,"Initializing simulation ...");
		GuiController.getInstance().setProgressFrameTitle("Initializing simulation ...");
		getController();
		
		if (getControl()!=null) {
			locker.lockSim(this);
			commander = new SimControllerCommandWorker();
			reactivator = new SimControllerReactivateWorker();
	
			// Initialize commands after getController to ensure control can be set
			commands = new SimControllerCommands(); 
			commands.initialize();
	
			// Reset status
			updateControllerStatus(Control.STATUS_INACTIVE);

			int todo = 2;
			if (DbConfig.getInstance().isShowGUI()) {
				SimControllerCounter counter = new SimControllerCounter();
				counter.initialize();
				todo += counter.getTotalCount();
			}
			GuiController.getInstance().setProgressFrameTodo(todo);
			resetTimeOut();

			initializeSymbols();
			resetTimeOut();
			initializeModules();
			resetTimeOut();
			GuiController.getInstance().incrementProgressFrameDone();

			symbols.getSymbolLinks();
			resetTimeOut();
			symbols.getContextLinks();
			resetTimeOut();
			symbols.setShowProgress(false);
			resetTimeOut();
			modules.getModuleSymbols();
			resetTimeOut();
			modules.setShowProgress(false);
			resetTimeOut();
			GuiController.getInstance().incrementProgressFrameDone();

			SimStateHistoryWorker.getInstance().initialize();

			assignments = new SimControllerAssignments();
			
			locker.unlockSim(this);
		}
		Messenger.getInstance().debug(this,"Initialized simulation");
	}

	private void start() {
		locker.lockSim(this);
		if (isActive()) {
			locker.unlockSim(this);
			return;
		}
		Messenger.getInstance().debug(this,"Starting simulation ...");
		updateControllerStatus(Control.STATUS_ACTIVATING);
		getController();
		// Start converting scraped text
		SimConvertScrapedTextWorker.getInstance().start();
		if (getControl().isLearnExamples()) {
			// (Re)Initialize examples
			initializeExamples();
			// Start learning examples
			SimLearnExamplesWorker.getInstance().start();
		}
		if (getControl().isDoAssignments()) {
			// Start doing assignments
			SimDoAssignmentsWorker.getInstance().start();
		}
		if (getControl().isAutoReactivate()) {
			// Start reactivator
			reactivator.start();
		}
		// Update status
		updateControllerStatus(Control.STATUS_ACTIVE);
		locker.unlockSim(this);
		Messenger.getInstance().debug(this,"Started simulation");
	}

	private void stop(boolean stoppingDatabase) {
		locker.lockSim(this);
		if (!isActive()) {
			locker.unlockSim(this);
			return;
		}
		Messenger.getInstance().debug(this,"Stopping simulation ...");
		updateControllerStatus(Control.STATUS_DEACTIVATING);
		// Stop reactivator
		reactivator.stop();
		// Stop converting scraped text
		SimConvertScrapedTextWorker.getInstance().stop();
		// Stop learning examples
		SimLearnExamplesWorker.getInstance().stop();
		// Stop doing assignments
		SimDoAssignmentsWorker.getInstance().stop();
		// Save state
		saver.saveState(stoppingDatabase,getControl().isSaveModuleSymbolState());
		List<Long> addedSymbolLinkIdList = saver.getAddedSymbolLinkIdList();
		List<Long> addedContextLinkIdList = saver.getAddedContextLinkIdList();
		if (!stoppingDatabase) {
			if (addedSymbolLinkIdList.size()>0) {
				Messenger.getInstance().debug(this,"Loading added symbol links ...");
				SimController.getInstance().getSymbols().getAddedSymbolLinks(addedSymbolLinkIdList);
				Messenger.getInstance().debug(this,"Loaded added symbol links");
			} 
			if (addedContextLinkIdList.size()>0) {
				Messenger.getInstance().debug(this,"Loading added context links ...");
				SimController.getInstance().getSymbols().getAddedContextLinks(addedContextLinkIdList);
				Messenger.getInstance().debug(this,"Loaded added context links");
			}
		}
		// Update status
		updateControllerStatus(Control.STATUS_INACTIVE);
		locker.unlockSim(this);
		Messenger.getInstance().debug(this,"Stopped simulation");
	}

	private void getController() {
		setDone(false);
		getControllerNoLock();
		waitTillDone();
	}

	private void initializeSymbols() {
		symbols	= new SimControllerSymbols(); 
		symbols.initialize();
	}
	
	private void initializeModules() {
		modules	= new SimControllerModules(); 
		modules.initialize();
	}

	private void initializeExamples() {
		examples = new SimControllerExamples();
		examples.initialize();
	}
	
	private void updateControllerStatus(String status) {
		lockMe(this);
		control.setCommandId(0);
		control.setStatus(status);
		updateControllerNoLock(control);
		unlockMe(this);
	}

	private void resetControllerCommand() {
		lockMe(this);
		control.setCommandId(0);
		updateControllerNoLock(control);
		unlockMe(this);
	}

	private boolean isActive() {
		lockMe(this);
		boolean r = isActiveNoLock();
		unlockMe(this);
		return r;
	}

	private boolean isActiveNoLock() {
		return !control.getStatus().equals(Control.STATUS_INACTIVE);
	}
	
	private void getControllerNoLock() {
		getRequest = new ReqGet(ZACSModel.CONTROL_CLASS_FULL_NAME);
		if (updateCommandOnly) {
			getRequest.getProperties().add("command");
		} else {
			getRequest.getProperties().add(ReqGet.ALL_PROPERTIES);
		}
		getRequest.addSubscriber(this);
		DbRequestQueue.getInstance().addRequest(getRequest,this);
	}
	
	private void updateControllerNoLock(Control c) {
		updateRequest = new ReqUpdate(ZACSModel.CONTROL_CLASS_FULL_NAME,c.getId());
		updateRequest.getUpdateObject().setLinkValue("command",c.getCommandId());
		updateRequest.getUpdateObject().setPropertyValue("status",new StringBuilder(c.getStatus()));
		updateRequest.addSubscriber(this);
		DbRequestQueue.getInstance().addRequest(updateRequest,this);
	}
}

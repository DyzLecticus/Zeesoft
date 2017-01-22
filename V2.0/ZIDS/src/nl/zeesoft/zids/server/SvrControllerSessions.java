package nl.zeesoft.zids.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zacs.database.model.Assignment;
import nl.zeesoft.zacs.simulator.SimAssignmentHandler;
import nl.zeesoft.zacs.simulator.SimController;
import nl.zeesoft.zids.database.model.Dialog;
import nl.zeesoft.zids.database.model.Session;
import nl.zeesoft.zids.database.model.SessionDialogVariable;
import nl.zeesoft.zids.database.model.SessionVariable;
import nl.zeesoft.zids.database.model.ZIDSModel;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.model.helpers.HlpGetControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqGetFilter;
import nl.zeesoft.zodb.database.request.ReqObject;
import nl.zeesoft.zodb.database.request.ReqRemove;
import nl.zeesoft.zodb.database.request.ReqUpdate;
import nl.zeesoft.zodb.event.EvtEvent;

public class SvrControllerSessions extends HlpGetControllerObject {
	private static SvrControllerSessions	controller							= null;
	
	private ReqUpdate 						updateRequest						= null;

	private ReqGet 							getSessionVariableRequest			= new ReqGet(ZIDSModel.SESSION_VARIABLE_CLASS_FULL_NAME);
	private ReqGet 							getSessionDialogVariableRequest		= new ReqGet(ZIDSModel.SESSION_DIALOG_VARIABLE_CLASS_FULL_NAME);
	private ReqGet 							getAssignmentRequest				= new ReqGet(ZIDSModel.ASSIGNMENT_CLASS_FULL_NAME);
	
	private ReqAdd 							addSessionRequest					= new ReqAdd(ZIDSModel.SESSION_CLASS_FULL_NAME);
	private ReqAdd 							addSessionVariableRequest			= new ReqAdd(ZIDSModel.SESSION_VARIABLE_CLASS_FULL_NAME);
	private ReqAdd 							addSessionDialogVariableRequest		= new ReqAdd(ZIDSModel.SESSION_DIALOG_VARIABLE_CLASS_FULL_NAME);

	private ReqRemove 						removeSessionRequest				= new ReqRemove(ZIDSModel.SESSION_CLASS_FULL_NAME);
	private ReqRemove 						removeSessionVariableRequest		= new ReqRemove(ZIDSModel.SESSION_VARIABLE_CLASS_FULL_NAME);
	private ReqRemove 						removeSessionDialogVariableRequest	= new ReqRemove(ZIDSModel.SESSION_DIALOG_VARIABLE_CLASS_FULL_NAME);
	
	private Assignment						assignment							= null;
	private SimAssignmentHandler 			assignmentHandler 					= null;
	
	private SvrControllerLocker				locker								= null;
	
	private SvrControllerSessions() {
		super(ZIDSModel.SESSION_CLASS_FULL_NAME);
		locker = new SvrControllerLocker();
	}
	
	public static SvrControllerSessions getInstance() {
		if (controller==null) {
			controller = new SvrControllerSessions();
		}
		return controller;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	public List<Session> getSessionsAsList() {
		List<Session> r = new ArrayList<Session>();
		for (HlpObject obj: getObjectsAsList()) {
			r.add((Session) obj);
		}
		return r;
	}

	public Session getSessionById(long id) {
		Session r = null;
		HlpObject obj = getObjectById(id);
		if (obj!=null) {
			r = (Session) obj;
		}
		return r;
	}

	public Session getOrAddSessionBySessionId(String sessionId) {
		Session r = null;
		locker.lockMe(this);
		if (sessionId.length()>0) {
			r = getSessionBySessionIdNoLock(sessionId);
			if (r!=null && r.getDateTimeEnd()>0) {
				sessionId = "";
				r = null;
			}
		}
		if (r==null) {
			r = addSessionNoLock(sessionId);
		}
		locker.unlockMe(this);
		return r;
	}
	
	public Session getSessionBySessionId(String sessionId) {
		Session r = null;
		locker.lockMe(this);
		r = getSessionBySessionIdNoLock(sessionId);
		locker.unlockMe(this);
		return r;
	}
	
	public void updateSession(Session session) {
		locker.lockMe(this);
		updateSessionNoLock(session);
		locker.unlockMe(this);
	}

	public SessionDialogVariable getSessionDialogVariableByCode(Session s,String code) {
		SessionDialogVariable r = null;
		locker.lockMe(this);
		for (SessionDialogVariable sdv: s.getDialogVariables()) {
			if (sdv.getCode().equals(code)) {
				r = sdv;
				break;
			}
		}
		locker.unlockMe(this);
		return r;
	}

	public SessionVariable getSessionVariableByCode(Session s,String code) {
		SessionVariable r = null;
		locker.lockMe(this);
		for (SessionVariable sv: s.getVariables()) {
			if (sv.getCode().equals(code)) {
				r = sv;
				break;
			}
		}
		locker.unlockMe(this);
		return r;
	}
	
	private SessionDialogVariable getOrAddSessionDialogVariableByCodeNoLock(Session s,String code) {
		SessionDialogVariable r = null;
		for (SessionDialogVariable sdv: s.getDialogVariables()) {
			if (sdv.getCode().equals(code)) {
				r = sdv;
				break;
			}
		}
		if (r==null) {
			r = new SessionDialogVariable();
			r.setSession(s);
			r.setCode(code);
			addSessionDialogVariableNoLock(s,r); 
		}
		return r;
	}

	private SessionVariable getOrAddSessionVariableByCodeNoLock(Session s,String code) {
		SessionVariable r = null;
		for (SessionVariable sv: s.getVariables()) {
			if (sv.getCode().equals(code)) {
				r = sv;
				break;
			}
		}
		if (r==null) {
			r = new SessionVariable();
			r.setSession(s);
			r.setCode(code);
			addSessionVariableNoLock(s,r); 
		}
		return r;
	}

	public void addOrUpdateSessionDialogVariable(Session s, String code, StringBuilder value) {
		locker.lockMe(this);
		s.appendLogLine(false,"- Dialog variable: " + code + " = " + value,true);
		SessionDialogVariable variable = getOrAddSessionDialogVariableByCodeNoLock(s,code);
		variable.setValue(value);
		updateSessionDialogVariableNoLock(variable);
		locker.unlockMe(this);
	}
	
	public void addOrUpdateSessionVariable(Session s, String code, StringBuilder value) {
		locker.lockMe(this);
		s.appendLogLine(false,"- Session variable: " + code + " = " + value,true);
		SessionVariable variable = getOrAddSessionVariableByCodeNoLock(s,code);
		variable.setValue(value);
		updateSessionVariableNoLock(variable);
		locker.unlockMe(this);
	}

	public void removeSession(Session session) {
		locker.lockMe(this);
		removeSessionNoLock(session);
		locker.unlockMe(this);
	}
	
	public void removeSessionVariablesFromSession(Session session) {
		locker.lockMe(this);
		removeSessionVariablesFromSessionNoLock(session);
		locker.unlockMe(this);
	}

	public void removeSessionDialogVariablesFromSession(Session session) {
		locker.lockMe(this);
		removeSessionDialogVariablesFromSessionNoLock(session);
		locker.unlockMe(this);
	}

	public void closeSession(Session session) {
		locker.lockMe(this);
		closeSessionNoLock(session,"- Closed session");
		locker.unlockMe(this);
	}

	public void closeExpiredSessions(long lastActivityBefore) {
		locker.lockMe(this);
		List<Session> sessions = getSessionsAsList();
		for (Session session: sessions) {
			if (session.getDateTimeEnd()==0 && session.getDateTimeLastActivity()<lastActivityBefore) {
				closeSessionNoLock(session,"- Closed expired session");
			}
		}
		locker.unlockMe(this);
	}
	
	public int removeClosedSessions() {
		int removed = 0;
		locker.lockMe(this);
		List<Session> sessions = getSessionsAsList();
		for (Session session: sessions) {
			if (session.getDateTimeEnd()>0) {
				removeSessionVariablesFromSessionNoLock(session);
				removeSessionDialogVariablesFromSessionNoLock(session);
				removeSessionNoLock(session);
				removeObject(session);
				removed++;
			}
		}
		locker.unlockMe(this);
		return removed;
	}
	
	@Override
	public void initialize() {
		Messenger.getInstance().debug(this,"Initializing sessions ...");
		assignmentHandler = SimController.getInstance().getAssignmentHandler();
		super.initialize();
		Messenger.getInstance().debug(this,"Loaded sessions: " + getObjectsAsList().size());

		setDone(false);
		getSessionVariableRequest.getProperties().add(ReqGet.ALL_PROPERTIES);
		getSessionVariableRequest.addSubscriber(this);
		DbRequestQueue.getInstance().addRequest(getSessionVariableRequest,this);
		waitTillDone();
		
		Messenger.getInstance().debug(this,"Initialized sessions");
	}

	@Override
	public void reinitialize() {
		getSessionVariableRequest = new ReqGet(ZIDSModel.SESSION_DIALOG_VARIABLE_CLASS_FULL_NAME);
		getSessionDialogVariableRequest = new ReqGet(ZIDSModel.SESSION_DIALOG_VARIABLE_CLASS_FULL_NAME);
		getAssignmentRequest = new ReqGet(ZIDSModel.ASSIGNMENT_CLASS_FULL_NAME);
		assignment = null;
		super.reinitialize();
	}

	@Override
	public void handleEvent(EvtEvent e) {
		super.handleEvent(e);
		if (e.getType().equals(DbController.DB_STARTED) && e.getValue().toString().equals("true")) {
			initialize();
		} else if (e.getType().equals(DbController.DB_UPDATED_MODEL)) {
			reinitialize();
		} else if (e.getValue()!=null && e.getValue() instanceof ReqObject) {
			ReqObject request = (ReqObject) e.getValue();
			if (request.hasError()) {
				Messenger.getInstance().error(this,"Error executing request: " + request.getErrors().get(0).getMessage());
				setDone(true);
			} else if (e.getValue()==getSessionVariableRequest) {
				for (ReqDataObject object: getSessionVariableRequest.getObjects()) {
					SessionVariable sv = new SessionVariable();
					sv.fromDataObject(object.getDataObject());
					Session s = getSessionById(sv.getSessionId());
					if (s!=null) {
						s.getVariables().add(sv);
					}
				}
				Messenger.getInstance().debug(this,"Loaded session variables: " + getSessionVariableRequest.getObjects().size());
				getSessionDialogVariableRequest.getProperties().add(ReqGet.ALL_PROPERTIES);
				getSessionDialogVariableRequest.addSubscriber(this);
				DbRequestQueue.getInstance().addRequest(getSessionDialogVariableRequest,this);
			} else if (e.getValue()==getSessionDialogVariableRequest) {
				for (ReqDataObject object: getSessionDialogVariableRequest.getObjects()) {
					SessionDialogVariable sdv = new SessionDialogVariable();
					sdv.fromDataObject(object.getDataObject());
					Session s = getSessionById(sdv.getSessionId());
					if (s!=null) {
						s.getDialogVariables().add(sdv);
					}
				}
				Messenger.getInstance().debug(this,"Loaded session dialog variables: " + getSessionDialogVariableRequest.getObjects().size());
				getAssignmentRequest.getProperties().add(ReqGet.ALL_PROPERTIES);
				getAssignmentRequest.addSubscriber(this);
				getAssignmentRequest.addFilter("name",ReqGetFilter.EQUALS,ZIDSModel.SESSION_HANDLING_ASSIGNMENT_NAME);
				DbRequestQueue.getInstance().addRequest(getAssignmentRequest,this);
			} else if (e.getValue()==getAssignmentRequest) {
				for (ReqDataObject object: getAssignmentRequest.getObjects()) {
					lockMe(this);
					assignment = new Assignment();
					assignment.fromDataObject(object.getDataObject());
					unlockMe(this);
					Messenger.getInstance().debug(this,"Loaded assignment: " + assignment.getName());
					break;
				}
				setDone(true);
			} else if (e.getValue()==addSessionRequest) {
				setDone(true);
			} else if (e.getValue()==addSessionVariableRequest) {
				setDone(true);
			} else if (e.getValue()==addSessionDialogVariableRequest) {
				setDone(true);
			} else if (e.getValue()==removeSessionRequest) {
				setDone(true);
			} else if (e.getValue()==removeSessionVariableRequest) {
				setDone(true);
			} else if (e.getValue()==removeSessionDialogVariableRequest) {
				setDone(true);
			} else if (updateRequest!=null && e.getValue()==updateRequest) {
				setDone(true);
			}
		}
	}

	public StringBuilder[] getOutputAndContextForInputAndContext(StringBuilder input,StringBuilder context, boolean contextDynamic, boolean correctInput, boolean correctLineEnd, boolean correctInputOnly) {
		StringBuilder[] r = new StringBuilder[6];
		lockMe(this);
		if (context==null) {
			context = new StringBuilder();
		}
		assignment.setContext(context);
		assignment.setContextDynamic(contextDynamic);
		assignment.setStopOnLineEndSymbol(true);
		assignment.setCorrectInput(correctInput);
		assignment.setCorrectLineEnd(correctLineEnd);
		assignment.setCorrectInputOnly(correctInputOnly);
		assignment.setInput(input);
		doAssignmentNoLock();
		r[0] = assignment.getOutput();
		r[1] = assignment.getInputContext();
		r[2] = assignment.getOutputContext();
		r[3] = assignment.getCorrectedInput();
		r[4] = assignment.getCorrectedInputSymbols();
		r[5] = assignment.getLog();
		unlockMe(this);
		return r;
	}

	
	@Override
	protected void addedObject(HlpObject object) {
		Session s = (Session) object;
		Dialog d = SvrControllerDialogs.getInstance().getDialogById(s.getDialogId());
		if (d!=null) {
			s.setDialog(d);
		}
	}

	@Override
	protected HlpObject getNewObject() {
		return new Session();
	}

	private void doAssignmentNoLock() {
		assignmentHandler.setWorkingAssignment(assignment);
		while(assignmentHandler.getWorkingAssignment()!=null) {
			assignmentHandler.workOnWorkingAssignment();
		}
	}

	private Session getSessionBySessionIdNoLock(String sessionId) {
		Session r = null;
		List<Session> sessions = getSessionsAsList();
		for (Session session: sessions) {
			if (session.getSessionId().equals(sessionId)) {
				r = session;
				break;
			}
		}
		return r;
	}

	private Session addSessionNoLock(String sessionId) {
		if (sessionId.length()==0) {
			sessionId = "" + (new Date()).getTime();
		}
		Session s = new Session();
		s.setSessionId(sessionId);
		s.setDateTimeStart((new Date()).getTime());
		setDone(false);
		addSessionRequest = new ReqAdd(ZIDSModel.SESSION_CLASS_FULL_NAME);
		addSessionRequest.getObjects().add(new ReqDataObject(s.toDataObject()));
		addSessionRequest.addSubscriber(this);
		DbRequestQueue.getInstance().addRequest(addSessionRequest,this);
		waitTillDone();
		if (addSessionRequest.getImpactedIds().size()>0) {
			s.setId(addSessionRequest.getImpactedIds().get(0));
			addObject(s);
		} else {
			Messenger.getInstance().error(this,"Failed to create session: " + sessionId);
		}
		return s;
	}

	private void updateSessionDialogVariableNoLock(SessionDialogVariable sessionDialogVariable) {
		setDone(false);
		updateRequest = sessionDialogVariable.getNewUpdateRequest(this);
		DbRequestQueue.getInstance().addRequest(updateRequest,this);
		waitTillDone();
	}

	private void addSessionDialogVariableNoLock(Session s,SessionDialogVariable sdv) {
		setDone(false);
		addSessionDialogVariableRequest = new ReqAdd(ZIDSModel.SESSION_DIALOG_VARIABLE_CLASS_FULL_NAME);
		addSessionDialogVariableRequest.getObjects().add(new ReqDataObject(sdv.toDataObject()));
		addSessionDialogVariableRequest.addSubscriber(this);
		DbRequestQueue.getInstance().addRequest(addSessionDialogVariableRequest,this);
		waitTillDone();
		if (addSessionDialogVariableRequest.getImpactedIds().size()>0) {
			sdv.setId(addSessionDialogVariableRequest.getImpactedIds().get(0));
			s.getDialogVariables().add(sdv);
		} else {
			Messenger.getInstance().error(this,"Failed to create session dialog variable: " + s.getSessionId() + ", code: " + sdv.getCode());
		}
	}

	private void addSessionVariableNoLock(Session s,SessionVariable sv) {
		setDone(false);
		addSessionVariableRequest = new ReqAdd(ZIDSModel.SESSION_VARIABLE_CLASS_FULL_NAME);
		addSessionVariableRequest.getObjects().add(new ReqDataObject(sv.toDataObject()));
		addSessionVariableRequest.addSubscriber(this);
		DbRequestQueue.getInstance().addRequest(addSessionVariableRequest,this);
		waitTillDone();
		if (addSessionVariableRequest.getImpactedIds().size()>0) {
			sv.setId(addSessionVariableRequest.getImpactedIds().get(0));
			s.getVariables().add(sv);
		} else {
			Messenger.getInstance().error(this,"Failed to create session variable: " + s.getSessionId() + ", code: " + sv.getCode());
		}
	}

	private void updateSessionVariableNoLock(SessionVariable sessionVariable) {
		setDone(false);
		updateRequest = sessionVariable.getNewUpdateRequest(this);
		DbRequestQueue.getInstance().addRequest(updateRequest,this);
		waitTillDone();
	}

	private void closeSessionNoLock(Session session,String thought) {
		session.setDateTimeEnd((new Date()).getTime());
		session.appendLogLine(false,thought,true);
		updateSessionNoLock(session);
	}

	private void removeSessionNoLock(Session session) {
		setDone(false);
		removeSessionRequest = new ReqRemove(ZIDSModel.SESSION_CLASS_FULL_NAME,session.getId());
		removeSessionRequest.addSubscriber(this);
		DbRequestQueue.getInstance().addRequest(removeSessionRequest,this);
		waitTillDone();
	}
	
	private void removeSessionVariablesFromSessionNoLock(Session session) {
		setDone(false);
		removeSessionVariableRequest = new ReqRemove(ZIDSModel.SESSION_VARIABLE_CLASS_FULL_NAME);
		removeSessionVariableRequest.addSubscriber(this);
		removeSessionVariableRequest.getGet().addFilter("session",ReqGetFilter.CONTAINS,"" + session.getId());
		DbRequestQueue.getInstance().addRequest(removeSessionVariableRequest,this);
		waitTillDone();
		session.getVariables().clear();
	}

	private void removeSessionDialogVariablesFromSessionNoLock(Session session) {
		setDone(false);
		removeSessionDialogVariableRequest = new ReqRemove(ZIDSModel.SESSION_DIALOG_VARIABLE_CLASS_FULL_NAME);
		removeSessionDialogVariableRequest.addSubscriber(this);
		removeSessionDialogVariableRequest.getGet().addFilter("session",ReqGetFilter.CONTAINS,"" + session.getId());
		DbRequestQueue.getInstance().addRequest(removeSessionDialogVariableRequest,this);
		waitTillDone();
		session.getDialogVariables().clear();
	}

	private void updateSessionNoLock(Session session) {
		setDone(false);
		updateRequest = session.getNewUpdateRequest(this);
		DbRequestQueue.getInstance().addRequest(updateRequest,this);
		waitTillDone();
	}

}

package nl.zeesoft.zids.dialog.controller;

import nl.zeesoft.zids.database.model.DialogVariable;
import nl.zeesoft.zids.database.model.Session;
import nl.zeesoft.zids.database.model.SessionDialogVariable;
import nl.zeesoft.zids.dialog.pattern.PtnManager;
import nl.zeesoft.zids.server.SvrControllerSessions;
import nl.zeesoft.zodb.database.model.helpers.HlpControllerObject;

public abstract class CtrObject extends HlpControllerObject {
	private StringBuilder response 			= new StringBuilder();
	private StringBuilder responseContext	= new StringBuilder();
	
	public abstract void validateSessionDialogVariables(Session s, PtnManager m);
	
	protected boolean allVariablesFilled(Session s) {
		boolean filled = true;
		if (s.getDialog()!=null) {
			for (DialogVariable dv: s.getDialog().getVariables()) {
				SessionDialogVariable sv = SvrControllerSessions.getInstance().getSessionDialogVariableByCode(s,dv.getCode());
				if (sv==null || sv.getValue()==null || sv.getValue().length()==0) {
					filled = false;
					break;
				}
			}
		}
		return filled;
	}

	/**
	 * @return the responseContext
	 */
	public StringBuilder getResponseContext() {
		return responseContext;
	}

	/**
	 * @param responseContext the responseContext to set
	 */
	protected final void setResponseContext(StringBuilder responseContext) {
		this.responseContext = responseContext;
	}

	/**
	 * @return the response
	 */
	public StringBuilder getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	protected final void setResponse(StringBuilder response) {
		this.response = response;
	}

	@Override
	protected void initialize() {
		// Not implemented
	}
}

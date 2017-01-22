package nl.zeesoft.zdsm.process.impl;

import nl.zeesoft.zdsm.database.model.Response;
import nl.zeesoft.zdsm.database.model.Service;
import nl.zeesoft.zdsm.process.PrHandler;
import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

public class SelfAuthorize extends PrHandler {
	private String sessionId = "";
	
	@Override
	public void handleProcess() {
		Response authorizer = getNewServiceResponse(Service.SELF_AUTHORIZER,"200");
		if (authorizer!=null && authorizer.isSuccess()) {
			sessionId = parseCookieValues(authorizer.getHeader()).get("sessionid");
			if (sessionId!=null && sessionId.length()>0) {
				addLogLine("Authorizing session: " + sessionId + " ...");
				Service selfAuthorize = getServiceByName(Service.SELF_AUTHORIZATION_MANAGER);
				if (selfAuthorize!=null) {
					selfAuthorize.setHeader(new StringBuilder("Cookie: sessionid=" + sessionId + "\n"));
					Response authorize = getNewServiceResponse(selfAuthorize,"200");
					if (authorize.isSuccess()) {
						XMLFile file = getResponseBodyAsXMLFile(authorize.getBody());
						XMLElem msgElem = file.getRootElement().getChildByName("message");
						XMLElem dirElem = file.getRootElement().getChildByName("redirect");
						if (dirElem!=null) {
							addLogLine("Autorized session: " + sessionId + ", redirect " + dirElem.getValue());
							getLog().setSuccess(true);
						} else if (msgElem!=null) {
							addLogLine(msgElem.getValue().toString());
						} else {
							addLogLine("Failed to autorize session: " + sessionId);
						}
					}
				} else {
					getLog().setSuccess(false);
				}
			} else {
				addLogLine("No session to authorize");
				getLog().setSuccess(true);
			}
		} else {
			getLog().setSuccess(false);
		}		
	}

	/**
	 * @return the sessionId
	 */
	protected String getSessionId() {
		return sessionId;
	}
}

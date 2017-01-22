package nl.zeesoft.zdsm.process.impl;

import nl.zeesoft.zdsm.database.model.Response;
import nl.zeesoft.zdsm.database.model.Service;
import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

public class SelfAuthorized extends SelfAuthorize {
	@Override
	public void handleProcess() {
		super.handleProcess();
		if (getLog().isSuccess() && getSessionId().length()>0) {
			Service selfAuthorized = getServiceByName(Service.SELF_SESSION_AUTHORIZED);
			if (selfAuthorized!=null) {
				selfAuthorized.setHeader(new StringBuilder("Cookie: sessionid=" + getSessionId() + "\n"));
				Response authorized = getNewServiceResponse(selfAuthorized,"200");
				if (authorized.isSuccess()) {
					XMLFile file = getResponseBodyAsXMLFile(authorized.getBody());
					XMLElem valElem = file.getRootElement().getChildByName("value");
					if (valElem==null) {
						getLog().setSuccess(false);
						addLogLine("Session authorized check failed for session: " + getSessionId());
					} else if (!valElem.getValue().toString().equals("true")) {
						getLog().setSuccess(false);
						addLogLine("Session: " + getSessionId() +", authorized: " + valElem.getValue());
					}
				} else {
					getLog().setSuccess(false);
				}
			} else {
				getLog().setSuccess(false);
			}
		}
	}
}

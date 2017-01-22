package nl.zeesoft.zodb.protocol;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.client.ClRequest;
import nl.zeesoft.zodb.client.ClSession;
import nl.zeesoft.zodb.client.ClSessionManager;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.file.XMLFile;

public class PtcClient extends PtcObject {
	private ClSession 	session  	= null;

	@Override
	public StringBuffer processInputAndReturnOutput(StringBuffer input) {
		StringBuffer output = new StringBuffer();
		
		ClSessionManager sessionManager = ClSessionManager.getInstance();
		if ((input!=null) && (input.length()>0)) {
    		if (Generic.stringBufferStartsWith(input,PtcObject.STOP_SESSION)) {
    			sessionManager.stopSession(session.getSessionId());
    			sessionManager.publishEvent(new EvtEvent(ClSessionManager.SERVER_STOPPED_SESSION, session, "Server stopped session: " + session.getSessionId()));
    			output = null;
    		} else if (Generic.stringBufferStartsWith(input,PtcObject.START_SESSION)) {
    			String[] val = Generic.getValuesFromString(Generic.stringBufferReplace(input,"\n","").toString());
    			if ((val.length>2) && (!val[1].trim().equals("")) && (!val[2].trim().equals(""))) {
    				long cCRC = DbConfig.getInstance().getModel().getCrc();
    				long sCRC = Long.parseLong(val[2]);

    				session.setSessionId(Long.parseLong(val[1]));
    				sessionManager.publishEvent(new EvtEvent(ClSessionManager.STARTED_SESSION, session, "Started session: " + session.getSessionId()));
    				
    				if (cCRC!=sCRC) {
            			if (!(this instanceof PtcClientControl)) { 
                			ClSessionManager.getInstance().stopSession(session.getSessionId());
            			}
            			sessionManager.publishEvent(new EvtEvent(ClSessionManager.CRC_MISMATCH, session, "Client model CRC does not match server model CRC: " + cCRC + " <> " + sCRC));
    				}
    				output = null;
    			}
    		} else if (Generic.stringBufferStartsWith(input,AUTHORIZE_SESSION_FAILED)) {
    			sessionManager.publishEvent(new EvtEvent(ClSessionManager.FAILED_TO_AUTHORIZE_SESSION, session, "Failed to authorize session"));
    			output = null;
			} else if (Generic.stringBufferStartsWith(input,AUTHORIZE_SESSION_SUCCESS)) {
				String[] val = Generic.getValuesFromString(Generic.stringBufferReplace(input,"\n","").toString());
				if ((val.length>4) && (!val[1].trim().equals("")) && (!val[2].trim().equals("")) && (!val[3].trim().equals("")) && (!val[4].trim().equals(""))) {
					session.setUserId(Long.parseLong(val[1].trim()));
					session.setUserName(val[2].trim());
					session.setUserAdmin(Boolean.parseBoolean(val[3].trim()));
					session.setUserLevel(Integer.parseInt(val[4].trim()));
					Messenger.getInstance().debug(this, "Authorize session succes, id: " + session.getUserId() + ", name: " + session.getUserName() + ", admin: " + session.isUserAdmin() + ", level: " + session.getUserLevel());
					sessionManager.publishEvent(new EvtEvent(ClSessionManager.AUTHORIZED_SESSION, session, "Authorized session"));
				}
				output = null;
			} else if (Generic.stringBufferStartsWith(input,"<")) {
				XMLFile f = new XMLFile();
				String err = f.parseXML(input);
				if (err.length()==0) {
					if (f.getRootElement().getName().equals("request")) {
						ClRequest r = ClRequest.fromXml(f);
						try {
							if (r.getActionRequest().length()>0) {
								output = processInputAndReturnOutput(new StringBuffer(r.getActionResponse()));
							}
							session.getRequestQueue().processRequestResponse(r,this);
						} catch (Exception e) {
							String callStack = Generic.getCallStackString(e.getStackTrace(),"");
							String msg = "Exception while processing response: " + e.toString() + "\n" + callStack;
							Messenger.getInstance().error(this, msg);
							r.setActionResponse(msg);
							r.setError(true);
						}
						if (r.getError()) {
							sessionManager.publishEvent(new EvtEvent(ClSessionManager.REQUEST_RESPONSE_ERROR, session, r));
						}
					} else {
						Messenger.getInstance().error(this, "Unhandled response type: " + f.getRootElement().getName());
					}
				} else {
					Messenger.getInstance().error(this, "Failed to parse XML: " + err);
				}
				f.cleanUp();
				output = null;
			}
			
		}
		
		return output;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(ClSession session) {
		this.session = session;
	}

	/**
	 * @return the session
	 */
	protected ClSession getSession() {
		return session;
	}
	
}

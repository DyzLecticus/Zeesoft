package nl.zeesoft.zids.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zid.session.Session;
import nl.zeesoft.zid.session.SessionDialogHandler;
import nl.zeesoft.zid.session.SessionManager;

public class SessionHandler extends HandlerObject {
	private String					key				= "";
	private SessionManager			sessionManager	= null;
	private SessionDialogHandler	dialogHandler	= null;
	
	public SessionHandler(Messenger msgr,String key,SessionManager s,SessionDialogHandler h) {
		super(msgr,"/sessions.json");
		this.key = key;
		this.sessionManager = s;
		this.dialogHandler = h;
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		ZStringBuilder res = null;

		ZStringBuilder js = new ZStringBuilder();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				js.append(line);
			}
		} catch (IOException e) {
			getMessenger().error(this,"I/O exception while reading JSON POST request",e);
		}
		
		JsFile json = new JsFile();
		json.fromStringBuilder(js);
		JsElem actionElem = json.rootElement.getChildByName("action");
		if (actionElem==null || actionElem.value.length()==0) {
			res = getErrorJson("Missing mandatory 'action' attribute.");
		} else {
			if (actionElem.value.toString().equals("open")) {
				res = openSession();
			} else {
				JsElem idElem = json.rootElement.getChildByName("externalId");
				if (idElem==null || idElem.value.length()==0) {
					res = getErrorJson("Missing mandatory 'externalId' attribute.");
				} else {
					String externalId = idElem.value.toString();
					Session session = sessionManager.getSession(externalId);
					if (session==null) {
						res = getErrorJson("Session not found with externalId: " + externalId);
					} else {
						if (actionElem.value.toString().equals("input")) {
							JsElem inputElem = json.rootElement.getChildByName("input");
							if (inputElem==null || inputElem.value==null || inputElem.value.length()==0) {
								res = getErrorJson("Missing mandatory 'input' attribute.");
							} else {
								session.setInput(new ZStringSymbolParser(inputElem.value));
								dialogHandler.handleSessionInput(session);
								res = getSessionJson(session);
							}
						} else if (actionElem.value.toString().equals("close")) {
							res = closeSession(session);
						} else {
							res = getErrorJson("Requested action is not supported: " + actionElem.value);
						}
					}
				}
			}
		}

		response.setContentType("application/json");
		try {
			PrintWriter out = response.getWriter();
			out.print(res);
		} catch (IOException e) {
			getMessenger().error(this,"I/O exception while writing JSON POST response",e);
		}
	}

	@Override
	protected ZStringBuilder buildResponse() {
		return null;
	}
	
	private ZStringBuilder openSession() {
		Session session = sessionManager.openSession();
		session.setExternalId(getNewExternalId(session));
		return getSessionJson(session);
	}

	private ZStringBuilder closeSession(Session session) {
		sessionManager.closeSession(session.getId());
		return getSessionJson(session);
	}

	private ZStringBuilder getErrorJson(String error) {
		JsFile file = new JsFile();
		file.rootElement = new JsElem();
		JsElem errorElem = new JsElem("error",error,true);
		file.rootElement.children.add(errorElem);
		return file.toStringBuilderReadFormat();
	}
	
	private ZStringBuilder getSessionJson(Session session) {
		JsFile file = new JsFile();
		file.rootElement = new JsElem();
		file.rootElement.children.add(new JsElem("externalId",session.getExternalId(),true));
		file.rootElement.children.add(new JsElem("start","" + session.getStart().getDate().getTime()));
		file.rootElement.children.add(new JsElem("log",session.getLog(),true));
		file.rootElement.children.add(new JsElem("output",session.getOutput(),true));
		if (session.getEnd()!=null) {
			file.rootElement.children.add(new JsElem("end","" + session.getEnd().getDate().getTime()));
		}
		return file.toStringBuilderReadFormat();
	}
	
	private String getNewExternalId(Session session) {
		ZStringEncoder encoder = new ZStringEncoder();
		Date d = new Date();
		encoder.append(d.getTime() + String.format("%08d",session.getId()) + d.getTime());
		encoder.encodeKey(key,0);
		return encoder.toString();
	}
}

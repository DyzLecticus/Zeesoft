package nl.zeesoft.zids.server.requesthandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zids.json.JsElem;
import nl.zeesoft.zids.json.JsFile;
import nl.zeesoft.zids.server.SvrControllerDatabase;
import nl.zeesoft.zids.server.SvrControllerSessions;
import nl.zeesoft.zids.server.SvrSessionHandler;
import nl.zeesoft.zodb.Messenger;

public class RhPostDialog extends RhRequestHandlerObject {
	private JsFile 		responseJSON 	= null;

	@Override
	public String getMethod() {
		return POST;
	}

	@Override
	public String getPath() {
		return "/dialog";
	}

	@Override
	public void handleRequest(HttpServletRequest request,HttpServletResponse response) throws IOException {
		lockMe(this);
		StringBuilder js = new StringBuilder();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				js.append(line);
			}
		} catch (Exception e) {
			Messenger.getInstance().error(this,"Error while parsing JSON POST data");
		}
		JsFile jsf = new JsFile();
		jsf.fromStringBuilder(js);
		responseJSON = handleSessionRequest(jsf,response);
		generateResponse(request,response);
		unlockMe(this);
	}

	protected JsFile handleSessionRequest(JsFile sessionRequest,HttpServletResponse servletResponse) {
		JsFile response = null;
		SvrSessionHandler handler = new SvrSessionHandler(SvrControllerDatabase.getInstance().getPatternManager());
		StringBuilder type = sessionRequest.rootElement.getChildValueByName("type");
		StringBuilder sessionId = sessionRequest.rootElement.getChildValueByName("sessionId");
		StringBuilder context = sessionRequest.rootElement.getChildValueByName("context");
		StringBuilder input = sessionRequest.rootElement.getChildValueByName("input");
		String err = handler.handleSessionRequest(type, sessionId, context, input);
		if (err.length()>0) {
			servletResponse.setStatus(400);
			response = getErrorJSON(err);
		} else {
			SvrControllerSessions.getInstance().updateSession(handler.getSession());
			response = handler.getSession().toJSON();
			addTypeToResponse(response,type);
		}
		return response;
	}
	
	private void generateResponse(HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		if (responseJSON!=null) {
			out.println(responseJSON.toStringBuilderReadFormat());
		} else {
			response.setStatus(400);
			out.println(getErrorJSON("Failed to parse request").toStringBuilderReadFormat());
		}
	}

	private JsFile getErrorJSON(String msg) {
		JsFile f = new JsFile();
		f.rootElement = new JsElem();
		JsElem err = new JsElem();
		err.name = "error";
		err.value = new StringBuilder(msg);
		err.cData = true;
		f.rootElement.children.add(err);
		return f;
	}

	private void addTypeToResponse(JsFile response,StringBuilder type) {
		addCDataElementToResponse(response,"type",type);
	}

	private void addCDataElementToResponse(JsFile response,String name,StringBuilder value) {
		JsElem c = new JsElem();
		c.name = name;
		c.value = value;
		c.cData = true;
		response.rootElement.children.add(0,c);
	}
}

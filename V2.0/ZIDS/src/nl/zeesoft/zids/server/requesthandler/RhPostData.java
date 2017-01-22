package nl.zeesoft.zids.server.requesthandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zids.json.JsFile;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.request.ReqObject;
import nl.zeesoft.zodb.event.EvtEvent;

public class RhPostData extends RhRequestHandlerObject {
	private ReqObject postRequest = null;

	@Override
	public void handleEvent(EvtEvent e) {
		if (postRequest!=null && e.getValue()!=null && e.getValue().equals(postRequest)) {
			setDone(true);
		}
	}

	@Override
	public String getMethod() {
		return POST;
	}

	@Override
	public String getPath() {
		return "/data";
	}

	@Override
	public void handleRequest(HttpServletRequest request,HttpServletResponse response) throws IOException {
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
		postRequest = RhReqJSONToObject.jsonToRequest(jsf); 
		if (postRequest!=null) {
			executePostRequest();
		}
		generateResponse(request,response);
	}

	private void executePostRequest() {
		postRequest.addSubscriber(this);
		setDone(false);
		DbRequestQueue.getInstance().addRequest(postRequest,this);
		waitTillDone();
	}
	
	private void generateResponse(HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		if (postRequest!=null) {
			out.println(RhReqObjectToJSON.requestToJSON(postRequest).toStringBuilderReadFormat());
		} else {
			response.setStatus(400);
			out.println("{\"error\": \"Failed to parse request\"}");
		}
	}

}

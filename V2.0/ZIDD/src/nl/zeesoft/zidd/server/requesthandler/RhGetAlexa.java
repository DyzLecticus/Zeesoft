package nl.zeesoft.zidd.server.requesthandler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zidd.server.resource.RscDialogUtterancesHtml;
import nl.zeesoft.zids.server.requesthandler.RhRequestHandlerObject;

public class RhGetAlexa extends RhRequestHandlerObject {
	@Override
	public String getPath() {
		return "/alexa";
	}

	@Override
	public String getFile() {
		return "utterances.html";
	}

	@Override
	public void handleRequest(HttpServletRequest request,HttpServletResponse response) throws IOException {
		RscDialogUtterancesHtml html = new RscDialogUtterancesHtml(getTitle(),getBackgroundColor());
		html.addToResponse(response);
	}
}

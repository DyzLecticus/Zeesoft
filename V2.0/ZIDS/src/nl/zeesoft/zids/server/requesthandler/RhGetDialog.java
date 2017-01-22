package nl.zeesoft.zids.server.requesthandler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zids.server.resource.RscDialogDialogExamplesHtml;
import nl.zeesoft.zids.server.resource.RscDialogDialogsHtml;
import nl.zeesoft.zids.server.resource.RscDialogIndexHtml;
import nl.zeesoft.zids.server.resource.RscDialogSpeakerHtml;

public class RhGetDialog extends RhRequestHandlerObject {
	@Override
	public String getPath() {
		return "/dialog";
	}

	@Override
	public void handleRequest(HttpServletRequest request,HttpServletResponse response) throws IOException {
		if (request.getServletPath().endsWith("dialogs.html")) {
			RscDialogDialogsHtml html = new RscDialogDialogsHtml(getTitle(),getBackgroundColor());
			html.addToResponse(response);
		} else if (request.getServletPath().endsWith("dialogExamples.html")) {
			RscDialogDialogExamplesHtml html = new RscDialogDialogExamplesHtml(getTitle(),getBackgroundColor());
			html.addToResponse(response);
		} else if (request.getServletPath().endsWith("speaker.html")) {
			RscDialogSpeakerHtml html = new RscDialogSpeakerHtml(getTitle(),getBackgroundColor());
			html.addToResponse(response);
		} else {
			RscDialogIndexHtml html = new RscDialogIndexHtml(getTitle(),getBackgroundColor());
			html.addToResponse(response);
		}
	}
}

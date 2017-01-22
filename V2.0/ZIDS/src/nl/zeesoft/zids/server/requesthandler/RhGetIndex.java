package nl.zeesoft.zids.server.requesthandler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zids.server.resource.RscIndexHtml;

public class RhGetIndex extends RhRequestHandlerObject {
	@Override
	public void handleRequest(HttpServletRequest request,HttpServletResponse response) throws IOException {
		RscIndexHtml index = new RscIndexHtml(getTitle(),getBackgroundColor());
		index.addToResponse(response);
	}
}

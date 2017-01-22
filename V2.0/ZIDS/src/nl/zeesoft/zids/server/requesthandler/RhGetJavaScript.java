package nl.zeesoft.zids.server.requesthandler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zids.server.resource.RscZIDSJavaScript;

public class RhGetJavaScript extends RhRequestHandlerObject {
	@Override
	public String getFile() {
		return "ZIDS.js";
	}
	@Override
	public void handleRequest(HttpServletRequest request,HttpServletResponse response) throws IOException {
		RscZIDSJavaScript js = new RscZIDSJavaScript();
		js.addToResponse(response);
	}
}

package nl.zeesoft.zids.server.requesthandler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zids.server.resource.RscZIDSCss;

public class RhGetCss extends RhRequestHandlerObject {
	@Override
	public String getFile() {
		return "ZIDS.css";
	}
	@Override
	public void handleRequest(HttpServletRequest request,HttpServletResponse response) throws IOException {
		RscZIDSCss css = new RscZIDSCss();
		css.addToResponse(response);
	}
}

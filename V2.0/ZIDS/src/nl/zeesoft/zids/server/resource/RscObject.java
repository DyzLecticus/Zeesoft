package nl.zeesoft.zids.server.resource;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public abstract class RscObject {
	public abstract String getContentType();
	public abstract StringBuilder toStringBuilder();
	public void addToResponse(HttpServletResponse response) throws IOException {
		response.setContentType(getContentType());
		PrintWriter out = response.getWriter();
		out.println(toStringBuilder());
	}
}

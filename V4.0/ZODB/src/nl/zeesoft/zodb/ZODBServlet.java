package nl.zeesoft.zodb;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zodb.app.handler.HandlerObject;

//import nl.zeesoft.zsds.handler.HandlerObject;

@WebServlet("/ZODBServlet")
public class ZODBServlet extends HttpServlet {
	private static final long			serialVersionUID	= 1L;
	
	private Config						configuration		= null;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		boolean debug = false;
		String dbg = config.getInitParameter("debug");
		if (dbg!=null && dbg.length()>0) {
			debug = Boolean.parseBoolean(dbg);
		}
		
		String installDir = config.getInitParameter("installDir");
		if (installDir==null || installDir.length()==0) {
			installDir = config.getServletContext().getRealPath("");
		}
		if (installDir.length()>0) {
			if (!installDir.endsWith("/")) {
				installDir += "/";
			}
			installDir = installDir.replaceAll("\\\\","/");
		}
		
		String servletUrl = config.getInitParameter("baseUrl");
		if (servletUrl==null || servletUrl.length()==0) {
			servletUrl = "http://127.0.0.1:8080";
		}
		servletUrl += config.getServletContext().getContextPath();
		
		System.out.println("Installation directory: " + installDir);
		System.out.println("Servlet URL: " + servletUrl);
		
		configuration = getNewConfig();
		configuration.initialize(debug, installDir, servletUrl);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HandlerObject handler = configuration.getHandlerForRequest(request);
		if (handler!=null) {
			handler.doGet(request, response);
		} else {
			configuration.error(this,"Failed to handle request for path: " + request.getServletPath());
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HandlerObject handler = configuration.getHandlerForRequest(request);
		if (handler!=null) {
			handler.doPost(request, response);
		} else {
			configuration.error(this,"Failed to handle request for path: " + request.getServletPath());
		}
	}
	
	@Override
	public void destroy() {
		configuration.destroy();
		super.destroy();
	}

	protected Config getNewConfig() {
		return new Config();
	}
}

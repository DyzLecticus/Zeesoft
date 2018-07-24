package nl.zeesoft.zsds;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zsds.handler.HandlerObject;

/**
 * Servlet implementation class ZIDSServlet
 */
@WebServlet("/ZSDSServlet")
public class ZSDSServlet extends HttpServlet {
	private static final long			serialVersionUID	= 1L;
    
	private AppConfiguration			configuration		= null;
	
	private String						installDir			= "";
	private boolean						debug				= false;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		installDir = config.getInitParameter("installDir");
		if (installDir==null || installDir.length()==0) {
			installDir = config.getServletContext().getRealPath("");
		}
		if (!installDir.endsWith("/")) {
			installDir += "/";
		}
		
		String dbg = config.getInitParameter("debug");
		if (dbg!=null && dbg.length()>0) {
			debug = Boolean.parseBoolean(dbg);
		}
		
		System.out.println("installDir: " + installDir + ", debug: " + debug);
		
		configuration = new AppConfiguration(installDir,debug);
		configuration.initialize();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HandlerObject handler = configuration.getHandlerForRequest(request);
		if (handler!=null) {
			handler.doGet(request, response);
		} else {
			// TODO handle 404 not found
			response.getWriter().append("Not found: ").append(request.getContextPath());
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HandlerObject handler = configuration.getHandlerForRequest(request);
		if (handler!=null) {
			handler.doPost(request, response);
		} else {
			// TODO handle 404 not found
			response.getWriter().append("Not found: ").append(request.getContextPath());
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		configuration.destroy();
	}

	public AppConfiguration getConfiguration() {
		return configuration;
	}
}

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
		if (installDir.length()>0) {
			if (!installDir.endsWith("/")) {
				installDir += "/";
			}
			installDir = installDir.replaceAll("\\\\","/");
		}
		
		String dbg = config.getInitParameter("debug");
		if (dbg!=null && dbg.length()>0) {
			debug = Boolean.parseBoolean(dbg);
		}
		
		System.out.println("Installation: " + installDir);
		
		configuration = getNewAppConfiguration(installDir,debug);
		configuration.setStateManager(getNewAppStateManager());
		configuration.initialize();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HandlerObject handler = configuration.getHandlerForRequest(request);
		if (handler!=null) {
			handler.doGet(request, response);
		} else {
			configuration.getMessenger().error(this,"Failed to handle request for path: " + request.getServletPath());
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HandlerObject handler = configuration.getHandlerForRequest(request);
		if (handler!=null) {
			handler.doPost(request, response);
		} else {
			configuration.getMessenger().error(this,"Failed to handle request for path: " + request.getServletPath());
		}
	}
	
	@Override
	public void destroy() {
		configuration.destroy();
		super.destroy();
	}

	public AppConfiguration getConfiguration() {
		return configuration;
	}
	
	protected AppConfiguration getNewAppConfiguration(String installDir,boolean debug) {
		return new AppConfiguration(installDir,debug);
	}
	
	protected AppStateManager getNewAppStateManager() {
		return new AppStateManager(getConfiguration());
	}
}

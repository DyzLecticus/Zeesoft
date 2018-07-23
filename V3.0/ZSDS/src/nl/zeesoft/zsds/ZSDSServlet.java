package nl.zeesoft.zsds;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		String dbg = config.getInitParameter("debug");
		if (dbg!=null && dbg.length()>0) {
			debug = Boolean.parseBoolean(dbg);
		}

		configuration = new AppConfiguration(installDir,debug);
		configuration.initialize();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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

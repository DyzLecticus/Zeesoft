package nl.zeesoft.zids.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zids.database.cache.ZIDSCacheConfig;
import nl.zeesoft.zids.database.model.ZIDSDataGenerator;
import nl.zeesoft.zids.database.model.ZIDSModel;
import nl.zeesoft.zids.dialog.pattern.PtnManager;
import nl.zeesoft.zids.server.requesthandler.RhGetCss;
import nl.zeesoft.zids.server.requesthandler.RhGetData;
import nl.zeesoft.zids.server.requesthandler.RhGetDialog;
import nl.zeesoft.zids.server.requesthandler.RhGetIndex;
import nl.zeesoft.zids.server.requesthandler.RhGetJavaScript;
import nl.zeesoft.zids.server.requesthandler.RhPostData;
import nl.zeesoft.zids.server.requesthandler.RhPostDialog;
import nl.zeesoft.zids.server.requesthandler.RhRequestHandlerObject;
import nl.zeesoft.zids.server.resource.RscErrorHtml;
import nl.zeesoft.zodb.database.server.SvrHTTPResourceFactory;

@SuppressWarnings("serial")
public class ZIDSServlet extends HttpServlet {
	private SvrSessionCleanUpWorker			sessionCleanUpWorker	= new SvrSessionCleanUpWorker();
	private List<RhRequestHandlerObject> 	requestHandlers			= new ArrayList<RhRequestHandlerObject>();

	@Override
	public void init(ServletConfig config) throws ServletException {
		String installDir = config.getInitParameter("installDir");
		if (installDir==null || installDir.length()==0) {
			installDir = config.getServletContext().getRealPath("");
		}
		String dbg = config.getInitParameter("debug");
		boolean debug = false;
		if (dbg!=null && dbg.length()>0) {
			debug = Boolean.parseBoolean(dbg);
		}
		SvrControllerDatabase.getInstance().setPatternManager(getNewPatternManager());
		SvrControllerDatabase.getInstance().setDataGenerator(getNewDataGenerator());
		if (SvrControllerDatabase.getInstance().start(installDir,getModelClassName(),getCacheConfigClassName(),debug)) {
			addRequestHandlers();
		}
		sessionCleanUpWorker.start();
	}

	@Override
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		RhRequestHandlerObject handler = getHandlerForRequestMethod(request,RhRequestHandlerObject.POST);
		if (handler==null) {
			RscErrorHtml error = new RscErrorHtml(getTitle(),getBackgroundColor(),"No handler found for request path: " + request.getServletPath());
			error.addToResponse(response);
		} else {
			handler.handleRequest(request, response);
		}
	}
	
	@Override
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		RhRequestHandlerObject handler = getHandlerForRequestMethod(request,RhRequestHandlerObject.GET);
		if (handler==null) {
			RscErrorHtml error = new RscErrorHtml(getTitle(),getBackgroundColor(),"No handler found for request path: " + request.getServletPath());
			error.addToResponse(response);
		} else {
			handler.handleRequest(request, response);
		}
	}

	@Override
	public void destroy() {
		sessionCleanUpWorker.stop();
		SvrControllerDatabase.getInstance().stop();
	}
	
	protected String getModelClassName() {
		return ZIDSModel.class.getName();
	}

	protected String getCacheConfigClassName() {
		return ZIDSCacheConfig.class.getName();
	}
	
	protected ZIDSDataGenerator getNewDataGenerator() {
		return new ZIDSDataGenerator();
	}
	
	protected PtnManager getNewPatternManager() {
		return new PtnManager();
	}

	protected String getTitle() {
		return "ZIDS";
	}
	
	protected String getBackgroundColor() {
		return SvrHTTPResourceFactory.BACKGROUND_COLOR;
	}
	
	protected final void addRequestHandler(RhRequestHandlerObject rh) {
		rh.setTitle(getTitle());
		rh.setBackgroundColor(getBackgroundColor());
		requestHandlers.add(rh);
	}
	
	protected void addRequestHandlers() {
		addRequestHandler(new RhGetData());
		addRequestHandler(new RhGetDialog());
		addRequestHandler(new RhGetIndex());
		addRequestHandler(new RhGetJavaScript());
		addRequestHandler(new RhGetCss());
		addRequestHandler(new RhPostData());
		addRequestHandler(new RhPostDialog());
	}
	
	private RhRequestHandlerObject getHandlerForRequestMethod(HttpServletRequest request,String method) {
		RhRequestHandlerObject handler = null;
		for (RhRequestHandlerObject rh: requestHandlers) {
			if (rh.getMethod().equals(method) && request.getServletPath().equals(rh.getFileAlias())) {
				handler = rh;
				break;
			}
		}
		if (handler==null) {
			for (RhRequestHandlerObject rh: requestHandlers) {
				if (rh.getMethod().equals(method) && 
					(request.getServletPath().startsWith(rh.getPath()) || request.getServletPath().startsWith(rh.getPathAlias())) && 
					request.getServletPath().endsWith(rh.getFile())
					) {
					handler = rh;
					break;
				}
			}
		}
		if (handler==null) {
			for (RhRequestHandlerObject rh: requestHandlers) {
				if (rh.getMethod().equals(method) && 
					(request.getServletPath().startsWith(rh.getPath()) || request.getServletPath().startsWith(rh.getPathAlias()))
					) {
					handler = rh;
					break;
				}
			}
		}
		return handler;
	}
}

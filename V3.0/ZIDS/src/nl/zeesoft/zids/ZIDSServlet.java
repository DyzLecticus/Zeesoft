package nl.zeesoft.zids;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zid.session.SessionDialogHandler;
import nl.zeesoft.zid.session.SessionManager;
import nl.zeesoft.zids.dialog.Dialogs;
import nl.zeesoft.zids.handler.ChatHandler;
import nl.zeesoft.zids.handler.DialogHandler;
import nl.zeesoft.zids.handler.HandlerObject;
import nl.zeesoft.zids.handler.IndexHandler;
import nl.zeesoft.zids.handler.SessionHandler;
import nl.zeesoft.zids.handler.ZIDSJavaScriptHandler;
import nl.zeesoft.zids.handler.ZIDSStyleSheetHandler;
import nl.zeesoft.zspr.pattern.PatternManager;

/**
 * Servlet implementation class ZIDSServlet
 */
@WebServlet("/ZIDSServlet")
public class ZIDSServlet extends HttpServlet {
	private static final long		serialVersionUID	= 1L;
    
	private String					installDir			= "";
	private boolean					debug				= false;
	private String					key					= "";
	
	private Messenger				messenger			= null;
	private WorkerUnion				union				= null;
	
	private PatternManager			patternManager		= null;
	private Dialogs					dialogs				= null;
	private SessionDialogHandler	dialogHandler		= null;
	private SessionManager			sessionManager		= null;
	
	private List<HandlerObject>		handlers			= null;
	
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
		
		initializeConfiguration();
		System.out.println("installDir: " + installDir);
		System.out.println("debug: " + debug);
		System.out.println("key: " + key);
		
		// Messenger
		ZDKFactory factory = new ZDKFactory();
		messenger = factory.getMessenger();
		messenger.setPrintDebugMessages(debug);
		messenger.start();

		// Union
		union = factory.getWorkerUnion(messenger);

		// Pattern manager
		messenger.debug(this,"Initializing pattern manager ...");
		patternManager = getNewPatternManager();
		messenger.debug(this,"Initialized pattern manager");
		
		// Dialogs
		messenger.debug(this,"Initializing dialogs ...");
		dialogs = getNewDialogs();
		for (Dialog dialog: dialogs.getDialogs()) {
			messenger.debug(this,"- " + dialog.getName() + " " + dialog.getLanguage().getCode());
		}
		messenger.debug(this,"Initialized dialogs");
		
		// Dialog handler
		messenger.debug(this,"Initializing dialog handler ...");
		dialogHandler = getNewDialogHandler();
		messenger.debug(this,"Initialized dialog handler");

		// Session manager
		messenger.debug(this,"Initializing session manager ...");
		sessionManager = getNewSessionManager();
		messenger.debug(this,"Initialized session manager");
		
		handlers = getNewHandlers();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HandlerObject handler = getHandlerForRequest(request);
		if (handler!=null) {
			handler.doGet(request, response);
		} else {
			// TODO Auto-generated method stub
			response.getWriter().append("Served at: ").append(request.getContextPath());
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HandlerObject handler = getHandlerForRequest(request);
		if (handler!=null) {
			handler.doPost(request, response);
		} else {
			// TODO Auto-generated method stub
			doGet(request, response);
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		messenger.stop();
		union.stopWorkers();
		messenger.whileWorking();
	}

	public String getInstallDir() {
		return installDir;
	}

	public boolean isDebug() {
		return debug;
	}

	public String getKey() {
		return key;
	}

	public Messenger getMessenger() {
		return messenger;
	}

	public WorkerUnion getUnion() {
		return union;
	}

	public PatternManager getPatternManager() {
		return patternManager;
	}
	
	public Dialogs getDialogs() {
		return dialogs;
	}

	public SessionDialogHandler getDialogHandler() {
		return dialogHandler;
	}

	public SessionManager getSessionManager() {
		return sessionManager;
	}

	protected PatternManager getNewPatternManager() {
		PatternManager manager = new PatternManager();
		manager.initializePatterns();
		return manager;
	}

	protected Dialogs getNewDialogs() {
		Dialogs dialogs = new Dialogs();
		String err = dialogs.initialize(getInstallDir());
		if (err.length()>0) {
			getMessenger().error(this,"Error while importing dialogs: " + err);
		}
		return dialogs;
	}

	protected SessionDialogHandler getNewDialogHandler() {
		SessionDialogHandler handler = new SessionDialogHandler(getMessenger(),getDialogs().getDialogs(),getPatternManager());
		handler.setMaxOutputSymbols(48);
		handler.initialize();
		return handler;
	}

	protected SessionManager getNewSessionManager() {
		return new SessionManager(getMessenger());
	}

	protected List<HandlerObject> getNewHandlers() {
		List<HandlerObject> handlers = new ArrayList<HandlerObject>();
		handlers.add(new IndexHandler(getMessenger()));
		handlers.add(new DialogHandler(getMessenger(),getDialogs()));
		handlers.add(new ChatHandler(getMessenger()));
		handlers.add(new ZIDSJavaScriptHandler(getMessenger()));
		handlers.add(new ZIDSStyleSheetHandler(getMessenger()));
		handlers.add(new SessionHandler(getMessenger(),getKey(),getSessionManager(),getDialogHandler()));
		return handlers;
	}
	
	private void initializeConfiguration() {
		String fileName = installDir + "config.json";
		File config = new File(fileName);
		JsFile file = new JsFile();
		ZStringEncoder encode = new ZStringEncoder();
		if (!config.exists()) {
			key = encode.generateNewKey(1024);
			encode.append(key);
			file.rootElement = new JsElem("configuration");
			file.rootElement.children.add(new JsElem("debug","" + debug));
			file.rootElement.children.add(new JsElem("key",encode.compress().toString(),true));
			file.toFile(fileName,true);
		} else {
			file.fromFile(fileName);
			debug = Boolean.parseBoolean(file.rootElement.getChildByName("debug").value.toString());
			encode.append(file.rootElement.getChildByName("key").value.toString());
			key = encode.decompress().toString();
		}
	}
	
	private HandlerObject getHandlerForRequest(HttpServletRequest request) {
		String path = request.getServletPath();
		if (path.equals("/")) {
			path = "/index.html";
		}
		HandlerObject r = null;
		for (HandlerObject handler: handlers) {
			if (path.equals(handler.getPath())) {
				r = handler; 
				break;
			}
		}
		return r;
	}
}

package nl.zeesoft.zodb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zodb.app.AppObject;
import nl.zeesoft.zodb.app.AppZODB;
import nl.zeesoft.zodb.app.handler.HandlerObject;
import nl.zeesoft.zodb.app.handler.HtmlAppIndexHandler;
import nl.zeesoft.zodb.db.Client;
import nl.zeesoft.zodb.db.ClientListener;
import nl.zeesoft.zodb.db.DatabaseRequest;

public class Config {
	private Messenger			messenger			= null;
	private WorkerUnion			union				= null;
	
	private boolean				debug				= false;
	private String				installDir			= "";
	private String				dataDir				= "data/";
	private String				servletUrl			= "http://127.0.0.1";
	private boolean				selfTest			= false;
	
	private List<AppObject>		applications		= new ArrayList<AppObject>();
	
	private HandlerObject		notFoundHtmlHandler	= null;
	private HandlerObject		notFoundJsonHandler	= null;
	private HandlerObject		appIndexHtmlHandler	= null;
	
	public Config() {
		ZDKFactory factory = new ZDKFactory();
		messenger = factory.getMessenger();
		messenger.setPrintDebugMessages(debug);
		messenger.start();
		union = factory.getWorkerUnion(messenger);
		addApplication(new AppZODB(this));
		addApplications();
	}
	
	public void initialize(boolean debug,String installDir,String servletUrl) {
		initialize(debug,installDir,servletUrl,true);
	}
	
	public void initialize(boolean debug,String installDir,String servletUrl, boolean write) {
		this.debug = debug;
		this.installDir = installDir;
		this.servletUrl = servletUrl;
		
		File cfg = new File(installDir + "config.json");
		if (cfg.exists()) {
			JsFile json = new JsFile();
			ZStringBuilder err = json.fromFile(cfg.getAbsolutePath());
			if (err.length()>0) {
				error(this,err.toString());
			} else {
				fromJson(json);
				messenger.setPrintDebugMessages(debug);
			}
		} else {
			selfTest = debug;
			messenger.setPrintDebugMessages(debug);
			if (write) {
				install(cfg.getAbsolutePath());
			}
		}
		
		messenger.start();

		for (AppObject app: applications) {
			debug(this,"Initializing " + app.name + " ...");
			app.initialize(write);
			debug(this,"Initialized " + app.name);
			if (app.name.equals(AppZODB.NAME)) {
				notFoundHtmlHandler = app.notFoundHtmlHandler;
				notFoundJsonHandler = app.notFoundJsonHandler;
			}
		}
		appIndexHtmlHandler = getNewHtmlAppIndexHandler();
	}
	
	public HandlerObject getHandlerForRequest(HttpServletRequest request) {
		HandlerObject r = null;
		if (request.getServletPath().equals("/") || request.getServletPath().equals("/index.html")) {
			r = appIndexHtmlHandler;
		} else {
			String name = getApplicationNameFromPath(request.getServletPath());
			AppObject app = getApplication(name);
			if (app!=null) {
				r = app.getHandlerForRequest(request);
			}
			if (r==null) {
				if (request.getServletPath().endsWith(".json")) {
					r = notFoundJsonHandler;
				} else {
					r = notFoundHtmlHandler;
				}
			}
		}
		return r;
	}
	
	public void destroy() {
		for (int i = applications.size() - 1; i >= 0; i--) {
			AppObject app = applications.get(i);
			debug(this,"Destroying " + app.name + " ...");
			app.destroy();
			debug(this,"Destroyed " + app.name);
			app.configuration = null;
		}
		applications.clear();
		messenger.stop();
		union.stopWorkers();
		messenger.whileWorking();
	}
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("debug","" + debug));
		json.rootElement.children.add(new JsElem("selfTest","" + selfTest));
		json.rootElement.children.add(new JsElem("dataDir",dataDir,true));
		json.rootElement.children.add(new JsElem("servletUrl",servletUrl,true));
		JsElem appsElem = new JsElem("applications",true);
		json.rootElement.children.add(appsElem);
		for (AppObject app: applications) {
			JsElem appElem = new JsElem();
			appsElem.children.add(appElem);
			JsFile appJson = app.toJson();
			appElem.children = appJson.rootElement.children;
		}
		return json;
	}
	
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			debug = json.rootElement.getChildBoolean("debug",debug);
			selfTest = json.rootElement.getChildBoolean("selfTest",selfTest);
			dataDir = json.rootElement.getChildString("dataDir",dataDir);
			servletUrl = json.rootElement.getChildString("servletUrl",servletUrl);
			JsElem appsElem = json.rootElement.getChildByName("applications");
			for (JsElem appElem: appsElem.children) {
				String name = appElem.getChildString("name");
				AppObject app = getApplication(name);
				if (app!=null) {
					JsFile appJson = new JsFile();
					appJson.rootElement = appElem;
					app.fromJson(appJson);
				} else {
					error(this,"Application not found: " + name);
				}
			}
		}
	}

	public AppObject getApplication(String name) {
		AppObject r = null;
		for (AppObject app: applications) {
			if (app.name.equals(name)) {
				r = app;
				break;
			}
		}
		return r;
	}

	public String getApplicationUrl(String name) {
		String r = "";
		AppObject app = getApplication(name);
		if (app!=null) {
			if (app.url.length()>0) {
				r = app.url;
			} else {
				r = servletUrl + "/" + app.name;
			}
		}
		return r;
	}

	public AppZODB getZODB() {
		AppZODB r = null;
		AppObject app = getApplication(AppZODB.NAME);
		if (app!=null && app instanceof AppZODB) {
			r = (AppZODB) app;
		}
		return r;
	}

	public void handleDatabaseRequest(DatabaseRequest request,ClientListener listener) {
		Client client = new Client(this);
		client.addListener(listener);
		client.handleRequest(request);
	}

	public String getFullDataDir() {
		return installDir + dataDir;
	}

	public void debug(Object source,String message) {
		if (debug) {
			messenger.debug(source,message);
		}
	}
	
	public void warn(Object source,String message) {
		messenger.warn(source,message);
	}

	public void error(Object source,String message) {
		messenger.error(source,message);
	}

	public void error(Object source,String message,Exception e) {
		messenger.error(source,message,e);
	}
	
	public Messenger getMessenger() {
		return messenger;
	}

	public WorkerUnion getUnion() {
		return union;
	}
	
	public String getDataDir() {
		return dataDir;
	}

	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public boolean isSelfTest() {
		return selfTest;
	}
	
	public void setSelfTest(boolean selfTest) {
		this.selfTest = selfTest;
	}

	public List<AppObject> getApplications() {
		return new ArrayList<AppObject>(applications);
	}
	
	protected HandlerObject getNewHtmlAppIndexHandler() {
		return new HtmlAppIndexHandler(this);
	}
	
	protected void install(String fileName) {
		debug(this,"Installing ...");
		JsFile json = toJson();
		ZStringBuilder err = json.toFile(fileName,true);
		if (err.length()==0) {
			for (AppObject app: applications) {
				debug(this,"Installing " + app.name + " ...");
				app.install();
				debug(this,"Installed " + app.name);
			}
		}
		if (err.length()>0) {
			error(this,err.toString());
		} else {
			debug(this,"Installed");
		}
	}
	
	protected void addApplications() {
		// Override to extend
	}
	
	protected void addApplication(AppObject app) {
		applications.add(app);
	}
	
	private String getApplicationNameFromPath(String path) {
		String r = "";
		String[] elems = path.split("/");
		String lastElem = elems[(elems.length - 1)];
		if (elems.length==2 && !lastElem.contains(".")) {
			r = lastElem;
		} else if (elems.length>=3) {
			r = elems[1];
		}
		return r;
	}
}

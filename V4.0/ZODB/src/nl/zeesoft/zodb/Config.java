package nl.zeesoft.zodb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsAbleClient;
import nl.zeesoft.zdk.json.JsAbleClientRequest;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseResponse;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.ModZODB;
import nl.zeesoft.zodb.mod.handler.HandlerObject;
import nl.zeesoft.zodb.mod.handler.HtmlModIndexHandler;
import nl.zeesoft.zodb.mod.handler.JsonZODBRequestHandler;

public class Config implements JsAble {
	private Messenger			messenger				= null;
	private WorkerUnion			union					= null;
	
	private boolean				write					= true;
	private boolean				debug					= false;
	private String				installDir				= "";
	private String				dataDir					= "data/";
	private String				servletUrl				= "http://127.0.0.1";
	private WhiteList			whiteList				= new WhiteList();
	
	private List<ModObject>		modules					= new ArrayList<ModObject>();
	
	private HandlerObject		notFoundHtmlHandler		= null;
	private HandlerObject		notFoundJsonHandler		= null;
	private HandlerObject		forbiddenHtmlHandler	= null;
	private HandlerObject		forbiddenJsonHandler	= null;
	private HandlerObject		modIndexHtmlHandler		= null;
	
	public Config() {
		ZDKFactory factory = new ZDKFactory();
		messenger = factory.getMessenger();
		union = factory.getWorkerUnion(messenger);
		whiteList.getList().add("127.0.0.1");
		whiteList.getList().add("0:0:0:0:0:0:0:1");
		addModule(new ModZODB(this));
		addModules();
	}
	
	public void initialize(boolean debug,String installDir,String servletUrl) {
		initialize(debug,installDir,servletUrl,true);
	}
	
	public void initialize(boolean debug,String installDir,String servletUrl, boolean write) {
		this.debug = debug;
		this.installDir = installDir;
		this.servletUrl = servletUrl;
		this.write = write;
		
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
			messenger.setPrintDebugMessages(debug);
			if (write) {
				install();
			}
		}
		
		messenger.start();

		for (ModObject mod: modules) {
			debug(this,"Initializing " + mod.name + " ...");
			mod.initialize();
			debug(this,"Initialized " + mod.name);
			if (mod.name.equals(ModZODB.NAME)) {
				notFoundHtmlHandler = mod.notFoundHtmlHandler;
				notFoundJsonHandler = mod.notFoundJsonHandler;
				forbiddenHtmlHandler = mod.forbiddenHtmlHandler;
				forbiddenJsonHandler = mod.forbiddenJsonHandler;
			}
		}
		modIndexHtmlHandler = getNewHtmlAppIndexHandler();
	}
	
	public HandlerObject getHandlerForRequest(HttpServletRequest request) {
		HandlerObject r = null;
		String path = request.getServletPath().toLowerCase();
		if (!whiteList.isAllowed(request.getRemoteAddr())) {
			if (path.endsWith(".json")) {
				r = forbiddenJsonHandler;
			} else {
				r = forbiddenHtmlHandler;
			}
		} else {
			if (path.equals("/") || path.equals("/index.html")) {
				r = modIndexHtmlHandler;
			} else {
				String name = getModuleNameFromPath(path);
				ModObject mod = getModule(name);
				if (mod!=null) {
					r = mod.getHandlerForRequest(request);
				}
				if (r==null) {
					if (path.endsWith(".json")) {
						r = notFoundJsonHandler;
					} else {
						r = notFoundHtmlHandler;
					}
				}
			}
		}
		return r;
	}
	
	public void destroy() {
		for (int i = modules.size() - 1; i >= 0; i--) {
			ModObject mod = modules.get(i);
			debug(this,"Destroying " + mod.name + " ...");
			mod.destroy();
			debug(this,"Destroyed " + mod.name);
			mod.configuration = null;
		}
		modules.clear();
		messenger.stop();
		union.stopWorkers();
		messenger.handleMessages();
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("debug","" + debug));
		json.rootElement.children.add(new JsElem("dataDir",dataDir,true));
		json.rootElement.children.add(new JsElem("servletUrl",servletUrl,true));
		if (whiteList.getList().size()>0) {
			JsFile wl = whiteList.toJson();
			json.rootElement.children.add(wl.rootElement);
		}
		JsElem modsElem = new JsElem("modules",true);
		json.rootElement.children.add(modsElem);
		for (ModObject mod: modules) {
			JsElem modElem = new JsElem();
			modsElem.children.add(modElem);
			JsFile modJson = mod.toJson();
			modElem.children = modJson.rootElement.children;
		}
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			debug = json.rootElement.getChildBoolean("debug",debug);
			dataDir = json.rootElement.getChildString("dataDir",dataDir);
			servletUrl = json.rootElement.getChildString("servletUrl",servletUrl);
			whiteList.fromJson(json);
			JsElem modsElem = json.rootElement.getChildByName("modules");
			if (modsElem!=null) {
				for (JsElem modElem: modsElem.children) {
					String name = modElem.getChildString("name");
					ModObject mod = getModule(name);
					if (mod!=null) {
						JsFile modJson = new JsFile();
						modJson.rootElement = modElem;
						mod.fromJson(modJson);
					} else {
						error(this,"Module not found: " + name);
					}
				}
			}
		}
	}
	
	public ModObject getModule(String name) {
		ModObject r = null;
		for (ModObject mod: modules) {
			if (mod.name.equalsIgnoreCase(name)) {
				r = mod;
				break;
			}
		}
		return r;
	}

	public String getModuleUrl(String name) {
		String r = "";
		ModObject mod = getModule(name);
		if (mod!=null) {
			if (mod.url.length()>0) {
				r = mod.url;
			} else {
				r = servletUrl + "/" + mod.name;
			}
		}
		return r;
	}

	public ModZODB getZODB() {
		ModZODB r = null;
		ModObject mod = getModule(ModZODB.NAME);
		if (mod!=null && mod instanceof ModZODB) {
			r = (ModZODB) mod;
		}
		return r;
	}
	
	public StringBuilder getZODBKey() {
		StringBuilder r = new StringBuilder();
		ModZODB zodb = getZODB();
		if (zodb!=null) {
			r = zodb.getKey();
		}
		return r;
	}

	public void setZODBKey(StringBuilder key) {
		ModZODB zodb = getZODB();
		if (zodb!=null) {
			zodb.setKey(key);
		}
	}

	public ZStringBuilder rewriteConfig() {
		ZStringBuilder err = writeConfig();
		if (err.length()>0) {
			messenger.error(this,"Error writing configuration: " + err);
		}
		return err;
	}

	public void handleDatabaseRequest(DatabaseRequest request,JsClientListener listener) {
		handleDatabaseRequest(request,listener,10);
	}

	public void handleDatabaseRequest(DatabaseRequest request,JsClientListener listener,int timeoutSeconds) {
		JsAbleClient client = new JsAbleClient(getMessenger(),getUnion());
		client.addJsClientListener(listener);
		DatabaseResponse response = new DatabaseResponse();
		response.request = request;
		client.handleRequest(request,getModuleUrl(ModZODB.NAME) + JsonZODBRequestHandler.PATH,response,timeoutSeconds);
	}

	public DatabaseResponse handledDatabaseRequest(JsClientResponse response) {
		DatabaseResponse r = null;
		if (response.error.length()==0 &&
			response.request instanceof JsAbleClientRequest &&
			((JsAbleClientRequest) response.request).resObject instanceof DatabaseResponse
			) {
			r = (DatabaseResponse) ((JsAbleClientRequest) response.request).resObject;
		}
		return r;
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

	public List<ModObject> getModules() {
		return new ArrayList<ModObject>(modules);
	}

	protected HandlerObject getNewHtmlAppIndexHandler() {
		return new HtmlModIndexHandler(this);
	}

	protected ZStringBuilder writeConfig() {
		ZStringBuilder err = new ZStringBuilder();
		if (write) {
			File cfg = new File(installDir + "config.json");
			JsFile json = toJson();
			err = json.toFile(cfg.getAbsolutePath(),true);
		}
		return err;
	}
	
	protected void install() {
		debug(this,"Installing ...");
		ZStringBuilder err = writeConfig();
		if (err.length()==0) {
			for (ModObject mod: modules) {
				debug(this,"Installing " + mod.name + " ...");
				mod.install();
				debug(this,"Installed " + mod.name);
			}
		}
		if (err.length()>0) {
			error(this,err.toString());
		} else {
			debug(this,"Installed");
		}
	}

	protected void addModules() {
		// Override to extend
	}
	
	protected void addModule(ModObject mod) {
		modules.add(mod);
	}
	
	private String getModuleNameFromPath(String path) {
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

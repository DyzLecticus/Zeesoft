package nl.zeesoft.zsds;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.SequencePreprocessor;
import nl.zeesoft.zsd.dialog.DialogHandlerConfiguration;
import nl.zeesoft.zsd.dialog.DialogSet;
import nl.zeesoft.zsd.util.LanguageJsonGenerator;
import nl.zeesoft.zsds.handler.BaseJavaScriptHandler;
import nl.zeesoft.zsds.handler.BaseStyleSheetHandler;
import nl.zeesoft.zsds.handler.HandlerObject;
import nl.zeesoft.zsds.handler.HtmlIndexHandler;
import nl.zeesoft.zsds.handler.HtmlNotFoundHandler;
import nl.zeesoft.zsds.handler.HtmlTestHandler;
import nl.zeesoft.zsds.handler.JsonConfigHandler;
import nl.zeesoft.zsds.handler.JsonDialogRequestHandler;
import nl.zeesoft.zsds.handler.JsonDialogsHandler;
import nl.zeesoft.zsds.handler.JsonNotFoundHandler;

public class AppConfiguration {
	private Messenger					messenger				= null;
	private WorkerUnion					union					= null;
	private String						installDir				= "";
	private boolean						debug					= false;
	
	private BaseConfiguration			baseConfig				= null;
	private AppStateManager				stateManager			= null;
	
	private List<HandlerObject>			handlers				= null;
	private HandlerObject				notFoundHtmlHandler		= null;
	private HandlerObject				notFoundJsonHandler		= null;

	public AppConfiguration(String installDir,boolean debug) {
		handlers = getDefaultHandlers();
		for (HandlerObject handler: handlers) {
			if (handler instanceof HtmlNotFoundHandler) {
				notFoundHtmlHandler = handler;
			}
			if (handler instanceof JsonNotFoundHandler) {
				notFoundJsonHandler = handler;
			}
			if (notFoundHtmlHandler!=null && notFoundJsonHandler!=null) {
				break;
			}
		}
		ZDKFactory factory = new ZDKFactory();
		messenger = factory.getMessenger();
		messenger.setPrintDebugMessages(debug);
		messenger.start();
		union = factory.getWorkerUnion(messenger);
		this.installDir = installDir;
		this.debug = debug;
		stateManager = new AppStateManager(this);
	}

	public void initialize() {
		baseConfig = getNewBaseConfiguration();
		String fileName = installDir + "config.json";
		File file = new File(fileName);
		if (!file.exists()) {
			debug(this,"Installing ...");
			baseConfig.setDebug(debug);
			baseConfig.setDataDir(installDir + "data/");
			baseConfig.setGenerateReadFormat(debug);
			baseConfig.toJson().toFile(fileName,true);
			
			File dir = new File(baseConfig.getFullBaseDir());
			dir.mkdirs();
			dir = new File(baseConfig.getFullOverrideDir());
			dir.mkdirs();
			dir = new File(baseConfig.getFullExtendDir());
			dir.mkdirs();
			stateManager.generate();
			
			debug(this,"Installed");
		} else {
			JsFile json = new JsFile();
			ZStringBuilder err = json.fromFile(fileName);
			if (err.length()>0) {
				messenger.error(this,err.toString());
			} else {
				baseConfig.fromJson(json);
				debug = baseConfig.isDebug();
				messenger.setPrintDebugMessages(debug);
			}
		}

		messenger.setPrintDebugMessages(debug);
		messenger.start();
		
		stateManager.load();
	}

	public boolean isInitialized() {
		return stateManager.isInitialized();
	}

	public void destroy() {
		messenger.stop();
		union.stopWorkers();
		messenger.whileWorking();
	}
	
	public void debug(Object source, String msg) {
		if (messenger!=null) {
			messenger.debug(source,msg);
		}
	}

	public Messenger getMessenger() {
		return messenger;
	}

	public HandlerObject getHandlerForRequest(HttpServletRequest request) {
		HandlerObject r = null;
		String path = request.getServletPath();
		if (path.equals("/")) {
			path = "/index.html";
		}
		for (HandlerObject handler: handlers) {
			if (path.equals(handler.getPath())) {
				r = handler; 
				break;
			}
		}
		if (r==null) {
			if (!path.endsWith(".json")) {
				r = notFoundHtmlHandler;
			} else {
				r = notFoundJsonHandler;
			}
		}
		return r;
	}
	
	public WorkerUnion getUnion() {
		return union;
	}

	public String getInstallDir() {
		return installDir;
	}

	public boolean isDebug() {
		return debug;
	}

	public BaseConfiguration getBaseConfig() {
		return baseConfig;
	}

	public DialogHandlerConfiguration getDialogHandlerConfig() {
		return stateManager.getDialogHandlerConfig();
	}
	
	protected BaseConfiguration getNewBaseConfiguration() {
		return new BaseConfiguration();
	}

	protected DialogHandlerConfiguration buildNewDialogHandlerConfiguration() {
		DialogHandlerConfiguration r = getNewDialogHandlerConfiguration();
		r.setLanguagePreprocessor(getNewSequencePreprocessor());
		r.setEntityValueTranslator(getNewEntityValueTranslator());
		r.setDialogSet(getNewDialogSet());
		return new DialogHandlerConfiguration(messenger,union,baseConfig);
	}

	protected DialogHandlerConfiguration getNewDialogHandlerConfiguration() {
		return new DialogHandlerConfiguration(messenger,union,baseConfig);
	}
	
	protected SequencePreprocessor getNewSequencePreprocessor() {
		return new SequencePreprocessor();
	}

	protected EntityValueTranslator getNewEntityValueTranslator() {
		return new EntityValueTranslator();
	}

	protected DialogSet getNewDialogSet() {
		return new DialogSet();
	}

	protected LanguageJsonGenerator getNewLanguageJsonGenerator() {
		return new LanguageJsonGenerator();
	}
	
	protected List<HandlerObject> getDefaultHandlers() {
		List<HandlerObject> r = new ArrayList<HandlerObject>();
		r.add(new BaseStyleSheetHandler(this));
		r.add(new BaseJavaScriptHandler(this));
		r.add(new HtmlNotFoundHandler(this));
		r.add(new HtmlIndexHandler(this));
		r.add(new HtmlTestHandler(this));
		r.add(new JsonNotFoundHandler(this));
		r.add(new JsonConfigHandler(this));
		r.add(new JsonDialogsHandler(this));
		r.add(new JsonDialogRequestHandler(this));
		return r;
	}
}

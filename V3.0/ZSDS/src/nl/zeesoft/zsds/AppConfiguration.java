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
import nl.zeesoft.zsd.dialog.DialogHandlerTesterInitializer;
import nl.zeesoft.zsd.dialog.DialogSet;
import nl.zeesoft.zsd.interpret.SequenceInterpreterTester;
import nl.zeesoft.zsd.interpret.SequenceInterpreterTesterInitializer;
import nl.zeesoft.zsd.util.LanguageJsonGenerator;
import nl.zeesoft.zsds.dialogs.State;
import nl.zeesoft.zsds.handler.ApiClientJavaScriptHandler;
import nl.zeesoft.zsds.handler.BaseJavaScriptHandler;
import nl.zeesoft.zsds.handler.BaseStyleSheetHandler;
import nl.zeesoft.zsds.handler.EnvironmentJavaScriptHandler;
import nl.zeesoft.zsds.handler.HandlerObject;
import nl.zeesoft.zsds.handler.HtmlEnvironmentHandler;
import nl.zeesoft.zsds.handler.HtmlIndexHandler;
import nl.zeesoft.zsds.handler.HtmlNotFoundHandler;
import nl.zeesoft.zsds.handler.HtmlStateHandler;
import nl.zeesoft.zsds.handler.HtmlTestHandler;
import nl.zeesoft.zsds.handler.JsonAppTesterHandler;
import nl.zeesoft.zsds.handler.JsonConfigHandler;
import nl.zeesoft.zsds.handler.JsonDialogRequestHandler;
import nl.zeesoft.zsds.handler.JsonDialogsHandler;
import nl.zeesoft.zsds.handler.JsonGenerateHandler;
import nl.zeesoft.zsds.handler.JsonNotFoundHandler;
import nl.zeesoft.zsds.handler.JsonRebaseHandler;
import nl.zeesoft.zsds.handler.JsonReloadHandler;
import nl.zeesoft.zsds.handler.JsonSelfTestSummaryHandler;
import nl.zeesoft.zsds.handler.JsonStateHandler;
import nl.zeesoft.zsds.handler.JsonTestConfigHandler;
import nl.zeesoft.zsds.handler.JsonTestDialogRequestHandler;
import nl.zeesoft.zsds.handler.TestJavaScriptHandler;
import nl.zeesoft.zsds.tester.AppTester;

public class AppConfiguration {
	public static final String			PARAMETER_SELF_PORT_NUMBER			= "selfPortNumber"; 
	public static final String			PARAMETER_SELF_URL					= "selfUrl"; 
	
	private Messenger					messenger							= null;
	private WorkerUnion					union								= null;
	private String						installDir							= "";
	private boolean						debug								= false;
	
	private AppBaseConfiguration		base								= null;
	private AppStateManager				stateManager						= null;
	private AppTester					appTester							= null;
	
	private List<HandlerObject>			handlers							= null;
	private HandlerObject				notFoundHtmlHandler					= null;
	private HandlerObject				notFoundJsonHandler					= null;

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
	}

	public void setStateManager(AppStateManager stateManager) {
		this.stateManager = stateManager;
	}
	
	public void initialize(String contextPath) {
		base = getNewAppBaseConfiguration();
		String fileName = installDir + "config.json";
		File file = new File(fileName);
		String port = "";
		if (debug) {
			port = "8080";
		}
		if (!file.exists()) {
			debug(this,"Installing ...");
			base.setDebug(debug);
			base.setSelfTest(debug);
			base.setGenerateReadFormat(debug);
			base.setDataDir(installDir + "data/");
			base.getParameters().put(PARAMETER_SELF_PORT_NUMBER,port);
			ZStringBuilder err = base.toJson().toFile(fileName,true);
			if (err.length()>0) {
				messenger.error(this,err.toString());
			}
			File dir = new File(base.getFullBaseDir());
			dir.mkdirs();
			dir = new File(base.getFullOverrideDir());
			dir.mkdirs();
			dir = new File(base.getFullExtendDir());
			dir.mkdirs();
				
			messenger.setPrintDebugMessages(debug);
			addStateMasterContext();
			stateManager.generate(true,false);
			
			if (err.length()==0) {
				debug(this,"Installed");
			}
		} else {
			JsFile json = new JsFile();
			ZStringBuilder err = json.fromFile(fileName);
			if (err.length()>0) {
				messenger.error(this,err.toString());
			} else {
				base.fromJson(json);
				debug = base.isDebug();
				messenger.setPrintDebugMessages(debug);
				addStateMasterContext();
				stateManager.load();
			}
		}

		if (debug) {
			port = "8080";
		}
		if (base.getParameters().containsKey(PARAMETER_SELF_PORT_NUMBER)) {
			port = base.getParameters().get(PARAMETER_SELF_PORT_NUMBER);
		}
		if (port.length()>0) {
			port = ":" + port;
		}
		String selfUrl = "http://localhost" + port + contextPath;
		base.getParameters().put(PARAMETER_SELF_URL,selfUrl);
		
		messenger.start();
		
		appTester = getNewAppTester();
		appTester.initialize(selfUrl);
	}
	
	public boolean isInitialized() {
		return stateManager.isInitialized();
	}

	public boolean generate() {
		return stateManager.generate(true,true);
	}
	
	public boolean reload() {
		return stateManager.reload();
	}

	public boolean rebase() {
		return stateManager.rebase();
	}

	public boolean isReloading() {
		return stateManager.isReloading();
	}

	public boolean isGenerating() {
		return stateManager.isGenerating();
	}

	public boolean isTesting() {
		return stateManager.isTesting();
	}

	public boolean isBusy() {
		return stateManager.isBusy();
	}
	
	public void destroy() {
		SequenceInterpreterTester tester = stateManager.getTester();
		if (tester!=null && tester.isTesting()) {
			tester.stop();
		}
		appTester.stopAllTesters();
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

	public AppBaseConfiguration getBase() {
		return base;
	}
	
	public AppTester getAppTester() {
		return appTester;
	}

	public DialogHandlerConfiguration getDialogHandlerConfig() {
		return stateManager.getDialogHandlerConfig();
	}
	
	public SequenceInterpreterTester getTester() {
		return stateManager.getTester();
	}
	
	public String getLastModifiedHeader() {
		return stateManager.getLastModifiedHeader();
	}
	
	protected AppBaseConfiguration getNewAppBaseConfiguration() {
		return new AppBaseConfiguration();
	}

	protected AppTester getNewAppTester() {
		return new AppTester(messenger,union,base);
	}

	protected DialogHandlerConfiguration buildNewDialogHandlerConfiguration() {
		DialogHandlerConfiguration r = getNewDialogHandlerConfiguration();
		r.setLanguagePreprocessor(getNewSequencePreprocessor());
		r.setEntityValueTranslator(getNewEntityValueTranslator());
		r.setDialogSet(getNewDialogSet());
		return r;
	}

	protected DialogHandlerConfiguration getNewDialogHandlerConfiguration() {
		return new DialogHandlerConfiguration(messenger,union,base);
	}

	protected SequenceInterpreterTesterInitializer getNewSequenceInterpreterTesterInitializer(DialogHandlerConfiguration configuration) {
		return new DialogHandlerTesterInitializer(messenger,union,configuration);
	}

	protected SequencePreprocessor getNewSequencePreprocessor() {
		return new SequencePreprocessor();
	}

	protected EntityValueTranslator getNewEntityValueTranslator() {
		return new EntityValueTranslator();
	}

	protected DialogSet getNewDialogSet() {
		return new AppDialogSet();
	}

	protected LanguageJsonGenerator getNewLanguageJsonGenerator() {
		return new LanguageJsonGenerator();
	}
	
	protected HandlerObject getNewApiClientHandler() {
		return new ApiClientJavaScriptHandler(this);
	}
	
	protected List<HandlerObject> getDefaultHandlers() {
		List<HandlerObject> r = new ArrayList<HandlerObject>();
		// Main application
		r.add(new BaseStyleSheetHandler(this));
		r.add(new BaseJavaScriptHandler(this));
		r.add(getNewApiClientHandler());
		r.add(new TestJavaScriptHandler(this));
		r.add(new HtmlNotFoundHandler(this));
		r.add(new HtmlIndexHandler(this));
		r.add(new HtmlTestHandler(this));
		r.add(new HtmlStateHandler(this));
		r.add(new JsonNotFoundHandler(this));
		r.add(new JsonConfigHandler(this));
		r.add(new JsonDialogsHandler(this));
		r.add(new JsonTestDialogRequestHandler(this));
		r.add(new JsonDialogRequestHandler(this));
		r.add(new JsonStateHandler(this));
		r.add(new JsonGenerateHandler(this));
		r.add(new JsonReloadHandler(this));
		r.add(new JsonRebaseHandler(this));
		r.add(new JsonSelfTestSummaryHandler(this));
		// Test application
		r.add(new EnvironmentJavaScriptHandler(this));
		r.add(new HtmlEnvironmentHandler(this));
		r.add(new JsonTestConfigHandler(this));
		r.add(new JsonAppTesterHandler(this));
		return r;
	}
	
	private void addStateMasterContext() {
		if (debug && base.isSelfTest()) {
			if (base.getSupportedLanguages().contains(BaseConfiguration.LANG_ENG)) {
				List<String> mcs = base.getSupportedMasterContexts().get(BaseConfiguration.LANG_ENG);
				if (!mcs.contains(State.MASTER_CONTEXT_STATE)) {
					mcs.add(State.MASTER_CONTEXT_STATE);
				}
			}
			if (base.getSupportedLanguages().contains(BaseConfiguration.LANG_NLD)) {
				List<String> mcs = base.getSupportedMasterContexts().get(BaseConfiguration.LANG_NLD);
				if (!mcs.contains(State.MASTER_CONTEXT_STATE)) {
					mcs.add(State.MASTER_CONTEXT_STATE);
				}
			}
		}
	}
}

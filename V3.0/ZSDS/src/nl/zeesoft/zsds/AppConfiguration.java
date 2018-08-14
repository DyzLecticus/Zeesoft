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
import nl.zeesoft.zsds.handler.BaseJavaScriptHandler;
import nl.zeesoft.zsds.handler.BaseStyleSheetHandler;
import nl.zeesoft.zsds.handler.HandlerObject;
import nl.zeesoft.zsds.handler.HtmlIndexHandler;
import nl.zeesoft.zsds.handler.HtmlNotFoundHandler;
import nl.zeesoft.zsds.handler.HtmlStateHandler;
import nl.zeesoft.zsds.handler.HtmlTestHandler;
import nl.zeesoft.zsds.handler.JsonConfigHandler;
import nl.zeesoft.zsds.handler.JsonDialogRequestHandler;
import nl.zeesoft.zsds.handler.JsonDialogsHandler;
import nl.zeesoft.zsds.handler.JsonGenerateHandler;
import nl.zeesoft.zsds.handler.JsonNotFoundHandler;
import nl.zeesoft.zsds.handler.JsonRebaseHandler;
import nl.zeesoft.zsds.handler.JsonReloadHandler;
import nl.zeesoft.zsds.handler.JsonSelfTestHandler;
import nl.zeesoft.zsds.handler.JsonStateHandler;
import nl.zeesoft.zsds.handler.JsonTestDialogRequestHandler;

public class AppConfiguration {
	public static final String			PARAMETER_SELF_TEST_SUMMARY_URL		= "selfTestSummaryUrl"; 
	
	private Messenger					messenger							= null;
	private WorkerUnion					union								= null;
	private String						installDir							= "";
	private boolean						debug								= false;
	
	private BaseConfiguration			baseConfig							= null;
	private AppStateManager				stateManager						= null;
	
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
	
	public void initialize() {
		baseConfig = getNewBaseConfiguration();
		String fileName = installDir + "config.json";
		File file = new File(fileName);
		if (!file.exists()) {
			debug(this,"Installing ...");
			baseConfig.setDebug(debug);
			baseConfig.setDataDir(installDir + "data/");
			baseConfig.setGenerateReadFormat(debug);
			baseConfig.getParameters().put(PARAMETER_SELF_TEST_SUMMARY_URL,"http://localhost:8080/ZSDS/selfTestSummary.json");
			baseConfig.toJson().toFile(fileName,true);
			
			File dir = new File(baseConfig.getFullBaseDir());
			dir.mkdirs();
			dir = new File(baseConfig.getFullOverrideDir());
			dir.mkdirs();
			dir = new File(baseConfig.getFullExtendDir());
			dir.mkdirs();
			stateManager.generate(true,false);
			
			debug(this,"Installed");
		} else {
			JsFile json = new JsFile();
			ZStringBuilder err = json.fromFile(fileName);
			if (err.length()>0) {
				messenger.error(this,err.toString());
			} else {
				baseConfig.fromJson(json);
				debug = baseConfig.isDebug();
				if (debug) {
					if (baseConfig.getSupportedLanguages().contains(BaseConfiguration.LANG_ENG)) {
						List<String> mcs = baseConfig.getSupportedMasterContexts().get(BaseConfiguration.LANG_ENG);
						if (!mcs.contains(State.MASTER_CONTEXT_STATE)) {
							mcs.add(State.MASTER_CONTEXT_STATE);
						}
					}
					if (baseConfig.getSupportedLanguages().contains(BaseConfiguration.LANG_NLD)) {
						List<String> mcs = baseConfig.getSupportedMasterContexts().get(BaseConfiguration.LANG_NLD);
						if (!mcs.contains(State.MASTER_CONTEXT_STATE)) {
							mcs.add(State.MASTER_CONTEXT_STATE);
						}
					}
				}
				messenger.setPrintDebugMessages(debug);
				stateManager.load();
			}
		}
				
		messenger.setPrintDebugMessages(debug);
		messenger.start();
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

	public boolean isBusy() {
		return stateManager.isBusy();
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
	
	public SequenceInterpreterTester getTester() {
		return stateManager.getTester();
	}
	
	public String getLastModifiedHeader() {
		return stateManager.getLastModifiedHeader();
	}
	
	protected BaseConfiguration getNewBaseConfiguration() {
		return new BaseConfiguration();
	}

	protected DialogHandlerConfiguration buildNewDialogHandlerConfiguration() {
		DialogHandlerConfiguration r = getNewDialogHandlerConfiguration();
		r.setLanguagePreprocessor(getNewSequencePreprocessor());
		r.setEntityValueTranslator(getNewEntityValueTranslator());
		r.setDialogSet(getNewDialogSet());
		return r;
	}

	protected DialogHandlerConfiguration getNewDialogHandlerConfiguration() {
		return new DialogHandlerConfiguration(messenger,union,baseConfig);
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
	
	protected List<HandlerObject> getDefaultHandlers() {
		List<HandlerObject> r = new ArrayList<HandlerObject>();
		r.add(new BaseStyleSheetHandler(this));
		r.add(new BaseJavaScriptHandler(this));
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
		r.add(new JsonSelfTestHandler(this));
		return r;
	}
}

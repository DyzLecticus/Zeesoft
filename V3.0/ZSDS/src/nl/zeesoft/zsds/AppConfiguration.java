package nl.zeesoft.zsds;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.SequencePreprocessor;
import nl.zeesoft.zsd.dialog.DialogHandlerConfiguration;
import nl.zeesoft.zsd.dialog.DialogSet;
import nl.zeesoft.zsd.initialize.InitializeClass;
import nl.zeesoft.zsd.initialize.InitializerListener;
import nl.zeesoft.zsd.util.LanguageJsonGenerator;
import nl.zeesoft.zsds.handler.HandlerObject;
import nl.zeesoft.zsds.handler.IndexHandler;
import nl.zeesoft.zsds.handler.JsonConfigHandler;

public class AppConfiguration implements InitializerListener {
	private Messenger					messenger				= null;
	private WorkerUnion					union					= null;
	private String						installDir				= "";
	private boolean						debug					= false;
	
	private BaseConfiguration			baseConfig				= null;
	private AppStateManager				stateManager			= null;
	
	//private DialogHandlerConfiguration	dialogHandlerConfig		= null;
	
	private List<HandlerObject>			handlers				= null;

	public AppConfiguration(String installDir,boolean debug) {
		handlers = getDefaultHandlers();
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
		messenger.setPrintDebugMessages(debug);
		messenger.start();
		
		baseConfig = getNewBaseConfiguration();
		String fileName = installDir + "config.json";
		File file = new File(fileName);
		if (!file.exists()) {
			debug(this,"Installing ...");
			baseConfig.setBaseDir(installDir + "data/" + baseConfig.getBaseDir());
			baseConfig.setOverrideDir(installDir + "data/" + baseConfig.getOverrideDir());
			baseConfig.setExtendDir(installDir + "data/" + baseConfig.getExtendDir());
			baseConfig.setGenerateReadFormat(debug);
			baseConfig.toJson().toFile(fileName,true);
			
			File dir = new File(baseConfig.getBaseDir());
			dir.mkdirs();
			dir = new File(baseConfig.getOverrideDir());
			dir.mkdirs();
			dir = new File(baseConfig.getExtendDir());
			dir.mkdirs();
			stateManager.generate();
			
			debug(this,"Installed");
		} else {
			JsFile json = new JsFile();
			json.fromFile(fileName);
			baseConfig.fromJson(json);
		}
		
		stateManager.load();
	}

	@Override
	public void initializedClass(InitializeClass cls, boolean done) {
		if (done) {
			debug(this,"Initialized.");
		}
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
		r.add(new IndexHandler(this));
		r.add(new JsonConfigHandler(this));
		return r;
	}
}

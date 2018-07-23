package nl.zeesoft.zsds;

import java.io.File;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.SequencePreprocessor;
import nl.zeesoft.zsd.dialog.DialogHandlerConfiguration;
import nl.zeesoft.zsd.dialog.DialogSet;
import nl.zeesoft.zsd.util.LanguageJsonGenerator;

public class AppConfiguration {
	private Messenger					messenger				= null;
	private WorkerUnion					union					= null;
	private String						installDir				= "";
	private boolean						debug					= false;
	
	private BaseConfiguration			baseConfig				= null;
	private DialogHandlerConfiguration	dialogHandlerConfig		= null;

	public AppConfiguration(String installDir,boolean debug) {
		ZDKFactory factory = new ZDKFactory();
		messenger = factory.getMessenger();
		messenger.setPrintDebugMessages(debug);
		messenger.start();
		union = factory.getWorkerUnion(messenger);
		this.installDir = installDir;
		this.debug = debug;
	}

	public void initialize() {
		messenger.setPrintDebugMessages(debug);
		messenger.start();
		
		baseConfig = getNewBaseConfiguration();
		String fileName = installDir + "config.json";
		File file = new File(fileName);
		if (!file.exists()) {
			debug(this,"Installing ...");
			baseConfig.setBaseDir(installDir + baseConfig.getBaseDir());
			baseConfig.setOverrideDir(installDir + baseConfig.getOverrideDir());
			baseConfig.setExtendDir(installDir + baseConfig.getExtendDir());
			baseConfig.setGenerateReadFormat(debug);
			baseConfig.toJson().toFile(fileName,true);
			generateBaseData();
			debug(this,"Installed");
		} else {
			JsFile json = new JsFile();
			json.fromFile(fileName);
			baseConfig.fromJson(json);
		}

		debug(this,"Initializing ...");
		dialogHandlerConfig = getNewDialogHandlerConfiguration();
		dialogHandlerConfig.setLanguagePreprocessor(getNewSequencePreprocessor());
		dialogHandlerConfig.setEntityValueTranslator(getNewEntityValueTranslator());
		dialogHandlerConfig.setDialogSet(getNewDialogSet());
		dialogHandlerConfig.initialize();
		debug(this,"Initialized.");
	}

	public boolean isInitialized() {
		return dialogHandlerConfig.isDone();
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
	
	public void generateBaseData() {
		LanguageJsonGenerator generator = new LanguageJsonGenerator();
		SequencePreprocessor sp = getNewSequencePreprocessor();
		EntityValueTranslator t = getNewEntityValueTranslator();
		DialogSet ds = getNewDialogSet();
		generator.generate(baseConfig,sp,t,ds,baseConfig.getBaseDir());
	}

	public Messenger getMessenger() {
		return messenger;
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
		return dialogHandlerConfig;
	}
	
	protected BaseConfiguration getNewBaseConfiguration() {
		return new BaseConfiguration();
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
}

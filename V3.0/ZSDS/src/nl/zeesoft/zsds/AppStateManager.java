package nl.zeesoft.zsds;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.SequencePreprocessor;
import nl.zeesoft.zsd.dialog.DialogHandlerConfiguration;
import nl.zeesoft.zsd.dialog.DialogSet;
import nl.zeesoft.zsd.initialize.InitializeClass;
import nl.zeesoft.zsd.initialize.InitializerListener;
import nl.zeesoft.zsd.interpret.SequenceInterpreterTester;
import nl.zeesoft.zsd.interpret.SequenceInterpreterTesterInitializer;
import nl.zeesoft.zsd.interpret.TesterListener;
import nl.zeesoft.zsd.util.LanguageJsonGenerator;

public class AppStateManager extends Locker implements InitializerListener, TesterListener {
	private AppConfiguration						configuration				= null;
	private DataGenerator							generator					= null;

	private DialogHandlerConfiguration				dialogHandlerConfig			= null;
	private DialogHandlerConfiguration				dialogHandlerConfigLoad		= null;
	private boolean									reload						= false;
	private boolean									reading						= false;
	private boolean									writing						= false;
	private String									lastModifiedHeader			= getLastModifiedDateString();

	private boolean									testing						= false;
	private SequenceInterpreterTester				tester						= null;
	private SequenceInterpreterTesterInitializer	testerInitializer			= null;
	
	public AppStateManager(AppConfiguration configuration) {
		super(configuration.getMessenger());
		this.configuration = configuration;
		dialogHandlerConfig = configuration.buildNewDialogHandlerConfiguration();
		generator = new DataGenerator(configuration.getMessenger(),configuration.getUnion(),this);
	}

	public boolean load() {
		return load(false);
	}

	public boolean reload() {
		return load(true);
	}

	public boolean rebase() {
		boolean r = writeBaseLine(true);
		if (r) {
			lockMe(this);
			testing = true;
			initializeTester();
			unlockMe(this);
			configuration.debug(this,"Initializing tester ...");
		}
		return r;
	}
	
	public boolean generate(boolean load,boolean reload) {
		boolean r = false;
		lockMe(this);
		if (!generator.isWorking()) {
			generator.generate(load, reload);
			lastModifiedHeader = getLastModifiedDateString();
			r = true;
		}
		unlockMe(this);
		return r;
	}

	protected boolean generate() {
		boolean r = false;
		lockMe(this);
		if (!reading && !writing) {
			writing = true;
			r = true;
			lastModifiedHeader = getLastModifiedDateString();
		}
		unlockMe(this);
		if (r) {
			configuration.debug(this,"Generating data ...");
			LanguageJsonGenerator generator = configuration.getNewLanguageJsonGenerator();
			SequencePreprocessor sp = configuration.getNewSequencePreprocessor();
			EntityValueTranslator t = configuration.getNewEntityValueTranslator();
			DialogSet ds = configuration.getNewDialogSet();
			sp.initialize();
			t.initialize();
			ds.initialize(t);
			generator.generate(configuration.getBaseConfig(),sp,t,ds,configuration.getBaseConfig().getFullBaseDir());
			lockMe(this);
			writing = false;
			lastModifiedHeader = getLastModifiedDateString();
			unlockMe(this);
			configuration.debug(this,"Generated data");
		}
		return r;
	}

	@Override
	public void initializedClass(InitializeClass cls, boolean done) {
		if (cls.obj instanceof SequenceInterpreterTester) {
			if (cls.errors.length()>0) {
				configuration.getMessenger().warn(this,cls.errors.toString());
			}
			configuration.debug(this,"Initialized tester");
			boolean testing = false;
			lockMe(this);
			tester = testerInitializer.getTester();
			if (tester.start()) {
				testing = true;
			}
			lastModifiedHeader = getLastModifiedDateString();
			unlockMe(this);
			if (testing) {
				configuration.debug(this,"Testing ...");
			}
		} else {
			if (cls.errors.length()>0) {
				configuration.getMessenger().error(this,cls.errors.toString());
			}
			if (done) {
				boolean reloaded = false;
				lockMe(this);
				reading = false;
				reloaded = reload;
				dialogHandlerConfig = dialogHandlerConfigLoad;
				lastModifiedHeader = getLastModifiedDateString();
				testing = true;
				initializeTester();
				unlockMe(this);
				if (reloaded) {
					configuration.debug(this,"Reloaded data");
				} else {
					configuration.debug(this,"Loaded data");
					if (configuration.getAppTester().getConfiguration().isSelfTestAfterInit() &&
						configuration.getAppTester().getSelfTester()!=null) {
						if (!configuration.getAppTester().startSelfTesterIfNoSummary()) {
							configuration.debug(this,"Failed to start self test case tester");
						}
					}
				}
				configuration.debug(this,"Initializing tester ...");
			}
		}
	}

	@Override
	public void testingIsDone(Object tester) {
		configuration.debug(this,"Tested");
		boolean writeBaseLine = false;
		lockMe(this);
		testing = false;
		File test = new File(configuration.getBaseConfig().getFullSelfTestBaseLineFileName());
		if (!test.exists()) {
			writeBaseLine = true;
		}
		unlockMe(this);
		if (writeBaseLine) {
			writeBaseLine(false);
		}
	}

	public boolean isInitialized() {
		boolean r = false;
		lockMe(this);
		r = dialogHandlerConfig.isDone();
		unlockMe(this);
		return r;
	}

	public boolean isReloading() {
		boolean r = false;
		lockMe(this);
		if (reading) {
			r = reload;
		}
		unlockMe(this);
		return r;
	}

	public boolean isGenerating() {
		boolean r = false;
		lockMe(this);
		r = writing;
		unlockMe(this);
		return r;
	}

	public boolean isTesting() {
		boolean r = false;
		lockMe(this);
		if (testing || (tester!=null && tester.isTesting())) {
			r = true;
		}
		unlockMe(this);
		return r;
	}
	
	public boolean isBusy() {
		boolean r = false;
		lockMe(this);
		if (reading || writing) {
			r = true; 
		}
		unlockMe(this);
		return r;
	}
	
	public DialogHandlerConfiguration getDialogHandlerConfig() {
		DialogHandlerConfiguration r = null;
		lockMe(this);
		r = dialogHandlerConfig;
		unlockMe(this);
		return r;
	}

	public SequenceInterpreterTester getTester() {
		SequenceInterpreterTester r = null;
		lockMe(this);
		r = tester;
		unlockMe(this);
		return r;
	}
	
	public String getLastModifiedHeader() {
		String r = "";
		lockMe(this);
		r = lastModifiedHeader;
		unlockMe(this);
		return r;
	}

	private boolean load(boolean re) {
		boolean r = false;
		DialogHandlerConfiguration config = null;
		lockMe(this);
		if (!reading && !writing) {
			reading = true;
			reload = re;
			r = true;
			dialogHandlerConfigLoad = configuration.buildNewDialogHandlerConfiguration();
			config = dialogHandlerConfigLoad;
			lastModifiedHeader = getLastModifiedDateString();
		}
		unlockMe(this);
		if (r) {
			if (re) {
				configuration.debug(this,"Reloading data ...");
			} else {
				configuration.debug(this,"Loading data ...");
			}
			config.addListener(this);
			config.initialize();
		}
		return r;
	}
	
	private boolean writeBaseLine(boolean rebase) {
		boolean r = false;
		lockMe(this);
		SequenceInterpreterTester t = tester;
		if (t!=null) {
			r = true;
		}
		unlockMe(this);
		if (r) {
			if (rebase) {
				configuration.debug(this,"Rebasing ...");
			}
			ZStringBuilder err = t.getSummary().toFile(configuration.getBaseConfig().getFullSelfTestBaseLineFileName(),true);
			if (err.length()>0) {
				configuration.getMessenger().error(this,err.toString());
				r = false;
			} else if (rebase) {
				configuration.debug(this,"Rebased");
			}
		}
		return r;
	}

	private void initializeTester() {
		lastModifiedHeader = getLastModifiedDateString();
		testerInitializer = configuration.getNewSequenceInterpreterTesterInitializer(dialogHandlerConfig);
		testerInitializer.addListener(this);
		testerInitializer.getTester().addListener(this);
		testerInitializer.start();
	}
	
	private String getLastModifiedDateString() {
		Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormat.format(date);
	}
}

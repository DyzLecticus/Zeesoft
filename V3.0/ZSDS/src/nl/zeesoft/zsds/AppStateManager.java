package nl.zeesoft.zsds;

import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.SequencePreprocessor;
import nl.zeesoft.zsd.dialog.DialogHandlerConfiguration;
import nl.zeesoft.zsd.dialog.DialogSet;
import nl.zeesoft.zsd.initialize.InitializeClass;
import nl.zeesoft.zsd.initialize.InitializerListener;
import nl.zeesoft.zsd.util.LanguageJsonGenerator;

public class AppStateManager extends Locker implements InitializerListener {
	private AppConfiguration			configuration				= null;

	private DialogHandlerConfiguration	dialogHandlerConfig			= null;
	private DialogHandlerConfiguration	dialogHandlerConfigLoad		= null;
	private boolean						reload						= false;
	private boolean						reading						= false;
	private boolean						writing						= false;

	public AppStateManager(AppConfiguration configuration) {
		super(configuration.getMessenger());
		this.configuration = configuration;
		dialogHandlerConfig = configuration.buildNewDialogHandlerConfiguration();
	}

	public boolean load() {
		return load(false);
	}

	public boolean reload() {
		return load(true);
	}

	@Override
	public void initializedClass(InitializeClass cls, boolean done) {
		if (cls.errors.length()>0) {
			configuration.getMessenger().error(this,cls.errors.toString());
		}
		if (done) {
			boolean reloaded = false;
			lockMe(this);
			reading = false;
			reloaded = reload;
			dialogHandlerConfig = dialogHandlerConfigLoad;
			unlockMe(this);
			if (reloaded) {
				configuration.debug(this,"Reloaded data ...");
			} else {
				configuration.debug(this,"Loaded data ...");
			}
		}
	}
	
	public boolean generate() {
		boolean r = false;
		lockMe(this);
		if (!reading && !writing) {
			writing = true;
			r = true;
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
			generator.generate(configuration.getBaseConfig(),sp,t,ds,configuration.getBaseConfig().getBaseDir());
			lockMe(this);
			writing = false;
			unlockMe(this);
			configuration.debug(this,"Generated data");
		}
		return r;
	}

	public boolean isInitialized() {
		boolean r = false;
		lockMe(this);
		r = dialogHandlerConfig.isDone();
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

	private boolean load(boolean re) {
		boolean r = false;
		lockMe(this);
		if (!reading && !writing) {
			reading = true;
			reload = re;
			r = true;
		}
		dialogHandlerConfigLoad = configuration.buildNewDialogHandlerConfiguration();
		DialogHandlerConfiguration config = dialogHandlerConfigLoad;
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
}

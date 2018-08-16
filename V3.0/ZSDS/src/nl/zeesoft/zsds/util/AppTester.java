package nl.zeesoft.zsds.util;

import java.io.File;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.initialize.InitializeClass;
import nl.zeesoft.zsd.initialize.InitializerListener;

public class AppTester implements InitializerListener {
	public static final String		ENVIRONMENT_NAME_SELF		= "Self";
	public static final String		SELF_TEST_CASES_FILE		= "SelfTestCases.json";
	
	private TestConfiguration		configuration	= null;
	private SetTesterInitializer	initializer		= null;
	
	public AppTester(Messenger msgr,WorkerUnion uni,BaseConfiguration base) {
		configuration = new TestConfiguration(msgr,uni,base);
	}

	public void initialize(String selfUrl) {
		initialize(selfUrl,true);
	}

	public void initialize(String selfUrl,boolean write) {
		File file = new File(configuration.getBase().getDataDir() + "testConfig.json");
		if (file.exists()) {
			JsFile json = new JsFile();
			ZStringBuilder err = json.fromFile(file.getAbsolutePath());
			if (err.length()>0) {
				configuration.error(this,err.toString());
			} else {
				configuration.fromJson(json);
			}
		} else if (selfUrl.length()>0) {
			addSelfEnvironmentToConfiguration(selfUrl);
			if (write) {
				File dir = new File(configuration.getBase().getDataDir());
				if (!dir.exists()) {
					if (!dir.mkdirs()) {
						configuration.error(this,"Unable to create directory: " + dir.getAbsolutePath());
						write = false;
					}
				}
				if (write) {
					ZStringBuilder err = configuration.toJson().toStringBuilderReadFormat().toFile(file.getAbsolutePath());
					if (err.length()>0) {
						configuration.error(this,err.toString());
					}
				}
			}
		}
		if (selfUrl.length()>0 && configuration.getBase().isDebug() && configuration.getEnvironment(ENVIRONMENT_NAME_SELF)==null) {
			addSelfEnvironmentToConfiguration(selfUrl);
		}
		initializer = new SetTesterInitializer(configuration);
		initializer.addListener(this);
		initializer.start();
		configuration.debug(this,"Initializing application tester ...");
	}
	
	@Override
	public void initializedClass(InitializeClass cls, boolean done) {
		if (cls.errors.length()>0) {
			configuration.warn(cls.obj,cls.errors.toString());
		}
		if (done) {
			configuration.debug(this,"Initialized application tester");
		}
	}
	
	public boolean isInitialized() {
		return initializer.isDone();
	}
	
	public TestCaseSetTester getTester(String environmentName) {
		return initializer.getTester(environmentName);
	}
	
	protected void addSelfEnvironmentToConfiguration(String selfUrl) {
		TestEnvironment self = new TestEnvironment();
		self.name = ENVIRONMENT_NAME_SELF;
		self.url = selfUrl;
		self.fileName = SELF_TEST_CASES_FILE;
		configuration.getEnvironments().add(self);
	}

	public TestConfiguration getConfiguration() {
		return configuration;
	}
}

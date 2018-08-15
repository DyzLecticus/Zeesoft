package nl.zeesoft.zsds.util;

import java.io.File;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.initialize.InitializeClass;
import nl.zeesoft.zsd.initialize.InitializerListener;

public class AppTester implements InitializerListener {
	public static final String		ENVIRONMENT_NAME_SELF		= "Self";
	public static final String		GENERIC_TEST_CASES_FILE		= "GenericTestCases.json";
	
	private TestConfiguration		configuration	= null;
	private SetTesterInitializer	initializer		= null;
	
	public AppTester(Messenger msgr,WorkerUnion uni,BaseConfiguration base) {
		configuration = new TestConfiguration(msgr,uni,base);
	}

	public void initialize(String selfUrl) {
		File file = new File(configuration.getBase().getDataDir() + "testConfig.json");
		if (file.exists()) {
			// TODO: Load configuration JSON
		} else if (selfUrl.length()>0) {
			addSelfEnvironmentToConfiguration(selfUrl);
			// TODO: Write configuration JSON
		}
		if (selfUrl.length()>0 && configuration.getBase().isDebug() && configuration.getEnvironment(ENVIRONMENT_NAME_SELF)==null) {
			addSelfEnvironmentToConfiguration(selfUrl);
		}
		initializer = new SetTesterInitializer(configuration);
		initializer.addListener(this);
		initializer.start();
		configuration.error(this,"Initializing application tester ...");
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
	
	protected void addSelfEnvironmentToConfiguration(String selfUrl) {
		TestEnvironment self = new TestEnvironment();
		self.name = ENVIRONMENT_NAME_SELF;
		self.url = selfUrl;
		self.fileName = GENERIC_TEST_CASES_FILE;
		configuration.getEnvironments().add(self);
	}
}

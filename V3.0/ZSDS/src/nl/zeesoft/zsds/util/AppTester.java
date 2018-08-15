package nl.zeesoft.zsds.util;

import java.io.File;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.initialize.InitializeClass;
import nl.zeesoft.zsd.initialize.InitializerListener;

public class AppTester implements InitializerListener {
	public static final String		ENV_NAME_SELF	= "Self";
	
	private TestConfiguration		configuration	= null;
	private SetTesterInitializer	initializer		= null;
	
	public AppTester(Messenger msgr,WorkerUnion uni,BaseConfiguration base) {
		configuration = new TestConfiguration(msgr,uni,base);
	}

	public void initialize(String selfUrl) {
		File file = new File(configuration.getBase().getDataDir() + "testConfig.json");
		if (file.exists()) {
			// TODO: Load configuration JSON
		} else {
			TestEnvironment self = new TestEnvironment();
			self.name = "Self";
			self.url = selfUrl;
			self.fileName = "GenericTestCases.json";
			configuration.getEnvironments().add(self);
			// TODO: Write configuration JSON
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
}

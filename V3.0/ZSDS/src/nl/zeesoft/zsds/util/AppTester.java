package nl.zeesoft.zsds.util;

import java.io.File;
import java.io.InputStream;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsd.initialize.InitializeClass;
import nl.zeesoft.zsd.initialize.InitializerListener;
import nl.zeesoft.zsds.AppBaseConfiguration;
import nl.zeesoft.zsds.handler.JsonDialogRequestHandler;

public class AppTester implements InitializerListener {
	public static final String		ENVIRONMENT_NAME_SELF		= "Self";
	public static final String		SELF_TEST_CASES_FILE		= "SelfTestCases.json";
	
	private TestConfiguration		configuration	= null;
	private SetTesterInitializer	initializer		= null;
	
	public AppTester(Messenger msgr,WorkerUnion uni,AppBaseConfiguration base) {
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
			if (write) {
				File dir = new File(configuration.getBase().getDataDir());
				if (!dir.exists()) {
					if (!dir.mkdirs()) {
						configuration.error(this,"Unable to create directory: " + dir.getAbsolutePath());
						write = false;
					}
				}
				if (write) {
					dir = new File(configuration.getFullTestCaseDir());
					if (!dir.exists()) {
						if (!dir.mkdirs()) {
							configuration.error(this,"Unable to create directory: " + dir.getAbsolutePath());
							write = false;
						}
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
		if (selfUrl.length()>0 && configuration.getBase().isSelfTest()) {
			configuration.removeEnvironment(ENVIRONMENT_NAME_SELF);
			addSelfEnvironmentToConfiguration(selfUrl);
		}
		
		for (TestEnvironment env: configuration.getEnvironments()) {
			File dir = new File(configuration.getFullEnvironmentDirectory(env.name));
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					configuration.error(this,"Unable to create directory: " + dir.getAbsolutePath());
					break;
				}
			}
			if (env.name.equals(ENVIRONMENT_NAME_SELF)) {
				File selfTestCases = new File(dir.getAbsolutePath() + "/" + SELF_TEST_CASES_FILE);
				if (!selfTestCases.exists()) {
					ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
					InputStream input = classLoader.getResourceAsStream(SELF_TEST_CASES_FILE);
					if (input==null) {
						configuration.error(this,"Failed to load self test cases file from WAR: " + SELF_TEST_CASES_FILE);
					} else {
						ZStringBuilder content = new ZStringBuilder();
						ZStringBuilder err = content.fromInputStream(input);
						if (err.length()==0) {
							if (content.length()>0) {
								err = content.toFile(selfTestCases.getAbsolutePath());
							} else {
								configuration.error(this,"Self test cases file is empty: " + SELF_TEST_CASES_FILE);
							}
						}
						if (err.length()>0) {
							configuration.error(this,err.toString());
						}
					}
				}
			}
		}
		
		initializer = new SetTesterInitializer(configuration);
		if (configuration.getEnvironments().size()>0) {
			initializer.addListener(this);
			initializer.start();
			configuration.debug(this,"Initializing application tester ...");
		}
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

	public boolean reinitialize() {
		configuration.debug(this,"Initializing application tester ...");
		return initializer.start();
	}

	public void stopAllTesters() {
		for (TestEnvironment env: configuration.getEnvironments()) {
			TestCaseSetTester tester = getTester(env.name);
			if (tester.isTesting()) {
				tester.stop();
			}
		}
	}
	
	public TestCaseSetTester getSelfTester() {
		return initializer.getTester(ENVIRONMENT_NAME_SELF);
	}
	
	public TestCaseSetTester getTester(String environmentName) {
		return initializer.getTester(environmentName);
	}
	
	public boolean startSelfTesterIfNoSummary() {
		boolean r = false;
		TestCaseSetTester selfTester = getSelfTester();
		if (selfTester!=null) {
			r = selfTester.startIfNoSummary();
		}
		return r;
	}
	
	protected void addSelfEnvironmentToConfiguration(String selfUrl) {
		TestEnvironment self = new TestEnvironment();
		self.name = ENVIRONMENT_NAME_SELF;
		self.url = selfUrl + JsonDialogRequestHandler.PATH;
		self.directory = self.name + "/";
		configuration.getEnvironments().add(self);
	}

	public TestConfiguration getConfiguration() {
		return configuration;
	}
}

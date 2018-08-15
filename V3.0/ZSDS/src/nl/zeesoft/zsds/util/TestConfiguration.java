package nl.zeesoft.zsds.util;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsd.BaseConfiguration;

public class TestConfiguration {
	private Messenger					messenger		= null;
	private WorkerUnion					union			= null;
	private BaseConfiguration			base			= null;
	
	private String						testCaseDir		= "testCases/";
	private int							defaultSleep	= 10;
	private List<TestEnvironment>		environments	= new ArrayList<TestEnvironment>();
	
	public TestConfiguration(Messenger msgr,WorkerUnion uni,BaseConfiguration config) {
		messenger = msgr;
		union = uni;
		base = config;
	}

	public Messenger getMessenger() {
		return messenger;
	}

	public WorkerUnion getUnion() {
		return union;
	}

	public BaseConfiguration getBase() {
		return base;
	}

	public String getTestCaseDir() {
		return testCaseDir;
	}

	public void setTestCaseDir(String testCaseDir) {
		if (!testCaseDir.endsWith("/") && !testCaseDir.endsWith("\\")) {
			testCaseDir += "/";
		}
		this.testCaseDir = testCaseDir;
	}

	public int getDefaultSleep() {
		return defaultSleep;
	}

	public void setDefaultSleep(int defaultSleep) {
		this.defaultSleep = defaultSleep;
	}

	public List<TestEnvironment> getEnvironments() {
		return environments;
	}

	public void debug(Object source, String msg) {
		if (messenger!=null) {
			messenger.debug(source,msg);
		}
	}

	public void warn(Object source, String msg) {
		if (messenger!=null) {
			messenger.warn(source,msg);
		}
	}

	public void error(Object source, String msg) {
		if (messenger!=null) {
			messenger.error(source,msg);
		}
	}

	public TestEnvironment getEnvironment(String name) {
		TestEnvironment r = null;
		for (TestEnvironment env: environments) {
			if (env.name.equals(name)) {
				r = env;
				break;
			}
		}
		return r;
	}

	public String getFullTestCaseDir() {
		return base.getDataDir() + testCaseDir;
	}
	
	public String getFullEnvironmentFileName(String name) {
		String r = "";
		TestEnvironment env = getEnvironment(name);
		if (env!=null) {
			r = getFullTestCaseDir() + env.fileName;
		}
		return r;
	}
}

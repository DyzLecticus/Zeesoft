package nl.zeesoft.zsds.tester;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsds.AppBaseConfiguration;

public class TestConfiguration {
	private Messenger					messenger			= null;
	private WorkerUnion					union				= null;
	private AppBaseConfiguration		base				= null;
	
	private String						testCaseDir			= "testCases/";
	private int							defaultSleep		= 10;
	private boolean						retryIfBusy			= true;
	private int							maxRetries			= 60;
	private List<TestEnvironment>		environments		= new ArrayList<TestEnvironment>();
	
	public TestConfiguration() {
		messenger = null;
		union = null;
		base = new AppBaseConfiguration();
	}
	
	public TestConfiguration(Messenger msgr,WorkerUnion uni,AppBaseConfiguration config) {
		messenger = msgr;
		union = uni;
		base = config;
	}

	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("testCaseDir",testCaseDir,true));
		json.rootElement.children.add(new JsElem("defaultSleep","" + defaultSleep));
		json.rootElement.children.add(new JsElem("retryIfBusy","" + retryIfBusy));
		json.rootElement.children.add(new JsElem("maxRetries","" + maxRetries));
		JsElem envsElem = new JsElem("environments",true);
		json.rootElement.children.add(envsElem);
		for (TestEnvironment env: environments) {
			JsFile envJs = env.toJson();
			JsElem envElem = new JsElem();
			envsElem.children.add(envElem);
			envElem.children = envJs.rootElement.children;
		}
		return json;
	}

	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			testCaseDir = json.rootElement.getChildString("testCaseDir",testCaseDir);
			defaultSleep = json.rootElement.getChildInt("defaultSleep",defaultSleep);
			retryIfBusy = json.rootElement.getChildBoolean("retryIfBusy",retryIfBusy);
			maxRetries = json.rootElement.getChildInt("maxRetries",maxRetries);
			JsElem envsElem = json.rootElement.getChildByName("environments");
			if (envsElem!=null) {
				for (JsElem envElem: envsElem.children) {
					JsFile envJs = new JsFile();
					envJs.rootElement = envElem;
					TestEnvironment env = new TestEnvironment();
					env.fromJson(envJs);
					environments.add(env);
				}
			}
		}
	}
	
	public Messenger getMessenger() {
		return messenger;
	}

	public WorkerUnion getUnion() {
		return union;
	}

	public AppBaseConfiguration getBase() {
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

	public int getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	public boolean isRetryIfBusy() {
		return retryIfBusy;
	}

	public void setRetryIfBusy(boolean retryIfBusy) {
		this.retryIfBusy = retryIfBusy;
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
		error(source,msg,null);
	}

	public void error(Object source, String msg,Exception e) {
		if (messenger!=null) {
			messenger.error(source,msg,e);
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

	public void removeEnvironment(String name) {
		TestEnvironment r = getEnvironment(name);
		if (r!=null) {
			environments.remove(r);
		}
	}

	public String getFullTestCaseDir() {
		return base.getDataDir() + testCaseDir;
	}
	
	public String getFullEnvironmentDirectory(String name) {
		String r = "";
		TestEnvironment env = getEnvironment(name);
		if (env!=null) {
			r = getFullTestCaseDir() + env.directory;
		}
		return r;
	}
}

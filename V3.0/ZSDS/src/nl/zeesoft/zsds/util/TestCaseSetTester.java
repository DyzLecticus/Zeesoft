package nl.zeesoft.zsds.util;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zsd.initialize.Initializable;
import nl.zeesoft.zsd.interpret.TesterListener;

public class TestCaseSetTester extends Locker implements Initializable, TesterListener {
	private TestConfiguration		configuration	= null;
	private TestEnvironment			environment		= null;

	private List<TesterListener>	listeners		= new ArrayList<TesterListener>();
	
	private TestCaseSet				testCaseSet		= null;
	private List<TestCaseTester>	testers			= new ArrayList<TestCaseTester>();
	
	private boolean					testing			= false;
	private int						done			= 0;
	
	public TestCaseSetTester(TestConfiguration configuration,String environmentName) {
		super(configuration.getMessenger());
		this.configuration = configuration;
		environment = configuration.getEnvironment(environmentName);
	}

	public void addListener(TesterListener listener) {
		listeners.add(listener);
	}

	@Override
	public void initialize(List<ZStringBuilder> data) {
		if (data!=null && data.size()>0) {
			JsFile json = new JsFile();
			json.fromStringBuilder(data.get(0));
			if (json.rootElement==null) {
				if (getMessenger()!=null) {
					getMessenger().error(this,"Failed to parse test cases set JSON for environment: " + environment.name);
				}
			} else {
				TestCaseSet tcs = new TestCaseSet();
				tcs.fromJson(json);
				initialize(tcs);
			}
		}
	}
	
	@Override
	public void testingIsDone(Object tester) {
		// TODO Auto-generated method stub
		boolean r = false;
		lockMe(this);
		done++;
		if (done==testers.size()) {
			testing = false;
			r = true;
		}
		unlockMe(this);
		if (r) {
			for (TesterListener listener: listeners) {
				listener.testingIsDone(this);
			}
		}
	}
	
	public void initialize(TestCaseSet tcs) {
		testCaseSet = tcs;
		for (TestCase tc: testCaseSet.getTestCases()) {
			if (tc.io.size()>0) {
				TestCaseTester tct = new TestCaseTester(configuration,environment.name,tc,configuration.getDefaultSleep());
				tct.addListener(this);
				testers.add(tct);
			}
		}
	}

	public boolean isTesting() {
		boolean r = false;
		lockMe(this);
		r = testing;
		unlockMe(this);
		return r;
	}

	public boolean start() {
		boolean r = false;
		lockMe(this);
		if (!testing && testers.size()>0) {
			r = true;
			done = 0;
			testing = true;
			for (TestCaseTester tester: testers) {
				if (!tester.start()) {
					done++;
				}
			}
		}
		unlockMe(this);
		return r;
	}

	public TestEnvironment getEnvironment() {
		return environment;
	}

	public List<TestCaseTester> getTesters() {
		return testers;
	}
}

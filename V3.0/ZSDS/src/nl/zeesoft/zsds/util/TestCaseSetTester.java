package nl.zeesoft.zsds.util;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
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
	private JsFile					summary			= null;
	
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
				configuration.error(this,"Failed to parse test cases set JSON for environment: " + environment.name);
			} else {
				TestCaseSet tcs = new TestCaseSet();
				tcs.fromJson(json);
				initialize(tcs);
			}
		}
	}
	
	@Override
	public void testingIsDone(Object tester) {
		boolean r = false;
		lockMe(this);
		done++;
		if (done==testers.size()) {
			testing = false;
			r = true;
			summary = buildSummary(testers);
		}
		unlockMe(this);
		if (r) {
			configuration.debug(this,"Tested");
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
		if (r) {
			configuration.debug(this,"Testing environment: " + environment.name + " ...");
		}
		return r;
	}

	public boolean stop() {
		boolean r = false;
		lockMe(this);
		if (testing) {
			r = true;
			testing = true;
			for (TestCaseTester tester: testers) {
				tester.stop();
			}
		}
		unlockMe(this);
		if (r) {
			configuration.debug(this,"Testing stopped");
			for (TesterListener listener: listeners) {
				listener.testingIsDone(this);
			}
		}
		return r;
	}

	public TestEnvironment getEnvironment() {
		return environment;
	}

	public List<TestCaseTester> getTesters() {
		return testers;
	}

	public JsFile getSummary() {
		JsFile r = null;
		lockMe(this);
		r = summary;
		unlockMe(this);
		return r;
	}

	protected JsFile buildSummary(List<TestCaseTester> testers) {
		int successful = 0;
		int responses = 0;
		for (TestCaseTester test: testers) {
			if (test.getError().length()==0) {
				successful++;
				responses+=test.getResponses().size();
			}
		}
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("testCases","" + testers.size()));
		json.rootElement.children.add(new JsElem("successful","" + successful));
		json.rootElement.children.add(new JsElem("responses","" + responses));
		JsElem errsElem = new JsElem("errors",true);
		json.rootElement.children.add(errsElem);
		for (TestCaseTester test: testers) {
			if (test.getError().length()>0) {
				JsElem tcElem = new JsElem();
				errsElem.children.add(tcElem);
				tcElem.children.add(new JsElem("testCase",test.getTestCase().name,true));
				tcElem.children.add(new JsElem("error",test.getError(),true));
				
				if (test.getErrorTestCaseIO()!=null && test.getErrorDialogResponse()!=null) {
					JsElem reqElem = new JsElem("request");
					reqElem.children = test.getErrorTestCaseIO().request.toJson().rootElement.children; 
					tcElem.children.add(reqElem);
					
					JsElem expElem = new JsElem("expectedResponse");
					expElem.children = test.getErrorTestCaseIO().expectedResponse.toJson().rootElement.children; 
					tcElem.children.add(expElem);
					
					JsElem resElem = new JsElem("response");
					resElem.children = test.getErrorDialogResponse().toJson().rootElement.children; 
					tcElem.children.add(resElem);
				}
			}
		}
		return json;
	}
}

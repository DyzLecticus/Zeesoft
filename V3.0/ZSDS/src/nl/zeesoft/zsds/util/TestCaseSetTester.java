package nl.zeesoft.zsds.util;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZDate;
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
			TestCaseSet tcs = new TestCaseSet();
			boolean init = true;
			for (ZStringBuilder str: data) {
				JsFile json = new JsFile();
				json.fromStringBuilder(str);
				if (json.rootElement==null) {
					configuration.error(this,"Failed to parse test cases set JSON for environment: " + environment.name);
					init = false;
					break;
				} else {
					if (json.rootElement.getChildByName("testCases")!=null) {
						tcs.fromJson(json);
					} else if (json.rootElement.getChildByName("name")!=null) {
						TestCase tc = new TestCase();
						tc.fromJson(json);
						tcs.getTestCases().add(tc);
					}
				}
			}
			if (init) {
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
			summary = createSummary(testers);
		}
		unlockMe(this);
		if (r) {
			configuration.debug(this,"Tested environment: " + environment.name);
			for (TesterListener listener: listeners) {
				listener.testingIsDone(this);
			}
		}
	}
	
	public void initialize(TestCaseSet tcs) {
		lockMe(this);
		if (!testing) {
			testers.clear();
			testCaseSet = tcs;
			for (TestCase tc: testCaseSet.getTestCases()) {
				if (tc.io.size()>0) {
					TestCaseTester tct = new TestCaseTester(configuration,environment.name,tc,configuration.getDefaultSleep());
					tct.addListener(this);
					testers.add(tct);
				}
			}
		}
		unlockMe(this);
	}

	public boolean isTesting() {
		boolean r = false;
		lockMe(this);
		r = testing;
		unlockMe(this);
		return r;
	}

	public boolean startIfNoSummary() {
		lockMe(this);
		boolean r = startNoLock(true);
		unlockMe(this);
		if (r) {
			configuration.debug(this,"Testing environment: " + environment.name + " ...");
		}
		return r;
	}

	public boolean start() {
		lockMe(this);
		boolean r = startNoLock(false);
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
			testing = false;
			for (TestCaseTester tester: testers) {
				tester.stop();
			}
		}
		unlockMe(this);
		if (r) {
			configuration.debug(this,"Stopped testing environment: " + environment.name + " ...");
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

	protected JsFile createSummary(List<TestCaseTester> testers) {
		int successful = 0;
		int responses = 0;
		long totalTime = 0;
		List<String> coveredDialogs = new ArrayList<String>();
		
		for (TestCaseTester test: testers) {
			responses += test.getResponses().size();
			totalTime += test.getTotalTime();
			if (test.getError().length()==0) {
				successful++;
				for (TestCaseIO io: test.getTestCase().io) {
					String language = "";
					String masterContext = "";
					String context = "";
					if (io.expectedResponse.classifiedLanguages.size()>0) {
						language = io.expectedResponse.classifiedLanguages.get(0).symbol;
					}
					if (io.expectedResponse.classifiedMasterContexts.size()>0) {
						masterContext = io.expectedResponse.classifiedMasterContexts.get(0).symbol;
					}
					if (io.expectedResponse.classifiedContexts.size()>0) {
						context = io.expectedResponse.classifiedContexts.get(0).symbol;
					}
					if (language.length()>0 && masterContext.length()>0 && context.length()>0) {
						String dialogId = language + "/" + masterContext + "/" + context;
						if (!coveredDialogs.contains(dialogId)) {
							coveredDialogs.add(dialogId);
						}
					}
				}
			}
		}
		
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		ZDate timeStamp = new ZDate();
		json.rootElement.children.add(new JsElem("timeStamp",timeStamp.getDateTimeString(),true));
		json.rootElement.children.add(new JsElem("testCases","" + testers.size()));
		json.rootElement.children.add(new JsElem("successful","" + successful));
		json.rootElement.children.add(new JsElem("responses","" + responses));
		json.rootElement.children.add(new JsElem("averageResponseMs","" + (totalTime / responses)));
		JsElem errsElem = new JsElem("errors",true);
		json.rootElement.children.add(errsElem);
		JsElem covElem = new JsElem("coveredDialogs",true);
		json.rootElement.children.add(covElem);
		for (String dialogId: coveredDialogs) {
			covElem.children.add(new JsElem(null,dialogId,true));
		}
		JsElem logsElem = new JsElem("logs",true);
		json.rootElement.children.add(logsElem);
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
			if (test.getLog().length()>0) {
				JsElem logElem = new JsElem();
				logsElem.children.add(logElem);
				logElem.children.add(new JsElem("testCase",test.getTestCase().name,true));
				JsElem linesElem = new JsElem("log",true);
				logElem.children.add(linesElem);
				List<ZStringBuilder> logLines = test.getLog().split("\n");
				for (ZStringBuilder line: logLines) {
					linesElem.children.add(new JsElem(null,line,true));
				}
			}
		}
		return json;
	}
	
	private boolean startNoLock(boolean checkNoSummary) {
		boolean r = false;
		if (!testing && testers.size()>0 && (checkNoSummary==false || summary==null)) {
			r = true;
			done = 0;
			summary = null;
			testing = true;
			for (TestCaseTester tester: testers) {
				if (!tester.start()) {
					done++;
				}
			}
		}
		return r;
	}
}

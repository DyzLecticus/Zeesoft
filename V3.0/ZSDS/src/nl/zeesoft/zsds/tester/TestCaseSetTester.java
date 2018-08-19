package nl.zeesoft.zsds.tester;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zsd.dialog.DialogInstance;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.dialog.DialogSet;
import nl.zeesoft.zsd.http.ZHttpRequest;
import nl.zeesoft.zsd.initialize.Initializable;
import nl.zeesoft.zsd.interpret.TesterListener;
import nl.zeesoft.zsds.handler.JsonDialogsHandler;

public class TestCaseSetTester extends Locker implements Initializable, TesterListener {
	private TestConfiguration		configuration	= null;
	private TestEnvironment			environment		= null;

	private List<TesterListener>	listeners		= new ArrayList<TesterListener>();
	
	private TestCaseSet				testCaseSet		= null;
	private List<TestCaseTester>	testers			= new ArrayList<TestCaseTester>();
	
	private boolean					testing			= false;
	private DialogSet				dialogSet		= null;
	private int						done			= 0;
	private JsFile					summary			= null;
	
	private	TestCaseSetTesterWorker	worker			= null;
	private boolean					retrying		= false;
	private int						retries			= 0;
	
	public TestCaseSetTester(TestConfiguration configuration,String environmentName) {
		super(configuration.getMessenger());
		this.configuration = configuration;
		environment = configuration.getEnvironment(environmentName);
		worker = new TestCaseSetTesterWorker(configuration.getMessenger(),configuration.getUnion(),this);
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
			summary = createSummary(testers,dialogSet);
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

	public boolean isWaiting() {
		boolean r = false;
		lockMe(this);
		r = retrying;
		unlockMe(this);
		return r;
	}

	public boolean isTesting() {
		boolean r = false;
		lockMe(this);
		r = testing;
		unlockMe(this);
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

	protected boolean getDialogs() {
		boolean r = false;
		boolean stop = false;
		
		ZHttpRequest http = new ZHttpRequest(null,"GET",environment.url + JsonDialogsHandler.PATH);
		JsFile json = http.sendJsonRequest();
		if (configuration.isRetryIfBusy() && http.getResponseCode()==503) {
			lockMe(this);
			if (retrying) {
				retries++;
				if (retries>=configuration.getMaxRetries()) {
					configuration.error(this,"Cancelled test cases for environment: " + environment.name);
					r = true;
					stop = true;
				} else if ((retries % 10) == 1){
					configuration.debug(this,"Retrying ...");
				}
			} else {
				retrying = true;
				retries = 0;
			}
			unlockMe(this);
		} else {
			if (json.rootElement==null) {
				configuration.error(this,"Failed to obtain dialogs from environment: " + environment.url);
				r = true;
				stop = true;
			} else {
				lockMe(this);
				if (retrying) {
					configuration.debug(this,"Continuing ...");
					retrying = false;
				}
				unlockMe(this);
				dialogSet = new DialogSet();
				dialogSet.fromJson(json);
			}
			r = true;
		}
		
		if (stop) {
			stop();
		} else if (r) {
			lockMe(this);
			for (TestCaseTester tester: testers) {
				if (!tester.start()) {
					done++;
				}
			}
			unlockMe(this);
		}

		return r;
	}
	
	protected JsFile createSummary(List<TestCaseTester> testers, DialogSet dialogSet) {
		int successful = 0;
		int responses = 0;
		long totalTime = 0;
		List<String> coveredDialogs = new ArrayList<String>();
		List<String> notCoveredDialogs = new ArrayList<String>();
		
		for (TestCaseTester test: testers) {
			responses += test.getResponses().size();
			totalTime += test.getTotalTime();
			if (test.getError().length()==0) {
				successful++;
				for (DialogResponse response: test.getResponses()) {
					String language = "";
					String masterContext = "";
					String context = "";
					if (response.classifiedLanguages.size()>0) {
						language = response.classifiedLanguages.get(0).symbol;
					}
					if (response.classifiedMasterContexts.size()>0) {
						masterContext = response.classifiedMasterContexts.get(0).symbol;
					}
					if (response.contextOutputs.size()>0) {
						context = response.contextOutputs.get(0).context;
					} else if (response.classifiedContexts.size()>0) {
						context = response.classifiedContexts.get(0).symbol;
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
		int dialogCoveragePercentage = 0;
		if (dialogSet!=null && dialogSet.getDialogs().size()>0) {
			for (DialogInstance dialog: dialogSet.getDialogs()) {
				if (!coveredDialogs.contains(dialog.getId())) {
					notCoveredDialogs.add(dialog.getId());
				}
			}
			dialogCoveragePercentage = (coveredDialogs.size() * 100) / dialogSet.getDialogs().size();
		}
		
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		ZDate timeStamp = new ZDate();
		json.rootElement.children.add(new JsElem("timeStamp",timeStamp.getDateTimeString(),true));
		json.rootElement.children.add(new JsElem("testCases","" + testers.size()));
		json.rootElement.children.add(new JsElem("successful","" + successful));
		json.rootElement.children.add(new JsElem("responses","" + responses));
		if (responses>0) {
			json.rootElement.children.add(new JsElem("averageResponseMs","" + (totalTime / responses)));
		}
		if (dialogSet!=null && dialogSet.getDialogs().size()>0) {
			json.rootElement.children.add(new JsElem("dialogCoveragePercentage","" + dialogCoveragePercentage));
			JsElem covElem = new JsElem("notCoveredDialogs",true);
			json.rootElement.children.add(covElem);
			for (String dialogId: notCoveredDialogs) {
				covElem.children.add(new JsElem(null,dialogId,true));
			}
		} else {
			JsElem covElem = new JsElem("coveredDialogs",true);
			json.rootElement.children.add(covElem);
			for (String dialogId: coveredDialogs) {
				covElem.children.add(new JsElem(null,dialogId,true));
			}
		}
		
		JsElem errsElem = new JsElem("errors",true);
		json.rootElement.children.add(errsElem);
		
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
				if (test.getResponses().size()>0) {
					totalTime = test.getTotalTime();
					responses = test.getResponses().size();
					logElem.children.add(new JsElem("averageResponseMs","" + (totalTime / responses)));
				}
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
			dialogSet = null;
			summary = null;
			testing = true;
			worker.start();
		}
		return r;
	}
}

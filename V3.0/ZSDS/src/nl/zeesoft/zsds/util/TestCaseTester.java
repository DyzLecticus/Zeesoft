package nl.zeesoft.zsds.util;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.dialog.DialogRequest;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.dialog.DialogResponseOutput;
import nl.zeesoft.zsd.http.ZHttpRequest;
import nl.zeesoft.zsd.interpret.TesterListener;
import nl.zeesoft.zsd.sequence.SequenceClassifierResult;

public class TestCaseTester {
	private TestConfiguration			configuration			= null;
	private TestEnvironment				environment				= null;
	private TestCase					testCase				= null;
	private int							sleep					= 0;
	private TestCaseTesterWorker		worker					= null;
	private List<TesterListener>		listeners				= new ArrayList<TesterListener>();
	
	private List<DialogResponse>		responses				= new ArrayList<DialogResponse>();
	private String						error					= "";
	private TestCaseIO					errorTestCaseIO			= null;
	private DialogResponse				errorDialogResponse		= null;
	
	private boolean						retrying				= false;
	
	public TestCaseTester(TestConfiguration configuration,String environmentName,TestCase testCase,int sleep) {
		this.configuration = configuration;
		this.environment = configuration.getEnvironment(environmentName);
		this.testCase = testCase;
		this.sleep = sleep;
		worker = new TestCaseTesterWorker(configuration.getMessenger(),configuration.getUnion(),this,sleep);
	}

	public void addListener(TesterListener listener) {
		listeners.add(listener);
	}
	
	public boolean start() {
		boolean r = false;
		if (testCase.io.size()>0 && !worker.isWorking()) {
			responses.clear();
			error = "";
			errorTestCaseIO = null;
			errorDialogResponse = null;
			r = true;
			worker.start();
		}
		return r;
	}
	
	public void stop() {
		if (worker.isWorking()) {
			worker.stop();
		}
	}
	
	protected boolean test() {
		boolean done = false;
		int index = responses.size();
		if (index<testCase.io.size()) {
			TestCaseIO tcIO = testCase.io.get(index);
			DialogRequest request = tcIO.request;
			request.randomizeOutput = false;
			request.appendDebugLog = true;
			ZHttpRequest http = new ZHttpRequest(configuration.getMessenger(),"POST",environment.url);
			JsFile json = http.sendJsonRequest(request.toJson().toStringBuilder());
			if (json.rootElement==null) {
				if (configuration.isRetryIfBusy() && http.getResponseCode()==503 && !retrying) {
					if (retrying) {
						configuration.debug(this,"Retrying ...");
					} else {
						retrying = true;
						if (sleep<1000) {
							worker.setSleep(1000);
						}
					}
				} else {
					error = "Failed to obtain dialog response from " + environment.url;
					errorTestCaseIO = tcIO;
					done = true;
					configuration.error(this,error);
				}
			} else {
				if (retrying) {
					configuration.debug(this,"Continuing ...");
					retrying = false;
					if (sleep<1000) {
						worker.setSleep(sleep);
					}
				}
				DialogResponse response = new DialogResponse();
				response.fromJson(json);
				responses.add(response);
				if (error.length()==0) {
					error = compareResponses(response,tcIO.expectedResponse);
					if (error.length()>0) {
						errorTestCaseIO = tcIO;
						errorDialogResponse = response;
						done = true;
					}
				}
			}
		} else {
			done = true;
		}
		if (done) {
			for (TesterListener listener: listeners) {
				listener.testingIsDone(this);
			}
		}
		return done;
	}
	
	protected String compareResponses(DialogResponse response, DialogResponse expectedResponse) {
		String err = "";
		
		if (err.length()==0) {
			checkClassification(response.responseLanguages,expectedResponse.responseLanguages,"language","languages");
		}
		if (err.length()==0) {
			checkClassification(response.responseMasterContexts,expectedResponse.responseMasterContexts,"master context","master contexts");
		}
		if (err.length()==0) {
			checkClassification(response.responseContexts,expectedResponse.responseContexts,"context","contexts");
		}
		
		if (err.length()==0) {
			if (expectedResponse.contextOutputs.size()>0) {
				if (response.contextOutputs.size()<expectedResponse.contextOutputs.size()) {
					err = "Number of context outputs does not match expectation: " + response.contextOutputs.size() + " <> " + expectedResponse.contextOutputs.size();
				} else {
					DialogResponseOutput out = response.contextOutputs.get(0);
					DialogResponseOutput exp = expectedResponse.contextOutputs.get(0);
					if (out.output.length()>0 && exp.output.length()>0 && !out.output.equals(exp.output)) {
						err = "Context output does not match expectation: '" + out.output + "' <> '" + exp.output + "'";
					} else if (out.prompt.length()>0 && exp.prompt.length()>0 && !out.prompt.equals(exp.prompt)) {
						err = "Context prompt does not match expectation: '" + out.prompt + "' <> '" + exp.prompt + "'";
					}
				}
			}
		}
		
		return err;
	}
		
	protected String checkClassification(List<SequenceClassifierResult> results,List<SequenceClassifierResult> expectedResults,String name,String nameMulti) {
		String err = "";
		if (results.size()<expectedResults.size()) {
			err = "Number of response " + nameMulti + " does not match expectation: " + expectedResults + " <> " + expectedResults.size();
		} else if (expectedResults.size()>0) {
			String str = results.get(0).symbol;
			String exp = expectedResults.get(0).symbol;
			err = "Classified " + name + " does not match expectation: " + str + " <> " + exp;
		}
		return err;
	}
	
	public TestCase getTestCase() {
		return testCase;
	}
	
	public List<DialogResponse> getResponses() {
		return responses;
	}

	public String getError() {
		return error;
	}

	public TestCaseIO getErrorTestCaseIO() {
		return errorTestCaseIO;
	}

	public DialogResponse getErrorDialogResponse() {
		return errorDialogResponse;
	}
}

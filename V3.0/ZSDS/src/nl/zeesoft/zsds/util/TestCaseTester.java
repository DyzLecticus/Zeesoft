package nl.zeesoft.zsds.util;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.dialog.DialogRequest;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.http.ZHttpRequest;
import nl.zeesoft.zsd.interpret.TesterListener;

public class TestCaseTester {
	private TestConfiguration			configuration			= null;
	private TestEnvironment				environment				= null;
	private TestCase					testCase				= null;
	private TestCaseTesterWorker		worker					= null;
	private List<TesterListener>		listeners				= new ArrayList<TesterListener>();
	
	private List<DialogResponse>		responses				= new ArrayList<DialogResponse>();
	private String						error					= "";
	private TestCaseIO					errorTestCaseIO			= null;
	private DialogResponse				errorDialogResponse		= null;
	
	public TestCaseTester(TestConfiguration configuration,String environmentName,TestCase testCase,int sleep) {
		this.configuration = configuration;
		this.environment = configuration.getEnvironment(environmentName);
		this.testCase = testCase;
		worker = new TestCaseTesterWorker(configuration.getMessenger(),configuration.getUnion(),this,sleep);
	}

	public void addListener(TesterListener listener) {
		listeners.add(listener);
	}
	
	public boolean start() {
		boolean r = false;
		if (testCase.io.size()>0 && !worker.isWorking()) {
			r = true;
			worker.start();
		}
		return r;
	}
	
	protected boolean test() {
		boolean done = false;
		int index = responses.size();
		if (index<testCase.io.size()) {
			TestCaseIO tcIO = testCase.io.get(index);
			DialogRequest request = tcIO.request;
			ZHttpRequest http = new ZHttpRequest(configuration.getMessenger(),"POST",environment.url);
			JsFile json = http.sendJsonRequest(request.toJson().toStringBuilder());
			if (json.rootElement==null) {
				error = "Failed to obtain dialog response from " + environment.url;
				errorTestCaseIO = tcIO;
				done = true;
				configuration.error(this,error);
			} else {
				DialogResponse response = new DialogResponse();
				response.fromJson(json);
				responses.add(response);
				error = compareResponses(response,tcIO.expectedResponse);
				if (error.length()>0) {
					errorTestCaseIO = tcIO;
					errorDialogResponse = response;
				}
			}
		} else {
			done = true;
			for (TesterListener listener: listeners) {
				listener.testingIsDone(this);
			}
		}
		return done;
	}
	
	protected String compareResponses(DialogResponse response, DialogResponse expectedResponse) {
		String err = "";
		// TODO: Compare responses
		return err;
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

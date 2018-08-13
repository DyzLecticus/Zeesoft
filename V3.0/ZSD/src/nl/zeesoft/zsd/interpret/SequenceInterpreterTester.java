package nl.zeesoft.zsd.interpret;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsd.DialogHandler;
import nl.zeesoft.zsd.dialog.DialogHandlerConfiguration;
import nl.zeesoft.zsd.dialog.DialogIO;
import nl.zeesoft.zsd.dialog.DialogInstance;
import nl.zeesoft.zsd.initialize.Initializable;

public class SequenceInterpreterTester extends Locker implements Initializable {
	private DialogHandlerConfiguration			configuration		= null;
	
	private List<SequenceInterpreterTest>		tests				= new ArrayList<SequenceInterpreterTest>();
	private List<SequenceInterpreterTestResult>	results				= new ArrayList<SequenceInterpreterTestResult>();
	
	private boolean								testing				= false;
	private SequenceInterpreterTestWorker		worker				= null;
	private Date								started				= null;
	private JsFile								summary				= null;
	private JsFile								baseLineSummary		= null;

	private List<TesterListener>				listeners			= new ArrayList<TesterListener>();

	public SequenceInterpreterTester(DialogHandlerConfiguration configuration) {
		super(null);
		this.configuration = configuration;
		worker = new SequenceInterpreterTestWorker(null,null,this);
	}
	
	public SequenceInterpreterTester(Messenger msgr, WorkerUnion uni,DialogHandlerConfiguration configuration) {
		super(msgr);
		this.configuration = configuration;
		worker = new SequenceInterpreterTestWorker(msgr,uni,this);
	}
	
	public void addListener(TesterListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void initialize(List<ZStringBuilder> data) {
		if (data!=null && data.size()>0) {
			baseLineSummary = new JsFile();
			baseLineSummary.fromStringBuilder(data.get(0));
			if (baseLineSummary.rootElement==null || baseLineSummary.rootElement.children.size()==0) {
				baseLineSummary = null;
			}
		}
		initialize();
	}
	
	public void initialize() {
		for (DialogInstance dialog: configuration.getDialogSet().getDialogs()) {
			if (configuration.getBase().getSupportedMasterContexts().get(dialog.getLanguage()).contains(dialog.getMasterContext())) {
				for (DialogIO example: dialog.getExamples()) {
					boolean found = false;
					for (SequenceInterpreterTest test: tests) {
						if (test.dialogId.equals(dialog.getId()) && test.input.equals(example.input)) {
							found = true;
						}
					}
					if (!found) {
						boolean languageUnique = true;
						for (String language: configuration.getBase().getSupportedLanguages()) {
							if (!language.equals(dialog.getLanguage())) {
								DialogInstance test = configuration.getDialogSet().getDialog(language,dialog.getMasterContext(),dialog.getContext());
								if (test!=null) {
									for (DialogIO testExample: test.getExamples()) {
										if (testExample.input.equals(example.input)) {
											languageUnique = false;
											break;
										}
									}
								}
							}
							if (!languageUnique) {
								break;
							}
						}
						tests.add(getTestForDialogExample(dialog,example,languageUnique));
					}
				}
			}
		}
	}
	
	public boolean start() {
		boolean r = false;
		lockMe(this);
		if (tests.size()>0 && !testing && !worker.isWorking()) {
			results.clear();
			summary = null;
			testing = true;
			r = testing;
			started = new Date();
			worker.start();
		}
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

	public int getDonePercentage() {
		int r = 0;
		lockMe(this);
		if (tests.size()>0) {
			r = (results.size() * 100) / tests.size();
		}
		unlockMe(this);
		return r;
	}
	
	public JsFile getSummary() {
		JsFile r = null;
		lockMe(this);
		r = summary;
		unlockMe(this);
		return r;
	}
	
	protected DialogHandlerConfiguration getConfiguration() {
		return configuration;
	}

	protected SequenceInterpreterTest getNewSequenceInterpreterTest() {
		return new SequenceInterpreterTest();
	}

	protected SequenceInterpreterTestResult getNewSequenceInterpreterTestResult() {
		return new SequenceInterpreterTestResult();
	}

	protected DialogHandler getNewDialogHandler() {
		return new DialogHandler(getConfiguration());
	}
	
	protected SequenceInterpreterTest getTestForDialogExample(DialogInstance dialog,DialogIO example,boolean languageUnique) {
		SequenceInterpreterTest test = getNewSequenceInterpreterTest();
		test.dialogId = dialog.getId();
		test.languageUnique = languageUnique;
		test.input = example.input;
		test.expectedLanguage = dialog.getLanguage();
		test.expectedMasterContext = dialog.getMasterContext();
		test.expectedContext = dialog.getContext();
		return test;
	}

	protected InterpreterRequest getRequestForTest(SequenceInterpreterTest test) {
		InterpreterRequest request = new InterpreterRequest();
		request.isTestRequest = true;
		if (test.languageUnique) {
			request.classifyLanguage = true;
		} else {
			request.language = test.expectedLanguage;
		}
		request.classifyMasterContext = true;
		request.classifyContext = true;
		request.input = test.input;
		return request;
	}

	protected InterpreterResponse getResponseForTest(DialogHandler handler,SequenceInterpreterTest test,InterpreterRequest request) {
		return handler.handleInterpreterRequest(request);
	}
	
	protected boolean test() {
		boolean done = false;
		lockMe(this);
		int num = results.size();
		SequenceInterpreterTest test = null;
		if (num<tests.size()) {
			test = tests.get(num);
		}
		unlockMe(this);
		if (test!=null) {
			SequenceInterpreterTestResult result = getNewSequenceInterpreterTestResult();
			result.test = test;
			InterpreterRequest request = getRequestForTest(test);
			DialogHandler handler = getNewDialogHandler();
			Date start = new Date();
			InterpreterResponse response = getResponseForTest(handler,test,request);
			result.time = ((new Date()).getTime() - start.getTime());
			result.response = response;
			result.determineError();
			lockMe(this);
			results.add(result);
			unlockMe(this);
		} else {
			done = true;
			lockMe(this);
			testing = false;
			beforeCreateSummary(results);
			summary = createSummary();
			unlockMe(this);
			for (TesterListener listener: listeners) {
				listener.testingIsDone(this);
			}
		}
		return done;
	}
	
	protected void addTotalsToParent(JsElem parent) {
		// Override to extend
	}

	protected void beforeCreateSummary(List<SequenceInterpreterTestResult> results) {
		// Override to extend
	}

	private JsFile createSummary() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		ZDate timeStamp = new ZDate();
		json.rootElement.children.add(new JsElem("timeStamp",timeStamp.getDateTimeString(),true));
		JsElem totalsElem = new JsElem("totals");
		json.rootElement.children.add(totalsElem);
		JsElem errsElem = new JsElem("errors",true);
		json.rootElement.children.add(errsElem);
		int totalSuccess = 0;
		long totalTime = 0;
		SortedMap<String,Integer> dialogIdErrors = new TreeMap<String,Integer>(); 
		if (results.size()>0) {
			for (SequenceInterpreterTestResult result: results) {
				totalTime = totalTime + result.time;
				if (result.error.length()==0) {
					totalSuccess++;
				} else {
					JsElem errElem = new JsElem();
					errsElem.children.add(errElem);
					result.addErrorJsonToParent(errElem);
					Integer errs = dialogIdErrors.get(result.test.dialogId);
					if (errs==null) {
						errs = new Integer(0);
					}
					errs++;
					dialogIdErrors.put(result.test.dialogId,errs);
				}
			}
		}
		float successPercentage = 0.0F;
		long averageRequestMs = 0;
		if (results.size()>0) {
			successPercentage = ((float)totalSuccess * 100F) / (float)results.size();
			averageRequestMs = (totalTime / results.size());
		}
		totalsElem.children.add(new JsElem("tests","" + tests.size()));
		totalsElem.children.add(new JsElem("successful","" + totalSuccess));
		totalsElem.children.add(new JsElem("successPercentage","" + successPercentage));
		if (baseLineSummary!=null) {
			float previousPercentage = baseLineSummary.rootElement.getChildByName("totals").getChildFloat("successPercentage",-1F);
			if (previousPercentage>=0) {
				float difference = (successPercentage - previousPercentage);
				totalsElem.children.add(new JsElem("baseLineDifference","" + difference));
			}
		}
		totalsElem.children.add(new JsElem("durationMs","" + ((new Date()).getTime() - started.getTime())));
		totalsElem.children.add(new JsElem("averageRequestMs","" + averageRequestMs));
		addTotalsToParent(totalsElem);
		if (dialogIdErrors.size()>0) {
			JsElem dialogsElem = new JsElem("errorsPerDialog",true);
			totalsElem.children.add(dialogsElem);
			SortedMap<Integer,List<String>> errorDialogIds = new TreeMap<Integer,List<String>>();
			for (Entry<String,Integer> entry: dialogIdErrors.entrySet()) {
				List<String> dialogIds = errorDialogIds.get(entry.getValue());
				if (dialogIds==null) {
					dialogIds = new ArrayList<String>();
				}
				dialogIds.add(entry.getKey());
				errorDialogIds.put(entry.getValue(),dialogIds);
			}
			for (Entry<Integer,List<String>> entry: errorDialogIds.entrySet()) {
				for (String dialogId: entry.getValue()) {
					JsElem dialogElem = new JsElem();
					dialogsElem.children.add(0,dialogElem);
					dialogElem.children.add(new JsElem("id",dialogId,true));
					dialogElem.children.add(new JsElem("errors","" + entry.getKey()));
				}
			}
		}
		return json;
	}
}

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
import nl.zeesoft.zsd.sequence.SequenceClassifierResult;

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
			for (DialogIO example: dialog.getExamples()) {
				boolean found = false;
				for (String language: configuration.getBase().getSupportedLanguages()) {
					if (!language.equals(dialog.getLanguage())) {
						DialogInstance test = configuration.getDialogSet().getDialog(language,dialog.getMasterContext(),dialog.getContext());
						if (test!=null) {
							for (DialogIO testExample: test.getExamples()) {
								if (testExample.input.equals(example.input)) {
									found = true;
									break;
								}
							}
						}
					}
					if (found) {
						break;
					}
				}
				SequenceInterpreterTest test = new SequenceInterpreterTest();
				test.dialogId = dialog.getId();
				test.input = example.input;
				if (!found) {
					test.expectedLanguage = dialog.getLanguage();
				}
				test.expectedMasterContext = dialog.getMasterContext();
				test.expectedContext = dialog.getContext();
				tests.add(test);
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
			InterpreterRequest request = new InterpreterRequest();
			if (test.expectedLanguage.length()>0) {
				request.classifyLanguage = true;
			}
			request.classifyMasterContext = true;
			request.classifyContext = true;
			request.input = test.input;
			DialogHandler handler = new DialogHandler(configuration);
			InterpreterResponse response = handler.handleInterpreterRequest(request);
			SequenceInterpreterTestResult result = new SequenceInterpreterTestResult();
			result.test = test;
			result.response = response;
			result.determineResult();
			lockMe(this);
			results.add(result);
			unlockMe(this);
		} else {
			done = true;
			lockMe(this);
			testing = false;
			summary = createSummary();
			unlockMe(this);
			for (TesterListener listener: listeners) {
				listener.testingIsDone();
			}
		}
		return done;
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
		SortedMap<String,Integer> dialogIdErrors = new TreeMap<String,Integer>(); 
		if (results.size()>0) {
			for (SequenceInterpreterTestResult result: results) {
				if (result.success) {
					totalSuccess++;
				} else {
					JsElem errElem = new JsElem();
					errsElem.children.add(errElem);
					errElem.children.add(new JsElem("dialog",result.test.dialogId,true));
					errElem.children.add(new JsElem("input",result.test.input,true));
					if (result.response.request.classifyLanguage && result.response.responseLanguages.size()==0) {
						errElem.children.add(new JsElem("error","Failed to classify language",true));
					} else if (result.response.responseMasterContexts.size()==0) {
						errElem.children.add(new JsElem("error","Failed to classify master context",true));
					} else if (result.response.responseContexts.size()==0) {
						errElem.children.add(new JsElem("error","Failed to classify context",true));
					} else if (result.test.expectedLanguage.length()>0 && result.response.responseLanguages.size()>0 && !result.response.responseLanguages.get(0).symbol.equals(result.test.expectedLanguage)) {
						SequenceClassifierResult res = result.response.responseLanguages.get(0);
						SequenceClassifierResult found = getResultBySymbol(result.response.responseLanguages,result.test.expectedLanguage);
						ZStringBuilder err = getMismatchError("language",res.symbol,res.probNormalized,result.test.expectedLanguage,found);
						errElem.children.add(new JsElem("error",err,true));
					} else if (result.response.responseMasterContexts.size()>0 && !result.response.responseMasterContexts.get(0).symbol.equals(result.test.expectedMasterContext)) {
						SequenceClassifierResult res = result.response.responseMasterContexts.get(0);
						SequenceClassifierResult found = getResultBySymbol(result.response.responseMasterContexts,result.test.expectedMasterContext);
						ZStringBuilder err = getMismatchError("master context",res.symbol,res.probNormalized,result.test.expectedMasterContext,found);
						errElem.children.add(new JsElem("error",err,true));
					} else if (result.response.responseContexts.size()>0 && !result.response.responseContexts.get(0).symbol.equals(result.test.expectedContext)) {
						SequenceClassifierResult res = result.response.responseContexts.get(0);
						SequenceClassifierResult found = getResultBySymbol(result.response.responseContexts,result.test.expectedContext);
						ZStringBuilder err = getMismatchError("context",res.symbol,res.probNormalized,result.test.expectedContext,found);
						errElem.children.add(new JsElem("error",err,true));
					}
					Integer errs = dialogIdErrors.get(result.test.dialogId);
					if (errs==null) {
						errs = new Integer(0);
					}
					errs++;
					dialogIdErrors.put(result.test.dialogId,errs);
				}
			}
		}
		int successPercentage = 0;
		if (results.size()>0) {
			successPercentage = (totalSuccess * 100) / results.size();
		}
		totalsElem.children.add(new JsElem("tests","" + tests.size()));
		totalsElem.children.add(new JsElem("durationMs","" + ((new Date()).getTime() - started.getTime())));
		totalsElem.children.add(new JsElem("successful","" + totalSuccess));
		totalsElem.children.add(new JsElem("successPercentage","" + successPercentage));
		if (baseLineSummary!=null) {
			int previousPercentage = baseLineSummary.rootElement.getChildByName("totals").getChildInt("successPercentage",-1);
			if (previousPercentage>=0) {
				int difference = (successPercentage - previousPercentage);
				totalsElem.children.add(new JsElem("baseLineDifference","" + difference));
			}
		}
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
	
	private ZStringBuilder getMismatchError(String property, String symbol, double prob, String expected,SequenceClassifierResult expectedFound) {
		ZStringBuilder err = new ZStringBuilder();
		err.append("Response ");
		err.append(property);
		err.append(" does not match expected ");
		err.append(property);
		err.append(": ");
		err.append(symbol);
		err.append(" (");
		err.append("" + prob);
		err.append(") <> ");
		err.append(expected);
		err.append(" (");
		if (expectedFound!=null) {
			err.append("" + expectedFound.probNormalized);
		} else {
			err.append("?");
		}
		err.append(")");
		return err;
	}
	
	private SequenceClassifierResult getResultBySymbol(List<SequenceClassifierResult> results,String symbol) {
		SequenceClassifierResult r = null;
		for (SequenceClassifierResult res: results) {
			if (res.symbol.equals(symbol)) {
				r = res;
				break;
			}
		}
		return r;
	}
}

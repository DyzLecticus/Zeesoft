package nl.zeesoft.zsd.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsd.DialogHandler;
import nl.zeesoft.zsd.interpret.InterpreterRequest;
import nl.zeesoft.zsd.interpret.InterpreterResponse;
import nl.zeesoft.zsd.interpret.SequenceInterpreterTest;
import nl.zeesoft.zsd.interpret.SequenceInterpreterTestResult;
import nl.zeesoft.zsd.interpret.SequenceInterpreterTester;

public class DialogHandlerTester extends SequenceInterpreterTester {
	private SortedMap<String,List<String>> 	dialogInputs			= new TreeMap<String,List<String>>();
	private int								outputTests				= 0;
	private int								succesfulOutputTests	= 0;
	private long							totalOutputTime			= 0;
	
	public DialogHandlerTester(DialogHandlerConfiguration configuration) {
		super(null,null,configuration);
	}

	public DialogHandlerTester(Messenger msgr, WorkerUnion uni, DialogHandlerConfiguration configuration) {
		super(msgr,uni,configuration);
	}

	@Override
	protected SequenceInterpreterTest getNewSequenceInterpreterTest() {
		return new DialogHandlerTest();
	}
	
	@Override
	protected SequenceInterpreterTestResult getNewSequenceInterpreterTestResult() {
		return new DialogHandlerTestResult();
	}

	@Override
	protected SequenceInterpreterTest getTestForDialogExample(DialogInstance dialog,DialogIO example,boolean languageUnique) {
		DialogHandlerTest test = (DialogHandlerTest) super.getTestForDialogExample(dialog,example,languageUnique);
		if (dialog.getHandlerClassName().length()==0 && example.output.length()>0 &&
			!(example.output.contains("{") && example.output.contains("}"))
			) {
			List<String> dialogInput = dialogInputs.get(dialog.getId());
			if (dialogInput==null) {
				dialogInput = new ArrayList<String>();
				dialogInputs.put(dialog.getId(),dialogInput);
			}
			if (!dialogInput.contains(example.input.toString())) {
				dialogInput.add(example.input.toString());
				test.expectedOutput = example.output;
				for (DialogVariable variable: dialog.getVariables()) {
					if (variable.initialValue.length()==0) {
						for (DialogVariablePrompt prompt: variable.prompts) {
							if (!(prompt.prompt.contains("{") && prompt.prompt.contains("}"))) {
								test.expectedPrompt = prompt.prompt;
							}
							break;
						}
					}
					break;
				}
			}
		}
		return test;
	}
	
	@Override
	protected InterpreterRequest getRequestForTest(SequenceInterpreterTest test) {
		DialogRequest request = new DialogRequest();
		boolean allActions = false;
		if (test instanceof DialogHandlerTest) {
			DialogHandlerTest tst = (DialogHandlerTest) test;
			if (tst.expectedOutput.length()>0) {
				allActions = true;
			}
		}
		if (allActions) {
			request.setAllActions(true);
			if (!test.languageUnique) {
				request.classifyLanguage = false;
				request.language = test.expectedLanguage;
			}
		} else {
			if (test.languageUnique) {
				request.classifyLanguage = true;
			}
			request.classifyMasterContext = true;
			request.classifyContext = true;
		}
		request.appendDebugLog = false;
		request.randomizeOutput = false;
		request.input = test.input;
		return request;
	}

	@Override
	protected InterpreterResponse getResponseForTest(DialogHandler handler,SequenceInterpreterTest test,InterpreterRequest request) {
		return handler.handleDialogRequest((DialogRequest) request);
	}

	@Override
	protected void beforeCreateSummary(List<SequenceInterpreterTestResult> results) {
		outputTests = 0;
		totalOutputTime = 0;
		succesfulOutputTests = 0;
		for (SequenceInterpreterTestResult result: results) {
			DialogHandlerTestResult res = (DialogHandlerTestResult) result;
			DialogHandlerTest tst = (DialogHandlerTest) result.test;
			if (tst.expectedOutput.length()>0) {
				totalOutputTime = (totalOutputTime + res.time);
				outputTests++;
				if (res.error.length()==0) {
					succesfulOutputTests++;
				}
			}
		}
	}

	@Override
	protected void addTotalsToParent(JsElem parent) {
		float successPercentageOutputTests = 0.0F;
		long averageOutputTestRequestMs = 0;
		if (outputTests>0) {
			successPercentageOutputTests = ((float)succesfulOutputTests * 100F) / (float)outputTests;
			averageOutputTestRequestMs = (totalOutputTime / outputTests);
		}
		parent.children.add(new JsElem("outputTests","" + outputTests));
		parent.children.add(new JsElem("succesfulOutputTests","" + succesfulOutputTests));
		parent.children.add(new JsElem("successPercentageOutputTests","" + successPercentageOutputTests));
		parent.children.add(new JsElem("averageOutputTestRequestMs","" + averageOutputTestRequestMs));
	}
}

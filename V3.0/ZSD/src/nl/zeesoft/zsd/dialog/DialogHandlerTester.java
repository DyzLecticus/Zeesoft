package nl.zeesoft.zsd.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
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
		if (example.output.length()>0) {
			List<String> dialogInput = dialogInputs.get(dialog.getId());
			if (dialogInput==null) {
				dialogInput = new ArrayList<String>();
				dialogInputs.put(dialog.getId(),dialogInput);
			}
			if (!dialogInput.contains(example.input.toString())) {
				dialogInput.add(example.input.toString());
				test.expectedOutput = example.output;
				if (dialog.getVariables().size()>0) {
					for (DialogVariablePrompt prompt: dialog.getVariables().get(0).prompts) {
						test.expectedPrompt = prompt.prompt;
						break;
					}
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
			} else {
				request.language = test.expectedLanguage;
			}
			request.classifyMasterContext = true;
			request.classifyContext = true;
		}
		request.isTestRequest = true;
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
	protected JsFile createSummary(List<SequenceInterpreterTestResult> results) {
		JsFile json = super.createSummary(results);

		int outputTests = 0;
		long totalOutputTime = 0;
		int succesfulOutputTests = 0;
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
		float successPercentageOutputTests = 0.0F;
		long averageOutputTestRequestMs = 0;
		if (outputTests>0) {
			successPercentageOutputTests = ((float)succesfulOutputTests * 100F) / (float)outputTests;
			averageOutputTestRequestMs = (totalOutputTime / outputTests);
		}
		
		JsElem parent = json.rootElement.getChildByName("totals");
		parent.children.add(new JsElem("outputTests","" + outputTests));
		parent.children.add(new JsElem("succesfulOutputTests","" + succesfulOutputTests));
		parent.children.add(new JsElem("successPercentageOutputTests","" + successPercentageOutputTests));
		parent.children.add(new JsElem("averageOutputTestRequestMs","" + averageOutputTestRequestMs));
		
		return json;
	}
}

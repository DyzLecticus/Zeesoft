package nl.zeesoft.zsd.dialog;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zsd.interpret.SequenceInterpreterTestResult;

public class DialogHandlerTestResult extends SequenceInterpreterTestResult {
	private boolean outputError = false;
	private boolean promptError = false;
	
	@Override
	public void determineError() {
		super.determineError();
		if (error.length()==0 && test instanceof DialogHandlerTest && response instanceof DialogResponse) {
			DialogHandlerTest tst = (DialogHandlerTest) test;
			DialogResponse res = (DialogResponse) response;
			if (tst.expectedOutput.length()>0 && (res.contextOutputs.size()==0 || !res.contextOutputs.get(0).output.equals(tst.expectedOutput))) {
				error = "Response output does not match expected output";
				outputError = true;
			} else if (tst.expectedPrompt.length()>0 && (res.contextOutputs.size()==0 || !res.contextOutputs.get(0).prompt.equals(tst.expectedPrompt))) {
				error = "Response prompt does not match expected prompt";
				promptError = true;
			}
		}
	}

	@Override
	public void addErrorJsonToParent(JsElem parent) {
		super.addErrorJsonToParent(parent);
		if (outputError || promptError) {
			DialogHandlerTest tst = (DialogHandlerTest) test;
			DialogResponse res = (DialogResponse) response;
			ZStringSymbolParser output = new ZStringSymbolParser();
			ZStringSymbolParser prompt = new ZStringSymbolParser();
			if (res.contextOutputs.size()>0) {
				output = res.contextOutputs.get(0).output;
				prompt = res.contextOutputs.get(0).prompt;
			}
			if (outputError) {
				parent.children.add(new JsElem("output",output,true));
				parent.children.add(new JsElem("expectedOutput",tst.expectedOutput,true));
			} else if (promptError) {
				parent.children.add(new JsElem("prompt",prompt,true));
				parent.children.add(new JsElem("expectedPrompt",tst.expectedPrompt,true));
			}
		}
	}
}

package nl.zeesoft.zid.dialog;

import nl.zeesoft.zdk.ZStringSymbolParser;

/**
 * Dialog variable question / answer example.
 */
public class DialogVariableExample {
	private ZStringSymbolParser	question	= null; 
	private ZStringSymbolParser	answer		= null;
	
	protected DialogVariableExample(ZStringSymbolParser question, ZStringSymbolParser answer) {
		this.question = question;
		this.answer = answer;
	}
	
	public ZStringSymbolParser getQuestion() {
		return question;
	}

	public ZStringSymbolParser getAnswer() {
		return answer;
	}
}

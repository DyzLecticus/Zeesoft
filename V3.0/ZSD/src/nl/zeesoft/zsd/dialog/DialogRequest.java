package nl.zeesoft.zsd.dialog;

import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.interpret.InterpreterRequest;

public class DialogRequest extends InterpreterRequest {
	public SortedMap<String,String> dialogVariableValues = new TreeMap<String,String>();

	public DialogRequest() {
		
	}
	
	public DialogRequest(ZStringSymbolParser input) {
		this.input = input;
	}
	
	public DialogRequest(ZStringSymbolParser prompt,ZStringSymbolParser input) {
		this.prompt = prompt;
		this.input = input;
	}

	public DialogRequest(String input) {
		this.input.append(input);
	}
	
	public DialogRequest(String prompt,String input) {
		this.prompt.append(prompt);
		this.input.append(input);
	}
}

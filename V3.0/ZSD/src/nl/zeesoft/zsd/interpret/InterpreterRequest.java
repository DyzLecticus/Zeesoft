package nl.zeesoft.zsd.interpret;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;

public class InterpreterRequest {
	public ZStringSymbolParser	prompt					= new ZStringSymbolParser();
	public ZStringSymbolParser	input					= new ZStringSymbolParser();
	public String				language				= "";
	public String				masterContext			= "";
	public String				context					= "";
	
	public boolean				appendDebugLog			= false;
	public boolean				classifyLanguage		= false;
	public boolean				correctInput			= false;
	public boolean				classifyMasterContext	= false;
	public boolean				classifyContext			= false;
	public boolean				translateEntiyValues	= false;

	public List<String>			translateEntityTypes	= new ArrayList<String>();
		
	public InterpreterRequest() {
		
	}
	
	public InterpreterRequest(ZStringSymbolParser input) {
		this.input = input;
	}
	
	public InterpreterRequest(ZStringSymbolParser prompt,ZStringSymbolParser input) {
		this.prompt = prompt;
		this.input = input;
	}

	public InterpreterRequest(String input) {
		this.input.append(input);
	}
	
	public InterpreterRequest(String prompt,String input) {
		this.prompt.append(prompt);
		this.input.append(input);
	}
	
	public void setAllActions(boolean value) {
		appendDebugLog = value;
		classifyLanguage = value;
		correctInput = value;
		classifyMasterContext = value;
		classifyContext = value;
		translateEntiyValues = value;
	}
}

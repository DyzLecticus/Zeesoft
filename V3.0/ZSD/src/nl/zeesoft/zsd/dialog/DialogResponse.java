package nl.zeesoft.zsd.dialog;

import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.interpret.InterpreterResponse;

public class DialogResponse extends InterpreterResponse {
	public ZStringSymbolParser						output							= new ZStringSymbolParser();
	public SortedMap<String,DialogVariableValue>	responseDialogVariableValues	= new TreeMap<String,DialogVariableValue>();
	
	public DialogResponse(DialogRequest r) {
		super(r);
	}
	
	public DialogRequest getRequest() {
		return (DialogRequest) request;
	}
}

package nl.zeesoft.zsd.dialog;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.interpret.InterpreterResponse;

public class DialogResponse extends InterpreterResponse {
	public ZStringSymbolParser		output							= new ZStringSymbolParser();
	public SortedMap<String,String>	responseDialogVariableValues	= new TreeMap<String,String>();
	
	public DialogResponse(DialogRequest r) {
		super(r);
		for (Entry<String,String> entry: r.dialogVariableValues.entrySet()) {
			responseDialogVariableValues.put(entry.getKey(),entry.getValue());
		}
	}
}

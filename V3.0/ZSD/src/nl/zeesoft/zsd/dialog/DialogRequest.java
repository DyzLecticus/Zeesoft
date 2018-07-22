package nl.zeesoft.zsd.dialog;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.interpret.InterpreterRequest;

public class DialogRequest extends InterpreterRequest {
	public SortedMap<String,DialogVariableValue>	dialogVariableValues	= new TreeMap<String,DialogVariableValue>();
	
	public double									matchThreshold			= 0.7D;
	public boolean									randomizeOutput			= true;

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
	
	public String getDialogId() {
		return language + "/" + masterContext + "/" + context;
	}
	
	@Override
	public JsFile toJson() {
		JsFile json = super.toJson();
		json.rootElement.children.add(new JsElem("matchThreshold","" + matchThreshold));
		json.rootElement.children.add(new JsElem("randomizeOutput","" + randomizeOutput));
		JsElem valsElem = new JsElem("dialogVariableValues",true);
		json.rootElement.children.add(valsElem);
		for (Entry<String,DialogVariableValue> entry: dialogVariableValues.entrySet()) {
			JsElem valElem = new JsElem();
			valsElem.children.add(valElem);
			valElem.children.add(new JsElem("name",entry.getValue().name,true));
			valElem.children.add(new JsElem("externalValue",entry.getValue().externalValue,true));
			valElem.children.add(new JsElem("internalValue",entry.getValue().internalValue,true));
		}
		return json;
	}
	
	@Override
	public void fromJson(JsFile json) {
		super.fromJson(json);
		matchThreshold = json.rootElement.getChildDouble("matchThreshold",matchThreshold);
		randomizeOutput = json.rootElement.getChildBoolean("randomizeOutput",randomizeOutput);
		JsElem valsElem = json.rootElement.getChildByName("dialogVariableValues");
		if (valsElem!=null) {
			dialogVariableValues.clear();
			for (JsElem val: valsElem.children) {
				DialogVariableValue dvv = new DialogVariableValue();
				dvv.name = val.getChildString("name","");
				dvv.externalValue = val.getChildString("externalValue","");
				dvv.internalValue = val.getChildString("internalValue","");
				dialogVariableValues.put(dvv.name,dvv);
			}
		}
	}
}

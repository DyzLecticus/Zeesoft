package nl.zeesoft.zsd.dialog;

import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.interpret.InterpreterResponse;

public class DialogResponse extends InterpreterResponse {
	public SortedMap<String,DialogResponseOutput>	contextOutputs	= new TreeMap<String,DialogResponseOutput>();
	
	public DialogResponse() {
		super(null);
	}
	
	public DialogResponse(DialogRequest r) {
		super(r);
	}
	
	public DialogRequest getRequest() {
		return (DialogRequest) request;
	}
	
	@Override
	public JsFile toJson() {
		JsFile json = super.toJson();
		JsElem coElem = new JsElem("contextOutputs",true);
		json.rootElement.children.add(coElem);
		for (DialogResponseOutput output: contextOutputs.values()) {
			JsElem oElem = new JsElem();
			coElem.children.add(oElem);
			oElem.children.add(new JsElem("context",output.context,true));
			oElem.children.add(new JsElem("output",output.output,true));
			oElem.children.add(new JsElem("prompt",output.prompt,true));
			oElem.children.add(new JsElem("promptVariable",output.promptVariable,true));
			JsElem dvElem = new JsElem("dialogVariableValues",true);
			oElem.children.add(dvElem);
			for (DialogVariableValue dvv: output.values.values()) {
				JsElem dElem = new JsElem();
				dvElem.children.add(dElem);
				dElem.children.add(new JsElem("name",dvv.name,true));
				dElem.children.add(new JsElem("externalValue",dvv.externalValue,true));
				dElem.children.add(new JsElem("internalValue",dvv.internalValue,true));
			}
		}
		return json;
	}
	
	@Override
	public void fromJson(JsFile json) {
		super.fromJson(json);
		JsElem coElem = json.rootElement.getChildByName("contextOutputs");
		if (coElem!=null) {
			contextOutputs.clear();
			for (JsElem oElem: coElem.children) {
				DialogResponseOutput output = new DialogResponseOutput();
				output.context = oElem.getChildString("context");
				output.output = oElem.getChildZStringSymbolParser("output");
				output.prompt = oElem.getChildZStringSymbolParser("prompt");
				output.promptVariable = oElem.getChildString("promptVariable");
				contextOutputs.put(output.context,output);
				JsElem dvElem = oElem.getChildByName("dialogVariableValues");
				if (dvElem!=null) {
					for (JsElem dElem: dvElem.children) {
						DialogVariableValue dvv = new DialogVariableValue();
						dvv.name = dElem.getChildString("name");
						dvv.externalValue = dElem.getChildString("externalValue");
						dvv.internalValue = dElem.getChildString("internalValue");
						output.values.put(dvv.name,dvv);
					}
				}
			}
		}
	}
}

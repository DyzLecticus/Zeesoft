package nl.zeesoft.zsdm.dialog;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class DialogExample implements JsAble {
	public ZStringSymbolParser	input					= new ZStringSymbolParser();
	public ZStringSymbolParser	output					= new ZStringSymbolParser();
	
	public boolean				toLanguageClassifier	= true;
	public boolean				toMasterClassifier		= true;
	public boolean				toContextClassifier		= true;
	
	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("input",input,true));
		json.rootElement.children.add(new JsElem("output",output,true));
		json.rootElement.children.add(new JsElem("toLanguageClassifier","" + toLanguageClassifier));
		json.rootElement.children.add(new JsElem("toMasterClassifier","" + toMasterClassifier));
		json.rootElement.children.add(new JsElem("toContextClassifier","" + toContextClassifier));
		return json;
	}
	
	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			input = json.rootElement.getChildZStringSymbolParser("input",input);
			output = json.rootElement.getChildZStringSymbolParser("output",output);
			toLanguageClassifier = json.rootElement.getChildBoolean("toLanguageClassifier",toLanguageClassifier);
			toMasterClassifier = json.rootElement.getChildBoolean("toMasterClassifier",toMasterClassifier);
			toContextClassifier = json.rootElement.getChildBoolean("toContextClassifier",toContextClassifier);
		}
	}
}

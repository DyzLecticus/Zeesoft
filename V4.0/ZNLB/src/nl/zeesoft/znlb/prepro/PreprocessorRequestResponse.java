package nl.zeesoft.znlb.prepro;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.znlb.lang.LanguageRequest;

public class PreprocessorRequestResponse extends LanguageRequest {
	public ZStringSymbolParser	sequence	= new ZStringSymbolParser();
	public ZStringSymbolParser	processed	= new ZStringSymbolParser();
	
	@Override
	public JsFile toJson() {
		JsFile json = super.toJson();
		json.rootElement.children.add(new JsElem("sequence",sequence,true));
		if (processed.length()>0) {
			json.rootElement.children.add(new JsElem("processed",processed,true));
		}
		return json;
	}
	
	@Override
	public void fromJson(JsFile json) {
		super.fromJson(json);
		if (json.rootElement!=null) {
			sequence = json.rootElement.getChildZStringSymbolParser("sequence",sequence);
			processed = json.rootElement.getChildZStringSymbolParser("processed",processed);
		}
	}
}

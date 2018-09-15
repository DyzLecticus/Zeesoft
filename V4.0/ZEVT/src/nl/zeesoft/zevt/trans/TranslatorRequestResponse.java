package nl.zeesoft.zevt.trans;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.lang.LanguageRequest;

public class TranslatorRequestResponse extends LanguageRequest {
	public ZStringSymbolParser	sequence		= new ZStringSymbolParser();
	public ZStringSymbolParser	translation		= new ZStringSymbolParser();
	
	@Override
	public JsFile toJson() {
		JsFile json = super.toJson();
		if (sequence.length()>0) {
			json.rootElement.children.add(new JsElem("sequence",sequence,true));
		} else if (translation.length()>0) {
			json.rootElement.children.add(new JsElem("translation",translation,true));
		}
		return json;
	}
	
	@Override
	public void fromJson(JsFile json) {
		super.fromJson(json);
		if (json.rootElement!=null) {
			sequence = json.rootElement.getChildZStringSymbolParser("sequence",sequence);
			translation = json.rootElement.getChildZStringSymbolParser("translation",translation);
		}
	}
}

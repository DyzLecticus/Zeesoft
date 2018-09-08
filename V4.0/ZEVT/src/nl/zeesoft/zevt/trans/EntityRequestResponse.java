package nl.zeesoft.zevt.trans;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class EntityRequestResponse {
	public ZStringSymbolParser	sequence				= new ZStringSymbolParser();
	public ZStringSymbolParser	entityValueTranslation	= new ZStringSymbolParser();
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		if (sequence.length()>0) {
			json.rootElement.children.add(new JsElem("sequence",sequence,true));
		} else if (entityValueTranslation.length()>0) {
			json.rootElement.children.add(new JsElem("entityValueTranslation",entityValueTranslation,true));
		}
		return json;
	}
	
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			sequence = json.rootElement.getChildZStringSymbolParser("sequence",sequence);
			entityValueTranslation = json.rootElement.getChildZStringSymbolParser("entityValueTranslation",entityValueTranslation);
		}
	}
}

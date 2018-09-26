package nl.zeesoft.zsc.confab;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class TrainingSequence implements JsAble {
	public ZStringSymbolParser	sequence	= new ZStringSymbolParser();
	public ZStringSymbolParser	context		= new ZStringSymbolParser();
	
	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("sequence",sequence,true));
		json.rootElement.children.add(new JsElem("context",context,true));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			sequence = json.rootElement.getChildZStringSymbolParser("sequence",sequence);
			context = json.rootElement.getChildZStringSymbolParser("context",context);
		}
	}
}

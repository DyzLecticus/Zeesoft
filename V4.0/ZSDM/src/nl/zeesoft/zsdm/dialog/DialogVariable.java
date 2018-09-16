package nl.zeesoft.zsdm.dialog;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class DialogVariable implements JsAble {
	public String						name	= "";
	public String						type	= "";
	public boolean						session	= false;
	public List<ZStringSymbolParser>	prompts	= new ArrayList<ZStringSymbolParser>();
	
	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("name",name,true));
		json.rootElement.children.add(new JsElem("type",type,true));
		json.rootElement.children.add(new JsElem("session","" + session));
		JsElem prsElem = new JsElem("prompts",true);
		json.rootElement.children.add(prsElem);
		for (ZStringSymbolParser prompt: prompts) {
			prsElem.children.add(new JsElem(null,prompt,true));
		}
		return json;
	}
	
	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			prompts.clear();
			name = json.rootElement.getChildString("name",name);
			type = json.rootElement.getChildString("type",type);
			session = json.rootElement.getChildBoolean("session",session);
			JsElem prsElem = json.rootElement.getChildByName("prompts");
			for (JsElem prElem: prsElem.children) {
				if (prElem.value!=null && prElem.value.length()>0) {
					prompts.add(new ZStringSymbolParser(prElem.value));
				}
			}
		}
	}
}

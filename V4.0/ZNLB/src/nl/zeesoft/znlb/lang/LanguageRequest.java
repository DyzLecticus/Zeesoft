package nl.zeesoft.znlb.lang;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public abstract class LanguageRequest implements JsAble {
	public List<String>			languages		= new ArrayList<String>();
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem langsElem = new JsElem("languages",true);
		json.rootElement.children.add(langsElem);
		for (String language: languages) {
			langsElem.children.add(new JsElem(null,language,true));
		}
		return json;
	}
	
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			JsElem langsElem = json.rootElement.getChildByName("languages");
			if (langsElem!=null) {
				for (JsElem langElem: langsElem.children) {
					languages.add(langElem.value.toString());
				}
			}
		}
	}
}

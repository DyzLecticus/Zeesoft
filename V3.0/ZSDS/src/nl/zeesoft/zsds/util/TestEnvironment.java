package nl.zeesoft.zsds.util;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class TestEnvironment {
	public String	name		= "";
	public String	url			= "";
	public String	fileName	= "";
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("name",name,true));
		json.rootElement.children.add(new JsElem("url",url,true));
		json.rootElement.children.add(new JsElem("fileName",fileName,true));
		return json;
	}

	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			name = json.rootElement.getChildString("name");
			url = json.rootElement.getChildString("url");
			fileName = json.rootElement.getChildString("fileName");
		}
	}
}

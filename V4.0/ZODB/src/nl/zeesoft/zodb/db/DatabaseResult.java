package nl.zeesoft.zodb.db;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class DatabaseResult {
	public String	name	= "";
	public long		id		= 0L;
	public JsFile	obj		= null;
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("name",name,true));
		json.rootElement.children.add(new JsElem("id","" + id));
		if (obj!=null && obj.rootElement!=null) {
			JsElem objElem = new JsElem("object");
			json.rootElement.children.add(objElem);
			objElem.children.add(obj.rootElement);
		}
		return json;
	}
	
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			name = json.rootElement.getChildString("name",name);
			id = json.rootElement.getChildLong("id",id);
			JsElem objElem = json.rootElement.getChildByName("object");
			if (objElem!=null && objElem.children.size()>0) {
				obj = new JsFile();
				obj.rootElement = objElem.children.get(0);
			}
		}
	}
}

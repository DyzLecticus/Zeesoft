package nl.zeesoft.zodb.db;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class DatabaseResult {
	public String	name		= "";
	public long		id			= 0L;
	public long		modified	= 0L;
	public JsFile	obj			= null;
	
	public DatabaseResult() {
		
	}
	
	public DatabaseResult(IndexElement element) {
		this.name = element.name;
		this.id = element.id;
		this.modified = element.modified;
		this.obj = element.obj;
	}
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("name",name,true));
		json.rootElement.children.add(new JsElem("id","" + id));
		if (modified>0) {
			json.rootElement.children.add(new JsElem("modified","" + modified));
		}
		if (obj!=null && obj.rootElement!=null && obj.rootElement.children.size()>0) {
			JsElem objElem = new JsElem("object");
			json.rootElement.children.add(objElem);
			objElem.children = obj.rootElement.children;
		}
		return json;
	}
	
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			name = json.rootElement.getChildString("name",name);
			id = json.rootElement.getChildLong("id",id);
			modified = json.rootElement.getChildLong("modified",modified);
			JsElem objElem = json.rootElement.getChildByName("object");
			if (objElem!=null && objElem.children.size()>0) {
				obj = new JsFile();
				obj.rootElement = new JsElem();
				obj.rootElement.children = objElem.children;
			}
		}
	}
}

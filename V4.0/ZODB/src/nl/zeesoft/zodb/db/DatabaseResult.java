package nl.zeesoft.zodb.db;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class DatabaseResult implements JsAble {
	public ZStringBuilder	name		= new ZStringBuilder();
	public long				id			= 0L;
	public long				modified	= 0L;
	
	public JsFile			object		= null;
	public ZStringBuilder	encoded		= null;
	
	public DatabaseResult() {
		
	}
	
	public DatabaseResult(IndexElement element) {
		this.name = element.name;
		this.id = element.id;
		this.modified = element.modified;
		this.object = element.obj;
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("name",name,true));
		json.rootElement.children.add(new JsElem("id","" + id));
		if (modified>0) {
			json.rootElement.children.add(new JsElem("modified","" + modified));
		}
		if (encoded!=null && encoded.length()>0) {
			json.rootElement.children.add(new JsElem("encoded",encoded,true));
		} else if (object!=null && object.rootElement!=null && object.rootElement.children.size()>0) {
			JsElem objElem = new JsElem("object");
			json.rootElement.children.add(objElem);
			objElem.children = object.rootElement.children;
		}
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			name = json.rootElement.getChildZStringBuilder("name",name);
			id = json.rootElement.getChildLong("id",id);
			modified = json.rootElement.getChildLong("modified",modified);
			encoded = json.rootElement.getChildZStringBuilder("encoded",encoded);
			JsElem objElem = json.rootElement.getChildByName("object");
			if (objElem!=null && objElem.children.size()>0) {
				object = new JsFile();
				object.rootElement = new JsElem();
				object.rootElement.children = objElem.children;
			}
		}
	}
}

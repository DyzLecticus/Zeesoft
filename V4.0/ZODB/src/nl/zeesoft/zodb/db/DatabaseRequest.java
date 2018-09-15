package nl.zeesoft.zodb.db;

import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class DatabaseRequest implements JsAble {
	public static final String	TYPE_LIST	= "LIST";
	public static final String	TYPE_GET	= "GET";
	public static final String	TYPE_ADD	= "ADD";
	public static final String	TYPE_SET	= "SET";
	public static final String	TYPE_REMOVE	= "REMOVE";
	
	public String				type		= "";
	public int					start		= 0;
	public int					max			= 10;
	public long					id			= 0L;
	public String				name		= "";
	public String				contains		= "";
	public String				startsWith	= "";
	public JsFile				obj			= null;
	
	public DatabaseRequest() {
		
	}
	
	public DatabaseRequest(String type) {
		this.type = type;
	}
	
	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("type",type,true));
		if (id>0) {
			json.rootElement.children.add(new JsElem("id","" + id));
		}
		if (name.length()>0) {
			json.rootElement.children.add(new JsElem("name",name,true));
		} else if (contains.length()>0) {
			json.rootElement.children.add(new JsElem("contains",contains,true));
		} else if (startsWith.length()>0) {
			json.rootElement.children.add(new JsElem("startsWith",startsWith,true));
		}
		if (type.equals(TYPE_LIST)) {
			json.rootElement.children.add(new JsElem("start","" + start));
			json.rootElement.children.add(new JsElem("max","" + max));
		}
		if (obj!=null && obj.rootElement!=null && obj.rootElement.children.size()>0) {
			JsElem objElem = new JsElem("object");
			json.rootElement.children.add(objElem);
			objElem.children = obj.rootElement.children;
		}
		return json;
	}
	
	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			type = json.rootElement.getChildString("type",type);
			id = json.rootElement.getChildLong("id",id);
			name = json.rootElement.getChildString("name",name);
			contains = json.rootElement.getChildString("contains",contains);
			startsWith = json.rootElement.getChildString("startsWith",startsWith);
			start = json.rootElement.getChildInt("start",start);
			max = json.rootElement.getChildInt("max",max);
			JsElem objElem = json.rootElement.getChildByName("object");
			if (objElem!=null && objElem.children.size()>0) {
				obj = new JsFile();
				obj.rootElement = new JsElem();
				obj.rootElement.children = objElem.children;
			}
		}
	}
}

package nl.zeesoft.zodb.db.idx;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class IndexRequest extends SearchIndex {
	public static final String	TYPE_ADD	= "ADD";
	public static final String	TYPE_REMOVE	= "REMOVE";
	
	public String type = "";
	public String name = "";
	
	@Override
	public JsFile toJson() {
		JsFile json = null;
		if (type.equals(TYPE_ADD)) {
			json = super.toJson();
		} else {
			json = new JsFile();
			json.rootElement = new JsElem();
		}
		json.rootElement.children.add(0,new JsElem("name",name,true));
		json.rootElement.children.add(0,new JsElem("type",type,true));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		super.fromJson(json);
		if (json.rootElement!=null) {
			type = json.rootElement.getChildString("type",type);
			name = json.rootElement.getChildString("name",name);
		}
	}
}

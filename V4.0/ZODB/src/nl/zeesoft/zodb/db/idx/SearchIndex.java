package nl.zeesoft.zodb.db.idx;

import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class SearchIndex implements JsAble {
	public String		objectNamePrefix 	= "";
	public String		propertyName	 	= "";
	public boolean		numeric				= false;
	public boolean		unique				= false;
	public boolean		added				= false;
	
	public String getName() {
		return getName(objectNamePrefix,propertyName);
	}

	protected static String getName(String objNamePrefix, String propName) {
		return objNamePrefix + ":" + propName;
	}

	protected String getIndexValueForObject(JsFile obj) {
		String r = "";
		JsElem propElem = obj.rootElement.getChildByName(propertyName);
		if (propElem!=null && propElem.value!=null && propElem.value.length()>0) {
			r = propElem.value.toString();
		}
		return r;
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("objectNamePrefix",objectNamePrefix,true));
		json.rootElement.children.add(new JsElem("propertyName",propertyName,true));
		json.rootElement.children.add(new JsElem("numeric","" + numeric));
		json.rootElement.children.add(new JsElem("unique","" + unique));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			objectNamePrefix = json.rootElement.getChildString("objectNamePrefix",objectNamePrefix);
			propertyName = json.rootElement.getChildString("propertyName",propertyName);
			numeric = json.rootElement.getChildBoolean("numeric",numeric);
			unique = json.rootElement.getChildBoolean("unique",unique);
		}
	}
}

package nl.zeesoft.zodb.db.idx;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.db.IndexElement;

public class SearchIndex implements JsAble {
	private IndexObject	index				= null;
	
	public String		objectNamePrefix 	= "";
	public String		propertyName	 	= "";
	public boolean		numeric				= false;
	public boolean		unique				= false;
	public boolean		added				= false;
	
	public String getName() {
		return getName(objectNamePrefix,propertyName);
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

	protected static String getName(String objNamePrefix, String propName) {
		return objNamePrefix + ":" + propName;
	}

	protected ZStringBuilder getIndexValueForObject(JsFile obj) {
		ZStringBuilder r = null;
		JsElem propElem = obj.rootElement.getChildByName(propertyName);
		if (propElem!=null) {
			if (propElem.value!=null) {
				Database.removeControlCharacters(propElem.value);
				r = propElem.value;
			} else if (numeric && propElem.array) {
				r = new ZStringBuilder("" + propElem.children.size());
			}
		}
		return r;
	}

	protected void initialize(Messenger msgr) {
		if (numeric) {
			index = new NumericIndex(msgr,this);
		} else {
			index = new StringIndex(msgr,this);
		}
	}

	protected void destroy() {
		if (index!=null) {
			index.destroy();
		}
	}

	protected boolean hasObject(IndexElement element) {
		return index.hasObject(element);
	}
	
	protected void addObjects(List<IndexElement> elements) {
		index.addObjects(elements);
	}

	protected void addObject(IndexElement element) {
		index.addObject(element);
	}

	protected void setObject(IndexElement element) {
		index.setObject(element);
	}
	
	protected void removeObject(IndexElement element) {
		index.removeObject(element);
	}
	
	protected List<IndexElement> listObjects(boolean ascending,boolean invert,String operator,ZStringBuilder indexValue) {
		return index.listObjects(ascending, invert, operator, indexValue);
	}
	
	protected void clear() {
		if (index!=null) {
			index.clear();
		}
	}
}

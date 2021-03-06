package nl.zeesoft.zodb.db;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.db.idx.IndexConfig;

public class DatabaseRequest implements JsAble {
	public static final String	TYPE_ADD			= "ADD";
	public static final String	TYPE_LIST			= "LIST";
	public static final String	TYPE_GET			= "GET";
	public static final String	TYPE_SET			= "SET";
	public static final String	TYPE_REMOVE			= "REMOVE";

	public static final String	ENC_ASCII			= "ASCII";
	public static final String	ENC_KEY				= "KEY";

	public static final String	OP_EQUALS			= "EQUALS";
	public static final String	OP_CONTAINS			= "CONTAINS";
	public static final String	OP_STARTS_WITH		= "STARTS_WITH";
	public static final String	OP_ENDS_WITH		= "ENDS_WITH";
	public static final String	OP_GREATER			= "GREATER";
	public static final String	OP_GREATER_OR_EQUAL	= "GREATER_OR_EQUAL";
	
	public String				type				= "";
	public int					readTimeOutSeconds	= 3;
	public int					start				= 0;
	public int					max					= 10;
	public long					id					= 0L;
	public ZStringBuilder		name				= new ZStringBuilder();
	public long					modAfter			= 0L;
	public long					modBefore			= 0L;
	public String				encoding			= "";
	
	public String				index				= "";
	public boolean				invert				= false;
	public String				operator			= "";
	public ZStringBuilder		value				= new ZStringBuilder();
	public boolean				ascending			= true;

	public JsFile				object				= null;
	public ZStringBuilder		encoded				= null;
	
	public DatabaseRequest() {
		
	}
	
	public DatabaseRequest(String type) {
		this.type = type;
	}
	
	public DatabaseRequest(String type,String nameStartsWith) {
		this.type = type;
		this.index = IndexConfig.IDX_NAME;
		this.operator = OP_STARTS_WITH;
		this.value.append(nameStartsWith);
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("type",type,true));
		if (id>0) {
			json.rootElement.children.add(new JsElem("id","" + id));
		}
		if (readTimeOutSeconds!=3) {
			json.rootElement.children.add(new JsElem("readTimeOutSeconds","" + readTimeOutSeconds));
		}
		if (name.length()>0) {
			json.rootElement.children.add(new JsElem("name",name,true));
		}
		if (type.equals(TYPE_LIST)) {
			json.rootElement.children.add(new JsElem("start","" + start));
			json.rootElement.children.add(new JsElem("max","" + max));
		}
		if (type.equals(TYPE_LIST) || type.equals(TYPE_GET) || type.equals(TYPE_REMOVE)) {
			if (modAfter>0L) {
				json.rootElement.children.add(new JsElem("modAfter","" + modAfter));
			}
			if (modBefore>0L) {
				json.rootElement.children.add(new JsElem("modBefore","" + modBefore));
			}
		}
		if (index.length()>0) {
			json.rootElement.children.add(new JsElem("index",index,true));
			if (operator.length()>0) {
				json.rootElement.children.add(new JsElem("invert","" + invert));
				json.rootElement.children.add(new JsElem("operator",operator,true));
				json.rootElement.children.add(new JsElem("value",value,true));
			}
			json.rootElement.children.add(new JsElem("ascending","" + ascending));
		}
		if (object!=null && object.rootElement!=null && object.rootElement.children.size()>0) {
			JsElem objElem = new JsElem("object");
			json.rootElement.children.add(objElem);
			objElem.children = object.rootElement.children;
		}
		if (encoding.length()>0) {
			json.rootElement.children.add(new JsElem("encoding",encoding,true));
		}
		if (encoded!=null && encoded.length()>0) {
			json.rootElement.children.add(new JsElem("encoded",encoded,true));
		}
		return json;
	}
	
	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			type = json.rootElement.getChildString("type",type);
			readTimeOutSeconds = json.rootElement.getChildInt("readTimeOutSeconds",readTimeOutSeconds);
 			name = json.rootElement.getChildZStringBuilder("name",name);
			start = json.rootElement.getChildInt("start",start);
			max = json.rootElement.getChildInt("max",max);
			id = json.rootElement.getChildLong("id",id);
			modAfter = json.rootElement.getChildLong("modAfter",modAfter);
			modBefore = json.rootElement.getChildLong("modBefore",modBefore);
			
			index = json.rootElement.getChildString("index",index);
			invert = json.rootElement.getChildBoolean("invert",invert);
			operator = json.rootElement.getChildString("operator",operator);
			value = json.rootElement.getChildZStringBuilder("value",value);
			ascending = json.rootElement.getChildBoolean("ascending",ascending);
			
			JsElem objElem = json.rootElement.getChildByName("object");
			if (objElem!=null && objElem.children.size()>0) {
				object = new JsFile();
				object.rootElement = new JsElem();
				object.rootElement.children = objElem.children;
			}
			encoding = json.rootElement.getChildString("encoding",encoding);
			encoded = json.rootElement.getChildZStringBuilder("encoded",encoded);
		}
	}
}

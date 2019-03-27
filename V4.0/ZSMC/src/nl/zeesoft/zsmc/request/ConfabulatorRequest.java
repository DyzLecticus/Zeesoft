package nl.zeesoft.zsmc.request;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class ConfabulatorRequest implements JsAble {
	public final static String	CORRECT			= "CORRECT";
	public final static String	CONTEXT			= "CONTEXT";
	public final static String	EXTEND			= "EXTEND";
	
	public String				type			= "";
	public String				name			= "";

	public ZStringSymbolParser	input			= new ZStringSymbolParser();
	public boolean				caseSensitive	= false;
	public long					maxTime			= 1000;
	public boolean				appendLog		= false;
	public double				threshold		= 0.3D;
	public double				noise			= 0D;
	public boolean				strict			= true;
	public String				unknownSymbol	= "[?]";
	
	public String				contextSymbol	= "";
	public String				alphabet		= "";
	
	public int					extend			= 1;
	
	public ConfabulatorRequest() {
		
	}
	
	public ConfabulatorRequest(String type,String name,String input) {
		this.type = type;
		this.name = name;
		this.input.append(input);
	}
	
	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("type",type,true));
		json.rootElement.children.add(new JsElem("name",name,true));
		json.rootElement.children.add(new JsElem("input",input,true));
		json.rootElement.children.add(new JsElem("caseSensitive","" + caseSensitive));
		json.rootElement.children.add(new JsElem("maxTime","" + maxTime));
		json.rootElement.children.add(new JsElem("appendLog","" + appendLog));
		json.rootElement.children.add(new JsElem("threshold","" + threshold));
		json.rootElement.children.add(new JsElem("noise","" + noise));
		if (type.equals(CORRECT)) {
			json.rootElement.children.add(new JsElem("strict","" + strict));
			json.rootElement.children.add(new JsElem("contextSymbol",contextSymbol,true));
			json.rootElement.children.add(new JsElem("alphabet",alphabet,true));
		} else if (type.equals(EXTEND)) {
			json.rootElement.children.add(new JsElem("strict","" + strict));
			json.rootElement.children.add(new JsElem("contextSymbol",contextSymbol,true));
			json.rootElement.children.add(new JsElem("extend","" + extend));
			json.rootElement.children.add(new JsElem("unknownSymbol",unknownSymbol));
		}
		return json;
	}
	
	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			type = json.rootElement.getChildString("type",type);
			name = json.rootElement.getChildString("name",name);
			input = json.rootElement.getChildZStringSymbolParser("input",input);
			caseSensitive = json.rootElement.getChildBoolean("caseSensitive",caseSensitive);
			maxTime = json.rootElement.getChildLong("maxTime",maxTime);
			appendLog = json.rootElement.getChildBoolean("appendLog",appendLog);
			threshold = json.rootElement.getChildDouble("threshold",threshold);
			noise = json.rootElement.getChildDouble("noise",noise);
			strict = json.rootElement.getChildBoolean("strict",strict);
			unknownSymbol = json.rootElement.getChildString("unknownSymbol",unknownSymbol);
			contextSymbol = json.rootElement.getChildString("contextSymbol",contextSymbol);
			alphabet = json.rootElement.getChildString("alphabet",alphabet);
			extend = json.rootElement.getChildInt("extend",extend);
		}
	}	
}

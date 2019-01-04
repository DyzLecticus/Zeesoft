package nl.zeesoft.zsc.confab;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class ConfabulatorRequest implements JsAble {
	public final static String	CORRECT			= "CORRECT";
	public final static String	CONTEXT			= "CONTEXT";
	public final static String	EXTEND			= "EXTEND";
	public final static String	SYNONYMIZE		= "SYNONYMIZE";
	
	public String				type			= "";
	public String				name			= "";
	
	public boolean				appendLog		= false;
	public boolean				caseSensitive	= false;
	public double				noise			= 0D;
	public ZStringSymbolParser	input			= new ZStringSymbolParser();
	public long					maxTime			= 1000;
	
	public String				contextSymbol	= "";
	public boolean				validate		= true;
	public boolean				parallel		= true;
	public String				alphabet		= "";
	
	public int					extend			= 1;
	
	public int					width			= 4;
	
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
		json.rootElement.children.add(new JsElem("appendLog","" + appendLog));
		json.rootElement.children.add(new JsElem("caseSensitive","" + caseSensitive));
		json.rootElement.children.add(new JsElem("noise","" + noise));
		json.rootElement.children.add(new JsElem("input",input,true));
		json.rootElement.children.add(new JsElem("maxTime","" + maxTime));
		if (type.equals(CORRECT)) {
			json.rootElement.children.add(new JsElem("contextSymbol",contextSymbol,true));
			json.rootElement.children.add(new JsElem("validate","" + validate));
			json.rootElement.children.add(new JsElem("parallel","" + parallel));
			json.rootElement.children.add(new JsElem("alphabet",alphabet,true));
		} else if (type.equals(EXTEND)) {
			json.rootElement.children.add(new JsElem("contextSymbol",contextSymbol,true));
			json.rootElement.children.add(new JsElem("parallel","" + parallel));
			json.rootElement.children.add(new JsElem("extend","" + extend));
		} else if (type.equals(SYNONYMIZE)) {
			json.rootElement.children.add(new JsElem("contextSymbol",contextSymbol,true));
			json.rootElement.children.add(new JsElem("parallel","" + parallel));
			json.rootElement.children.add(new JsElem("width","" + width));
		}
		return json;
	}
	
	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			type = json.rootElement.getChildString("type",type);
			name = json.rootElement.getChildString("name",name);
			appendLog = json.rootElement.getChildBoolean("appendLog",appendLog);
			caseSensitive = json.rootElement.getChildBoolean("caseSensitive",caseSensitive);
			noise = json.rootElement.getChildDouble("noise",noise);
			input = json.rootElement.getChildZStringSymbolParser("input",input);
			maxTime = json.rootElement.getChildLong("maxTime",maxTime);
			contextSymbol = json.rootElement.getChildString("contextSymbol",contextSymbol);
			validate = json.rootElement.getChildBoolean("validate",validate);
			parallel = json.rootElement.getChildBoolean("parallel",parallel);
			alphabet = json.rootElement.getChildString("alphabet",alphabet);
			extend = json.rootElement.getChildInt("extend",extend);
			width = json.rootElement.getChildInt("width",width);
		}
	}	
}

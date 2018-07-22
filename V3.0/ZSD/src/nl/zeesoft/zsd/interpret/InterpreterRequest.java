package nl.zeesoft.zsd.interpret;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class InterpreterRequest {
	public ZStringSymbolParser	prompt						= new ZStringSymbolParser();
	public ZStringSymbolParser	input						= new ZStringSymbolParser();
	public String				language					= "";
	public String				masterContext				= "";
	public String				context						= "";
	
	public boolean				appendDebugLog				= false;
	public boolean				classifyLanguage			= false;
	public boolean				correctInput				= false;
	public boolean				classifyMasterContext		= false;
	public boolean				classifyContext				= false;
	public double				classifyContextThreshold	= 0.7D;
	public boolean				translateEntityValues		= false;
	public List<String>			translateEntityTypes		= new ArrayList<String>();
		
	public InterpreterRequest() {
		
	}
	
	public InterpreterRequest(ZStringSymbolParser input) {
		this.input = input;
	}
	
	public InterpreterRequest(ZStringSymbolParser prompt,ZStringSymbolParser input) {
		this.prompt = prompt;
		this.input = input;
	}

	public InterpreterRequest(String input) {
		this.input.append(input);
	}
	
	public InterpreterRequest(String prompt,String input) {
		this.prompt.append(prompt);
		this.input.append(input);
	}
	
	public void setAllActions(boolean value) {
		appendDebugLog = value;
		classifyLanguage = value;
		correctInput = value;
		classifyMasterContext = value;
		classifyContext = value;
		translateEntityValues = value;
	}
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("prompt",prompt,true));
		json.rootElement.children.add(new JsElem("input",input,true));
		json.rootElement.children.add(new JsElem("language",language,true));
		json.rootElement.children.add(new JsElem("masterContext",masterContext,true));
		json.rootElement.children.add(new JsElem("context",context,true));
		json.rootElement.children.add(new JsElem("appendDebugLog","" + appendDebugLog));
		json.rootElement.children.add(new JsElem("classifyLanguage","" + classifyLanguage));
		json.rootElement.children.add(new JsElem("correctInput","" + correctInput));
		json.rootElement.children.add(new JsElem("classifyMasterContext","" + classifyMasterContext));
		json.rootElement.children.add(new JsElem("classifyContext","" + classifyContext));
		json.rootElement.children.add(new JsElem("classifyContextThreshold","" + classifyContextThreshold));
		json.rootElement.children.add(new JsElem("translateEntityValues","" + translateEntityValues));
		JsElem typesElem = new JsElem("translateEntityTypes",true);
		json.rootElement.children.add(typesElem);
		for (String language: translateEntityTypes) {
			typesElem.children.add(new JsElem(null,language,true));
		}
		return json;
	}
	
	public void fromJson(JsFile json) {
		prompt = getChildJsonZStringSymbolParser(json.rootElement,"prompt",prompt);
		input = getChildJsonZStringSymbolParser(json.rootElement,"input",input);
		language = getChildJsonString(json.rootElement,"language",language);
		masterContext = getChildJsonString(json.rootElement,"masterContext",masterContext);
		context = getChildJsonString(json.rootElement,"context",masterContext);
		appendDebugLog = getChildJsonBoolean(json.rootElement,"appendDebugLog",appendDebugLog);
		classifyLanguage = getChildJsonBoolean(json.rootElement,"classifyLanguage",classifyLanguage);
		correctInput = getChildJsonBoolean(json.rootElement,"correctInput",correctInput);
		classifyMasterContext = getChildJsonBoolean(json.rootElement,"classifyMasterContext",classifyMasterContext);
		classifyContext = getChildJsonBoolean(json.rootElement,"classifyContext",classifyContext);
		classifyContextThreshold = getChildJsonDouble(json.rootElement,"classifyContextThreshold",classifyContextThreshold);
		translateEntityValues = getChildJsonBoolean(json.rootElement,"translateEntityValues",translateEntityValues);
		JsElem typesElem = json.rootElement.getChildByName("translateEntityTypes");
		if (typesElem!=null) {
			translateEntityTypes.clear();
			for (JsElem type: typesElem.children) {
				translateEntityTypes.add(type.value.toString());
			}
		}
	}
	
	protected ZStringSymbolParser getChildJsonZStringSymbolParser(JsElem parent,String name,ZStringSymbolParser def) {
		ZStringSymbolParser r = def;
		ZStringBuilder v = parent.getChildValueByName(name);
		if (v!=null) {
			r = new ZStringSymbolParser(v);
		}
		return r;
	}
	
	protected String getChildJsonString(JsElem parent,String name,String def) {
		String r = def;
		ZStringBuilder v = parent.getChildValueByName(name);
		if (v!=null) {
			r = v.toString();
		}
		return r;
	}
	
	protected boolean getChildJsonBoolean(JsElem parent,String name,boolean def) {
		boolean r = def;
		ZStringBuilder v = parent.getChildValueByName(name);
		if (v!=null) {
			r = Boolean.parseBoolean(v.toString());
		}
		return r;
	}

	protected double getChildJsonDouble(JsElem parent,String name,double def) {
		double r = def;
		ZStringBuilder v = parent.getChildValueByName(name);
		if (v!=null) {
			r = Double.parseDouble(v.toString());
		}
		return r;
	}
}

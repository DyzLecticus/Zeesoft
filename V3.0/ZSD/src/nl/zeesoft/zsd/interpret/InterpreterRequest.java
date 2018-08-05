package nl.zeesoft.zsd.interpret;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class InterpreterRequest {
	public ZStringSymbolParser	prompt							= new ZStringSymbolParser();
	public ZStringSymbolParser	input							= new ZStringSymbolParser();
	public String				language						= "";
	public String				masterContext					= "";
	public String				context							= "";
	
	public boolean				appendDebugLog					= false;
	public boolean				classifyLanguage				= false;
	public boolean				correctInput					= false;
	public boolean				classifyMasterContext			= false;
	public double				classifyMasterContextThreshold	= 0.25D;
	public boolean				classifyContext					= false;
	public double				classifyContextThreshold		= 0.25D;
	public boolean				translateEntityValues			= false;
	public List<String>			translateEntityTypes			= new ArrayList<String>();
	public boolean				checkProfanity					= false;
		
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
		checkProfanity = value;
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
		json.rootElement.children.add(new JsElem("classifyMasterContextThreshold","" + classifyMasterContextThreshold));
		json.rootElement.children.add(new JsElem("classifyContext","" + classifyContext));
		json.rootElement.children.add(new JsElem("classifyContextThreshold","" + classifyContextThreshold));
		json.rootElement.children.add(new JsElem("checkProfanity","" + checkProfanity));
		json.rootElement.children.add(new JsElem("translateEntityValues","" + translateEntityValues));
		JsElem typesElem = new JsElem("translateEntityTypes",true);
		json.rootElement.children.add(typesElem);
		for (String language: translateEntityTypes) {
			typesElem.children.add(new JsElem(null,language,true));
		}
		return json;
	}
	
	public void fromJson(JsFile json) {
		prompt = json.rootElement.getChildZStringSymbolParser("prompt",prompt);
		input = json.rootElement.getChildZStringSymbolParser("input",input);
		language = json.rootElement.getChildString("language",language);
		masterContext = json.rootElement.getChildString("masterContext",masterContext);
		context = json.rootElement.getChildString("context",masterContext);
		appendDebugLog = json.rootElement.getChildBoolean("appendDebugLog",appendDebugLog);
		classifyLanguage = json.rootElement.getChildBoolean("classifyLanguage",classifyLanguage);
		correctInput = json.rootElement.getChildBoolean("correctInput",correctInput);
		classifyMasterContext = json.rootElement.getChildBoolean("classifyMasterContext",classifyMasterContext);
		classifyMasterContextThreshold = json.rootElement.getChildDouble("classifyMasterContextThreshold",classifyMasterContextThreshold);
		classifyContext = json.rootElement.getChildBoolean("classifyContext",classifyContext);
		classifyContextThreshold = json.rootElement.getChildDouble("classifyContextThreshold",classifyContextThreshold);
		checkProfanity = json.rootElement.getChildBoolean("checkProfanity",checkProfanity);
		translateEntityValues = json.rootElement.getChildBoolean("translateEntityValues",translateEntityValues);
		JsElem typesElem = json.rootElement.getChildByName("translateEntityTypes");
		if (typesElem!=null) {
			translateEntityTypes.clear();
			for (JsElem type: typesElem.children) {
				translateEntityTypes.add(type.value.toString());
			}
		}
	}
}

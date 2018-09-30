package nl.zeesoft.zsdm.dialog;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.db.InitializerDatabaseObject;

public class Dialog implements InitializerDatabaseObject {
	public static final String		VARIABLE_NAME_NEXT_DIALOG	= "nextDialog";
	public static final String		VARIABLE_TYPE_NEXT_DIALOG	= "NXT";

	private String					name						= "";
	private String					language					= "";
	private String					masterContext				= "";
	private String					context						= "";
	
	private List<DialogExample>		examples					= new ArrayList<DialogExample>();
	private List<DialogVariable>	variables					= new ArrayList<DialogVariable>();

	public void initialize() {
		// Override to extend
	}
	
	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("language",language,true));
		json.rootElement.children.add(new JsElem("masterContext",masterContext,true));
		json.rootElement.children.add(new JsElem("context",context,true));
		JsElem exsElem = new JsElem("examples",true);
		json.rootElement.children.add(exsElem);
		for (DialogExample example: examples) {
			JsElem exElem = new JsElem();
			exsElem.children.add(exElem);
			exElem.children = example.toJson().rootElement.children;
		}
		JsElem vrsElem = new JsElem("variables",true);
		json.rootElement.children.add(vrsElem);
		for (DialogVariable variable: variables) {
			JsElem vrElem = new JsElem();
			vrsElem.children.add(vrElem);
			vrElem.children = variable.toJson().rootElement.children;
		}
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			examples.clear();
			language = json.rootElement.getChildString("language",language);
			masterContext = json.rootElement.getChildString("masterContext",masterContext);
			context = json.rootElement.getChildString("context",context);
			JsElem exsElem = json.rootElement.getChildByName("examples");
			if (exsElem!=null) {
				for (JsElem exElem: exsElem.children) {
					DialogExample example = new DialogExample();
					JsFile ex = new JsFile();
					ex.rootElement = exElem;
					example.fromJson(ex);
					examples.add(example);
				}
			}
			JsElem vrsElem = json.rootElement.getChildByName("variables");
			if (vrsElem!=null) {
				for (JsElem vrElem: vrsElem.children) {
					DialogVariable variable = new DialogVariable();
					JsFile vr = new JsFile();
					vr.rootElement = vrElem;
					variable.fromJson(vr);
					variables.add(variable);
				}
			}
		}
	}

	@Override
	public String getObjectName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setlanguage(String language) {
		this.language = language;
	}
	
	public String getLanguage() {
		return language;
	}

	public String getMasterContext() {
		return masterContext;
	}

	public void setMasterContext(String masterContext) {
		this.masterContext = masterContext;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<DialogExample> getExamples() {
		return examples;
	}

	public List<DialogVariable> getVariables() {
		return variables;
	}
	
	public void addExample(String input,String output) {
		addExample(input,output,"",true,true,true);
	}
	
	public void addExample(String input,String output,String filterContext) {
		addExample(input,output,filterContext,true,true,true);
	}
	
	public void addExample(String input,String output,String filterContext,boolean lang, boolean mc, boolean c) {
		DialogExample example = new DialogExample();
		example.input.append(input);
		example.output.append(output);
		example.filterContext = filterContext;
		example.toLanguageClassifier = lang;
		example.toMasterClassifier = mc;
		example.toContextClassifier = c;
		examples.add(example);
	}
	
	public void addNextDialogVariable() {
		addVariable(VARIABLE_NAME_NEXT_DIALOG,VARIABLE_TYPE_NEXT_DIALOG);
	}
	
	public void addVariable(String name, String type) {
		addVariable(name,type,false);
	}
	
	public void addVariable(String name, String type, boolean session) {
		DialogVariable variable = new DialogVariable();
		variable.name = name;
		variable.type = type;
		variable.session = session;
		variables.add(variable);
	}
	
	public DialogVariable getVariable(String name) {
		DialogVariable r = null;
		for (DialogVariable variable: variables) {
			if (variable.name.equals(name)) {
				r = variable;
				break;
			}
		}
		return r;
	}

	public void addVariablePrompt(String name,String prompt) {
		DialogVariable r = getVariable(name);
		if (r!=null) {
			r.prompts.add(new ZStringSymbolParser(prompt));
		}
	}

	public ZStringSymbolParser getTypedSequence(ZStringSymbolParser sequence) {
		ZStringSymbolParser r = new ZStringSymbolParser(sequence);
		for (DialogVariable variable: variables) {
			r.replace("{" + variable.name + "}","[" + variable.type + "]");
		}
		return r;
	}
}

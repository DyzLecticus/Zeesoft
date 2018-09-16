package nl.zeesoft.zsdm.dialog;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.db.InitializerDatabaseObject;

public class Dialog implements InitializerDatabaseObject {
	private String					name			= "";
	private String					language		= "";
	private String					masterContext	= "";
	private String					context			= "";
	
	private List<DialogExample>		examples		= new ArrayList<DialogExample>();

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
	
	public void addExample(String input,String output) {
		addExample(input,output,true,true,true);
	}
	
	public void addExample(String input,String output,boolean lang, boolean mc, boolean c) {
		DialogExample example = new DialogExample();
		example.input.append(input);
		example.output.append(output);
		example.toLanguageClassifier = lang;
		example.toMasterClassifier = mc;
		example.toContextClassifier = c;
		examples.add(example);
	}
}

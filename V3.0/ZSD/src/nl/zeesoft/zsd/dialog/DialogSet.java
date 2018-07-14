package nl.zeesoft.zsd.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericHandshake;
import nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericQnA;
import nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericHandshake;
import nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericQnA;
import nl.zeesoft.zsd.initialize.Initializable;

public class DialogSet implements Initializable {
	private List<String>					languages				= new ArrayList<String>();
	private SortedMap<String,List<DialogInstance>>	languageDialogs			= new TreeMap<String,List<DialogInstance>>();

	public DialogSet() {
		for (DialogInstance dialog: getDefaultDialogs()) {
			addDialog(dialog);
		}
	}

	@Override
	public void initialize(List<ZStringBuilder> data) {
		if (data==null || data.size()==0) {
			initialize();
		} else {
			for (ZStringBuilder dat: data) {
				JsFile json = new JsFile();
				json.fromStringBuilder(dat);
				fromJson(json);
			}
			for (DialogInstance d: getDialogs()) {
				d.initializeMatcher();
			}
		}
	}
	
	public void initialize() {
		for (DialogInstance dialog: getDialogs()) {
			dialog.initialize();
			dialog.initializeMatcher();
		}
	}
	
	public void addDialog(DialogInstance dialog) {
		List<DialogInstance> dialogs = languageDialogs.get(dialog.getLanguage());
		if (dialogs==null) {
			dialogs = new ArrayList<DialogInstance>();
			languageDialogs.put(dialog.getLanguage(),dialogs);
		}
		dialogs.add(dialog);
		if (!languages.contains(dialog.getLanguage())) {
			languages.add(dialog.getLanguage());
		}
	}

	public List<DialogInstance> getDialogs() {
		List<DialogInstance> r = new ArrayList<DialogInstance>();
		for (Entry<String,List<DialogInstance>> entry: languageDialogs.entrySet()) {
			for (DialogInstance dialog: entry.getValue()) {
				r.add(dialog);
			}
		}
		return r;
	}

	public List<DialogInstance> getDialogs(String language) {
		return getDialogs(language,"","");
	}

	public List<DialogInstance> getDialogs(String language,String masterContext) {
		return getDialogs(language,masterContext,"");
	}

	public List<DialogInstance> getDialogs(String language,String masterContext,String context) {
		List<DialogInstance> r = new ArrayList<DialogInstance>();
		List<DialogInstance> dialogs = languageDialogs.get(language);
		if (dialogs!=null) {
			if (masterContext.length()>0 || context.length()>0) {
				for (DialogInstance dialog: dialogs) {
					if (
						(masterContext.length()==0 || dialog.getMasterContext().equals(masterContext)) &&
						(context.length()==0 || dialog.getContext().equals(context))
						) {
						r.add(dialog);
					}
				}
			} else {
				r = new ArrayList<DialogInstance>(dialogs);
			}
		}
		return r;
	}

	public DialogInstance getDialog(String language,String masterContext,String context) {
		DialogInstance r = null;
		List<DialogInstance> dialogs = getDialogs(language,masterContext,context);
		if (dialogs.size()>0) {
			r = dialogs.get(0);
		}
		return r;
	}
	
	public List<String> getLanguages() {
		return languages;
	}
	
	protected List<DialogInstance> getDefaultDialogs() {
		List<DialogInstance> r = new ArrayList<DialogInstance>();
		r.add(new EnglishGenericHandshake());
		r.add(new EnglishGenericQnA());
		r.add(new DutchGenericHandshake());
		r.add(new DutchGenericQnA());
		return r;
	}
	
	public void fromJson(JsFile json) {
		JsElem dialogsElem = json.rootElement.getChildByName("dialogs");
		if (dialogsElem!=null) {
			for (JsElem dialogElem: dialogsElem.children) {
				String language = dialogElem.getChildValueByName("language").toString();
				String masterContext = dialogElem.getChildValueByName("masterContext").toString();
				String context = dialogElem.getChildValueByName("context").toString();
				String handler = dialogElem.getChildValueByName("handler").toString();
				DialogInstance d = getDialog(language,masterContext,context);
				if (d==null) {
					d = new DialogInstance();
					d.setLanguage(language);
					d.setMasterContext(masterContext);
					d.setContext(context);
					addDialog(d);
				}
				d.setHandlerClassName(handler);
				JsElem exsElem = dialogElem.getChildByName("examples");
				if (exsElem!=null) {
					for (JsElem exElem: exsElem.children) {
						ZStringSymbolParser input = new ZStringSymbolParser(exElem.getChildValueByName("input"));
						ZStringSymbolParser output = new ZStringSymbolParser(exElem.getChildValueByName("output"));
						boolean found = false;
						for (DialogIO example: d.getExamples()) {
							if (example.input.equals(input) && example.output.equals(output)) {
								found = true;
								break;
							}
						}
						if (!found) {
							DialogIO example = new DialogIO();
							example.input = input;
							example.output = output;
							d.getExamples().add(example);
						}
					}
				}
				JsElem varsElem = dialogElem.getChildByName("variables");
				if (varsElem!=null) {
					for (JsElem varElem: varsElem.children) {
						String name = varElem.getChildValueByName("name").toString();
						String type = varElem.getChildValueByName("name").toString();
						String complexName = varElem.getChildValueByName("name").toString();
						String complexType = varElem.getChildValueByName("type").toString();
						DialogVariable variable = d.getVariables().get(name);
						if (variable==null) {
							variable = new DialogVariable();
							variable.name = name;
							variable.type = type;
							variable.complexName = complexName;
							variable.complexType = complexType;
							d.getVariables().put(name,variable);
						}
						JsElem vexsElem = varElem.getChildByName("examples");
						if (vexsElem!=null) {
							for (JsElem exElem: vexsElem.children) {
								ZStringSymbolParser question = new ZStringSymbolParser(exElem.getChildValueByName("question"));
								ZStringSymbolParser answer = new ZStringSymbolParser(exElem.getChildValueByName("answer"));
								boolean found = false;
								for (DialogVariableQA example: variable.examples) {
									if (example.question.equals(question) && example.answer.equals(answer)) {
										found = true;
										break;
									}
								}
								if (!found) {
									DialogVariableQA example = new DialogVariableQA();
									example.question = question;
									example.answer = answer;
									variable.examples.add(example);
								}
							}
						}
					}
				}
			}
		}
	}
}

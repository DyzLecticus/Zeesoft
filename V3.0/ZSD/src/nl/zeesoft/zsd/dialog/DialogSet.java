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
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferCost;
import nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferDuration;
import nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferQnA;
import nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericCancel;
import nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericClassification;
import nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericHandshake;
import nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguage;
import nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericMath;
import nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericProfanity;
import nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericQnA;
import nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericThanks;
import nl.zeesoft.zsd.dialog.dialogs.dutch.DutchRoomBooking;
import nl.zeesoft.zsd.dialog.dialogs.dutch.DutchRoomQnA;
import nl.zeesoft.zsd.dialog.dialogs.dutch.DutchSupportRequest;
import nl.zeesoft.zsd.dialog.dialogs.english.EnglishForeignTransferCost;
import nl.zeesoft.zsd.dialog.dialogs.english.EnglishForeignTransferDuration;
import nl.zeesoft.zsd.dialog.dialogs.english.EnglishForeignTransferQnA;
import nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericCancel;
import nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericClassification;
import nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericHandshake;
import nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericLanguage;
import nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericMath;
import nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericProfanity;
import nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericQnA;
import nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericThanks;
import nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBooking;
import nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomQnA;
import nl.zeesoft.zsd.dialog.dialogs.english.EnglishSupportRequest;
import nl.zeesoft.zsd.initialize.Initializable;

public class DialogSet implements Initializable {
	private EntityValueTranslator					translator				= null;
	private SortedMap<String,List<DialogInstance>>	languageDialogs			= new TreeMap<String,List<DialogInstance>>();

	public DialogSet() {
		for (DialogInstance dialog: getDefaultDialogs()) {
			addDialog(dialog);
		}
	}

	public void setTranslator(EntityValueTranslator translator) {
		this.translator = translator;
	}
	
	@Override
	public void initialize(List<ZStringBuilder> data) {
		if (data==null || data.size()==0) {
			if (translator!=null) {
				initialize(translator);
			}
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
	
	public void initialize(EntityValueTranslator t) {
		for (DialogInstance dialog: getDialogs()) {
			dialog.initialize(t);
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

	public DialogInstance getDialog(String language,String masterContext,String context) {
		DialogInstance r = null;
		List<DialogInstance> dialogs = getDialogs(language,masterContext,context);
		if (dialogs.size()>0 && dialogs.get(0).getMasterContext().equals(masterContext) && dialogs.get(0).getContext().equals(context)) {
			r = dialogs.get(0);
		}
		return r;
	}
	
	protected List<DialogInstance> getDefaultDialogs() {
		List<DialogInstance> r = new ArrayList<DialogInstance>();
		r.add(new EnglishGenericClassification());
		r.add(new EnglishGenericCancel());
		r.add(new EnglishGenericThanks());
		r.add(new EnglishGenericHandshake());
		r.add(new EnglishGenericLanguage());
		r.add(new EnglishGenericQnA());
		r.add(new EnglishGenericMath());
		r.add(new EnglishGenericProfanity());
		r.add(new EnglishSupportRequest());
		r.add(new EnglishRoomBooking());
		r.add(new EnglishRoomQnA());
		r.add(new EnglishForeignTransferCost());
		r.add(new EnglishForeignTransferDuration());
		r.add(new EnglishForeignTransferQnA());
		r.add(new DutchGenericClassification());
		r.add(new DutchGenericCancel());
		r.add(new DutchGenericThanks());
		r.add(new DutchGenericHandshake());
		r.add(new DutchGenericLanguage());
		r.add(new DutchGenericQnA());
		r.add(new DutchGenericMath());
		r.add(new DutchGenericProfanity());
		r.add(new DutchSupportRequest());
		r.add(new DutchRoomBooking());
		r.add(new DutchRoomQnA());
		r.add(new DutchForeignTransferCost());
		r.add(new DutchForeignTransferDuration());
		r.add(new DutchForeignTransferQnA());
		return r;
	}
	
	public void fromJson(JsFile json) {
		JsElem dialogsElem = json.rootElement.getChildByName("dialogs");
		if (dialogsElem!=null) {
			for (JsElem dialogElem: dialogsElem.children) {
				String language = dialogElem.getChildString("language");
				String masterContext = dialogElem.getChildString("masterContext");
				String context = dialogElem.getChildString("context");
				if (language.length()>0 && masterContext.length()>0 && context.length()>0) {
					String handler = dialogElem.getChildString("handler");
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
							ZStringSymbolParser input = exElem.getChildZStringSymbolParser("input");
							ZStringSymbolParser output = exElem.getChildZStringSymbolParser("output");
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
							String name = varElem.getChildString("name");
							if (name.length()>0) {
								DialogVariable variable = d.getVariable(name);
								if (variable==null) {
									variable = new DialogVariable();
									variable.name = name;
									d.getVariables().add(variable);
								}
								variable.type = varElem.getChildString("type");
								variable.complexName = varElem.getChildString("complexName");
								variable.complexType = varElem.getChildString("complexType");
								variable.initialValue = varElem.getChildString("initialValue");
								JsElem prsElem = varElem.getChildByName("prompts");
								if (prsElem!=null) {
									for (JsElem prElem: prsElem.children) {
										ZStringSymbolParser prompt = new ZStringSymbolParser(prElem.value);
										boolean found = false;
										for (DialogVariablePrompt pr: variable.prompts) {
											if (pr.prompt.equals(prompt)) {
												found = true;
												break;
											}
										}
										if (!found) {
											DialogVariablePrompt example = new DialogVariablePrompt();
											example.prompt = prompt;
											variable.prompts.add(example);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private List<DialogInstance> getDialogs(String language,String masterContext,String context) {
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
}

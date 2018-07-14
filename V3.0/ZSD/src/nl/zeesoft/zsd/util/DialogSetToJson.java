package nl.zeesoft.zsd.util;

import java.util.Map.Entry;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.dialog.DialogIO;
import nl.zeesoft.zsd.dialog.DialogInstance;
import nl.zeesoft.zsd.dialog.DialogSet;
import nl.zeesoft.zsd.dialog.DialogVariable;
import nl.zeesoft.zsd.dialog.DialogVariableQA;

public class DialogSetToJson {
	public JsFile toJson(DialogSet ds,String language) {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem dialogsElem = new JsElem("dialogs",true);
		json.rootElement.children.add(dialogsElem);
		for (DialogInstance dialog: ds.getDialogs()) {
			if (language.length()==0 || dialog.getLanguage().equals(language)) {
				JsElem dialogElem = new JsElem();
				dialogsElem.children.add(dialogElem);
				dialogElem.children.add(new JsElem("language",dialog.getLanguage(),true));
				dialogElem.children.add(new JsElem("masterContext",dialog.getMasterContext(),true));
				dialogElem.children.add(new JsElem("context",dialog.getContext(),true));
				dialogElem.children.add(new JsElem("handler",dialog.getHandlerClassName(),true));
				if (dialog.getExamples().size()>0) {
					JsElem exsElem = new JsElem("examples",true);
					dialogElem.children.add(exsElem);
					for (DialogIO example: dialog.getExamples()) {
						JsElem exElem = new JsElem();
						exsElem.children.add(exElem);
						exElem.children.add(new JsElem("input",example.input,true));
						exElem.children.add(new JsElem("output",example.output,true));
					}
				}
				if (dialog.getVariables().size()>0) {
					JsElem varsElem = new JsElem("variables",true);
					dialogElem.children.add(varsElem);
					for (Entry<String,DialogVariable> entry: dialog.getVariables().entrySet()) {
						JsElem varElem = new JsElem();
						varsElem.children.add(varElem);
						varElem.children.add(new JsElem("name",entry.getValue().name,true));
						varElem.children.add(new JsElem("type",entry.getValue().type,true));
						if (entry.getValue().complexName.length()>0 && entry.getValue().complexType.length()>0) {
							varElem.children.add(new JsElem("complexName",entry.getValue().complexName,true));
							varElem.children.add(new JsElem("complexType",entry.getValue().complexType,true));
						}
						if (entry.getValue().examples.size()>0) {
							JsElem exsElem = new JsElem("examples",true);
							varElem.children.add(exsElem);
							for (DialogVariableQA example: entry.getValue().examples) {
								JsElem exElem = new JsElem();
								exsElem.children.add(exElem);
								exElem.children.add(new JsElem("question",example.question,true));
								exElem.children.add(new JsElem("answer",example.answer,true));
							}
						}
					}
				}
			}
		}
		return json;
	}
}

package nl.zeesoft.zsd.util;

import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.dialog.Dialog;
import nl.zeesoft.zsd.dialog.DialogIO;
import nl.zeesoft.zsd.dialog.DialogVariable;
import nl.zeesoft.zsd.dialog.DialogVariableQA;

/**
 * An DialogToJson instance can be used to translate a set of dialog objects into a JSON file.
 */
public class DialogToJson {
	
	public JsFile getJsonForDialogs(List<Dialog> dialogs) {
		return getJsonForDialogs(dialogs,true,false);
	}
	
	public JsFile getJsonForDialogs(List<Dialog> dialogs,boolean languageContext,boolean masterContext) {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem seqsElem = new JsElem("sequences",true);
		json.rootElement.children.add(seqsElem);
		addJsonForDialogs(seqsElem,dialogs,languageContext,masterContext);
		return json;
	}

	public void addJsonForDialogs(JsElem parent,List<Dialog> dialogs,boolean languageContext,boolean masterContext) {
		String cntxt = "";
		for (Dialog dialog: dialogs) {
			for (DialogIO example: dialog.getExamples()) {
				if (languageContext) {
					cntxt = dialog.getLanguage();
				} else if (masterContext) {
					cntxt = dialog.getMasterContext();
				} else {
					cntxt = dialog.getContext();
				}
				TsvToJson.checkAddSequenceElement(parent,
					new ZStringBuilder(example.input),
					null,
					new ZStringBuilder(cntxt)
					);
			}
			for (Entry<String,DialogVariable> entry: dialog.getVariables().entrySet()) {
				for (DialogVariableQA example: entry.getValue().examples) {
					if (languageContext) {
						cntxt = dialog.getLanguage();
					} else if (masterContext) {
						cntxt = dialog.getMasterContext();
					} else {
						cntxt = dialog.getContext();
					}
					TsvToJson.checkAddSequenceElement(parent,
						new ZStringBuilder(example.answer),
						null,
						new ZStringBuilder(cntxt)
						);
				}
			}
		}
	}
}

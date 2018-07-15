package nl.zeesoft.zsd.util;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.dialog.DialogIO;
import nl.zeesoft.zsd.dialog.DialogInstance;
import nl.zeesoft.zsd.dialog.DialogVariable;
import nl.zeesoft.zsd.dialog.DialogVariablePrompt;

/**
 * An DialogToJson instance can be used to translate a set of dialog objects into a JSON file.
 */
public class DialogToJson {
	
	public JsFile getJsonForDialogs(List<DialogInstance> dialogs) {
		return getJsonForDialogs(dialogs,true,false);
	}
	
	public JsFile getJsonForDialogs(List<DialogInstance> dialogs,boolean languageContext,boolean masterContext) {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem seqsElem = new JsElem("sequences",true);
		json.rootElement.children.add(seqsElem);
		addJsonForDialogs(seqsElem,dialogs,languageContext,masterContext);
		return json;
	}

	public void addJsonForDialogs(JsElem parent,List<DialogInstance> dialogs,boolean languageContext,boolean masterContext) {
		String cntxt = "";
		List<ZStringBuilder> added = new ArrayList<ZStringBuilder>();
		for (DialogInstance dialog: dialogs) {
			for (DialogIO example: dialog.getExamples()) {
				if (languageContext) {
					cntxt = dialog.getLanguage();
				} else if (masterContext) {
					cntxt = dialog.getMasterContext();
				} else {
					cntxt = dialog.getContext();
				}
				boolean found = false;
				for (ZStringBuilder sb: added) {
					if (sb.equals(example.input)) {
						found = true;
						break;
					}
				}
				if (!found) {
					added.add(example.input);
					TsvToJson.checkAddSequenceElement(parent,
						example.input,
						null,
						new ZStringBuilder(cntxt)
						);
				}
			}
			for (DialogVariable variable: dialog.getVariables()) {
				for (DialogVariablePrompt prompt: variable.prompts) {
					if (languageContext) {
						cntxt = dialog.getLanguage();
					} else if (masterContext) {
						cntxt = dialog.getMasterContext();
					} else {
						cntxt = dialog.getContext();
					}
					TsvToJson.checkAddSequenceElement(parent,
						new ZStringBuilder(prompt.prompt),
						null,
						new ZStringBuilder(cntxt)
						);
				}
			}
		}
	}
}

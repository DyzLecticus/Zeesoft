package nl.zeesoft.zsd.util;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.dialog.DialogIO;
import nl.zeesoft.zsd.dialog.DialogInstance;
import nl.zeesoft.zsd.dialog.DialogVariable;
import nl.zeesoft.zsd.dialog.DialogVariablePrompt;

/**
 * An DialogToJson instance can be used to translate a set of dialog objects into a JSON file.
 */
public class DialogToJson {
	
	public JsFile getJsonForDialogs(List<DialogInstance> dialogs) {
		return getJsonForDialogs(dialogs,true,null);
	}
	
	public JsFile getJsonForDialogs(List<DialogInstance> dialogs,boolean languageContext,List<String> masterContexts) {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem seqsElem = new JsElem("sequences",true);
		json.rootElement.children.add(seqsElem);
		addJsonForDialogs(seqsElem,dialogs,languageContext,masterContexts);
		return json;
	}

	public void addJsonForDialogs(JsElem parent,List<DialogInstance> dialogs,boolean languageContext,List<String> masterContexts) {
		String cntxt = "";
		for (DialogInstance dialog: dialogs) {
			List<ZStringBuilder> added = new ArrayList<ZStringBuilder>();
			if (masterContexts==null || masterContexts.size()==0 || masterContexts.contains(dialog.getMasterContext())) {
				for (DialogIO example: dialog.getExamples()) {
					if (languageContext) {
						cntxt = dialog.getLanguage();
					} else if (masterContexts!=null) {
						cntxt = dialog.getMasterContext();
					} else {
						cntxt = dialog.getContext();
					}
					boolean found = false;
					ZStringBuilder find = new ZStringBuilder(example.input);
					if (languageContext && example.output.length()>0) {
						find.append(" ");
						find.append(example.output);
					}
					for (ZStringBuilder sb: added) {
						if (sb.equals(find)) {
							found = true;
							break;
						}
					}
					if (!found) {
						added.add(find);
						if (languageContext && example.output.length()>0) {
							TsvToJson.checkAddSequenceElement(parent,
								example.input,
								example.output,
								new ZStringBuilder(cntxt)
								);
						} else {
							TsvToJson.checkAddSequenceElement(parent,
								example.input,
								null,
								new ZStringBuilder(cntxt)
								);
						}
					}
				}
				for (DialogVariable variable: dialog.getVariables()) {
					if (!variable.type.equals(BaseConfiguration.TYPE_NEXT_DIALOG)) {
						for (DialogVariablePrompt prompt: variable.prompts) {
							if (languageContext) {
								cntxt = dialog.getLanguage();
							} else if (masterContexts!=null) {
								cntxt = dialog.getMasterContext();
							} else {
								cntxt = dialog.getContext();
							}
							ZStringBuilder pr = new ZStringBuilder(prompt.prompt);
							pr.append(" [");
							pr.append(variable.type);
							pr.append("].");
							TsvToJson.checkAddSequenceElement(parent,
								pr,
								null,
								new ZStringBuilder(cntxt)
								);
						}
					}
				}
			}
		}
	}
}

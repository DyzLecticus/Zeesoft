package nl.zeesoft.zidd.server.resource;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zids.database.model.Dialog;
import nl.zeesoft.zids.database.model.DialogExample;
import nl.zeesoft.zids.database.model.DialogVariable;
import nl.zeesoft.zids.dialog.pattern.PtnManager;
import nl.zeesoft.zids.dialog.pattern.PtnObject;
import nl.zeesoft.zids.server.SvrControllerDatabase;
import nl.zeesoft.zids.server.SvrControllerDialogs;
import nl.zeesoft.zids.server.resource.RscHtmlObject;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.file.HTMLFile;

public class RscDialogUtterancesHtml extends RscHtmlObject {
	private static final int MAX_OVERALL = 999999;
	private static final int MAX_NUMBERS = 9999;

	public RscDialogUtterancesHtml(String title, String backgroundColor) {
		super(title, backgroundColor);
	}
	
	@Override
	public StringBuilder toStringBuilder() {
		List<Dialog> dialogs = SvrControllerDialogs.getInstance().getDialogsAsList();
		List<String> utterances = new ArrayList<String>();
		for (Dialog dialog: dialogs) {
			for (DialogExample dialogExample: dialog.getExamples()) {
				StringBuilder utterance = new StringBuilder();
				utterance.append(dialogExample.getInput().substring(0,dialogExample.getInput().length()-1));
				for (DialogVariable dv: dialog.getVariables()) {
					utterance = Generic.stringBuilderReplace(utterance,dv.getCode(),dv.getType().getName().toLowerCase());
				}
				utterance = Generic.stringBuilderReplace(utterance,".","");
				utterance = Generic.stringBuilderReplace(utterance,"!","");
				utterance = Generic.stringBuilderReplace(utterance,"?","");
				utterance = Generic.stringBuilderReplace(utterance,",","");
				if (!utterances.contains(utterance.toString().toLowerCase())) {
					utterances.add(utterance.toString().toLowerCase());
				}
			}
		}

		PtnManager manager = SvrControllerDatabase.getInstance().getPatternManager();
		for (PtnObject pattern: manager.getPatterns()) {
			if (pattern.getTypeSpecifier().equals("ENG")) {
				int added = 0;
				for (String str: pattern.getPatternStrings()) {
					str = str.replaceAll(",","");
					if (
						!Generic.stringContainsOneOfCharacters(str,Generic.NUMERIC) &&
						!utterances.contains(str)
						) {
						utterances.add(str);
						added++;
						if (added>=MAX_NUMBERS &&
							(pattern.getBaseValueType().equals(PtnObject.TYPE_NUMBER) || pattern.getBaseValueType().equals(PtnObject.TYPE_ORDER)
							)) {
							break;
						} else if (utterances.size()>=MAX_OVERALL) {
							break;
						}
					}
				}
			}
			if (utterances.size()>=MAX_OVERALL) {
				break;
			}
		}
		
		StringBuilder body = new StringBuilder();
		body.append("<div>");
		body.append("<b>Utterances</b><br />");
		for (String utterance: utterances) {
			body.append("DialogIntent {");
			body.append(utterance);
			body.append("|input}<br />");
		}
		body.append("</div>");
		HTMLFile f = getNewHTMLFile();
		f.getBodyElements().add(getMenuForPage("../","","").toString());
		f.getBodyElements().add(body.toString());
		return f.toStringBuilder();
	}
}

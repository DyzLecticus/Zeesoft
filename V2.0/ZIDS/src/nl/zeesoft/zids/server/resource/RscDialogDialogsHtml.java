package nl.zeesoft.zids.server.resource;

import java.util.List;

import nl.zeesoft.zids.database.model.Dialog;
import nl.zeesoft.zids.database.model.DialogExample;
import nl.zeesoft.zids.database.model.DialogVariable;
import nl.zeesoft.zids.database.model.DialogVariableExample;
import nl.zeesoft.zids.server.SvrControllerDialogs;
import nl.zeesoft.zodb.file.HTMLFile;

public class RscDialogDialogsHtml extends RscHtmlObject {
	public RscDialogDialogsHtml(String title, String backgroundColor) {
		super(title, backgroundColor);
	}

	protected boolean showExamples() {
		return false;
	}
	
	protected String getLinkHtml() {
		return "<a href=\"../dialogs/dialogExamples.html\">Show examples</a>";
	}
	
	@Override
	public StringBuilder toStringBuilder() {
		StringBuilder body = new StringBuilder();
		body.append("<div>");
		body.append("<b>Dialogs</b>");
		body.append("<ul>");
		List<Dialog> dialogs = SvrControllerDialogs.getInstance().getDialogsAsList();
		for (Dialog dialog: dialogs) {
			body.append("<li>");
			body.append(dialog.getName());
			body.append(" (Context symbol: ");
			body.append(dialog.getContextSymbol());
			if (dialog.getControllerClassName().length()>0) {
				body.append(", controller: ");
				body.append(dialog.getControllerClassName());
			}
			body.append(")");
			body.append("<ul>");
			if (showExamples() && dialog.getExamples().size()>0) {
				body.append("<li>");
				body.append("Examples:<br/>");
				body.append("<ul>");
				for (DialogExample dialogExample: dialog.getExamples()) {
					body.append("<li>");
					body.append(dialogExample.getInput());
					body.append("<br />");
					body.append(dialogExample.getOutput());
					body.append("</li>");
				}
				body.append("</ul>");
				body.append("</li>");
			}
			if (dialog.getVariables().size()>0) {
				body.append("<li>");
				body.append("Variables:");
				body.append("<ul>");
				for (DialogVariable dialogVariable: dialog.getVariables()) {
					body.append("<li>");
					body.append(dialogVariable.getCode());
					body.append(" ");
					body.append(dialogVariable.getPrompt1());
					body.append(" (Type: ");
					body.append(dialogVariable.getType().getName());
					body.append(", context symbol: ");
					body.append(dialogVariable.getContextSymbol());
					body.append(")");
					if (showExamples() && dialogVariable.getExamples().size()>0) {
						body.append("<ul>");
						body.append("<li>");
						body.append("Examples:<br/>");
						body.append("<ul>");
						for (DialogVariableExample dialogVariableExample: dialogVariable.getExamples()) {
							body.append("<li>");
							body.append(dialogVariableExample.getInput());
							body.append("<br />");
							body.append(dialogVariableExample.getOutput());
							body.append("</li>");
						}
						body.append("</ul>");
						body.append("</li>");
						body.append("</ul>");
					}
					body.append("</li>");
				}
				body.append("</ul>");
				body.append("</li>");
			}
			body.append("</ul>");
			body.append("</li>");
		}
		body.append("</ul>");
		body.append("</div>");
		HTMLFile f = getNewHTMLFile();
		f.getScriptFiles().add("../ZIDS.js");
		f.getStyleFiles().add("../ZIDS.css");
		f.getBodyElements().add(getMenuForPage("../","",getLinkHtml()).toString());
		f.getBodyElements().add(body.toString());
		return f.toStringBuilder();
	}
}

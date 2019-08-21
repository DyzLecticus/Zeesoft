package nl.zeesoft.zodb.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModZODB;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;

public class HtmlZODBIndex extends HtmlResource {
	public HtmlZODBIndex(Config config) {
		super(config);
	}
	@Override
	public ZStringBuilder toStringBuilder() {
		setTitle("ZODB - Functions");
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"../index.html\">Back to modules</a>");
		html.append("<hr />");
		
		html.append("<div>\n");
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
			html.append("<tr>\n");
			html.append("<td width=\"20%\">");
			html.append("<a href=\"" + getConfiguration().getDomainUrl(ModZODB.NAME) + "/dataManager.html\">Data manager</a>");
			html.append("</td>\n");
			html.append("<td>");
			html.append("View, add, update and remove objects.");
			html.append("</td>\n");
			html.append("</tr>\n");
			
			html.append("<tr>\n");
			html.append("<td>");
			html.append("<a href=\"" + getConfiguration().getDomainUrl(ModZODB.NAME) + "/indexManager.html\">Index manager</a>");
			html.append("</td>\n");
			html.append("<td>");
			html.append("View, add and remove indexes.");
			html.append("</td>\n");
			html.append("</tr>\n");
			
			html.append("<tr>\n");
			html.append("<td>");
			html.append("<a href=\"" + getConfiguration().getDomainUrl(ModZODB.NAME) + "/stateManager.html\">State manager</a>");
			html.append("</td>\n");
			html.append("<td>");
			html.append("Monitor and change the state of the database.");
			html.append("</td>\n");
			html.append("</tr>\n");
			
			if (getConfiguration().getModule(ModZODB.NAME).selfTest) {
				html.append("<tr>\n");
				html.append("<td>");
				html.append("<a href=\"" + getConfiguration().getDomainUrl(ModZODB.NAME) + JsonModTestResultsHandler.PATH + "\">Test results JSON</a>");
				html.append("</td>\n");
				html.append("<td>");
				html.append("Returns the self test results as a JSON file.");
				html.append("</td>\n");
				html.append("</tr>\n");
			}
		html.append("</tbody>\n");
		html.append("</table>\n");
		html.append("</div>\n");
		
		getBodyElements().add(html);
		
		return super.toStringBuilder();
	}
}

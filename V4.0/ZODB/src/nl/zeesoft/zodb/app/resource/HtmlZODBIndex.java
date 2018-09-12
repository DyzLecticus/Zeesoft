package nl.zeesoft.zodb.app.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.AppZODB;
import nl.zeesoft.zodb.app.handler.JsonAppTestResultsHandler;

public class HtmlZODBIndex extends HtmlResource {
	public HtmlZODBIndex(Config config) {
		super(config);
	}
	@Override
	public ZStringBuilder toStringBuilder() {
		setTitle("ZODB - Index");
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"../index.html\">Back to applications</a>");
		html.append("<hr />");
		
		html.append("<div>\n");
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
			html.append("<tr>\n");
			html.append("<td width=\"20%\">");
			html.append("<a href=\"" + getConfiguration().getApplicationUrl(AppZODB.NAME) + "/dataManager.html\">Data manager</a>");
			html.append("</td>\n");
			html.append("<td>");
			html.append("View, add, update and remove objects.");
			html.append("</td>\n");
			html.append("</tr>\n");
			
			if (getConfiguration().getApplication(AppZODB.NAME).selfTest) {
				html.append("<tr>\n");
				html.append("<td>");
				html.append("<a href=\"" + getConfiguration().getApplicationUrl(AppZODB.NAME) + JsonAppTestResultsHandler.PATH + "\">Test results JSON</a>");
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

package nl.zeesoft.zodb.app.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;

public class HtmlZODBDataManager extends HtmlResource {
	public HtmlZODBDataManager(Config config) {
		super(config);
	}
	@Override
	public ZStringBuilder toStringBuilder() {
		setTitle("ZODB - Data manager");
		
		getScriptFiles().add("zodb.js");
		getScriptFiles().add("dataManager.js");
		
		setOnload("ZODB.dm.onload();");
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"index.html\">Back to index</a>");
		html.append("<hr />");
		
		html.append("<div>\n");
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
			html.append("<tr>\n");
			html.append("<td width=\"20%\">");
			html.append("Name");
			html.append("</td>\n");
			html.append("<td colspan=\"2\">");
			html.append("<input type=\"text\" id=\"name\" style=\"width: 400px;\" />");
			html.append("</td>\n");
			html.append("</tr>\n");
			
			html.append("<tr>\n");
			html.append("<td>");
			html.append("Start / Max");
			html.append("</td>\n");
			html.append("<td colspan=\"2\">");
			html.append("<input type=\"number\" id=\"start\" value=\"0\" />");
			html.append("<input type=\"number\" id=\"max\" value=\"10\" />");
			html.append("<input type=\"button\" value=\"<\" onclick=\"ZODB.dm.prev();\" />");
			html.append("<input type=\"button\" value=\">\" onclick=\"ZODB.dm.next();\" />");
			html.append("</td>\n");
			html.append("</tr>\n");

			html.append("<tr>\n");
			html.append("<td>");
			html.append("</td>\n");
			html.append("<td width=\"1%\">");
			html.append("<input type=\"button\" value=\"Fetch\" onclick=\"ZODB.dm.list();\" />");
			html.append("</td>\n");
			html.append("<td id=\"fetched\">");
			html.append("</td>\n");
			html.append("</tr>\n");
		html.append("</tbody>\n");
		html.append("</table>\n");
		html.append("</div>\n");

		html.append("<div id=\"list\">\n");
		html.append("</div>\n");

		html.append("<div>\n");
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
			html.append("<tr>\n");
			html.append("<td>");
			html.append("<input type=\"button\" value=\"New\" onclick=\"ZODB.dm.new();\" />");
			html.append("<input type=\"button\" value=\"Remove\" onclick=\"ZODB.dm.remove();\" />");
			html.append("<input type=\"checkbox\" id=\"removeCheck\" />");
			html.append("</td>\n");
			html.append("</tr>\n");
			
			html.append("<tr>\n");
			html.append("<td>");
			html.append("<textarea id=\"object\" style=\"width: 100%; height: 200px;\" >\n");
			html.append("</textarea>\n");
			html.append("</td>\n");
			html.append("</tr>\n");
		html.append("</tbody>\n");
		html.append("</table>\n");
		html.append("</div>\n");

		html.append("<div>\n");
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
			html.append("<tr>\n");
			html.append("<td>");
			html.append("Name&nbsp;");
			html.append("<input type=\"text\" id=\"saveName\" style=\"width: 400px;\" />");
			html.append("<input type=\"button\" value=\"Save\" onclick=\"ZODB.dm.save();\" />");
			html.append("</td>\n");
			html.append("</tr>\n");
		html.append("</tbody>\n");
		html.append("</table>\n");
		html.append("</div>\n");

		getBodyElements().add(html);
		
		return super.toStringBuilder();
	}
}

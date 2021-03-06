package nl.zeesoft.zodb.mod.resource;

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
		html.append("<a href=\"index.html\">Back to functions</a>");
		html.append("<hr />");
		
		html.append("<div>\n");
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
			html.append("<tr>\n");
			html.append("<td valign=\"top\" width=\"20%\">");
			html.append("<select valign=\"top\" id=\"index\" onchange=\"ZODB.dm.changedIndex();\">");
			html.append("</select>");
			html.append("</td>\n");
			html.append("<td colspan=\"2\">");
			html.append("!<input type=\"checkbox\" id=\"invert\" />");
			html.append("<select id=\"operator\">");
			html.append("</select>");
			html.append("<input type=\"text\" id=\"value\" style=\"width: 400px;\" />");
			html.append("&nbsp;Descending<input type=\"checkbox\" id=\"descending\" onchange=\"ZODB.dm.list();\" />");
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
			html.append("<input type=\"button\" value=\"List\" onclick=\"ZODB.dm.list();\" />");
			html.append("</td>\n");
			html.append("<td id=\"listed\">");
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
			html.append("<input type=\"button\" value=\"Remove all\" onclick=\"ZODB.dm.removeAll();\" />");
			html.append("<input type=\"checkbox\" id=\"removeAllCheck\" />");
			html.append("</td>\n");
			html.append("</tr>\n");
			
			html.append("<tr>\n");
			html.append("<td>");
			html.append("<textarea id=\"object\" style=\"width: 100%; height: 200px;background-color:" + HIGHLIGHT_COLOR + ";\" >\n");
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
			html.append("<input type=\"text\" id=\"name\" style=\"width: 400px;\" />");
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

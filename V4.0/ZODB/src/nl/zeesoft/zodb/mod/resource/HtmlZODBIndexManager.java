package nl.zeesoft.zodb.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;

public class HtmlZODBIndexManager extends HtmlResource {
	public HtmlZODBIndexManager(Config config) {
		super(config);
	}
	@Override
	public ZStringBuilder toStringBuilder() {
		setTitle("ZODB - Index manager");
		
		getScriptFiles().add("zodb.js");
		getScriptFiles().add("indexManager.js");
		
		setOnload("ZODB.im.onload();");
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"index.html\">Back to functions</a>");
		html.append("<hr />");
		
		html.append("<div id=\"list\">\n");
		html.append("</div>\n");

		html.append("<div>\n");
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tr>\n");
		html.append("<th align=\"left\" width=\"20%\">");
		html.append("New&nbsp;index&nbsp;");
		html.append("</th>\n");
		html.append("<th>");
		html.append("</th>\n");
		html.append("</tr>\n");
		html.append("<tbody>\n");
			html.append("<tr>\n");
			html.append("<td>");
			html.append("Object&nbsp;name&nbsp;prefix&nbsp;");
			html.append("</td>\n");
			html.append("<td>");
			html.append("<input type=\"text\" id=\"objectNamePrefix\" style=\"width: 400px;\" />");
			html.append("</td>\n");
			html.append("</tr>\n");

			html.append("<tr>\n");
			html.append("<td>");
			html.append("Property&nbsp;name&nbsp;");
			html.append("</td>\n");
			html.append("<td>");
			html.append("<input type=\"text\" id=\"propertyName\" style=\"width: 400px;\" />");
			html.append("</td>\n");
			html.append("</tr>\n");
			
			html.append("<tr>\n");
			html.append("<td>");
			html.append("Numeric&nbsp;");
			html.append("</td>\n");
			html.append("<td>");
			html.append("<input type=\"checkbox\" id=\"numeric\" />");
			html.append("</td>\n");
			html.append("</tr>\n");
			
			html.append("<tr>\n");
			html.append("<td>");
			html.append("Unique&nbsp;");
			html.append("</td>\n");
			html.append("<td>");
			html.append("<input type=\"checkbox\" id=\"unique\" />");
			html.append("</td>\n");
			html.append("</tr>\n");
			
			html.append("<tr>\n");
			html.append("<td>");
			html.append("</td>\n");
			html.append("<td>");
			html.append("<input type=\"button\" value=\"Add\" onclick=\"ZODB.im.add();\" />");
			html.append("</td>\n");
			html.append("</tr>\n");
		html.append("</tbody>\n");
		html.append("</table>\n");
		html.append("</div>\n");

		getBodyElements().add(html);
		
		return super.toStringBuilder();
	}
}

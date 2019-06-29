package nl.zeesoft.zodb.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;

public class HtmlZODBStateManager extends HtmlResource {
	public HtmlZODBStateManager(Config config) {
		super(config);
	}
	@Override
	public ZStringBuilder toStringBuilder() {
		setTitle("ZODB - Data manager");
		
		getScriptFiles().add("zodb.js");
		getScriptFiles().add("stateManager.js");
		
		setOnload("ZODB.sm.onload();");
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"index.html\">Back to functions</a>");
		html.append("<hr />");
		
		html.append("<div>\n");
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
			html.append("<tr>\n");
			html.append("<td width=\"20%\">");
			html.append("State");
			html.append("</td>\n");
			html.append("<td id=\"state\">");
			html.append("</td>\n");
			html.append("</tr>\n");
			
			html.append("<tr>\n");
			html.append("<td>");
			html.append("</td>\n");
			html.append("<td>");
			html.append("<input type=\"button\" id=\"restart\" value=\"Restart\" onclick=\"ZODB.sm.restart();\" DISABLED />");
			html.append("<input type=\"checkbox\" id=\"restartCheck\" DISABLED />");
			html.append("</td>\n");
			html.append("</tr>\n");
		html.append("</tbody>\n");
		html.append("</table>\n");
		html.append("</div>\n");

		getBodyElements().add(html);
		
		return super.toStringBuilder();
	}
}

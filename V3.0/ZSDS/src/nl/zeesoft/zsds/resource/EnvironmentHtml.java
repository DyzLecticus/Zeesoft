package nl.zeesoft.zsds.resource;

import nl.zeesoft.zdk.ZStringBuilder;

public class EnvironmentHtml extends HtmlResource {
	public EnvironmentHtml() {
		setTitle("ZSDS - Test environments");
		
		getScriptFiles().add("zsds.js");
		getScriptFiles().add("environment.js");
		getStyleFiles().add("zsds.css");
		
		ZStringBuilder script = new ZStringBuilder();
		script.append("<script type=\"text/javascript\">\n");
		script.append("ZSDS.environment.onload = function() {\n");
		script.append("    ZSDS.environment.getTestConfig();\n");
		script.append("};\n");
		script.append("</script>\n");
		getHeadElements().add(script);
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"index.html\">Index</a>");
		html.append("<hr />");
		html.append("<div>\n");
		
		html.append("<div>\n");
			html.append("<table style=\"width: 100%;\">\n");
			html.append("<tbody>\n");
			
			html.append("<tr>\n");
			html.append("<td>");
			html.append("Environment");
			html.append("</td>\n");
			html.append("<td width=\"5%\">");
			html.append("<select id=\"selector\" onchange=\"ZDSD.environment.selectedEnvironment();\">&nbsp;");
			html.append("</td>\n");
			html.append("<td width=\"85%\">");
			html.append("<input type=\"text\" id=\"url\" style=\"width: 100%;\" DISABLED />");
			html.append("</select>");
			html.append("</td>\n");
			html.append("</tr>\n");
	
			html.append("<tr>\n");
			html.append("<td>");
			html.append("Directory");
			html.append("</td>\n");
			html.append("<td colspan=\"2\">");
			html.append("<input type=\"text\" id=\"directory\" style=\"width: 100%;\" DISABLED />");
			html.append("</td>\n");
			html.append("</tr>\n");
	
			html.append("</tbody>\n");
			html.append("</table>\n");
		html.append("</div>\n");

		html.append("<br />\n");
		html.append("<div>\n");
			html.append("<table style=\"width: 100%;\">\n");
			html.append("<tbody>\n");
			
			html.append("<tr>\n");
			html.append("<td>");
			html.append("<input type=\"button\" value=\"Refresh\" onclick=\"ZSDS.environment.getSummary();\"/>");
			html.append("</td>\n");
			html.append("</tr>\n");
			
			html.append("<tr>\n");
			html.append("<td>");
			html.append("<input type=\"button\" value=\"Test\" onclick=\"ZSDS.environment.test();\"/>");
			html.append("<input type=\"checkbox\" id=\"testCheck\"/>");
			html.append("</td>\n");
			html.append("</tr>\n");
			
			html.append("<tr>\n");
			html.append("<td>");
			html.append("<input type=\"button\" value=\"Reload\" onclick=\"ZSDS.environment.reload();\"/>");
			html.append("<input type=\"checkbox\" id=\"reloadCheck\"/>");
			html.append("</td>\n");
			html.append("</tr>\n");
			
			html.append("</tbody>\n");
			html.append("</table>\n");
		html.append("</div>\n");
		
		html.append("<br />\n");
		html.append("<div>\n");
		html.append("Test summary<br />\n");
		html.append("<textarea id=\"summary\"></textarea>\n");
		html.append("</div>\n");

		html.append("</div>\n");

		setOnload("ZSDS.environment.onload();");

		getBodyElements().add(html);
	}
}

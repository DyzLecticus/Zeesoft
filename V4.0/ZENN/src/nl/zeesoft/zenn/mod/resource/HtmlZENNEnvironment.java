package nl.zeesoft.zenn.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zenn.environment.EnvironmentConfig;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.resource.HtmlResource;

public class HtmlZENNEnvironment extends HtmlResource {
	public HtmlZENNEnvironment(Config config) {
		super(config);
	}
	@Override
	public ZStringBuilder toStringBuilder() {
		setTitle("ZENN - Environment");	
		
		getScriptFiles().add("ZODB.js");
		getScriptFiles().add("ZENN.js");
		getStyleFiles().add("ZENN.css");

		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"./index.html\">Back to functions</a>");
		html.append("<hr />");
		
		html.append("<div>\n");
		html.append("<table id=\"grid\">");
		html.append("<tbody>\n");
		for (int y = 0; y < EnvironmentConfig.SIZE_Y; y++) {
			html.append("<tr>");
			for (int x = 0; x < EnvironmentConfig.SIZE_X; x++) {
				String tdId = x + "-" + y;
				html.append("<td id=\"" + tdId + "\" class=\"black\">");
				html.append("</td>");
			}
			html.append("</tr>");
		}
		html.append("</tbody>\n");
		html.append("</table>");
		html.append("</div>\n");
		
		getBodyElements().add(html);
		
		return super.toStringBuilder();
	}
}

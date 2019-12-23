package nl.zeesoft.zsda.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModZODB;
import nl.zeesoft.zodb.mod.resource.HtmlResource;
import nl.zeesoft.zsda.mod.ModZSDA;

public class HtmlZSDAGridConfig extends HtmlResource {
	public HtmlZSDAGridConfig(Config config) {
		super(config);
	}
	@Override
	public ZStringBuilder toStringBuilder() {
		setTitle("ZSDA - Grid configuration");	

		getScriptFiles().add("../" + ModZODB.NAME + "/ZODB.js");
		getScriptFiles().add("../" + ModZSDA.NAME + "/gridConfig.js");
		
		setOnload("ZSDA.config.onload();");
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"./index.html\">Back to functions</a>");
		html.append("<hr />");
		
		html.append("<div>\n");
		
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
			html.append("<tr>\n");
			html.append("<td width=\"20%\">");
			html.append("Grid dimensions");
			html.append("</td>\n");
			html.append("<td>");
			html.append("<input type=\"number\" id=\"numRows\" />");
			html.append("<input type=\"number\" id=\"numColumns\" />");
			html.append("</td>\n");
			html.append("</tr>\n");
		html.append("</tbody>\n");
		html.append("</table>\n");
		
		html.append("</div>\n");
		
		html.append("<div id=\"configurations\">\n");
		html.append("</div>\n");
		
		getBodyElements().add(html);
		
		return super.toStringBuilder();
	}
}

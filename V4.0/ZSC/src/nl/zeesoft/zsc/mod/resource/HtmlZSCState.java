package nl.zeesoft.zsc.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.resource.HtmlResource;

public class HtmlZSCState extends HtmlResource {
	public HtmlZSCState(Config config) {
		super(config);
	}
	@Override
	public ZStringBuilder toStringBuilder() {
		setTitle("ZSC - State manager");
		
		getScriptFiles().add("../ZODB/zodb.js");
		getScriptFiles().add("stateManager.js");
		
		setOnload("ZSC.state.onload();");
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"index.html\">Back to functions</a>");
		html.append("<hr />");
		
		html.append("<div>\n");
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
			html.append("<tr>\n");
			html.append("<td>");
			html.append("<input value=\"Refresh\" type=\"button\" onclick=\"ZSC.state.refresh();\" />");
			html.append("</td>\n");
			html.append("</tr>\n");
			
			html.append("<tr>\n");
			html.append("<td>");
			html.append("<select id=\"selector\">");
			html.append("</select>");
			html.append("<input value=\"Retrain\" type=\"button\" onclick=\"ZSC.state.retrain();\" />");
			html.append("<input type=\"checkbox\" id=\"retrainCheck\" />");
			html.append("</td>\n");
			html.append("</tr>\n");
		
			html.append("<tr>\n");
			html.append("<td>");
			html.append("<input value=\"Reinitialize\" type=\"button\" onclick=\"ZSC.state.reinitialize();\" />");
			html.append("<input type=\"checkbox\" id=\"reinitializeCheck\" />");
			html.append("</td>\n");
			html.append("</tr>\n");
		
		html.append("</tbody>\n");
		html.append("</table>\n");
		html.append("</div>\n");
		
		html.append("<div>\n");
		html.append("<textarea id=\"state\" style=\"width: 100%; height: 400px;\" >\n");
		html.append("</textarea>\n");
		html.append("</div>\n");
		getBodyElements().add(html);
		
		return super.toStringBuilder();
	}
}

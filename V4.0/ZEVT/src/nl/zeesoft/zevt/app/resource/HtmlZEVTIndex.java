package nl.zeesoft.zevt.app.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zevt.app.AppZEVT;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.resource.HtmlResource;

public class HtmlZEVTIndex extends HtmlResource {
	public HtmlZEVTIndex(Config config) {
		super(config);
	}
	@Override
	public ZStringBuilder toStringBuilder() {
		setTitle("ZEVT - Index");
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"../index.html\">Back to applications</a>");
		html.append("<hr />");
		
		html.append("<div>\n");
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
			html.append("<tr>\n");
			html.append("<td width=\"20%\">");
			html.append("<a href=\"" + getConfiguration().getApplicationUrl(AppZEVT.NAME) + "/entityTranslator.html\">Entity translator</a>");
			html.append("</td>\n");
			html.append("<td>");
			html.append("Translate sequences to entity values and back.");
			html.append("</td>\n");
			html.append("</tr>\n");
		html.append("</tbody>\n");
		html.append("</table>\n");
		html.append("</div>\n");
		
		getBodyElements().add(html);
		
		return super.toStringBuilder();
	}
}

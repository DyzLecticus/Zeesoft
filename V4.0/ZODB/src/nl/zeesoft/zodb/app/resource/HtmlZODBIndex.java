package nl.zeesoft.zodb.app.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;

public class HtmlZODBIndex extends HtmlResource {
	public HtmlZODBIndex(Config config) {
		super(config);
	}
	@Override
	public ZStringBuilder toStringBuilder() {
		setTitle("ZODB - Index");
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("Zeesoft Object Database provides a simple JSON API to store JSON objects.\n");
		html.append("<br />\n");
		/*
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
		for (AppObject app: getConfiguration().getApplications()) {
			html.append("<tr>\n");
			html.append("<td width=\"20%\">");
			html.append("<a href=\"" + getConfiguration().getApplicationUrl(app.name) + "/index.html\">" + app.name + "</a>");
			html.append("</td>\n");
			html.append("<td>");
			html.append("</td>\n");
			html.append("</tr>\n");
		}
		html.append("</tbody>\n");
		html.append("</table>\n");
		*/
		
		getBodyElements().add(html);
		
		return super.toStringBuilder();
	}
}

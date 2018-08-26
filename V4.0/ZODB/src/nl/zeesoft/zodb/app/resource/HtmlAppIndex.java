package nl.zeesoft.zodb.app.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.AppObject;

public class HtmlAppIndex extends HtmlResource {
	public HtmlAppIndex(Config config) {
		super(config);
	}
	@Override
	public ZStringBuilder toStringBuilder() {
		setTitle("Applications");
		
		ZStringBuilder html = new ZStringBuilder();
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
		
		getBodyElements().add(html);
		
		return super.toStringBuilder();
	}
}

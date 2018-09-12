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
		html.append("This servlet consists of the following applications");
		html.append("<hr />");
		
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
		for (int i = (getConfiguration().getApplications().size() - 1); i >= 0; i--) {
			AppObject app = getConfiguration().getApplications().get(i);
			html.append("<tr>\n");
			html.append("<td width=\"20%\">");
			html.append("<a href=\"" + getConfiguration().getApplicationUrl(app.name) + "/index.html\">" + app.name + "</a>");
			html.append("</td>\n");
			html.append("<td>");
			html.append(app.desc);
			html.append("</td>\n");
			html.append("</tr>\n");
		}
		html.append("</tbody>\n");
		html.append("</table>\n");
		
		getBodyElements().add(html);
		
		return super.toStringBuilder();
	}
}

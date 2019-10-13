package nl.zeesoft.zodb.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;

public class HtmlModIndex extends HtmlResource {
	public HtmlModIndex(Config config) {
		super(config);
	}
	@Override
	public ZStringBuilder toStringBuilder() {
		setTitle("Application modules");
		setFaviconPath("favicon.ico");
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("This application consists of the following modules");
		html.append("<hr />");
		
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
		for (int i = (getConfiguration().getModules().size() - 1); i >= 0; i--) {
			ModObject mod = getConfiguration().getModules().get(i);
			html.append("<tr>\n");
			html.append("<td width=\"20%\">");
			html.append("<a href=\"" + getConfiguration().getDomainUrl(mod.name) + "/index.html\">" + mod.name + "</a>");
			html.append("</td>\n");
			html.append("<td>");
			html.append(mod.desc);
			html.append("</td>\n");
			html.append("</tr>\n");
		}
		html.append("</tbody>\n");
		html.append("</table>\n");
		
		getBodyElements().add(html);
		
		return super.toStringBuilder();
	}
}

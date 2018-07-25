package nl.zeesoft.zsds.resource;

import nl.zeesoft.zdk.ZStringBuilder;

public class NotFoundHtml extends HtmlResource {
	public NotFoundHtml() {
		setTitle("ZSDS - Page not found");
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("404 - Page not found");
		html.append("<br />");
		html.append("<a href=\"index.html\">Index</a>");

		getBodyElements().add(html);
	}
}

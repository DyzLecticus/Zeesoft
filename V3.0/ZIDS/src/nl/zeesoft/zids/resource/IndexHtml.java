package nl.zeesoft.zids.resource;

import nl.zeesoft.zdk.ZStringBuilder;

public class IndexHtml extends HtmlResource {
	public IndexHtml() {
		setTitle("Welcome");
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"dialogs.json\">Dialogs</a>");
		html.append("<br />");
		html.append("<a href=\"chat.html\">Chat</a>");

		getBodyElements().add(html);
	}
}

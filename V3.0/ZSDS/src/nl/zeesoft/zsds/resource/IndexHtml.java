package nl.zeesoft.zsds.resource;

import nl.zeesoft.zdk.ZStringBuilder;

public class IndexHtml extends HtmlResource {
	public IndexHtml() {
		setTitle("ZSDS - Welcome");
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"config.json\">Configuration</a>");
		/*
		html.append("<br />");
		html.append("<a href=\"chat.html\">Chat</a>");
		*/

		getBodyElements().add(html);
	}
}

package nl.zeesoft.zsds.resource;

import nl.zeesoft.zdk.ZStringBuilder;

public class IndexHtml extends HtmlResource {
	public IndexHtml() {
		setTitle("ZSDS - Welcome");
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"config.json\">Configuration JSON</a>");
		html.append("<br />");
		html.append("<a href=\"dialogs.json\">Dialogs JSON</a>");
		html.append("<br />");
		html.append("<a href=\"test.html\">Test</a>");
		html.append("<br />");
		html.append("<a href=\"selfTest.html\">Self test</a>");

		getBodyElements().add(html);
	}
}

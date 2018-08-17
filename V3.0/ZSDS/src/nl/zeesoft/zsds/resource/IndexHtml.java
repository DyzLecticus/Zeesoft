package nl.zeesoft.zsds.resource;

import nl.zeesoft.zdk.ZStringBuilder;

public class IndexHtml extends HtmlResource {
	public IndexHtml() {
		setTitle("ZSDS - Welcome");
		
		ZStringBuilder html = new ZStringBuilder();
		
		html.append("<h1>Zeesoft Smart Dialog Server</h1>");
		
		html.append("<h2>Main application</h2>");
		html.append("<a href=\"state.html\">State manager</a>");
		html.append("<br />");
		html.append("<a href=\"test.html\">Test</a>");
		html.append("<br />");
		html.append("<a href=\"selfTestSummary.json\">Self test summary JSON</a>");
		html.append("<br />");
		html.append("<a href=\"dialogs.json\">Dialogs JSON</a>");
		html.append("<br />");
		html.append("<a href=\"config.json\">Configuration JSON</a>");
		html.append("<br />");
		
		html.append("<h2>Test application</h2>");
		html.append("<a href=\"environment.html\">Test environments</a>");
		html.append("<br />");
		html.append("<a href=\"testConfig.json\">Test configuration JSON</a>");
		html.append("<br />");
		
		getBodyElements().add(html);
	}
}

package nl.zeesoft.zenn.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zenn.mod.ModZENN;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;
import nl.zeesoft.zodb.mod.resource.HtmlResource;

public class HtmlZENNIndex extends HtmlResource {
	public HtmlZENNIndex(Config config) {
		super(config);
	}
	@Override
	public ZStringBuilder toStringBuilder() {
		setTitle("ZENN - Functions");	
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"../index.html\">Back to modules</a>");
		html.append("<hr />");
		
		String url = getConfiguration().getDomainUrl(ModZENN.NAME);
		
		html.append("<div>\n");
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
			html.append("<tr>\n");
			html.append("<td width=\"20%\">");
			html.append("<a href=\"" + url + "/environment.html\">Environment</a>");
			html.append("</td>\n");
			html.append("<td>");
			html.append("Monitor the environment.");
			html.append("</td>\n");
			html.append("</tr>\n");
			
			if (getConfiguration().getModule(ModZENN.NAME).selfTest) {
				html.append("<tr>\n");
				html.append("<td>");
				html.append("<a href=\"" + url + JsonModTestResultsHandler.PATH + "\">Test results JSON</a>");
				html.append("</td>\n");
				html.append("<td>");
				html.append("Returns the self test results as a JSON file.");
				html.append("</td>\n");
				html.append("</tr>\n");
			}
		html.append("</tbody>\n");
		html.append("</table>\n");
		html.append("</div>\n");
		
		getBodyElements().add(html);
		
		return super.toStringBuilder();
	}
}

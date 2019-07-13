package nl.zeesoft.zbe.mod.resource;

import nl.zeesoft.zbe.mod.ModZBE;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;
import nl.zeesoft.zodb.mod.resource.HtmlResource;

public class HtmlZBEIndex extends HtmlResource {
	public HtmlZBEIndex(Config config) {
		super(config);
	}
	@Override
	public ZStringBuilder toStringBuilder() {
		setTitle("ZBE - Functions");	
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"../index.html\">Back to modules</a>");
		html.append("<hr />");
		
		String url = getConfiguration().getModuleUrl(ModZBE.NAME);
		
		html.append("<div>\n");
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
			html.append("<tr>\n");
			html.append("<td width=\"20%\">");
			html.append("<a href=\"" + url + "/stateManager.html\">State manager</a>");
			html.append("</td>\n");
			html.append("<td>");
			html.append("Manage the state of the confabulator set.");
			html.append("</td>\n");
			html.append("</tr>\n");
			
			html.append("<tr>\n");
			html.append("<td>");
			html.append("<a href=\"" + url + "/state.json\">State JSON</a>");
			html.append("</td>\n");
			html.append("<td>");
			html.append("Returns the confabulator set state as a JSON file.");
			html.append("</td>\n");
			html.append("</tr>\n");
		
			if (getConfiguration().getModule(ModZBE.NAME).selfTest) {
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

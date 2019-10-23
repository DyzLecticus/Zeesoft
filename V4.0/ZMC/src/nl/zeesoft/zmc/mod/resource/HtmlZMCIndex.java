package nl.zeesoft.zmc.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zmc.mod.ModZMC;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.resource.HtmlResource;

public class HtmlZMCIndex extends HtmlResource {
	public HtmlZMCIndex(Config config) {
		super(config);
	}
	@Override
	public ZStringBuilder toStringBuilder() {
		setTitle("ZMC - Functions");	
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"../index.html\">Back to modules</a>");
		html.append("<hr />");
		
		String url = getConfiguration().getDomainUrl(ModZMC.NAME);
		
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
			
			/*
			if (getConfiguration().getModule(ModZMC.NAME).selfTest) {
				html.append("<tr>\n");
				html.append("<td>");
				html.append("<a href=\"" + url + JsonModTestResultsHandler.PATH + "\">Test results JSON</a>");
				html.append("</td>\n");
				html.append("<td>");
				html.append("Returns the self test results as a JSON file.");
				html.append("</td>\n");
				html.append("</tr>\n");
			}
			*/
		html.append("</tbody>\n");
		html.append("</table>\n");
		html.append("</div>\n");
		
		getBodyElements().add(html);
		
		return super.toStringBuilder();
	}
}

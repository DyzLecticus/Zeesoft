package nl.zeesoft.znlb.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.znlb.mod.ModZNLB;
import nl.zeesoft.znlb.mod.handler.JsonZNLBLanguagesHandler;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;
import nl.zeesoft.zodb.mod.resource.HtmlResource;

public class HtmlZNLBIndex extends HtmlResource {
	public HtmlZNLBIndex(Config config) {
		super(config);
	}
	@Override
	public ZStringBuilder toStringBuilder() {
		setTitle("ZNLB - Functions");
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"../index.html\">Back to modules</a>");
		html.append("<hr />");
		
		String url = getConfiguration().getModuleUrl(ModZNLB.NAME);
		
		html.append("<div>\n");
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
			html.append("<tr>\n");
			html.append("<td>");
			html.append("<a href=\"" + getConfiguration().getModuleUrl(ModZNLB.NAME) + JsonZNLBLanguagesHandler.PATH + "\">Languages JSON</a>");
			html.append("</td>\n");
			html.append("<td>");
			html.append("Returns the supported languages as a JSON file.");
			html.append("</td>\n");
			html.append("</tr>\n");
				
			if (getConfiguration().getModule(ModZNLB.NAME).selfTest) {
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

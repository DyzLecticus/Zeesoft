package nl.zeesoft.zals.mod.resource;

import nl.zeesoft.zals.mod.ModZALS;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;
import nl.zeesoft.zodb.mod.resource.HtmlResource;

public class HtmlZALSIndex extends HtmlResource {
	public HtmlZALSIndex(Config config) {
		super(config);
	}
	@Override
	public ZStringBuilder toStringBuilder() {
		setTitle("ZSDM - Functions");	
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"../index.html\">Back to modules</a>");
		html.append("<hr />");
		
		String url = getConfiguration().getModuleUrl(ModZALS.NAME);
		
		html.append("<div>\n");
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
			/*
			html.append("<tr>\n");
			html.append("<td width=\"20%\">");
			html.append("<a href=\"" + url + "/entityTranslator.html\">Entity translator</a>");
			html.append("</td>\n");
			html.append("<td>");
			html.append("Translate sequences to entity values and back.");
			html.append("</td>\n");
			html.append("</tr>\n");
			
			html.append("<tr>\n");
			html.append("<td>");
			html.append("<a href=\"" + url + "/types.json\">Types JSON</a>");
			html.append("</td>\n");
			html.append("<td>");
			html.append("Returns the supported entity types as a JSON file.");
			html.append("</td>\n");
			html.append("</tr>\n");
			*/
		
			if (getConfiguration().getModule(ModZALS.NAME).selfTest) {
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
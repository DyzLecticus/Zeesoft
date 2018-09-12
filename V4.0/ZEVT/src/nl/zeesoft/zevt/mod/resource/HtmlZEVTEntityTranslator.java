package nl.zeesoft.zevt.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.resource.HtmlResource;

public class HtmlZEVTEntityTranslator extends HtmlResource {
	public HtmlZEVTEntityTranslator(Config config) {
		super(config);
	}
	@Override
	public ZStringBuilder toStringBuilder() {
		setTitle("ZEVT - Entity translator");
		
		getScriptFiles().add("../ZODB/zodb.js");
		getScriptFiles().add("entityTranslator.js");
		
		setOnload("ZEVT.et.onload();");
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"index.html\">Back to index</a>");
		html.append("<hr />");
		
		html.append("<div>\n");
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
			html.append("<tr>\n");
			html.append("<td width=\"20%\">");
			html.append("Sequence");
			html.append("</td>\n");
			html.append("<td>");
			html.append("<input type=\"text\" id=\"sequence\" style=\"width: 100%\" />");
			html.append("</td>\n");
			html.append("</tr>\n");

			html.append("<tr>\n");
			html.append("<td>");
			html.append("</td>\n");
			html.append("<td>");
			html.append("<input type=\"button\" value=\"Translate\" onclick=\"ZEVT.et.translate();\" />");
			html.append("</td>\n");
			html.append("</tr>\n");

			html.append("<tr>\n");
			html.append("<td>");
			html.append("Translation");
			html.append("</td>\n");
			html.append("<td>");
			html.append("<input type=\"text\" id=\"translation\" style=\"width: 100%\" />");
			html.append("</td>\n");
			html.append("</tr>\n");

			html.append("<tr>\n");
			html.append("<td>");
			html.append("</td>\n");
			html.append("<td>");
			html.append("<input type=\"button\" value=\"Retranslate\" onclick=\"ZEVT.et.retranslate();\" />");
			html.append("</td>\n");
			html.append("</tr>\n");
		html.append("</tbody>\n");
		html.append("</table>\n");
		html.append("</div>\n");

		getBodyElements().add(html);
		
		return super.toStringBuilder();
	}
}

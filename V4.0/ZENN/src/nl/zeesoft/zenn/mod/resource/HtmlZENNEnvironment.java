package nl.zeesoft.zenn.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zenn.environment.EnvironmentConfig;
import nl.zeesoft.zenn.mod.ModZENN;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModZODB;
import nl.zeesoft.zodb.mod.resource.HtmlResource;

public class HtmlZENNEnvironment extends HtmlResource {
	public HtmlZENNEnvironment(Config config) {
		super(config);
	}
	@Override
	public ZStringBuilder toStringBuilder() {
		setTitle("ZENN - Environment");	
		
		getScriptFiles().add("../" + ModZODB.NAME + "/ZODB.js");
		getScriptFiles().add("../" + ModZENN.NAME + "/state.js");
		getStyleFiles().add("../" + ModZENN.NAME + "/ZENN.css");

		setOnload("ZENN.state.onload();");
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"./index.html\">Back to functions</a>");
		html.append("<hr />");
		
		html.append("<div>\n");
		html.append("<table>");
		html.append("<tbody>\n");
		html.append("<tr>");
		html.append("<td valign=\"top\">");
			
			html.append("<table id=\"grid\" class=\"envTable\">");
			html.append("<tbody>\n");
			for (int y = 0; y < EnvironmentConfig.SIZE_Y; y++) {
				html.append("<tr class=\"envTr\">");
				for (int x = 0; x < EnvironmentConfig.SIZE_X; x++) {
					String tdId = x + "-" + y;
					html.append("<td id=\"" + tdId + "\" class=\"envTd black\">");
					html.append("</td>");
				}
				html.append("</tr>");
			}
			html.append("</tbody>\n");
			html.append("</table>");

		html.append("</td>");
		html.append("<td valign=\"top\">");
		
			html.append("<table>");
			html.append("<thead>\n");
			
			html.append("<tr>");
			html.append("<th>");
			html.append("</th>");
			html.append("<th>");
			html.append("Details");
			html.append("</th>");
			html.append("<th align=\"right\">");
			html.append("Score");
			html.append("</th>");
			html.append("</tr>");

			html.append("</thead>\n");
			html.append("<tbody>\n");
			
			html.append("<tr>");
			html.append("<td valign=\"top\">");
			html.append("<b>Best herbivore</b>");
			html.append("</td>");
			html.append("<td id=\"bestHerbivore\">");
			html.append("</td>");
			html.append("<td valign=\"top\" align=\"right\" id=\"bestHerbivoreScore\">");
			html.append("</td>");
			html.append("</tr>");
						
			html.append("<tr>");
			html.append("<td valign=\"top\">");
			html.append("<b>Best living herbivore</b>");
			html.append("</td>");
			html.append("<td id=\"bestLivingHerbivore\">");
			html.append("</td>");
			html.append("<td valign=\"top\" align=\"right\" id=\"bestLivingHerbivoreScore\">");
			html.append("</td>");
			html.append("</tr>");
			
			html.append("<tr>");
			html.append("<td valign=\"top\">");
			html.append("<b>Evolver herbivore</b>");
			html.append("</td>");
			html.append("<td id=\"evolverHerbivore\">");
			html.append("</td>");
			html.append("<td>");
			html.append("</td>");
			html.append("</tr>");
			
			html.append("<tr>");
			html.append("<td valign=\"top\">");
			html.append("<b>Best carnivore</b>");
			html.append("</td>");
			html.append("<td id=\"bestCarnivore\">");
			html.append("</td>");
			html.append("<td valign=\"top\" align=\"right\" id=\"bestCarnivoreScore\">");
			html.append("</td>");
			html.append("</tr>");
			
			html.append("<tr>");
			html.append("<td valign=\"top\">");
			html.append("<b>Best living carnivore</b>");
			html.append("</td>");
			html.append("<td id=\"bestLivingCarnivore\">");
			html.append("</td>");
			html.append("<td valign=\"top\" align=\"right\" id=\"bestLivingCarnivoreScore\">");
			html.append("</td>");
			html.append("</tr>");
			
			html.append("<tr>");
			html.append("<td valign=\"top\">");
			html.append("<b>Evolver carnivore</b>");
			html.append("</td>");
			html.append("<td id=\"evolverCarnivore\">");
			html.append("</td>");
			html.append("<td>");
			html.append("</td>");
			html.append("</tr>");
			
			html.append("</tbody>\n");
			html.append("</table>");
			
		html.append("</td>");
		html.append("</tr>");
		html.append("</tbody>\n");
		html.append("</table>");
			
		html.append("</div>\n");
		
		getBodyElements().add(html);
		
		return super.toStringBuilder();
	}
}

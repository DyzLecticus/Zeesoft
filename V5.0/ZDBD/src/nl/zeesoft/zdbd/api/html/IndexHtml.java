package nl.zeesoft.zdbd.api.html;

import nl.zeesoft.zdbd.api.HtmlResponse;
import nl.zeesoft.zdk.Str;

public class IndexHtml extends HtmlResponse {
	public IndexHtml() {
		title = "ZDBD";
		onload = "state.onload();";
		
		styleFiles.add("/main.css");
		
		scriptFiles.add("/main.js");
		scriptFiles.add("/modal.js");
		scriptFiles.add("/state.js");
		scriptFiles.add("/menu.js");
		scriptFiles.add("/theme.js");
	}
	
	@Override
	public Str renderBody() {
		Str r = new Str();
		append(r,"<div id=\"modal\"></div>");
		
		append(r,"<div id=\"app\">");
		
		append(r,"<div id=\"menu\">");
		append(r,"<div class=\"row\">");
			append(r,"<div class=\"column-left column-padding\">");
			append(r,"<label class=\"column-label-small\">App</label>");
			append(r,"</div>\n");
			append(r,"<div class=\"column-left column-padding\">");
			append(r,"<input type=\"button\" value=\"Quit\" onclick=\"menu.quit();\" />");
			append(r,"</div>");
			append(r,"<div class=\"column-right column-padding\" id=\"state\"></div>");
		append(r,"</div>\n");
		
		append(r,"<div class=\"row\">");
			append(r,"<div class=\"column-left column-padding\">");
			append(r,"<label class=\"column-label-small\">Theme</label>");
			append(r,"</div>\n");
			append(r,"<div class=\"column-left column-padding\">");
			append(r,"<input type=\"button\" value=\"Load\" onclick=\"menu.load();\" />");
			append(r,"</div>\n");
			append(r,"<div class=\"column-left column-padding\">");
			append(r,"<input type=\"button\" value=\"Save\" onclick=\"menu.save();\" />");
			append(r,"</div>\n");
			append(r,"<div class=\"column-left column-padding\">");
			append(r,"<input type=\"button\" value=\"Save as\" onclick=\"menu.saveAs();\" />");
			append(r,"</div>\n");
			append(r,"<div class=\"column-left column-padding\">");
			append(r,"<input type=\"button\" value=\"Delete\" onclick=\"menu.delete();\" />");
			append(r,"</div>\n");
			append(r,"<div class=\"column-left column-padding\">");
			append(r,"<input type=\"button\" value=\"New\" onclick=\"menu.new();\" />");
			append(r,"</div>\n");
		append(r,"</div>\n");
		append(r,"</div>"); // end menu

		append(r,"<hr />");

		append(r,"<div id=\"theme\" class=\"column-padding\">");
		
		append(r,"</div>"); // end theme
		
		append(r,"</div>"); // end app
		return r;
	}
}

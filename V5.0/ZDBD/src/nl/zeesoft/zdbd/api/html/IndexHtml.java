package nl.zeesoft.zdbd.api.html;

import nl.zeesoft.zdbd.api.HtmlResponse;
import nl.zeesoft.zdk.Str;

public class IndexHtml extends HtmlResponse {
	public IndexHtml() {
		title = "ZDBD";
		onload = "state.onload();";
		styleFiles.add("/main.css");
		scriptFiles.add("/main.js");
		scriptFiles.add("/state.js");
		scriptFiles.add("/menu.js");
	}
	
	@Override
	public Str renderBody() {
		Str r = new Str();
		append(r,"<div id=\"menu\">");

		r.sb().append("<div class=\"row\">");
		r.sb().append("<div class=\"column-left column-padding\">");
			r.sb().append("App:&nbsp;");
			r.sb().append("<input type=\"button\" value=\"Quit\" onclick=\"menu.quit();\" />");
		r.sb().append("</div>");
		r.sb().append("<div class=\"column-right column-padding\" id=\"state\"></div>");
		r.sb().append("</div>\n");
		
		r.sb().append("<div class=\"column-padding\">");
		r.sb().append("Theme:&nbsp;");
		r.sb().append("<input type=\"button\" value=\"Load\" onclick=\"menu.load();\" />");
		r.sb().append("<input type=\"button\" value=\"Save\" onclick=\"menu.save();\" />");
		r.sb().append("<input type=\"button\" value=\"Save as\" onclick=\"menu.saveAs();\" />");
		r.sb().append("<input type=\"button\" value=\"Delete\" onclick=\"menu.delete();\" />");
		r.sb().append("<input type=\"button\" value=\"New\" onclick=\"menu.new();\" />");
		r.sb().append("</div>\n");
		
		append(r,"</div>");
		return r;
	}
}

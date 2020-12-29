package nl.zeesoft.zdbd.api.html;

import nl.zeesoft.zdbd.api.HtmlResponse;
import nl.zeesoft.zdk.Str;

public class Index extends HtmlResponse {
	public Index() {
		title = "ZDBD";
	}
	
	@Override
	public Str renderBody() {
		Str r = new Str();
		r.sb().append("<div>");
		r.sb().append("Welcome!");
		r.sb().append("</div>");
		return r;
	}
}

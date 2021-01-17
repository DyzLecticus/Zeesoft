package nl.zeesoft.zdbd.api.html;

import nl.zeesoft.zdbd.api.HtmlResponse;
import nl.zeesoft.zdk.Str;

public class ByeHtml extends HtmlResponse {
	public ByeHtml() {
		title = "ZDBD";
		onload = "quit.onload();";
		styleFiles.add("/main.css");
		scriptFiles.add("/main.js");
		scriptFiles.add("/quit.js");
	}
	
	@Override
	public Str renderBody() {
		Str r = new Str();
		append(r,"<div>");
		append(r,"<h1>Bye</h1>");
		append(r,"Hope you had fun");
		append(r,"</div>");
		return r;
	}
}

package nl.zeesoft.zdk.app.neural.resources;

import nl.zeesoft.zdk.app.resource.HtmlResource;

public class IndexHtml extends HtmlResource {
	public IndexHtml(String title) {
		this.title = title;
		//onload = "state.onload();";
		scriptFiles.add("/index.js");
	}
}

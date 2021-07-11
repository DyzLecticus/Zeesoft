package nl.zeesoft.zdk.app.neural.resources;

import nl.zeesoft.zdk.app.resource.HtmlResource;

public class IndexHtml extends HtmlResource {
	public IndexHtml(String title) {
		this.title = title;
		onload = "loadState((state) => { console.log(state) });";
		scriptFiles.add("/index.js");
		styleFiles.add("/index.css");
	}
}

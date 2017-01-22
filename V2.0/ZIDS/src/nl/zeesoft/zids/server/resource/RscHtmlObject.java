package nl.zeesoft.zids.server.resource;

import nl.zeesoft.zodb.file.HTMLFile;

public abstract class RscHtmlObject extends RscObject {
	private String				title			= "";
	private String				backgroundColor	= "";

	public RscHtmlObject(String title, String backgroundColor) {
		this.title = title;
		this.backgroundColor = backgroundColor;
	}
	
	@Override
	public String getContentType() {
		return "text/html";
	}
	
	protected HTMLFile getNewHTMLFile() {
		HTMLFile f = new HTMLFile(title);
		f.setBodyBgColor(backgroundColor);
		return f;
	}
	
	protected StringBuilder getMenuForPage(String pathPrefix,String pageName,String addLinks) {
		StringBuilder html = new StringBuilder();
		html.append("<div>");
		if (!pageName.equals("home")) {
			html.append("<a href=\"" + pathPrefix + "index.html\">Home</a> ");
		} else {
			html.append("<b>Home</b> ");
		}
		if (!pageName.equals("data")) {
			html.append("<a href=\"" + pathPrefix + "data/index.html\">Data</a> ");
		} else {
			html.append("<b>Data</b> ");
		}
		if (!pageName.equals("dialog")) {
			html.append("<a href=\"" + pathPrefix + "dialogs/index.html\">Dialog</a> ");
		} else {
			html.append("<b>Dialog</b> ");
		}
		html.append(addLinks);
		html.append("<hr/>");
		html.append("</div>");
		return html;
	}
}

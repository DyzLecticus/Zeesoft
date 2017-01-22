package nl.zeesoft.zids.server.resource;

import nl.zeesoft.zodb.file.HTMLFile;

public class RscDialogIndexHtml extends RscHtmlObject {
	public RscDialogIndexHtml(String title, String backgroundColor) {
		super(title, backgroundColor);
	}

	@Override
	public StringBuilder toStringBuilder() {
		StringBuilder body = new StringBuilder();
		body.append("<div>");
		body.append("<ul>");
		body.append("<li>");
		body.append("<a href=\"./dialogs.html\">Dialogs</a><br />");
		body.append("</li>");
		body.append("<li>");
		body.append("<a href=\"./speaker.html\">Speaker</a><br />");
		body.append("</li>");
		body.append("</ul>");
		body.append("</div>");
		HTMLFile f = getNewHTMLFile();
		f.getBodyElements().add(getMenuForPage("../","dialog","").toString());
		f.getBodyElements().add(body.toString());
		return f.toStringBuilder();
	}
}

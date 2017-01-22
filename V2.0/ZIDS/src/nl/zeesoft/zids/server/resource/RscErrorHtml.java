package nl.zeesoft.zids.server.resource;

import nl.zeesoft.zodb.file.HTMLFile;

public class RscErrorHtml extends RscHtmlObject {
	private String message = "";
	
	public RscErrorHtml(String title,String backgroundColor,String msg) {
		super(title,backgroundColor);
		message = msg;
	}
	
	@Override
	public StringBuilder toStringBuilder() {
		StringBuilder body = new StringBuilder();
		body.append("<div>");
		body.append("<b>ERROR:</b> ");
		body.append(message);
		body.append("</div>");
		HTMLFile f = getNewHTMLFile();
		f.getBodyElements().add(getMenuForPage("../","","").toString());
		f.getBodyElements().add(body.toString());
		return f.toStringBuilder();
	}
}

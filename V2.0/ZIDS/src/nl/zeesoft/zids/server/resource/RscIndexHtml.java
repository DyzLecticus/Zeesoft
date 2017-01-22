package nl.zeesoft.zids.server.resource;

import nl.zeesoft.zodb.file.HTMLFile;

public class RscIndexHtml extends RscHtmlObject {
	public RscIndexHtml(String title, String backgroundColor) {
		super(title, backgroundColor);
	}

	@Override
	public StringBuilder toStringBuilder() {
		StringBuilder body = new StringBuilder();
		body.append("<div>");
		body.append("Welcome to the <b>Zeesoft Intelligent Dialog Server</b>. ");
		body.append("This server is based on the Zeesoft Artificial Cognition Simulator. ");
		body.append("It can be used to learn <a href=\"./dialog/dialogs.html\">dialogs</a> and then <a href=\"./dialog/speaker.html\">reproduce</a> them in a natural linguistic manner. ");
		body.append("JSON can be used to access the database <a href=\"./data/\">data</a> and <a href=\"./dialog/\">dialog</a> interfaces.");
		body.append("</div>");
		HTMLFile f = getNewHTMLFile();
		f.getBodyElements().add(getMenuForPage("./","home","").toString());
		f.getBodyElements().add(body.toString());
		return f.toStringBuilder();
	}
}

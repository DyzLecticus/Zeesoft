package nl.zeesoft.zids.server.resource;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.file.HTMLFile;

public class RscDataIndexHtml extends RscHtmlObject {
	public RscDataIndexHtml(String title, String backgroundColor) {
		super(title, backgroundColor);
	}

	@Override
	public StringBuilder toStringBuilder() {
		StringBuilder body = new StringBuilder();
		body.append("<div>");
		body.append("<ul>");
		body.append("<li>");
		body.append("<a href=\"./poster.html\">Poster</a><br />");
		body.append("</li>");
		for (MdlClass cls: DbConfig.getInstance().getModel().getClasses()) {
			if (!cls.isAbstr()) {
				body.append("<li>");
				body.append("<a href=\"./");
				body.append(cls.getFullName());
				body.append(".json?_start=0&_limit=100\">");
				body.append(cls.getFullName());
				body.append("</a>");
				body.append("</li>");
			}
		}
		body.append("</ul>");
		body.append("</div>");
		HTMLFile f = getNewHTMLFile();
		f.getBodyElements().add(getMenuForPage("../","data","").toString());
		f.getBodyElements().add(body.toString());
		return f.toStringBuilder();
	}
}

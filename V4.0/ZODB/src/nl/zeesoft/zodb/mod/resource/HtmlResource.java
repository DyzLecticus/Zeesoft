package nl.zeesoft.zodb.mod.resource;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;

public abstract class HtmlResource {
	private Config					configuration	= null;
	private String					title			= "";
	private String					onload			= "";

	private String					bodyBgColor		= "";
	
	private List<String> 			scriptFiles 	= new ArrayList<String>();
	private List<String> 			styleFiles		= new ArrayList<String>();
	private List<ZStringBuilder>	headElements 	= new ArrayList<ZStringBuilder>();
	private List<ZStringBuilder> 	bodyElements 	= new ArrayList<ZStringBuilder>();
	
	public HtmlResource(Config config) {
		configuration = config;
	}
	
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder html = new ZStringBuilder();
		html.append("<!DOCTYPE html>");
		html.append("\n");

		html.append("<html>");
		html.append("\n");
		
		html.append("<head>");
		html.append("\n");
		html.append("<title>");
		html.append(title);
		html.append("</title>");
		html.append("\n");
		for (String src: scriptFiles) {
			html.append("<script type=\"text/javascript\" src=\"");
			html.append(src);
			html.append("\"></script>");
			html.append("\n");
		}
		for (String href: styleFiles) {
			html.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"");
			html.append(href);
			html.append("\">");
			html.append("\n");
		}
		for (ZStringBuilder head: headElements) {
			html.append(head);
			html.append("\n");
		}
		html.append("</head>");
		html.append("\n");

		
		html.append("<body");
		if (onload.length()>0) {
			html.append(" onload=\"");
			html.append(onload);
			html.append("\"");
		}
		if (bodyBgColor.length()>0) {
			html.append(" bgcolor=\"");
			html.append(bodyBgColor);
			html.append("\"");
		}
		html.append(">");
		
		html.append("\n");
		for (ZStringBuilder body: bodyElements) {
			html.append(body);
			html.append("\n");
		}
		html.append("</body>");
		html.append("\n");

		html.append("</html>");
		html.append("\n");
		return html;
	}
	
	public Config getConfiguration() {
		return configuration;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public void setOnload(String onload) {
		this.onload = onload;
	}

	public void setBodyBgColor(String bodyBgColor) {
		this.bodyBgColor = bodyBgColor;
	}

	public List<String> getScriptFiles() {
		return scriptFiles;
	}

	public List<String> getStyleFiles() {
		return styleFiles;
	}

	public List<ZStringBuilder> getHeadElements() {
		return headElements;
	}

	public List<ZStringBuilder> getBodyElements() {
		return bodyElements;
	}
}

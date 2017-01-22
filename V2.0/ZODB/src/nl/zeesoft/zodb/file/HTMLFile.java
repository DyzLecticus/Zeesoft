package nl.zeesoft.zodb.file;

import java.util.ArrayList;
import java.util.List;

public class HTMLFile extends FileIO {
	private String			title			= "";
	private String			onload			= "";

	private String			bodyBgColor		= "";
	
	private List<String> 	scriptFiles 	= new ArrayList<String>();
	private List<String> 	styleFiles		= new ArrayList<String>();
	private List<String> 	headElements 	= new ArrayList<String>();
	private List<String> 	bodyElements 	= new ArrayList<String>();
	
	public HTMLFile(String title) {
		this.title = title;
	}
	
	public StringBuilder toStringBuilder() {
		StringBuilder html = new StringBuilder();
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
		for (String head: headElements) {
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
		for (String body: bodyElements) {
			html.append(body);
			html.append("\n");
		}
		html.append("</body>");
		html.append("\n");

		html.append("</html>");
		html.append("\n");
		return html;
	}

	/**
	 * @return the scriptFiles
	 */
	public List<String> getScriptFiles() {
		return scriptFiles;
	}

	/**
	 * @return the styleFiles
	 */
	public List<String> getStyleFiles() {
		return styleFiles;
	}

	/**
	 * @return the headElements
	 */
	public List<String> getHeadElements() {
		return headElements;
	}

	/**
	 * @param onload the onload to set
	 */
	public void setOnload(String onload) {
		this.onload = onload;
	}

	/**
	 * @return the bodyElements
	 */
	public List<String> getBodyElements() {
		return bodyElements;
	}

	/**
	 * @return the bodyBgColor
	 */
	public String getBodyBgColor() {
		return bodyBgColor;
	}

	/**
	 * @param bodyBgColor the bodyBgColor to set
	 */
	public void setBodyBgColor(String bodyBgColor) {
		this.bodyBgColor = bodyBgColor;
	}
}

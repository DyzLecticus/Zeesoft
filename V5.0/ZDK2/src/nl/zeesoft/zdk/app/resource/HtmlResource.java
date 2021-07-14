package nl.zeesoft.zdk.app.resource;

import java.util.ArrayList;
import java.util.List;

public class HtmlResource extends Resource {
	public static String	LINE_BREAK		= "<br />";
	public static String	SPACE			= "&nbsp;";
	
	public String			title			= "";
	public String			onload			= "";
	public String			faviconPath		= "/favicon.ico";
	
	public List<String>		scriptFiles 	= new ArrayList<String>();
	public List<String>		styleFiles		= new ArrayList<String>();

	@Override
	protected void render(StringBuilder r) {
		append(r, "<html>");
		append(r, "<head>");
		renderHeadContent(r);
		append(r, "</head>");
		renderBodyStartTag(r);
		append(r, renderBody());
		append(r, "</body>");
		append(r, "</html>");
	}

	protected void renderHeadContent(StringBuilder r) {
		append(r, "<title>" + title + "</title>");
		append(r, "<link rel=\"icon\" href=\"" + faviconPath + "\" type=\"image/png\" />");
		append(r, "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />");
		for (String src: scriptFiles) {
			append(r, "<script type=\"text/javascript\" src=\"" + src + "\"></script>");
		}
		for (String href: styleFiles) {
			append(r, "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + href + "\">");
		}
		append(r, "</head>");
	}

	protected void renderBodyStartTag(StringBuilder r) {
		StringBuilder tag = new StringBuilder("<body"); 
		if (onload.length()>0) {
			tag.append(" onload=\"");
			tag.append(onload);
			tag.append("\"");
		}
		tag.append(">");
		append(r, tag);
	}
	
	protected StringBuilder renderBody() {
		return new StringBuilder();
	}

	public static StringBuilder renderLinkListItem(String href, String content) {
		return wrapListItem(renderLink(href, content));
	}

	public static StringBuilder wrapListItem(StringBuilder content) {
		StringBuilder r = new StringBuilder("<li>");
		r.append(content);
		r.append("</li>");
		return r;
	}
	
	public static StringBuilder renderLink(String href, String content) {
		return renderLink(href, new StringBuilder(content));
	}
	
	public static StringBuilder renderLink(String href, StringBuilder content) {
		StringBuilder r = new StringBuilder();
		r.append("<a href=\"");
		r.append(href);
		r.append("\">");
		r.append(content);
		r.append("</a>");
		return r;
	}
	
	protected StringBuilder getAccordionStart(String id, String title) {
		StringBuilder r = new StringBuilder();
		r.append("<div onclick=\"dom.toggleVisible('" + id + "');\" class=\"clickable\">");
		r.append("<h2 class='accordion-title mt-0 mb-0'>" + title + "</h2>");
		r.append("</div>");
		r.append("<div id=\"" + id + "\" class=\"hidden\">");
		return r;
	}
	
	protected StringBuilder getAccordionEnd() {
		return new StringBuilder("</div>");
	}
}

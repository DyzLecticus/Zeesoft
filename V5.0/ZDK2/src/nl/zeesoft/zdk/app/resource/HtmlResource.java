package nl.zeesoft.zdk.app.resource;

import java.util.ArrayList;
import java.util.List;

public class HtmlResource extends Resource {
	public String			title			= "";
	public String			onload			= "";
	public String			faviconPath		= "/favicon.ico";

	public String			bodyBgColor		= "DEDEDE";
	
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
		append(r, "    <title>" + title + "</title>");
		append(r, "    <link rel=\"icon\" href=\"" + faviconPath + "\" type=\"image/png\" />");
		for (String src: scriptFiles) {
			append(r, "    <script type=\"text/javascript\" src=\"" + src + "\"></script>");
		}
		for (String href: styleFiles) {
			append(r, "    <link rel=\"stylesheet\" type=\"text/css\" href=\"" + href + "\">");
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
		if (bodyBgColor.length()>0) {
			tag.append(" bgcolor=\"");
			tag.append(bodyBgColor);
			tag.append("\"");
		}
		tag.append(">");
		append(r, tag);
	}
	
	protected StringBuilder renderBody() {
		return new StringBuilder();
	}
}

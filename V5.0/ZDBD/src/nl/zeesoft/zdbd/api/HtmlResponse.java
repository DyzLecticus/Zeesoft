package nl.zeesoft.zdbd.api;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Str;

public class HtmlResponse extends ResponseObject {
	public String			title			= "";
	public String			onload			= "";
	public String			faviconPath		= "favicon.ico";

	public String			bodyBgColor		= "DEDEDE";
	
	public List<String>		scriptFiles 	= new ArrayList<String>();
	public List<String>		styleFiles		= new ArrayList<String>();

	@Override
	public Str render() {
		Str r = new Str();
		r.sb().append("<html>");
		r.sb().append("\n");
		
		r.sb().append("<head>");
		r.sb().append("\n");
		r.sb().append("    <title>");
		r.sb().append(title);
		r.sb().append("</title>");
		r.sb().append("\n");
		r.sb().append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />");
		r.sb().append("\n");

		r.sb().append("    <link rel=\"icon\" href=\"" + faviconPath + "\" type=\"image/png\" />");
		r.sb().append("\n");
		for (String src: scriptFiles) {
			r.sb().append("    <script type=\"text/javascript\" src=\"");
			r.sb().append(src);
			r.sb().append("\"></script>");
			r.sb().append("\n");
		}
		for (String href: styleFiles) {
			r.sb().append("    <link rel=\"stylesheet\" type=\"text/css\" href=\"");
			r.sb().append(href);
			r.sb().append("\">");
			r.sb().append("\n");
		}
		r.sb().append(renderHead().sb());
		r.sb().append("</head>");
		r.sb().append("\n");
		
		r.sb().append("<body");
		if (onload.length()>0) {
			r.sb().append(" onload=\"");
			r.sb().append(onload);
			r.sb().append("\"");
		}
		if (bodyBgColor.length()>0) {
			r.sb().append(" bgcolor=\"");
			r.sb().append(bodyBgColor);
			r.sb().append("\"");
		}
		r.sb().append(">");
		r.sb().append("\n");
		
		r.sb().append(renderBody().sb());
		
		r.sb().append("</body>");
		r.sb().append("\n");
		r.sb().append("</html>");
		return r;
	}

	public Str renderHead() {
		return new Str();
	}

	public Str renderBody() {
		return new Str();
	}
}

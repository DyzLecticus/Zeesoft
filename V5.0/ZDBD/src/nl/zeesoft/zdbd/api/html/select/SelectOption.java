package nl.zeesoft.zdbd.api.html.select;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class SelectOption extends ResponseObject {
	public String			id				= "";
	public String			name			= "";
	public String			label			= "";
	public String			value			= "";
	
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"<div class=\"row\">");
		append(r,"<div class=\"column-left column-padding\">");
		Str val = new Str();
		if (value!=null) {
			val.sb().append(" value=\"");
			val.sb().append(value);
			val.sb().append("\"");
		}
		append(r,"<input name=\"");
		r.sb().append(name);
		r.sb().append("\" id=\"");
		r.sb().append(id);
		r.sb().append("\" type=\"radio\"");
		r.sb().append(val.sb());
		r.sb().append(" />");
		append(r,"</div>");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<label for=\"");
		r.sb().append(id);
		r.sb().append("\">");
		r.sb().append(label.replace(" ", "&nbsp;"));
		r.sb().append("</label>");
		append(r,"</div>");
		append(r,"</div>");
		return r;
	}
}

package nl.zeesoft.zdbd.api.html.form;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class FormProperty extends ResponseObject {
	public static String	TEXT			= "TEXT";
	public static String	TEXT_INPUT		= "TEXT_INPUT";
	public static String	NUMBER_INPUT	= "NUMBER_INPUT";
	
	public String			name			= "";
	public String			label			= "";
	public Object			value			= null;
	public String			renderAs		= TEXT_INPUT;
	
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"<div class=\"row\">");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,label);
		append(r,"</div>");
		append(r,"<div class=\"column-left column-padding\">");
		if (renderAs.equals(TEXT)) {
			if (value!=null) {
				append(r,toString());
			}
		}
		if (renderAs.equals(TEXT_INPUT) || renderAs.equals(NUMBER_INPUT)) {
			Str val = new Str();
			if (value!=null) {
				val.sb().append(" value=\"");
				val.sb().append(value.toString());
				val.sb().append("\"");
			}
			append(r,"<input id=\"");
			r.sb().append(name);
			r.sb().append("\"");
			r.sb().append(" type=\"");
			if (renderAs.equals(TEXT_INPUT)) {
				r.sb().append("text");
			} else if (renderAs.equals(TEXT_INPUT)) {
				r.sb().append("number");
			}
			r.sb().append("\"");
			r.sb().append(val.sb());
			r.sb().append(" />");
		}
		append(r,"</div>");
		append(r,"</div>");
		return r;
	}
}

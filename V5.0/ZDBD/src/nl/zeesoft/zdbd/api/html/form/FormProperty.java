package nl.zeesoft.zdbd.api.html.form;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class FormProperty extends ResponseObject {
	public static String	TEXT			= "TEXT";
	public static String	TEXT_INPUT		= "TEXT_INPUT";
	public static String	NUMBER_INPUT	= "NUMBER_INPUT";
	public static String	CHECKBOX_INPUT	= "CHECKBOX_INPUT";
	public static String	SELECT			= "SELECT";
	
	public String			name			= "";
	public String			label			= "";
	public Object			value			= null;
	public String			renderAs		= TEXT_INPUT;
	public Object			param			= null;
	public String			onChange		= "";
	
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"<div class=\"row\">");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<label class=\"column-label\" for=\"");
		r.sb().append(name);
		r.sb().append("\">");
		r.sb().append(label.replace(" ", "&nbsp;"));
		r.sb().append("</label>");
		append(r,"</div>");
		append(r,"<div class=\"column-left column-padding\">");
		if (renderAs.equals(TEXT)) {
			if (value!=null) {
				append(r,value.toString());
			}
		} else if (
			renderAs.equals(TEXT_INPUT) ||
			renderAs.equals(NUMBER_INPUT) ||
			renderAs.equals(CHECKBOX_INPUT)
			) {
			Str val = new Str();
			if (value!=null) {
				if (renderAs.equals(CHECKBOX_INPUT)) {
					if (value instanceof Boolean && (boolean)value) {
						val.sb().append(" checked");
					}
				} else {
					val.sb().append(" value=\"");
					val.sb().append(value.toString());
					val.sb().append("\"");
				}
			}
			append(r,"<input id=\"");
			r.sb().append(name);
			r.sb().append("\"");
			r.sb().append(" type=\"");
			if (renderAs.equals(TEXT_INPUT)) {
				r.sb().append("text");
			} else if (renderAs.equals(NUMBER_INPUT)) {
				r.sb().append("number");
			} else if (renderAs.equals(CHECKBOX_INPUT)) {
				r.sb().append("checkbox");
			}
			r.sb().append("\"");
			if (onChange.length()>0) {
				r.sb().append(" onchange=\"");
				r.sb().append(onChange);
				r.sb().append("\"");
			}
			r.sb().append(val.sb());
			r.sb().append(" />");
		} else if (renderAs.equals(SELECT)) {
			if (value instanceof ArrayList) {
				@SuppressWarnings("unchecked")
				List<Object> values = (ArrayList<Object>) value;
				append(r,"<select id=\"");
				r.sb().append(name);
				r.sb().append("\"");
				if (onChange.length()>0) {
					r.sb().append(" onchange=\"");
					r.sb().append(onChange);
					r.sb().append("\"");
				}
				r.sb().append(">");
				for (Object val: values) {
					append(r,"<option value=\"");
					r.sb().append(val);
					r.sb().append("\"");
					if (val.equals(param)) {
						r.sb().append(" SELECTED");
					}
					r.sb().append(">");
					r.sb().append(val.toString());
					r.sb().append("</option>");
				}
				append(r,"</select>");
			}
		}
		append(r,"</div>");
		append(r,"</div>");
		return r;
	}
}

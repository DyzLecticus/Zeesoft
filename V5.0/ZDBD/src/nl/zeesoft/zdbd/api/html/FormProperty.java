package nl.zeesoft.zdbd.api.html;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class FormProperty extends ResponseObject {
	public static String	TEXT			= "TEXT";
	public static String	TEXT_INPUT		= "TEXT_INPUT";
	public static String	NUMBER_INPUT	= "NUMBER_INPUT";
	public static String	ANY_INPUT		= "ANY_INPUT";
	public static String	RANGE_INPUT		= "RANGE_INPUT";
	public static String	CHECKBOX_INPUT	= "CHECKBOX_INPUT";
	public static String	BUTTON_INPUT	= "BUTTON_INPUT";
	public static String	BUTTON_DISABLED	= "BUTTON_DISABLED";
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
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<label class=\"column-label\" for=\"");
		r.sb().append(name);
		r.sb().append("\">");
		r.sb().append(label.replace(" ", "&nbsp;"));
		r.sb().append("</label>");
		append(r,"</div>");
		if (renderAs.equals(TEXT)) {
			Str cls = new Str(" class=\"column-left column-padding");
			if (value!=null) {
				if (value instanceof Integer ||
					value instanceof Float ||
					value.toString().endsWith("%")
					) {
					cls.sb().append(" column-number");
				}
				cls.sb().append("\"");
				append(r,"<div");
				r.sb().append(cls);
				r.sb().append(">");
				r.sb().append(value.toString());
				r.sb().append("</div>");
			}
		} else if (
			renderAs.equals(TEXT_INPUT) ||
			renderAs.equals(NUMBER_INPUT) ||
			renderAs.equals(ANY_INPUT) ||
			renderAs.equals(RANGE_INPUT) ||
			renderAs.equals(CHECKBOX_INPUT) ||
			renderAs.equals(BUTTON_INPUT) ||
			renderAs.equals(BUTTON_DISABLED)
			) {
			append(r,"<div class=\"column-left column-padding\">");
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
			Str range = new Str();
			if (renderAs.equals(RANGE_INPUT) && param!=null && param instanceof int[]) {
				int[] minMax = (int[])param;
				if (minMax.length==2) {
					range.sb().append(" min=\"");
					range.sb().append(minMax[0]);
					range.sb().append("\" max=\"");
					range.sb().append(minMax[1]);
					range.sb().append("\"");
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
			} else if (renderAs.equals(ANY_INPUT)) {
				r.sb().append("any");
			} else if (renderAs.equals(RANGE_INPUT)) {
				r.sb().append("range");
			} else if (renderAs.equals(CHECKBOX_INPUT)) {
				r.sb().append("checkbox");
			} else if (renderAs.equals(BUTTON_INPUT) || renderAs.equals(BUTTON_DISABLED)) {
				r.sb().append("button");
			}
			r.sb().append("\"");
			if (range.length()>0) {
				r.sb().append(range);
			}
			if ((renderAs.equals(BUTTON_INPUT) || renderAs.equals(BUTTON_DISABLED))
				&& param!=null
				) {
				r.sb().append(" onclick=\"");
				r.sb().append(param);
				r.sb().append("\"");
			}
			if (onChange.length()>0) {
				r.sb().append(" onchange=\"");
				r.sb().append(onChange);
				r.sb().append("\"");
			}
			r.sb().append(val.sb());
			if (renderAs.equals(BUTTON_DISABLED)) {
				r.sb().append(" DISABLED");
			}
			r.sb().append(" />");
			append(r,"</div>");
		} else if (renderAs.equals(SELECT)) {
			append(r,"<div class=\"column-left column-padding\">");
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
			append(r,"</div>");
		}
		return r;
	}
}

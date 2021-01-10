package nl.zeesoft.zdbd.api.html.form;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class FormHtml extends ResponseObject {
	public String				okLabel			= "Ok";
	public String				onOkClick		= "";
	public String				onCancelClick	= "";
	public List<FormProperty> 	properties		= new ArrayList<FormProperty>();
	
	public FormProperty addProperty(String name, String label) {
		return addProperty(name, label, null, FormProperty.TEXT_INPUT);
	}
	
	public FormProperty addProperty(String name, String label, Object value) {
		return addProperty(name, label, value, FormProperty.TEXT_INPUT);
	}
	
	public FormProperty addProperty(String name, String label, Object value, String renderAs) {
		return addProperty(name, label, value, renderAs, null);
	}
	
	public FormProperty addProperty(String name, String label, Object value, String renderAs, Object param) {
		FormProperty r = new FormProperty();
		r.name = name;
		r.label = label;
		r.value = value;
		r.renderAs = renderAs;
		r.param = param;
		properties.add(r);
		return r;
	}
	
	@Override
	public Str render() {
		Str r = new Str();
		for (FormProperty property: properties) {
			append(r,"<div class=\"row\">");
			append(r,property.render());
			append(r,"</div>");
		}
		append(r,renderOkCancel(okLabel,onOkClick,onCancelClick));
		return r;
	}
	
	public static Str renderOkCancel(String okLabel, String onOkClick, String onCancelClick) {
		Str r = new Str();
		if (onOkClick.length()>0 || onCancelClick.length()>0) {
			append(r,"<div class=\"row\">");
			append(r,"<div class=\"column-right column-padding\">");
			if (onOkClick.length()>0) {
				append(r,"<input id=\"formOk\" type=\"button\" value=\"");
				r.sb().append(okLabel);
				r.sb().append("\" onclick=\"");
				r.sb().append(onOkClick);
				r.sb().append("\" />");
			}
			if (onCancelClick.length()>0) {
				append(r,"<input id=\"formCancel\" type=\"button\" value=\"Cancel\" onclick=\"");
				r.sb().append(onCancelClick);
				r.sb().append("\" />");
			}
			append(r,"</div>");
			append(r,"</div>");
		}
		return r;
	}
}

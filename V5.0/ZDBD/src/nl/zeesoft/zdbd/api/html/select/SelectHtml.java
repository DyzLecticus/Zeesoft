package nl.zeesoft.zdbd.api.html.select;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdbd.api.html.form.FormHtml;
import nl.zeesoft.zdk.Str;

public class SelectHtml extends ResponseObject {
	public String				title			= "";
	public String				name			= "";
	public String				onOkClick		= "";
	public String				onCancelClick	= "";
	public List<SelectOption> 	options			= new ArrayList<SelectOption>();
	
	public SelectHtml(String title,String name) {
		this.title = title;
		this.name = name;
	}
	
	public SelectOption addOption(String label) {
		return addOption(label, label);
	}
	
	public SelectOption addOption(String label, String value) {
		SelectOption r = new SelectOption();
		r.id = name + options.size();
		r.name = name;
		r.label = label;
		r.value = value;
		options.add(r);
		return r;
	}
	
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"<div class=\"row column-padding\">");
		r.sb().append(title);
		r.sb().append("</div>");
		for (SelectOption option: options) {
			append(r,option.render());
		}
		append(r,FormHtml.renderOkCancel(onOkClick,onCancelClick));
		return r;
	}
}

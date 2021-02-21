package nl.zeesoft.zdbd.api.html.form;

import nl.zeesoft.zdbd.api.html.FormHtml;
import nl.zeesoft.zdk.Str;

public abstract class AbstractEditor extends FormHtml {
	protected String	function	= "";
	protected String	prevName	= "";
	protected String	nextName	= "";

	@Override
	public Str render() {
		Str r = new Str();
		append(r,renderPrevNextButtons(function,prevName,nextName));
		append(r,super.render());
		return r;
	}
	
	public static Str renderPrevNextButtons(String function, String prevName, String nextName) {
		Str r = new Str();
		if (prevName.length()>0 || nextName.length()>0) {
			append(r,"<div class=\"row\">");
			append(r,"<div class=\"column-left column-padding\">");
			append(r,"<input type=\"button\" value=\"<\" onclick=\"");
			r.sb().append(function);
			r.sb().append("('");
			r.sb().append(prevName);
			r.sb().append("');\"/>");
			append(r,"<input type=\"button\" value=\">\" onclick=\"");
			r.sb().append(function);
			r.sb().append("('");
			r.sb().append(nextName);
			r.sb().append("');\"/>");
			append(r,"</div>");
			append(r,"</div>");
		}
		return r;
	}
}

package nl.zeesoft.zdbd.api.html.form;

import nl.zeesoft.zdk.Str;

public class InstrumentList extends FormHtml {
	public static String[]	INSTRUMENT_NAMES = {"Drums","Bass","Stab","Arpeggiator","Echo"};
	
	@Override
	public Str render() {
		Str r = new Str();
		append(r,renderInstruments());
		return r;
	}
	
	public static Str renderInstruments() {
		Str r = new Str();
		int i = 0;
		for (String name: INSTRUMENT_NAMES) {
			append(r,"<div class=\"row");
			if (i % 2==0) {
				r.sb().append(" row-highlight");
			}
			r.sb().append("\">");
			append(r,"<div class=\"column-left column-padding\">");
			append(r,"<label class=\"column-label\">");
			r.sb().append(name);
			r.sb().append("</label>");
			append(r,"</div>");
			
			append(r,"<div class=\"column-left column-padding\">");
			append(r,"<input type=\"button\" value=\"Edit");
			r.sb().append("\" onclick=\"arpeggiators.edit('");
			r.sb().append(name);
			r.sb().append("');\"/>");
			append(r,"</div>");
			
			append(r,"</div>");
			i++;
		}
		return r;
	}
}

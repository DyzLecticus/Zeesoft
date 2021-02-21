package nl.zeesoft.zdbd.api.html.form;

import java.util.List;

import nl.zeesoft.zdbd.api.html.FormHtml;
import nl.zeesoft.zdbd.midi.Arpeggiator;
import nl.zeesoft.zdk.Str;

public class ArpeggiatorList extends FormHtml {
	protected List<Arpeggiator> arps 	= null;
	
	public ArpeggiatorList(List<Arpeggiator> arps) {
		this.arps = arps;
	}
	
	@Override
	public Str render() {
		Str r = new Str();
		append(r,renderArpeggiators(arps));
		append(r,renderAddArpeggiatorButton());
		return r;
	}
	
	public static Str renderArpeggiators(List<Arpeggiator> arps) {
		Str r = new Str();
		int i = 0;
		for (Arpeggiator arp: arps) {
			append(r,"<div class=\"row");
			if (i % 2==0) {
				r.sb().append(" row-highlight");
			}
			r.sb().append("\">");
			append(r,"<div class=\"column-left column-padding\">");
			append(r,"<label class=\"column-label\">");
			r.sb().append(arp.name);
			r.sb().append("</label>");
			append(r,"</div>");
			
			append(r,"<div class=\"column-left column-padding\">");
			append(r,"<input type=\"button\" value=\"Edit");
			r.sb().append("\" onclick=\"arpeggiators.edit('");
			r.sb().append(arp.name);
			r.sb().append("');\"/>");
			append(r,"</div>");
			
			append(r,"<div class=\"column-right column-padding\">");
			append(r,"<input type=\"button\" value=\"Delete\" onclick=\"arpeggiators.delete('");
			r.sb().append(arp.name);
			r.sb().append("');\" />");
			append(r,"</div>");
			append(r,"</div>");
			
			i++;
		}
		return r;
	}
	
	public static Str renderAddArpeggiatorButton() {
		Str r = new Str();
		append(r,"<div class=\"row\">");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<label class=\"column-label\"></label>");
		append(r,"</div>");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<input type=\"button\" value=\"Add\" onclick=\"arpeggiators.add();\" />");
		append(r,"</div>");
		append(r,"</div>");
		return r;
	}
}

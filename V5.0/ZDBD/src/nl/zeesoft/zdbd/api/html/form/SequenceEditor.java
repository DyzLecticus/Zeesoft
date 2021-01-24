package nl.zeesoft.zdbd.api.html.form;

import nl.zeesoft.zdbd.pattern.InstrumentPattern;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdbd.pattern.instruments.Note;
import nl.zeesoft.zdbd.pattern.instruments.PatternInstrument;
import nl.zeesoft.zdbd.pattern.instruments.Percussion1;
import nl.zeesoft.zdbd.pattern.instruments.Percussion2;
import nl.zeesoft.zdk.Str;

public class SequenceEditor extends FormHtml {
	private PatternSequence sequence			= null;
	private int				selectedPattern		= 0;
	
	public SequenceEditor(PatternSequence sequence, int selectedPattern) {
		this.sequence = sequence;
		this.selectedPattern = selectedPattern;
	}
	
	@Override
	public Str render() {
		Str r = new Str();
		append(r,super.render());
		append(r,renderSequence(sequence,selectedPattern));
		return r;
	}
	
	public static Str renderSequence(PatternSequence sequence, int selectedPattern) {
		Str r = new Str();
		append(r,"<div class=\"row\">");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<label class=\"column-label\">Pattern sequence</label>");
		append(r,"</div>");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,renderSequencePatternSelect(sequence,0));
		append(r,renderSequencePatternSelect(sequence,1));
		append(r,renderSequencePatternSelect(sequence,2));
		append(r,renderSequencePatternSelect(sequence,3));
		append(r,"</div>");
		append(r,"</div>");
		
		append(r,"<div class=\"row\">");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<label class=\"column-label\">Edit pattern</label>");
		append(r,"</div>");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,renderPatternSelect(0));
		append(r,"</div>");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<input type=\"button\" value=\"Clear\" onclick=\"sequence.clickedClearPattern();\" />");
		append(r,"<input type=\"button\" value=\"Copy\" onclick=\"sequence.copyPattern();\" />");
		append(r,"<input type=\"button\" value=\"Paste\" onclick=\"sequence.pastePattern();\" />");
		append(r,"</div>");
		append(r,"</div>");
		
		append(r,"<div class=\"row\">");
		for (PatternInstrument inst: InstrumentPattern.INSTRUMENTS) {
			append(r,"<div class=\"column-left column-padding\">");
			String label = inst.name().substring(0,2).toUpperCase();
			if (inst.name().equals(Percussion1.NAME)) {
				label = "P1";
			} else if (inst.name().equals(Percussion2.NAME)) {
				label = "P2";
			}
			append(r,"<label class=\"instrument-label\">");
			r.sb().append(label);
			r.sb().append("</label>");
			append(r,"</div>");
		}
		append(r,"</div>");

		for (int p = 0; p < sequence.sequence.length; p++) {
			InstrumentPattern pattern = new InstrumentPattern();
			if (p<sequence.patterns.size()) {
				pattern = sequence.patterns.get(p);
			}
			append(r,"<div id=\"pattern");
			r.sb().append(p);
			r.sb().append("\"");
			if (p!=selectedPattern) {
				r.sb().append(" class=\"hidden\"");
			}
			r.sb().append(">");
			for (int s = 0; s < sequence.rythm.getStepsPerPattern(); s++) {
				append(r,"<div class=\"row");
				if (s % sequence.rythm.stepsPerBeat==0) {
					r.sb().append(" row-highlight");
				}
				r.sb().append("\">");
				for (String name: pattern.getInstrumentNames()) {
					int value = pattern.getStepValue(name,s);
					append(r,"<div class=\"column-left column-padding\">");
					append(r,"<input type=\"button\" value=\"");
					r.sb().append(value);
					r.sb().append("\" class=\"pattern-step ");
					if (!name.equals(Note.NAME)) { 
						if (InstrumentPattern.isAccent(value)) { 
							r.sb().append("yellow");
						} else if (value!=PatternInstrument.OFF) {
							r.sb().append("orange");
						} else {
							r.sb().append("grey");
						}
					} else {
						r.sb().append("grey");
					}
					r.sb().append("\" onclick=\"sequence.clickedStepValue(this)\"");
					r.sb().append("\" id=\"");
					r.sb().append(name);
					r.sb().append("-");
					r.sb().append(s);
					r.sb().append("\" />");
					append(r,"</div>");
				}
				append(r,"</div>");
			}
			append(r,"</div>");
		}
		
		return r;
	}
	
	protected static Str renderSequencePatternSelect(PatternSequence sequence, int index) {
		return renderPatternSelect("sequencePattern-" + index,sequence.sequence[index],true,"sequence.changedSequencePatternSelect(this);");
	}
	
	protected static Str renderPatternSelect(int sel) {
		return renderPatternSelect("patternSelect",sel,false,"sequence.changedPatternSelect(this);");
	}
	
	protected static Str renderPatternSelect(String id, int sel, boolean addZero, String onChange) {
		Str r = new Str();
		append(r,"<select id=\"");
		r.sb().append(id);
		r.sb().append("\"");
		if (onChange.length()>0) {
			r.sb().append(" onchange=\"");
			r.sb().append(onChange);
			r.sb().append("\"");
		}
		r.sb().append(">");
		if (addZero) {
			append(r,"    <option value=\"-1\" ");
			if (sel==0) {
				r.sb().append(" SELECTED");
			}
			r.sb().append(">0</option>");
		}
		append(r,"    <option value=\"0\" ");
		if (sel==0) {
			r.sb().append(" SELECTED");
		}
		r.sb().append(">1</option>");
		append(r,"    <option value=\"1\" ");
		if (sel==1) {
			r.sb().append(" SELECTED");
		}
		r.sb().append(">2</option>");
		append(r,"    <option value=\"2\" ");
		if (sel==2) {
			r.sb().append(" SELECTED");
		}
		r.sb().append(">3</option>");
		append(r,"    <option value=\"3\" ");
		if (sel==3) {
			r.sb().append(" SELECTED");
		}
		r.sb().append(">4</option>");
		append(r,"</select>");
		return r;
	}
}

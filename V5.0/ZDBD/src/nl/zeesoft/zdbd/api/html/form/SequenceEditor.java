package nl.zeesoft.zdbd.api.html.form;

import nl.zeesoft.zdbd.midi.convertors.MidiNote;
import nl.zeesoft.zdbd.pattern.InstrumentPattern;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdbd.pattern.instruments.Note;
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
		append(r,"<label class=\"column-label\">Sequence</label>");
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
		append(r,"<label class=\"column-label\">Pattern</label>");
		append(r,"</div>");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,renderPatternSelect(0));
		append(r,"</div>");
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
					append(r,"<div class=\"column-left column-padding\">");
					int value = pattern.getStepValue(name,s);
					if (name.equals(Note.NAME)) {
						append(r,renderNoteSelect("",value,""));
					} else {
						append(r,"<input type=\"button\" value=\"");
						r.sb().append(value);
						r.sb().append("\" />");
					}
					append(r,"</div>");
				}
				append(r,"</div>");
			}
		}
		
		return r;
	}
	
	protected static Str renderSequencePatternSelect(PatternSequence sequence, int num) {
		return renderPatternSelect("sequencePattern" + num,sequence.sequence[num],true,"");
	}
	
	protected static Str renderPatternSelect(int sel) {
		return renderPatternSelect("patternSelect",sel,false,"sequence.changedPatternSelect();");
	}
	
	protected static Str renderPatternSelect(String id, int sel, boolean addZero, String onChange) {
		Str r = new Str();
		append(r,"<select id=\"");
		r.sb().append(id);
		r.sb().append("\"");
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
	
	protected static Str renderNoteSelect(String id, int sel, String onChange) {
		Str r = new Str();
		append(r,"<select id=\"");
		r.sb().append(id);
		r.sb().append("\"");
		r.sb().append(">");
		for (int n = 0; n < MidiNote.NOTE_CODES.length; n++) {
			append(r,"    <option value=\"");
			r.sb().append(n);
			r.sb().append("\"");
			if (n==sel) {
				r.sb().append(" SELECTED");
			}
			r.sb().append(">");
			r.sb().append(MidiNote.NOTE_CODES[n]);
			r.sb().append("</option>");
		}
		append(r,"</select>");
		return r;
	}
}

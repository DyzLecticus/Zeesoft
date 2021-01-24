package nl.zeesoft.zdbd.api.html.form;

import nl.zeesoft.zdbd.midi.convertors.MidiNote;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdbd.pattern.SequenceChord;
import nl.zeesoft.zdk.Str;

public class ChordEditor extends FormHtml {
	private PatternSequence sequence			= null;
	
	public ChordEditor(PatternSequence sequence) {
		this.sequence = sequence;
	}
	
	@Override
	public Str render() {
		Str r = new Str();
		append(r,super.render());
		append(r,renderChords(sequence));
		return r;
	}
	
	public static Str renderChords(PatternSequence sequence) {
		Str r = new Str();
		
		append(r,"<div class=\"row\">");
		append(r,"<div class=\"column-left column-padding chord-step\">");
		append(r,"<label>Step</label>");
		append(r,"</div>");
		append(r,"<div class=\"column-left column-padding chord-step\">");
		append(r,"<label>Chord</label>");
		append(r,"</div>");
		append(r,"</div>");
		
		int row = 0;
		for (SequenceChord chord: sequence.getSequencedChords()) {
			if (row%2==0) {
				append(r,"<div class=\"row row-highlight\">");
			} else {
				append(r,"<div class=\"row\">");
			}
			
			append(r,"<div class=\"column-left column-padding\">");
			append(r,"<input type=\"number\" class=\"chord-step\" value=\"");
			r.sb().append(chord.step);
			r.sb().append("\" onchange=\"chords.changedStep(this);\"");
			r.sb().append("\" id=\"chord-");
			r.sb().append(chord.step);
			r.sb().append("\"");
			if (row==0) {
				r.sb().append(" DISABLED");
			}
			r.sb().append(" />");
			
			append(r,renderNoteSelect("chord-" + chord.step,chord.baseNote,"chords.changedBaseNote(this);"));

			for (int i = 0; i < chord.interval.length; i++) {
				append(r,"<input type=\"number\" class=\"chord-value\" value=\"");
				r.sb().append(chord.interval[i]);
				r.sb().append("\" onchange=\"chords.changedInterval(this);\"");
				r.sb().append("\" id=\"chord-");
				r.sb().append(chord.step);
				r.sb().append("-");
				r.sb().append(i);
				r.sb().append("\" />");
			}
			append(r,"</div>");
			
			if (row>0) {
				append(r,"<div class=\"column-right column-padding\">");
				append(r,"<input type=\"button\" value=\"Delete\" onclick=\"chords.delete('");
				r.sb().append(chord.step);
				r.sb().append("');\" />");
				append(r,"</div>");
			}
				
			append(r,"</div>");
		
			row++;
		}

		append(r,"<div class=\"row\">");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<input type=\"button\" value=\"Add\" onclick=\"chords.add();\" />");
		append(r,"</div>");
		append(r,"</div>");
		return r;
	}
	
	protected static Str renderNoteSelect(String id, int sel, String onChange) {
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

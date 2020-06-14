package nl.zeesoft.zdk.midi;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.thread.CodeRunner;

public class DelayedNotePlayer extends CodeRunner {
	protected DelayedNotePlayer(DelayedNoteCode code) {
		super(code);
		setSleepNs(100000);
	}
	
	protected List<MidiNote> getNotes() {
		DelayedNoteCode code = (DelayedNoteCode) getCode();
		return code.notes;
	}
	
	protected List<MidiNote> getNotes(int channel) {
		DelayedNoteCode code = (DelayedNoteCode) getCode();
		List<MidiNote> r = new ArrayList<MidiNote>();
		for (MidiNote note: code.notes) {
			if (note.channel==channel) {
				r.add(note);
			}
		}
		return r;
	}
}
